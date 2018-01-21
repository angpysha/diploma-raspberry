package io.github.angpysha.diploma_raspberry.Sensors;

import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;

public class BMP180 {

    public BMP180() {}
    static {
        System.loadLibrary("raspiinfo");
    }

    public native Bmp180_Data GetDataEx();

    public native float[] GetData();

    public native void Testing();
}
