package io.github.angpysha.diploma_raspberry.BackgroundTasks;

import io.github.angpysha.diploma_bridge.Models.Bmp180_Data;

public interface IBMPCallback {
    void event(Bmp180_Data data);
}
