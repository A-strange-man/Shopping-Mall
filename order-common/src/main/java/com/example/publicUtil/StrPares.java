package com.example.publicUtil;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * @author CaoJing
 * @date 2021/07/26 15:35
 *
 * 字符串解析工具类
 * 判断查询关键字是否是纯数字
 */
public class StrPares {

    /**
     * 判断字符转是不是纯数字
     * @param str
     * @return true-纯数字  false-非纯数字
     */
    public static boolean isNum(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 字符串转换为数字
     * @param str
     * @return
     */
    public static BigInteger getNum(String str) {
        return BigInteger.valueOf(Integer.parseInt(str));
    }
}
