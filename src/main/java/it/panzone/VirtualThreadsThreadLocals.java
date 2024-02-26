package it.panzone;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

public class VirtualThreadsThreadLocals extends VirtualThreadsBenchmark {

    @Param({"1000", "10000"})
    private int parallelTasks;

    private final ThreadLocal<Integer> val = new ThreadLocal<>();

    @Benchmark
    public void withThreadLocals() {
        CountDownLatch countDownLatch = new CountDownLatch(parallelTasks);
        for(int i = 0; i< parallelTasks; i++){
            this.runner.accept(() -> {
                Integer tot = 0;
                for(int k = 0; k < 100; k++) {
                    Blackhole.consumeCPU(10);
                    Integer v = getVal();
                    tot += v;
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

    private Integer getVal(){
        Integer currentVal = val.get();
        if(currentVal != null) {
            return currentVal;
        }
        Integer v = 100;
        val.set(v);
        return v;
    }
}
