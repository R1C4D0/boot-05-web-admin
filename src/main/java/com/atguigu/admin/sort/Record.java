package com.atguigu.admin.sort;

import java.io.Serializable;

public class Record implements Serializable {
    private int A;
    private String B;

    public Record(int A, String B) {
        this.A = A;
        this.B = B;
    }

    public int getA() {
        return A;
    }

    public String getB() {
        return B;
    }
// getter 和 setter 方法省略

    @Override
    public String toString() {
        return "(" + A + ", " + B + ")";
    }
}


