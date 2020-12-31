package gm.swing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum GlobalThreadPool {
    INSTANCE;

    private final ExecutorService pool = Executors.newCachedThreadPool();

    public void submit(Runnable task) {
        pool.submit(task);
    }
}
