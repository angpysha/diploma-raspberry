package com.andrewpetrowski.diploma.raspberry;

import com.andrewpetrowski.diploma.raspberry.Sensors.DHT11;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        DHT11 dht11 = new DHT11();
        while(true)
        {  Thread.sleep(1000);
            dht11.getTemperature(7);

        }
    }
}
