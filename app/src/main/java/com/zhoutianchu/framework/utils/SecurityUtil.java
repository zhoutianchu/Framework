package com.zhoutianchu.framework.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhout on 2018/4/2.
 */

public class SecurityUtil {
    private SecurityUtil() {

    }

    private static String Algorithm = "DESede";//指定为3des算法

    private static SecureRandom random = new SecureRandom();

    /**
     * 生成rsa的公私钥对
     *
     * @param length
     * @return
     * @throws Exception
     */
    public static String[] generateKey(int length) throws Exception {
        String[] arr = new String[2];
        try {
            KeyPairGenerator ges = KeyPairGenerator.getInstance("RSA");
            ges.initialize(length);
            KeyPair pair = ges.generateKeyPair();
            arr[0] = Base64.encodeToString(pair.getPublic().getEncoded(), Base64.NO_WRAP);//公钥
            arr[1] = Base64.encodeToString(pair.getPrivate().getEncoded(), Base64.NO_WRAP);//私钥
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arr;
    }

    /**
     * rsa 加密
     *
     * @param publicKey
     * @param data
     * @return
     * @throws Exception
     */
    public static String RSAEncode(String publicKey, String data) throws Exception {
        byte[] pks = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pks);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk;
        pk = kf.generatePublic(x509EncodedKeySpec);
        Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cp.init(Cipher.ENCRYPT_MODE, pk);
        return Base64.encodeToString(cp.doFinal(data.getBytes("UTF-8")), Base64.NO_WRAP);
    }

    /**
     * rsa 解密
     *
     * @param privateKey
     * @param data
     * @return
     * @throws Exception
     */
    public static String RSADecode(String privateKey, String data) throws Exception {
        byte[] prs = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(prs);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pr = kf.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cp.init(Cipher.DECRYPT_MODE, pr);
        return new String(cp.doFinal(Base64.decode(data, Base64.NO_WRAP)), "UTF-8");
    }


    //key为加密密钥，长度为24字节
    public static byte[] encryptMode(byte[] key, byte[] src) throws Exception {
        SecretKey deskey = new SecretKeySpec(key, Algorithm);
        //格式化密钥
        //加密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return c1.doFinal(src);
    }

    //key为加密密钥，长度为24字节
    public static byte[] decryptMode(byte[] key, byte[] src) throws Exception {
        //格式化密钥
        SecretKey deskey = new SecretKeySpec(key, Algorithm);
        //解密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        return c1.doFinal(src);
    }


    /**
     * 模拟密码控件加密
     *
     * @param data
     * @param randomNum
     * @return
     * @throws Exception
     */
    public static String[] encode(String data, String randomNum) throws Exception {
        String result[] = new String[2];
        /**
         * 产生3des秘钥,秘钥长度为24字节,组成为k1+k2+k3
         * 此处客户端产生8字节随机数为k1，且k3=k1
         * 服务端产生8字节随机数为k2
         * k2为base64编码
         */
        byte[] arr = new byte[8];
        random.nextBytes(arr);
        byte[] key = new byte[24];
        System.arraycopy(arr, 0, key, 0, 8);
        System.arraycopy(Base64.decode(randomNum, Base64.NO_WRAP), 0, key, 8, 8);
        System.arraycopy(arr, 0, key, 16, 8);
        String res1 = Base64.encodeToString(encryptMode(key, data.getBytes("UTF-8")), Base64.NO_WRAP);
        String res2 = RSAEncode(Constants.pks, Base64.encodeToString(arr, Base64.NO_WRAP));
        result[0] = res1;
        result[1] = res2;
        return result;
    }

    /**
     * 模拟服务端解密
     *
     * @param data
     * @param clientRandomNum
     * @param randomNum
     * @return
     * @throws Exception
     */
    public static String decode(String data, String clientRandomNum, String randomNum) throws Exception {
        String cn = RSADecode(Constants.prs, clientRandomNum);
        byte key[] = new byte[24];
        System.arraycopy(Base64.decode(cn, Base64.NO_WRAP), 0, key, 0, 8);
        System.arraycopy(Base64.decode(randomNum, Base64.NO_WRAP), 0, key, 8, 8);
        System.arraycopy(Base64.decode(cn, Base64.NO_WRAP), 0, key, 16, 8);
        byte[] result = decryptMode(key, Base64.decode(data, Base64.NO_WRAP));
        return new String(result, "UTF-8");
    }

    public static String getRandom(){
        byte[] arr = new byte[8];
        random.nextBytes(arr);
        return Base64.encodeToString(arr,Base64.NO_WRAP);
    }
}
