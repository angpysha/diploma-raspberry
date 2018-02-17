package io.github.angpysha.diploma_raspberry.Socket;

import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.BMPRunner;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.DHTRunner;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.IBMPCallback;
import io.github.angpysha.diploma_raspberry.BackgroundTasks.IDHTCallback;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

public class Socket {
    private static volatile Socket _socket;
    public io.socket.client.Socket socketio;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Socket getInstanse(String url) {
        Socket localinstanse = _socket;

        if (localinstanse == null) {
            synchronized (Socket.class) {
                localinstanse = _socket;

                if (localinstanse == null) {
                    localinstanse = new Socket(url);
                }
            }
        }

        return localinstanse;
    }

    private Socket(String url) {
        try {
            socketio = IO.socket(url);
            if (!socketio.connected())
                socketio.connect();
            socketio.on(io.socket.client.Socket.EVENT_CONNECT, OnConnect);
            socketio.on("update", UpdateData);
        } catch (URISyntaxException e) {
            System.out.print(e.getMessage());
        }
    }

    private Emitter.Listener OnConnect = args -> {
        System.out.println("Socket connected");
    };

    private Emitter.Listener UpdateData = args -> {
        //   System.out.println("socket");
        new BMPRunner(this.ibmpCallback).Run();
        new DHTRunner(this.idhtCallback).Run();
        socketio.emit("updated", "data");
    };

    private IBMPCallback ibmpCallback = data -> {
        System.out.println(String.format("%s: Temperature: %f: Pressure: %f, Altitude: %f",
                df.format(data.getCreated_at()), data.getTemperature(), data.getPressure(), data.getAltitude()));
    };

    private IDHTCallback idhtCallback = data -> {
        System.out.println(String.format("%s: Temperature: %f; Humidity: %f"
                , df.format(data.getCreated_at())
                , data.getTemperature()
                , data.getHumidity()));
    };

}
