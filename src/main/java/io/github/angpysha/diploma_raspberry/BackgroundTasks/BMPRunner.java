package io.github.angpysha.diploma_raspberry.BackgroundTasks;

import io.github.angpysha.diploma_bridge.Controllers.BmpController;
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;
import io.github.angpysha.diploma_raspberry.DelayRun.BmpDelayRunner;
import io.github.angpysha.diploma_raspberry.Sensors.BMP180;
import com.google.gson.reflect.TypeToken;
import io.github.angpysha.diploma_raspberry.Socket.Socket;
import io.github.angpysha.diploma_raspberry.SocketActions.IPressureBase;
import io.github.angpysha.diploma_raspberry.SocketActions.PressureNoAction;
import io.socket.emitter.Emitter;

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
    private Socket socket;
    private IPressureBase pressureAction = new PressureNoAction();

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
        if (callback != null)
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

    public BMPRunner(int time, Socket socket, IBMPCallback callback) {
        bmpController = new BmpController();
        this.callback = callback;
        this.interval = time;
        this.socket = socket;
        if (socket != null) {
            socket.socketio.on("changepressure", ChangePressure);
        }
        this.TimerRunner();
        this.delayRunner = BmpDelayRunner.getInstance(60 * 15);
    }

    public BMPRunner(IBMPCallback callback) {
        bmpController = new BmpController();
        this.callback = callback;
        this.socket = Socket.getInstanse("https://raspi-info-bot.herokuapp.com/");
    }

    private Emitter.Listener ChangePressure = args -> {
        pressureAction.ChangeState();
    };

    private void append(Bmp180_Data data, Boolean result) {
        System.out.println(String.format("Result: %s", result.toString()));
        if (!result) {
            delayRunner.AppendNew(data, (new TypeToken<List<Bmp180_Data>>() {
            }).getType());
            if (!delayRunner.getRunning())
                delayRunner.execute();
        }
    }

    public IPressureBase getPressureAction() {
        return pressureAction;
    }

    public void setPressureAction(IPressureBase pressureAction) {
        this.pressureAction = pressureAction;
    }
}
