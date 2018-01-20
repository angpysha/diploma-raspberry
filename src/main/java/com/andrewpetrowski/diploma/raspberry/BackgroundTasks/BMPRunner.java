package com.andrewpetrowski.diploma.raspberry.BackgroundTasks;

import com.andrewpetrowski.diploma.bridgelib.Controllers.BmpController;
import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController;
import com.andrewpetrowski.diploma.bridgelib.Models.Bmp180_Data;
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.andrewpetrowski.diploma.raspberry.DelayRun.BmpDelayRunner;
import com.andrewpetrowski.diploma.raspberry.Sensors.BMP180;
import com.andrewpetrowski.diploma.raspberry.Sensors.DHT11;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BMPRunner implements IEntityRunner<BmpController> {

    private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    private BMP180 bmp180 = new BMP180();
    private BmpController bmpController;
    private BmpDelayRunner delayRunner;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private int interval;

    IBMPCallback callback;

    @Override
    public void Run() {
        float[] data = this.bmp180.GetData();
        Bmp180_Data bmp180_data = new Bmp180_Data(data[0], data[1], data[2]);
        boolean result = false;
        try {
            result = bmpController.AddAsync(bmp180_data).get();
            append(bmp180_data, result);
        } catch (InterruptedException e) {
            result = false;
//            e.printStackTrace();
            append(bmp180_data, result);
        } catch (ExecutionException e) {
            result = false;
//            e.printStackTrace();
            append(bmp180_data, result);
        }

        callback.event(bmp180_data);

    }

    @Override
    public void TimerRunner() {
        ses.scheduleAtFixedRate(this::Run,
                0, interval, TimeUnit.SECONDS);
    }

    public BMPRunner(int time, IBMPCallback callback) {
        bmpController = new BmpController();
        this.callback = callback;
        this.interval = time;
        this.TimerRunner();
        this.delayRunner = BmpDelayRunner.getInstance(60 * 15);

    }

    private void append(Bmp180_Data data, Boolean result) {
        System.out.println(String.format("Result: %s", result.toString()));
        if (!result) {
            delayRunner.AppendNew(data, (new TypeToken<List<Bmp180_Data>>() {
            }).getType());
            if (!delayRunner.getRunning())
                delayRunner.execute();
        }
    }
}
