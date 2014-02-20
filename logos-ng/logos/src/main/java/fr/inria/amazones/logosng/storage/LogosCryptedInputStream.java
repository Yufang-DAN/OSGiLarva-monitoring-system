package fr.inria.amazones.logosng.storage;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherInputStream;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import java.security.AlgorithmParameters;

import java.io.InputStream;
import java.io.IOException;

import static fr.inria.amazones.logosng.storage.Constants.EVENT_KEY_ALG;
import static fr.inria.amazones.logosng.storage.Constants.INITIAL_IV;
import static fr.inria.amazones.logosng.storage.Constants.EVENT_CIPHER_ALG;
import static javax.crypto.Cipher.DECRYPT_MODE;

public class LogosCryptedInputStream extends LogosInputStream {

    private CipherInputStream cis;

    public LogosCryptedInputStream(InputStream is, SecretKey sk) throws Exception {
        super(is);
        AlgorithmParameters algParams = AlgorithmParameters.getInstance(EVENT_KEY_ALG);
        algParams.init(new IvParameterSpec(INITIAL_IV));
        Cipher cipher = Cipher.getInstance(EVENT_CIPHER_ALG);
        cipher.init(DECRYPT_MODE, sk, algParams);
        cis = new CipherInputStream(is, cipher);
    }

    @Override
    public int available() throws IOException {
        return cis.available();
    }

    @Override
    public void close() throws IOException {
        cis.close();
    }

    @Override
    public void mark(int readlimit) {
        cis.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return cis.markSupported();
    }

    @Override
    public int read() throws IOException {
        return cis.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return cis.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return cis.read(b, off, len);
    }

    @Override
    public void reset() throws IOException {
        cis.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return cis.skip(n);
    }
}
