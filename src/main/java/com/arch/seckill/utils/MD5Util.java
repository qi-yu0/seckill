package com.arch.seckill.utils;

import org.springframework.stereotype.Component;

import org.apache.commons.codec.digest.DigestUtils;
@Component
public class MD5Util {

    private static  final String salt="1a2b3c4d";
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static  String frontEndEncrypt(String inputPassword){
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPassword
                +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    public static String backEndEncrypt(String frontEndEncryptionPassword,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2) + frontEndEncryptionPassword +salt.charAt(5)
                + salt.charAt(4);
        return md5(str);
    }

    public static String inputToDbPass(String inputPassword,String salt){
        String receivedPass = frontEndEncrypt(inputPassword);
        String dbPass;
        dbPass = backEndEncrypt(receivedPass,salt);
        return dbPass;
    }
}
