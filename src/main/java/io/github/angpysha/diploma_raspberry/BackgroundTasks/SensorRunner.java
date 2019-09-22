package io.github.angpysha.diploma_raspberry.BackgroundTasks;

import io.github.angpysha.diploma_raspberry.Sensors.Sensor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorRunner {
    private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    private List<Sensor> Sensors;


    public SensorRunner(int period,List<Sensor> sensors) {
        this.Sensors = sensors;

        ses.scheduleAtFixedRate(this::Run,0,period, TimeUnit.SECONDS);
    }

    private void Run() {
        for (Sensor sensor: Sensors) {
            try {
                sensor.method.invoke(sensor.instanse);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
