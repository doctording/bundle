package com.dq.bundle.demo3;

import com.dq.bundle.demo1.Hello;

/**
 * @Author mubi
 * @Date 2021/3/13 16:24
 */
public class Teacher {
    public void welcome(Hello hello) {
        hello.say("teacher");
    }

    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        Hello hello = new Hello();
        teacher.welcome(hello);
    }
}
