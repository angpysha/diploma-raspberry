package com.andrewpetrowski.diploma.raspberry.BackgroundTasks;

import com.andrewpetrowski.diploma.bridgelib.Models.Bmp180_Data;

public interface IBMPCallback {
    void event(Bmp180_Data data);
}
