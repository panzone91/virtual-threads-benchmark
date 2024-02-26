package it.panzone;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;

import java.util.concurrent.CountDownLatch;

public class VirtualThreadsBlockingOperations extends VirtualThreadsBenchmark {

    @Param({"100"})
    private int parallelTasks;
    @Benchmark
    public void blockingTask() {
        CountDownLatch countDownLatch = new CountDownLatch(parallelTasks);
        for(int i = 0; i< parallelTasks; i++){
            this.runner.accept(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}
