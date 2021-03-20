package com.ssm_crud.test;/*
 *文件名: MyTest
 *创建者: zdx
 *创建时间:2021/3/17 20:53
 *描述: TODO
 */

import org.junit.Test;

public class MyTest {
    @Test
    public void test01(){
        double a = 265.35;
        double b = 6598.32;

        System.out.println(a + b);
        A:for (int i = 0; i < 10; i++) {
            System.out.println(i);
            for (int j = 0; j < 10; j++) {
                System.out.println(i + "\t" + j);
                if(j == 5){
                    break A;
                }
            }
        }
    }

}


