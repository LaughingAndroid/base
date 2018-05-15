package com.kibey.android.utils;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.TreeMap;

/**
 * api签名工具
 */
public class SignUtils {

    private static final String ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String[] sign(TreeMap<String, String> head, TreeMap<String, String> map, long t, String s) {
        try {
            return makeSign(head, map, s, APPConfig.CHARSET_UTF8, t + "");
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * @param secretKey 密钥
     * @param charset
     * @return
     * @throws Exception
     */
    public static String[] makeSign(TreeMap<String, String> head, TreeMap<String, String> map, String secretKey, String charset, String t) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        byteStream.write(t.getBytes(charset));
        byteStream.write(secretKey.getBytes(charset));

        StringBuffer sb = new StringBuffer();

        for (String key : head.keySet()) {
            if (!key.equals("x-sn") && !key.equals("Cookie") && !key.equals("User-Agent")) {
                sb.append(key).append(head.get(key));
            }
        }

        if (null != map
                && !map.isEmpty()) {

            for (String key : map.keySet()) {
                sb.append(key).append(map.get(key));
            }

        }

        byte[] bytes = sb.toString().getBytes(charset);
//        if (bytes.length > 1024) {
//            byteStream.write(bytes, 0, 1024);
//        } else {
        byteStream.write(bytes);
//        }

        byte[] array = byteStream.toByteArray();
        String origin = "";
        if (Logs.IS_DEBUG) {
            origin = new String(array);
            Logs.i("echo_sign makeSign: " + origin);
        }
        String sign = Md5Util.encryptToSHA(array) + "/" + t;
        if (Logs.IS_DEBUG) {
            Logs.i("echo_sign makeSign: " + sign);
        }

        byteStream.close();
        return new String[]{sign, origin};
    }


    public static void main(String[] args) {
        String sign = Md5Util.encryptToSHA("1453042436_ECHO_x-av9x-c2x-dtffffffff-9e2f-1f63-ffff-ffffaeadd40dx-netwapx-uuidffffffff-9e2f-1f63-ffff-ffffaeadd40dx-v89x-vs3.9android_v89app_channelyingyongbaot1453042436035v9".getBytes());
        System.out.print("sign:" + sign);
    }
}
