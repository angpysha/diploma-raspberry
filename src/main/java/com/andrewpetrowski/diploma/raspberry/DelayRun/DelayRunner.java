package com.andrewpetrowski.diploma.raspberry.DelayRun;

import com.andrewpetrowski.diploma.bridgelib.Helpers.EntitySerializer;
import com.andrewpetrowski.diploma.bridgelib.Models.Entity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class DelayRunner<T extends Entity> {
    protected int delay;
    protected String filepath;
    protected List<T> data;
    protected Boolean isRunning = false;

    EntitySerializer<T> serializer;


    public Boolean getRunning() {
        return isRunning;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public DelayRunner(int delay,Type type)
    {
        this.delay = delay;
        this.data = new ArrayList<>();
        this.serializer = new EntitySerializer<>();

        Init(type);
    }

    protected void Init(Type type) {
        try {
            this.data = serializer.FromJsonFileToListAsync(filepath,type).get();
        } catch (Exception ex) {

        }
    }

    public Boolean AppendNew(T entity, Type type) {
        try {
            data.add(entity);
            serializer.ListToJsonFileAsync(data,filepath);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Boolean Clear() {
        try {
            File file =new File(filepath);
            file.delete();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<T> getData() {
        return data;
    }
}
