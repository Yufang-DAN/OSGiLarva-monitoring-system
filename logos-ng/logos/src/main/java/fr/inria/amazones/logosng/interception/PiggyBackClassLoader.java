package fr.inria.amazones.logosng.interception;

import java.net.URL;
import java.util.List;

public class PiggyBackClassLoader extends ClassLoader {

    private final List<ClassLoader> classLoaders;

    public PiggyBackClassLoader(List<ClassLoader> classLoaders) {
        this.classLoaders = classLoaders;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            try {
                return classLoader.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException(name);
    }

    @Override
    public URL getResource(String name) {
        for (ClassLoader loader : classLoaders) {
            URL url = loader.getResource(name);
            if (url != null) {
                return url;
            }
        }
        return null;
    }
}
