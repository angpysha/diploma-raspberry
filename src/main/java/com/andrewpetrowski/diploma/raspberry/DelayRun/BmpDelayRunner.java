package com.andrewpetrowski.diploma.raspberry.DelayRun;

import com.andrewpetrowski.diploma.bridgelib.Controllers.BmpController;
import com.andrewpetrowski.diploma.bridgelib.Models.Bmp180_Data;
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BmpDelayRunner extends DelayRunner<Bmp180_Data> implements IDelayRun {

    private static volatile BmpDelayRunner instance;

    public static BmpDelayRunner getInstance(int delay) {
        BmpDelayRunner localinstance = instance;
        if (localinstance == null) {
            synchronized (BmpDelayRunner.class) {
                localinstance = instance;

                if (localinstance == null) {
                    localinstance = new BmpDelayRunner(delay);
                }
            }
        }
        return localinstance;
    }

    private BmpDelayRunner(int delay) {
        super(delay, (new TypeToken<List<Bmp180_Data>>(){}).getType());
        this.filepath = "~/bmp.json";
    }

    @Override
    public Boolean Send() {
        try {
            BmpController controller = new BmpController();

            Boolean result = controller.AddAsync(data).get();

            if (result) {
                ses.shutdown();
                this.Clear();
                this.isRunning = false;
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
//            e.printStackTrace();
            return false;
        }
    }
}
