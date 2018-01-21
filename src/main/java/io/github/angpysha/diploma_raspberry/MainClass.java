package com.andrewpetrowski.diploma.raspberry;

import com.andrewpetrowski.diploma.bridgelib.Models.Bmp180_Data;
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.andrewpetrowski.diploma.raspberry.BackgroundTasks.BMPRunner;
import com.andrewpetrowski.diploma.raspberry.BackgroundTasks.DHTRunner;
import com.andrewpetrowski.diploma.raspberry.BackgroundTasks.IBMPCallback;
import com.andrewpetrowski.diploma.raspberry.BackgroundTasks.IDHTCallback;
import com.andrewpetrowski.diploma.raspberry.Sensors.BMP180;
import com.andrewpetrowski.diploma.raspberry.Sensors.DHT11;

import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

/**
 * Main class for program
 * @version 1.0
 * @author Andrew Petrowsky
 */
public class MainClass implements IDHTCallback,IBMPCallback{

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws InterruptedException {

        MainClass mainClass = new MainClass();
        mainClass.start();

        }


    public void start() {
       // BMP180 bmp180 = new BMP180();
        DHTRunner dhtRunner = new DHTRunner(3600,this);
        BMPRunner bmpRunner = new BMPRunner(3605,this);
        while (true){
         //   bmp180.Testing();
        }
    }

    @Override
    public void acceptDHT(DHT11_Data dht11_data) {
        System.out.println(String.format("%s: Temperature: %f; Humidity: %f"
                ,df.format(dht11_data.getCreated_at())
                ,dht11_data.getTemperature()
                ,dht11_data.getHumidity()));
    }

    @Override
    public void event(Bmp180_Data data) {
        System.out.println(String.format("%s: Temperature: %f: Pressure: %f, Altitude: %f",
               df.format(data.getCreated_at()), data.getTemperature(),data.getPressure(),data.getAltitude()));
    }
}
