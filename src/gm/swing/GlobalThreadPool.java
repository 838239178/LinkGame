package gm.swing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 本程序的线程池，使用的是CachedThreadPool
 *
 * @author 施嘉宏
 */
public enum GlobalThreadPool {
    INSTANCE;

    private final ExecutorService pool = Executors.newCachedThreadPool();

    public void submit(Runnable task) {
        pool.submit(task);
    }
}
