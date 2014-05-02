package fr.inria.amazones.logosng.storage;

import fr.inria.amazones.logosng.logging.ServiceLoggingEvent;
import org.osgi.framework.BundleContext;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.zip.GZIPOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.Integer.valueOf;

public class AppendOnlyCompressedCryptedEventStore {

    private static final String CONFIG_FILEPREFIX = "logosng.logging.logfilename";
    private static final String CONFIG_FILE_SIZE = "logosng.logging.logfilesize";
    private static final String CONFIG_MAX_NUMBER_OF_SAVED_FILES = "logosng.logging.maxnumberofsavedfiles";
    private static final String CONFIG_PROTECTED = "logosng.protected";
    private static final String CONFIG_PROTECTED_CONSOLEDEBUG = "logosng.protected.consoledebug";
    private static final String CONFIG_CRYPTED = "logosng.crypted";
    private static final String CONFIG_COMPRESS = "logosng.compress";

    private long byteLimitUntilRotation = 0;
    private File currentFile;
    private ObjectOutputStream oos;
    private int numFile = 1;
    private int maxNumberOfFiles;
    private String prefixFileName;
    private LogosOutputStream los;
    private FileOutputStream fos;
    private boolean debug = false;
    private boolean compress = false;
    private int debugCpt = 1;
    private final Logger internalLogger;

    public AppendOnlyCompressedCryptedEventStore(BundleContext bc) {
        this.internalLogger = fr.inria.amazones.logging.Logger.getLogger(bc, this.getClass());
        try {
            if ("false".equals(bc.getProperty(CONFIG_PROTECTED))) {
                if ("true".equals(bc.getProperty(CONFIG_PROTECTED_CONSOLEDEBUG))) {
                    this.debug = true;
                }
                if ("true".equals(bc.getProperty(CONFIG_COMPRESS))) {
                    internalLogger.config("Working with COMPRESS logs");
                    this.compress = true;
                }
            }
            this.maxNumberOfFiles = valueOf(bc.getProperty(CONFIG_MAX_NUMBER_OF_SAVED_FILES));
            this.prefixFileName = bc.getProperty(CONFIG_FILEPREFIX) + "-" + (new Date().getTime());
            this.byteLimitUntilRotation = Long.valueOf(bc.getProperty(CONFIG_FILE_SIZE));
            this.currentFile = new File(prefixFileName + ".cur");
            fos = new FileOutputStream(currentFile);
            SecretKey sk = new SecretKeySpec(
                    new byte[]{37, 22, -47, -64, 127, 112, 51, -24,
                            92, 5, 57, -116, 5, 111, 32, -54}, "AES");
            los = new LogosCryptedOutputStream(fos, sk);

            if ("false".equals(bc.getProperty(CONFIG_PROTECTED))) {
                if ("false".equals(bc.getProperty(CONFIG_CRYPTED))) {
                    internalLogger.config("Working with UNCRYPTED logs");
                    los = new LogosOutputStream(fos);
                    this.debug = true;
                }
            }
            this.oos = new ObjectOutputStream(los);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public synchronized void log(ServiceLoggingEvent e) {
        if (debug) {
            internalLogger.info(debugCpt + "\n" + e);
            debugCpt++;
        }
        try {
            oos.writeObject(e);
            oos.flush();
            if (los.getCurrentLength() >= byteLimitUntilRotation) {
                performRotation();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void reopenStream() throws Exception {
        fos = new FileOutputStream(currentFile);
        los.setOutputStream(fos);
        oos.reset();
    }

    private void performRotation() throws IOException {
        if (numFile > maxNumberOfFiles) {
            fos.close();
            System.exit(0);
        }
        try {
            fos.close();
            if (this.compress) {
                final File dest = new File(prefixFileName + "." + numFile + ".gz");
                this.compress(dest);
            } else {
                final File dest = new File(prefixFileName + "." + numFile);
                currentFile.renameTo(dest);
            }
            reopenStream();
            numFile++;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void compress(File dest) throws IOException {
        final FileInputStream rawInputStream = new FileInputStream(currentFile);
        final GZIPOutputStream deflaterOutputStream = new GZIPOutputStream(
                new FileOutputStream(dest));
        int nread;
        final byte[] buffer = new byte[4096];
        while ((nread = rawInputStream.read(buffer)) != -1) {
            deflaterOutputStream.write(buffer, 0, nread);
        }
        rawInputStream.close();
        deflaterOutputStream.finish();
        deflaterOutputStream.close();
    }
}
