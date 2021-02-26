package com.libseat.utils.utils;

import java.util.List;
import java.util.Random;

public class UsernameGenerateUtils {

    private UsernameGenerateUtils(){};

    /**
     *  随机获取英文+数字（用户名）
     * @param engCode 小写英文的数量
     * @param numCode 数字的数量
     * @return
     */
    public static String getUsername(int engCode, int numCode, List<String> allUsername){
        while(true){
            //声明一个StringBuffer存储随机数
            StringBuffer sb = new StringBuffer();
            char[] englishCodeArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            char[] numCodeArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            Random random = new Random();
            //获取英文
            for (int i = 0; i <engCode; i++){
                char num = englishCodeArray[random.nextInt(englishCodeArray.length)];
                sb.append(num);
            }
            //获取数字
            for (int i = 0; i <numCode; i++){
                char num = numCodeArray[random.nextInt(numCodeArray.length)];
                sb.append(num);
            }
            String username=sb.toString();
            if(!allUsername.contains(username)){
                return username;
            }
        }
    }
}
