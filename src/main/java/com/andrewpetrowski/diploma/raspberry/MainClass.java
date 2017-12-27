package com.andrewpetrowski.diploma.raspberry;

import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.andrewpetrowski.diploma.raspberry.BackgroundTasks.DHTRunner;
import com.andrewpetrowski.diploma.raspberry.BackgroundTasks.IDHTCallback;
import com.andrewpetrowski.diploma.raspberry.Sensors.DHT11;

/**
 * Main class for program
 * @version 1.0
 * @author Andrew Petrowsky
 */
public class MainClass implements IDHTCallback{

    public static void main(String[] args) throws InterruptedException {

        MainClass mainClass = new MainClass();
        mainClass.start();

        }


    public void start() {
        DHTRunner dhtRunner = new DHTRunner(3600,this);
        while (true){

        }
    }

    @Override
    public void acceptDHT(DHT11_Data dht11_data) {
        System.out.println(String.format("Temperature: %f; Humidity: %f"
                ,dht11_data.getTemperature()
                ,dht11_data.getHumidity()));
    }
}
