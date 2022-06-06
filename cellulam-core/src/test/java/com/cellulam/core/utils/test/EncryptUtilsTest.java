package com.cellulam.core.utils.test;

import com.cellulam.core.security.HashUtils;
import com.cellulam.core.security.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;

/**
 * @author eric.li
 * @date 2022-06-06 13:29
 */
public class EncryptUtilsTest {
    @Test
    public void testEncrypt() {
        String s = "abcd:878787";
        String str = RSAUtils.rsaPublicKeyEncrypt(s, RSAUtils.DEFAULT_PUBLIC_KEY);
        System.out.println(str);

        String text = RSAUtils.rsaPrivateKeyDecrypt(str, RSAUtils.DEFAULT_PRIVATE_KEY);

        System.out.println(text);

        Assert.assertEquals(s, text);
    }

    @Test
    public void testGenerateKeyPair() {
        KeyPair keyPair = RSAUtils.generateRSAKeyPair();
        String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());

        String s = "abc";
        String str = RSAUtils.rsaPublicKeyEncrypt(s, publicKey);
        System.out.println(str);

        String text = RSAUtils.rsaPrivateKeyDecrypt(str, privateKey);

        System.out.println(text);
        System.out.println("\n==============private key===============\n");
        System.out.println(privateKey);
        System.out.println("\n==============public key===============\n");
        System.out.println(publicKey);

        Assert.assertEquals(s, text);

    }

    @Test
    public void testMd5() {
        System.out.println(HashUtils.sha32("admin"));
        System.out.println(HashUtils.sha128("admin"));
        System.out.println(HashUtils.sha256("admin"));
        System.out.println(HashUtils.sha512("admin"));
    }
}
