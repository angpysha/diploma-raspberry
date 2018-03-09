package io.github.angpysha.diploma_raspberry.DelayRun;

import io.github.angpysha.diploma_bridge.Controllers.BmpController;
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;
import com.google.gson.reflect.TypeToken;
import io.github.angpysha.diploma_raspberry.AppConfig;

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
            AppConfig config = AppConfig.getInstanse("appconfig.ini");
            controller.setBaseUrl(config.getapiUrl());
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
