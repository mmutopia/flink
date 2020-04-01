package com.mmutopia.flink;

import java.util.Random;

/**
 * Created by Utopia on 2020/4/1
 */
public class StringSolution {
    public static void main(String[] args) {
        String str = "zhangsanzhansan";
        System.out.println(str.charAt(1));
        System.out.println(String.valueOf(Math.random()* 10000));
        System.out.println(new Random().nextInt(100));
    }
}
