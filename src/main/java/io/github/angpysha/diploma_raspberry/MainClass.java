package io.github.angpysha.diploma_raspberry;

import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;
import io.github.angpysha.diploma_bridge.Models.DHT11_Data;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.BMPRunner;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.DHTRunner;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.IBMPCallback;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.IDHTCallback;
import io.github.angpysha.diploma_raspberry.Socket.Socket;

import java.text.SimpleDateFormat;

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
        AppConfig appConfig = AppConfig.getInstanse("appconfig.ini");
        Socket socket = Socket.getInstanse(appConfig.getSocketUrl());
        socket.socketio.connect();
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
