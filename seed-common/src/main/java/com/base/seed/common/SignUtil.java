package com.base.seed.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * URL签名算法
 *
 * @author zz 2018/6/13.
 */
public class SignUtil {

    /**
     * URL签名
     *
     * @param params: 参数Map集合
     * @param encode: 是否使用UTF-8编码
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params, boolean encode)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();

        // 参数按照字母排序
        Arrays.sort(keys);
        StringBuilder temp = new StringBuilder();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }

        return md5(temp.toString()).toUpperCase();
    }

    private static String md5(String pwd) throws NoSuchAlgorithmException {
        //用于加密的字符
        char[] md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
        byte[] btInput = pwd.getBytes();

        //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
        MessageDigest mdInst = MessageDigest.getInstance("MD5");

        //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
        mdInst.update(btInput);

        // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
        byte[] md = mdInst.digest();

        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            str[k++] = md5String[byte0 >>> 4 & 0xf];
            str[k++] = md5String[byte0 & 0xf];
        }

        //返回经过加密后的字符串
        return new String(str);
    }

}
