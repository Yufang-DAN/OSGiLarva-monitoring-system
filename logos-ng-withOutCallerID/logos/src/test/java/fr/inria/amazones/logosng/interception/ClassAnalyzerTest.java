package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logging.Logger;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassAnalyzerTest {

    private class NotSerializable {

    }

    private class CanBeSerialized implements Serializable {

    }

    private interface SerializableInterface {

        public String sayHello(String who, int counter);
    }

    private interface NotSerializableInterfaceFromReturnType {

        public String sayHello(String who, int counter);

        public NotSerializable bogus();
    }

    private interface NotSerializableInterfaceFromArgumentType {

        public String sayHello(String who, int counter);

        public void bogus(NotSerializable foo);
    }

    @Test
    public void checkCanBeSerialized() {
        BundleContext bc = mock(BundleContext.class);
        ClassAnalyzer analyzer = new ClassAnalyzer(bc);

        assertThat(analyzer.canBeSerialized(int.class), is(true));
        assertThat(analyzer.canBeSerialized(CanBeSerialized.class), is(true));
        assertThat(analyzer.canBeSerialized(NotSerializable.class), is(false));
        assertThat(analyzer.canBeSerialized(Void.class), is(true));
    }

    @Test
    public void checkAreAllSerializableClasses() {
        Class[] allSerializable = {
                String.class, int.class, Integer.class, CanBeSerialized.class
        };

        Class[] notAllSerializable = {
                String.class, int.class, Integer.class, CanBeSerialized.class, NotSerializable.class
        };

        ClassAnalyzer analyzer = new ClassAnalyzer(null);

        assertThat(analyzer.areAllSerializableClasses(asList(allSerializable)), is(true));
        assertThat(analyzer.areAllSerializableClasses(asList(notAllSerializable)), is(false));
    }

    @Test
    public void checkHaveOnlySerializableData() {
        ClassAnalyzer analyzer = new ClassAnalyzer(null);

        Class[] classes1 = {
                SerializableInterface.class
        };

        Class[] classes2 = {
                SerializableInterface.class, NotSerializableInterfaceFromArgumentType.class
        };

        Class[] classes3 = {
                SerializableInterface.class, NotSerializableInterfaceFromReturnType.class
        };

        Class[] classes4 = {
                SerializableInterface.class, SerializableInterface.class
        };

        assertThat(analyzer.haveOnlySerializableData(classes1), is(true));
        assertThat(analyzer.haveOnlySerializableData(classes2), is(false));
        assertThat(analyzer.haveOnlySerializableData(classes3), is(false));
        assertThat(analyzer.haveOnlySerializableData(classes4), is(true));
    }

    @Test
    public void checkOriginatesFromTrustedBundle() throws ClassNotFoundException {
        ClassAnalyzer analyzer = new ClassAnalyzer(null);

        Set<String> trustedBundles = new HashSet<String>(asList("foo.bar"));

        Bundle[] bundles = {
                mock(Bundle.class),
                mock(Bundle.class)
        };
        when(bundles[0].getSymbolicName()).thenReturn("plop");
        when(bundles[1].getSymbolicName()).thenReturn("foo.bar");

        when(bundles[0].loadClass(CanBeSerialized.class.getCanonicalName()))
                .thenThrow(new ClassNotFoundException());
        when(bundles[1].loadClass(CanBeSerialized.class.getCanonicalName()))
                .thenReturn(CanBeSerialized.class);

        assertThat(
                analyzer.originatesFromTrustedBundle(
                        "",
                        trustedBundles,
                        bundles),
                is(true));

        assertThat(
                analyzer.originatesFromTrustedBundle(
                        new CanBeSerialized(),
                        trustedBundles,
                        bundles),
                is(true));

        assertThat(
                analyzer.originatesFromTrustedBundle(
                        new NotSerializable(),
                        trustedBundles,
                        bundles),
                is(false));
    }
}
