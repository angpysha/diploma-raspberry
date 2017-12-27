package com.andrewpetrowski.diploma.raspberry.Sensors;

import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

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
        System.loadLibrary("dhtdata");
    }

    /**
     *
     */
    public DHT11() {

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }



      //  GpioUtil.export(7, GpioUtil.DIRECTION_OUT);
    }

    /**
     * Get sensor data
     * @deprecated This method is much slower, then native JNI method.
     * And not recommend for using.
     * @param pin pin number
     */
    @Deprecated
    public Float[] getData(final int pin) {
        int laststate = Gpio.HIGH;

        int j = 0,i;
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;

        Gpio.pinMode(pin, Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.LOW);
        Gpio.delay(20);

        Gpio.digitalWrite(pin, Gpio.HIGH);
        Gpio.delayMicroseconds(40);
        Gpio.pinMode(pin, Gpio.INPUT);

        Gpio.delayMicroseconds(10);
        for (i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(pin) == laststate) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            laststate = Gpio.digitalRead(pin);
          //  System.out.print(laststate);
            if (counter == 255) {
                break;
            }

            /* ignore first 3 transitions */
            if ((i >= 4) && (i % 2 == 0)) {
                /* shove each bit into the storage bytes */
                dht11_dat[j / 8] <<= 1;
                if (counter > 50) {
                    dht11_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
        if (j >= 40 && checkParity()) {
            float h = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (h > 100) {
                h = dht11_dat[0]; // for DHT11
            }
            float c = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if (c > 125) {
                c = dht11_dat[2]; // for DHT11
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                c = -c;
            }
            final float f = c * 1.8f + 32;
            Float[] data = new Float[2];
            data[0] = c;
            data[1]=h;
            return data;
            //System.out.println("Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
        } else {
            return new Float[2];
            //System.out.println("Data not good, skip");
        }

    }

    /**
     * Check for data validity
     * @return  Validity result
     */
    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }
}
