package com.ludm;

public class Util {

    public static void printLog() {
        System.out.println("Thread=" + Thread.currentThread().getId() + " "
                + " ClassName=" + Thread.currentThread().getStackTrace()[2].getClassName()
                + " MethodName=" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
