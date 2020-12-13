package com.doudou.boottest.util;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RSAUtil {
    private static final String ALGORITHM = "RSA";
    private static final Integer KEY_SIZE_DEFAULT = 4096;
    private static KeyPairGenerator keyPairGenerator;
    private static KeyFactory keyFactory;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String ins = "娃哈哈";
        PKeyPair pKeyPair = createKeyPair(2048);
        String pubk = base64Encode(pKeyPair.getPublicKey().getEncoded());
        String s = encryptByPublicKey(ins, pubk);
        System.out.println(s);
        String prik = base64Encode(pKeyPair.getPrivateKey().getEncoded());
        String ss = decryptByPrivateKey(s, prik);
        System.out.println(ss);
    }

    public static String encryptByPublicKey(String sources, String publicKey) {
        PublicKey key = getPublicKey(base64Decode(publicKey));
        return encryptByPublicKey(sources, key);
    }

    public static String decryptByPrivateKey(String sources, String privateKey) {
        PrivateKey key = getPrivateKey(base64Decode(privateKey));
        return decryptByPrivateKey(sources, key);
    }

    /**
     * Encode byte array by Base64 format.
     *
     * @param source source byte data
     * @return base64 encoded string
     */
    public static String base64Encode(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    /**
     * Decode base64 encoded string by Base64 format.
     *
     * @param encoded encoded string
     * @return source byte array
     */
    public static byte[] base64Decode(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }


    /**
     * Convert public key from byte array to {@link PublicKey}
     *
     * @param keyBytes key byte array
     * @return {@link PublicKey}
     */
    public static PublicKey getPublicKey(byte[] keyBytes) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Convert private key from byte array to {@link PrivateKey}
     *
     * @param keyBytes key byte array
     * @return {@link PrivateKey}
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     *
     * @param sources
     * @param publicKey
     * @return
     */
    public static String encryptByPublicKey(String sources, PublicKey publicKey) {
        byte[] rs = null;
        try {
            rs = doFinal(sources.getBytes(StandardCharsets.UTF_8), publicKey, Cipher.ENCRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
        return base64Encode(rs);
    }

    /**
     * Decrypt by private key.
     *
     * @param encryptedSource encrypt source
     * @param privateKey {@link PrivateKey}
     * @return source
     */
    public static String decryptByPrivateKey(String encryptedSource, PrivateKey privateKey) {
        byte[] rs = null;
        try {
            rs = doFinal(base64Decode(encryptedSource), privateKey, Cipher.DECRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
        return new String(rs);
    }



    /**
     *
     */
    @Setter
    @Getter
    public static class PKeyPair {
        private PrivateKey privateKey;
        private PublicKey publicKey;

        public PKeyPair(KeyPair keyPair) {
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        }
    }

    /**
     *
     * @param keySize
     * @return
     */
    public static synchronized PKeyPair createKeyPair(int keySize) {
        if (keySize < 2048) {
            throw new IllegalArgumentException("SRA key size must >= 2048");
        }

        keyPairGenerator.initialize(keySize, new SecureRandom());
        KeyPair pair = keyPairGenerator.generateKeyPair();
        return new PKeyPair(pair);
    }




    /**
     *
     * @param sourceBytes
     * @param key {@link Key}
     * @return
     * @throws NoSuchPaddingException {@link NoSuchPaddingException}
     * @throws NoSuchAlgorithmException {@link NoSuchAlgorithmException}
     * @throws InvalidKeyException {@link InvalidKeyException}
     * @throws BadPaddingException {@link BadPaddingException}
     * @throws IllegalBlockSizeException {@link IllegalBlockSizeException}
     */
    private static byte[] doFinal(byte[] sourceBytes, Key key, int mode)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key);
        int pageSize = ((RSAKey) key).getModulus().bitLength() / 8 - (mode == Cipher.ENCRYPT_MODE ? 11 : 0);
        List<Byte[]> bytesList = split(sourceBytes, pageSize);
        List<Byte> result = new ArrayList<>();
        for (Byte[] bytes : bytesList) {
            result.addAll(convert(cipher.doFinal(copy(bytes))));
        }
        return convert(result);
    }


    /**
     *
     * @param bytes
     * @param pageSize
     * @return
     */
    private static List<Byte[]> split(byte[] bytes, int pageSize) {
        int remain = bytes.length % pageSize;
        int pages = remain != 0 ? bytes.length / pageSize + 1 : bytes.length / pageSize;
        List<Byte[]> bytesList = new ArrayList<>();
        Byte[] temp;
        for (int page = 0; page < pages; page++) {
            if (page == pages - 1 && remain != 0) {
                temp = new Byte[remain];
                System.arraycopy(copy(bytes), page * pageSize, temp, 0, remain);
            } else {
                temp = new Byte[pageSize];
                System.arraycopy(copy(bytes), page * pageSize, temp, 0, pageSize);
            }
            bytesList.add(temp);
        }
        return bytesList;
    }


    /**
     * Copy from byte array to Byte array
     * @param bytes Sources byte array
     * @return Byte array
     */
    private static Byte[] copy(byte[] bytes) {
        Byte[] bytesU = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytesU[i] = bytes[i];
        }
        return bytesU;
    }

    /**
     * Copy from Byte array to byte array
     * @param bytes Sources Byte array
     * @return byte array
     */
    private static byte[] copy(Byte[] bytes) {
        byte[] bytesL = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytesL[i] = bytes[i];
        }
        return bytesL;
    }

    /**
     * Convert byte list to byte array
     * @param byteList sources byte list
     * @return byte array
     */
    private static byte[] convert(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    /**
     * Convert byte array to byte list
     * @param bytes Sources byte array
     * @return byte list
     */
    private static List<Byte> convert(byte[] bytes) {
        List<Byte> byteList = new ArrayList<>(bytes.length);
        for (byte b : bytes) {
            byteList.add(b);
        }
        return byteList;
    }
}
