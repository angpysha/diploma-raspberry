package com.andrewpetrowski.diploma.raspberry.DelayRun;

import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController;
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DhtDelayRunner extends DelayRunner<DHT11_Data> implements IDelayRun {

    private static volatile DhtDelayRunner instance;

    public static DhtDelayRunner getInstance(int delay) {
        DhtDelayRunner localInstance = instance;

        if (localInstance == null) {
            synchronized (DhtDelayRunner.class) {
                localInstance = instance;

                if (localInstance == null) {
                    instance = localInstance = new DhtDelayRunner(delay);
                }
            }
        }
        return localInstance;
    }

    private DhtDelayRunner(int delay) {
        super(delay,(new TypeToken<List<DHT11_Data>>(){}).getType());
        this.filepath = "~/dht.json";
    }



    @Override
    public Boolean Send() {
        System.out.println("Delay running");
        try {
            DhtController controller = new DhtController();
            Boolean  result;
//            if (this.data.size() < 5) {
//                result = false;
//            } else {
                result = controller.AddAsync(data).get();
//            }

            if (result) {
                ses.shutdown();
                this.Clear();
                this.isRunning = false;
                return true;
            }
            else
                return false;
        } catch (InterruptedException e) {
//            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void execute() {
        this.ses.scheduleAtFixedRate(this::Send,0,
                delay, TimeUnit.SECONDS);
        this.isRunning = true;
    }


}
