package com.akin.myapplication;

public class Quote {
    public byte[] bytes;
    public int quoteid;
    public static int count;

    Quote(byte[] b){
        bytes=b;
        quoteid=count;
        count++;
    }
}
