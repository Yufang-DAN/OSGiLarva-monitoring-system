package fr.inria.amazones.logosng.storage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;

import java.security.AlgorithmParameters;

import java.io.IOException;
import java.io.OutputStream;

import static fr.inria.amazones.logosng.storage.Constants.INITIAL_IV;
import static fr.inria.amazones.logosng.storage.Constants.EVENT_CIPHER_ALG;
import static fr.inria.amazones.logosng.storage.Constants.EVENT_KEY_ALG;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class LogosCryptedOutputStream extends LogosOutputStream {

    private Cipher cipher;
    private byte[] ibuffer = new byte[1];
    private byte[] obuffer;

    public LogosCryptedOutputStream(OutputStream o, SecretKey sk) throws Exception {
        super(o);
        AlgorithmParameters algParams = AlgorithmParameters.getInstance(EVENT_KEY_ALG);
        algParams.init(new IvParameterSpec(INITIAL_IV));
        this.cipher = Cipher.getInstance(EVENT_CIPHER_ALG);
        this.cipher.init(ENCRYPT_MODE, sk, algParams);
    }

    @Override
    public void write(int b) throws IOException {
        ibuffer[0] = (byte) b;
        obuffer = cipher.update(ibuffer, 0, 1);
        if (obuffer != null) {
            out.write(obuffer);
            obuffer = null;
        }
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        obuffer = cipher.update(b, off, len);
        if (obuffer != null) {
            out.write(obuffer);
            obuffer = null;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            obuffer = cipher.doFinal();
        } catch (IllegalBlockSizeException e) {
            obuffer = null;
        } catch (BadPaddingException e) {
            obuffer = null;
        }
        try {
            flush();
        } catch (IOException ignored) {
        }
        out.close();
    }
}
