package io.github.angpysha.diploma_raspberry.Sensors;

import io.github.angpysha.diploma_bridge.Models.DHT11_Data;

/**
 * Gets DHT11 sensors data
 * @author Andrew Petrowsky
 * @version 0.8
 */
public class DHT11 {
    private static final int    MAXTIMINGS  = 85;
    private final int[]         dht11_dat   = { 0, 0, 0, 0, 0 };

    /**
     *  (Native) Get sensor data as {@link DHT11_Data} object
     * @param pin pin number
     * @return DHT11 data
     */
    public native DHT11_Data GetDhtData(int pin);

    /**
     * (Native) Get sensor data
     * @param pin Pin number
     * @return Float array, wich contains two elements <br/>
     *  0 - temperature <br/>
     *  1 - humidity
     */
    public native float[] readData(int pin);



    static {
        System.loadLibrary("raspiinfo");
    }

    /**
     * Initialize wiring library
     */
    public DHT11() {

//        // setup wiringPi
//        if (Gpio.wiringPiSetup() == -1) {
//            System.out.println(" ==>> GPIO SETUP FAILED");
//            return;
//        }



      //  GpioUtil.export(7, GpioUtil.DIRECTION_OUT);
    }


    /**
     * Check for data validity
     * @return  Validity result
     */
    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }
}
