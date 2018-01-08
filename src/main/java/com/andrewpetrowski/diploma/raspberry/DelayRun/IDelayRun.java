package com.andrewpetrowski.diploma.raspberry.DelayRun;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface IDelayRun {
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    Boolean Send();

    default void execute() {
        this.ses.scheduleAtFixedRate(this::Send,0,
                60*10, TimeUnit.SECONDS);
    }
}
