package it.panzone;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

public class VirtualThreadsPreEmptive extends VirtualThreadsBenchmark {

    @Param({"10000"})
    private int parallelTasks;
    @Benchmark
    public void cpuHeavyTask() {
        CountDownLatch countDownLatch = new CountDownLatch(parallelTasks);
        for(int i = 0; i< parallelTasks; i++){
            this.runner.accept(() -> {
                Blackhole.consumeCPU(500);
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
