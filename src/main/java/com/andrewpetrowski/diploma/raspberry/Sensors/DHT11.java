package com.andrewpetrowski.diploma.raspberry.Sensors;

import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

/**
 *
 */
public class DHT11 {
    private static final int    MAXTIMINGS  = 85;
    private final int[]         dht11_dat   = { 0, 0, 0, 0, 0 };

    /**
     *
     * @param pin
     * @return
     */
    public native DHT11_Data GetDhtData(int pin);

    public native float[] readData(int pin);


    public float[] readData() {
        float[] data = this.readData(7);
        int stopCounter = 0;
        while (!isValid(data)) {
            stopCounter++;
            if (stopCounter > 10) {
               // throw new RuntimeException("Sensor return invalid data 10 times:" + data[0] + ", " + data[1]);
            }
            data = this.readData(7);
        }
        return data;
    }

    private boolean isValid(float[] data) {
        return data[0] > 0 && data[0] < 100 && data[1] > 0 && data[1] < 100;
    }

    /**
     *
     */
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
     *
     * @param pin
     */
    public void getTemperature(final int pin) {
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
            System.out.println("Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
        } else {
            System.out.println("Data not good, skip");
        }

    }

    /**
     *
     * @return
     */
    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }
}
