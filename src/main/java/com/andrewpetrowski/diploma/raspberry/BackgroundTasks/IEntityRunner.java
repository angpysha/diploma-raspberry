package com.andrewpetrowski.diploma.raspberry.BackgroundTasks;

import com.andrewpetrowski.diploma.bridgelib.Controllers.BaseController;

/**
 * This interface provides all methods declarations, which needed to
 * background run
 * @param <T> Entity class
 */
public interface IEntityRunner<T extends BaseController> {

    /**
     * Run method
     * <i>In this method you should implement Task execution</i>
     */
    void Run();

    /**
     * Run Task in background by time
     */
    void TimerRunner();
}
