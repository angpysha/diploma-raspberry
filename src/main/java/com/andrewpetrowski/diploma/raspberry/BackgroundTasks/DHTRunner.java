package com.andrewpetrowski.diploma.raspberry.BackgroundTasks;

import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController;
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.andrewpetrowski.diploma.raspberry.DelayRun.DhtDelayRunner;
import com.andrewpetrowski.diploma.raspberry.Sensors.DHT11;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class, which collect DHT11 data and send them to server
 * @author Andrew Petrowsky
 * @version 1.0
 */
public class DHTRunner implements IEntityRunner<DhtController> {

    private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    private DHT11 dht = new DHT11();
    private DhtController dhtController;
    private DhtDelayRunner delayRunner;
    /**
     * Callback for event handling {@link IDHTCallback}
     */
    IDHTCallback callback;

    /**
     * GPIO pin number for listening
     */
    int pin=7;

    /**
     * Sets pin number
     * @param pin pin number
     */
    public void setPin(int pin) {
        this.pin = pin;
    }

    /**
     * Gets pin number
     * @return pin number
     */
    public int getPin() {
        return pin;
    }

    /**
     * Get DHT11 data once
     */
    @Override
    public void Run() {

        float[] data = dht.readData(pin);
        DHT11_Data sensorData = new DHT11_Data(data[0],data[1]);
    //    System.out.println("timer");
    //    System.out.println(String.format("Temp: %f, hum: %f",data[0],data[1]));
        boolean result = false;
        try {
            result = dhtController.AddAsync(sensorData).get();
//            if (!result){
//                delayRunner.AppendNew(sensorData,(new TypeToken<List<DHT11_Data>>(){}).getType());
//                if (!delayRunner.getRunning())
//                    delayRunner.execute();
//            }
            append(sensorData,result);

        } catch (InterruptedException e) {
            result =false;
            append(sensorData,result);
            //   e.printStackTrace();
        } catch (ExecutionException e) {
            result=false;
            append(sensorData,result);
            //  e.printStackTrace();
        } catch (Exception ex)
        {
            result = false;
            append(sensorData,result);
            //   ex.printStackTrace();
        } finally {

        }

      //  if (result)
           callback.acceptDHT(sensorData);
      // System.out.println("fasd");
    }

    /**
     * Start background worker
     */
    @Override
    public void TimerRunner() {
        ses.scheduleAtFixedRate(this::Run,
                0,interval, TimeUnit.SECONDS);
    }


    /**
     *  Create class instance
     * @param time  Check period (in seconds)
     * @param idhtCallback Class, which implement interface (always this)
     */
    public DHTRunner(int time, IDHTCallback idhtCallback) {
        dhtController = new DhtController();
        callback = idhtCallback;
        this.interval = time;
        this.TimerRunner();
        delayRunner = DhtDelayRunner.getInstance(60*15);
    }

    /**
     * Create class instance
     */
    public DHTRunner() {
        this.TimerRunner();
    }


    /**
     * Update interval
     */
    public int interval = 3600;

    private void append(DHT11_Data data,Boolean result) {
        System.out.println(String.format("Result: %s",result.toString()));
        if (!result){
            delayRunner.AppendNew(data,(new TypeToken<List<DHT11_Data>>(){}).getType());
            if (!delayRunner.getRunning())
                delayRunner.execute();
        }
    }
}
