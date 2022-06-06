package com.cellulam.core.security;


import com.cellulam.core.exceptions.SysException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA
 *
 * @author eric.li
 * @date 2022-06-06 12:12
 */
public final class RSAUtils {

    public static final String DEFAULT_PRIVATE_KEY = "MIICWgIBAAKBgHggf70q/E7H2VDvzywx07d3Swe4F5QhtUsoOUA3ol0J8dLGLITn\n" +
            "MPR8SWjX8h4Ula9UPoYK9YDxElZKaJhNtIzPS6J9wACl7ehAhCpngtc42rXqLbdS\n" +
            "IjT/hgaQOclOCALIPqGN6esoyyfaa47f3e8Cg+nL6ptNAyfkXZjXT7AxAgMBAAEC\n" +
            "gYApLtscO1Rsnc8/FDfuataum1M5vj83JadFsPCPt4MOao5hOFei6K+74bA5JW94\n" +
            "KI54oWUeBzvLjNpAgxoAvQMclpY8uX20Mi//AhIYAT/Odgx3fjhcPmGEcPLJJC5M\n" +
            "m9MsEckbZOgJQGg69hFjqVyaTsQlNQckh47ciI6w9d14kQJBAOUPYI0bfGZwPHCf\n" +
            "C2qvKuV1cdJ+oVHEIq8dHNFehvOTei0lq48u/WMZPaQLNPLL+3YPcRBArvKjW1y9\n" +
            "D+FodI8CQQCGQVMIuYIEGnBP3BLH3qkF7b+Smpgiky5QsSw0/uP8qZXP3GwyMSDP\n" +
            "7sQVscaX4r0MCiStfFRfQf3CJ8l9X28/AkA429RPsp7ynrGiExi2ZrzzMHAMnDpX\n" +
            "HaSuaz+YCSrek6EkS9GGPVfQnZdbfRyrmE7XQ4YBTs0elcY8AwJKJa+pAkAs2+Ls\n" +
            "xV9mE5d3d6dm1UVQz9H1IGa5Fsig+bI1uv1ezSNbouEcHEJEeBlYzl/3HDHitfjv\n" +
            "Zt2iPqCVoUm39I1tAkA8BQk90m4ekKN/4s2FlOVQWjdyLvk/ayoTbkJXnNccZba6\n" +
            "kjYGouTycAZ9Qd9ECzswg3/1bSPFUtYoqlzm9Tya";

    public static final String DEFAULT_PUBLIC_KEY = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgHggf70q/E7H2VDvzywx07d3Swe4\n" +
            "F5QhtUsoOUA3ol0J8dLGLITnMPR8SWjX8h4Ula9UPoYK9YDxElZKaJhNtIzPS6J9\n" +
            "wACl7ehAhCpngtc42rXqLbdSIjT/hgaQOclOCALIPqGN6esoyyfaa47f3e8Cg+nL\n" +
            "6ptNAyfkXZjXT7AxAgMBAAE=";

    private static final int KEY_SIZE = 1024;
    private static final String UTF_8 = "utf-8";
    private static final String SHA_256_WITH_RSA = "SHA256withRSA";
    private static final String RSA = "RSA";

    private static final int CIPHER_LENGTH = 117;
    private static final int DECRYPT_BLOCK = 128;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyPair generateRSAKeyPair() throws SysException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(KEY_SIZE, new SecureRandom());
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new SysException("Generate KeyPair fail", e);
        }

    }

    public static String getKeyStr(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String sign(String plainText, PrivateKey privateKey) throws SysException {
        try {
            Signature privateSignature = Signature.getInstance(SHA_256_WITH_RSA);
            privateSignature.initSign(privateKey);
            privateSignature.update(plainText.getBytes(UTF_8));

            byte[] signature = privateSignature.sign();
            return Base64.encodeBase64String(signature);

        } catch (Exception e) {
            throw new SysException("RSA sign failed", e);
        }

    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws SysException {
        try {
            Signature publicSignature = Signature.getInstance(SHA_256_WITH_RSA);
            publicSignature.initVerify(publicKey);
            publicSignature.update(plainText.getBytes(UTF_8));

            byte[] signatureBytes = Base64.decodeBase64(signature);

            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
            throw new SysException("verify RSA sign failed", e);
        }

    }


    public static PrivateKey getPrivateKey(String keyStr) throws SysException {
        try {
            byte[] keyBytes = Base64.decodeBase64(keyStr);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(RSA);
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new SysException("generate private key failed", e);
        }

    }

    public static PublicKey getPublicKey(String keyStr) throws SysException {

        try {
            byte[] keyBytes = Base64.decodeBase64(keyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(RSA);
            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new SysException("generate public key failed", e);
        }

    }

    /**
     * rsa encrypt by private key
     *
     * @param text       text
     * @param privateKey base64 encode private key
     * @return encrypted string
     */
    public static String rsaPrivateKeyEncrypt(String text, String privateKey) throws SysException {
        return Base64.encodeBase64String(encrypt(text, getPrivateKey(privateKey)));
    }


    /**
     * rsa encrypt by public key
     *
     * @param text
     * @param publicKey base64 encode public key
     * @return encrypted string
     */
    public static String rsaPublicKeyEncrypt(String text, String publicKey) throws SysException {
        return Base64.encodeBase64String(encrypt(text, getPublicKey(publicKey)));
    }

    /**
     * rsa decode
     *
     * @param text       encrypted string
     * @param privateKey base64 encode private key
     * @return decode string
     */
    public static String rsaPrivateKeyDecrypt(String text, String privateKey) {
        return decrypt(Base64.decodeBase64(text), getPrivateKey(privateKey));
    }


    private static byte[] encrypt(String text, Key key) throws SysException {
        byte[] cipherText;
        ByteArrayOutputStream out;
        ByteArrayInputStream in;
        try {
            //final Cipher cipher = Cipher.getInstance(PADDING);
            final Cipher cipher = Cipher.getInstance(RSA);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = text.getBytes();
            in = new ByteArrayInputStream(bytes);
            out = new ByteArrayOutputStream();
            byte[] temp = new byte[CIPHER_LENGTH];
            int end;
            while ((end = in.read(temp)) != -1) {
                out.write(cipher.doFinal(temp, 0, end));
            }

            cipherText = out.toByteArray();

        } catch (Exception e) {
            throw new SysException("RSA encrypt failed", e);
        }
        return cipherText;
    }


    /**
     * rsa decode by public key
     *
     * @param text
     * @param publicKey base64 encode public key
     * @return decode string
     */
    public static String rsaPublicKeyDecrypt(String text, String publicKey) throws SysException {
        return decrypt(Base64.decodeBase64(text), getPublicKey(publicKey));
    }

    public static String decrypt(byte[] text, Key key) throws SysException {
        ByteArrayInputStream in;
        ByteArrayOutputStream out;
        try {
            // get an RSA cipher object and print the provider
            //final Cipher cipher = Cipher.getInstance(PADDING);
            final Cipher cipher = Cipher.getInstance(RSA);
            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);

            in = new ByteArrayInputStream(text);
            out = new ByteArrayOutputStream();
            byte[] tempBytes = new byte[DECRYPT_BLOCK];
            int end;
            while ((end = in.read(tempBytes)) != -1) {
                out.write(cipher.doFinal(tempBytes, 0, end));
            }

            return new String(out.toByteArray());
        } catch (Exception e) {
            throw new SysException("RSA decrypt failed", e);
        }

    }
}
