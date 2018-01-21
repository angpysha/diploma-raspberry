package com.andrewpetrowski.diploma.raspberry.Sensors;

import com.andrewpetrowski.diploma.bridgelib.Models.Bmp180_Data;

public class BMP180 {

    public BMP180() {}
    static {
        System.loadLibrary("raspiinfo");
    }

    public native Bmp180_Data GetDataEx();

    public native float[] GetData();

    public native void Testing();
}
