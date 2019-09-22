package io.github.angpysha.diploma_raspberry.Sensors;

import java.lang.reflect.Method;
import io.github.angpysha.diploma_bridge.Models.Entity;

public class Sensor extends Entity {
    public Method method;
    public Object instanse;

    public Sensor(Method method,Object instanse) {
        this.method = method;
        this.instanse = instanse;
    }
}
