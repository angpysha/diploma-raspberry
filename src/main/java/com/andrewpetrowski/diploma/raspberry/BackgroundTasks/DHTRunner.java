package com.andrewpetrowski.diploma.raspberry.BackgroundTasks;

import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController;
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.andrewpetrowski.diploma.raspberry.Sensors.DHT11;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DHTRunner implements IEntityRunner<DhtController> {

    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    DHT11 dht = new DHT11();
    DhtController dhtController;
    IDHTCallback callback;

    int pin=7;

    @Override
    public void Run() {

        float[] data = dht.readData(pin);
        DHT11_Data sensorData = new DHT11_Data(data[0],data[1]);
    //    System.out.println("timer");
    //    System.out.println(String.format("Temp: %f, hum: %f",data[0],data[1]));
        boolean result = false;
        try {
            result = dhtController.AddAsync(sensorData).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

      //  if (result)
           callback.acceptDHT(sensorData);
      // System.out.println("fasd");
    }

    @Override
    public void TimerRunner() {
        ses.scheduleAtFixedRate(this::Run,
                0,interval, TimeUnit.SECONDS);
    }

    public DHTRunner(int time, IDHTCallback idhtCallback) {
        dhtController = new DhtController();
        callback = idhtCallback;
        this.interval = time;
        this.TimerRunner();
    }

    public DHTRunner() {
        this.TimerRunner();
    }


    public int interval = 3600;

}
