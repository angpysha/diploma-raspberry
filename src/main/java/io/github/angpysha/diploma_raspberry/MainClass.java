package io.github.angpysha.diploma_raspberry;

import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;
import io.github.angpysha.diploma_bridge.Models.DHT11_Data;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.*;
import io.github.angpysha.diploma_raspberry.Sensors.Sensor;
import io.github.angpysha.diploma_raspberry.Socket.Socket;

import java.io.Console;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
//        AppConfig appConfig = AppConfig.getInstanse("appconfig.ini");
//        Socket socket = Socket.getInstanse(appConfig.getSocketUrl());
//        socket.socketio.connect();
//        DHTRunner dhtRunner = new DHTRunner(3600,this);
//        BMPRunner bmpRunner = new BMPRunner(3600,this);
        File libsDir = new File("./libs");
        File[] files = libsDir.listFiles();
        List<Sensor> sensors = new ArrayList<>();
//        for (File file1:files) {
//            String ext = getFileExtension(file1);
//            if (ext.contains("so")) {
//                String filename = file1.getAbsolutePath();
//                System.out.println("Load nativelib: " +filename);
//              //  System.load(filename);
//            }
//        }

        for (File file:files) {
           // System.out.println(file.getName());
            String ext = getFileExtension(file);
           // System.out.println(ext);
            if (ext.contains("jar")) {
                try {
                    System.out.println("Enter to dynamic loader");
                    ClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()},
                            getClass().getClassLoader());
                    System.out.println(classLoader.toString());

                    String name = file.getName().replace(".jar", "");
                    System.out.println(name);
                    Class<?> clazz = Class.forName("io.github.angpysha.raspberry." + name + ".Launcher", true, classLoader);
                    System.out.println(clazz.toString());
                    Method method = clazz.getDeclaredMethod("DoOperation");
                    Object instanse = clazz.newInstance();
                    sensors.add(new Sensor(method,instanse));
//                    Object meth = method.invoke(instanse);
//                    String str = (String)meth;
//                    System.out.println(str);
                    //System.out.println(meth.hashCode());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
//            } else if (ext.contains("so")) {
//                String filename = file.getAbsolutePath();
//                System.load(filename);
//            }
        }

        SensorRunner sensorRunner = new SensorRunner(3600,sensors);
        while (true){
         //   bmp180.Testing();
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
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
