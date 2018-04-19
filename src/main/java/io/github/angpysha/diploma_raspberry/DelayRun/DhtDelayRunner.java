package io.github.angpysha.diploma_raspberry.DelayRun;


import com.google.gson.reflect.TypeToken;
import io.github.angpysha.diploma_bridge.Controllers.DhtController;
import io.github.angpysha.diploma_bridge.Models.DHT11_Data;
import io.github.angpysha.diploma_bridge.Models.DhtSearch;
import io.github.angpysha.diploma_raspberry.AppConfig;

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
        try {
            DhtController controller = new DhtController();
            AppConfig config = AppConfig.getInstanse("appconfig.ini");
            controller.setBaseUrl(config.getapiUrl());
            Boolean  result;
//            if (this.data.size() < 5) {
//                result = false;
//            } else {

            for (DHT11_Data el:data) {
                if (controller.GetCount(el,DHT11_Data.class, DhtSearch.class) > 0) {
                    data.remove(el);
                }
            }
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
        } catch (NoSuchMethodException e) {
       //     e.printStackTrace();
            return true;
        }
    }

    @Override
    public void execute() {
        this.ses.scheduleAtFixedRate(this::Send,0,
                delay, TimeUnit.SECONDS);
        this.isRunning = true;
    }


}
