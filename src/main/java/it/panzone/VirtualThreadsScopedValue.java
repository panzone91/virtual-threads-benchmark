package it.panzone;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@Fork(value = 1, warmups = 1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class VirtualThreadsScopedValue {

    @Param({"1000", "10000"})
    private int parallelTasks;

    private ScopedValue<Integer> scopedValue = ScopedValue.newInstance();

    @Benchmark
    public void withScopedValue() {
        CountDownLatch countDownLatch = new CountDownLatch(parallelTasks);
        Random r = new Random();
        for(int i = 0; i< parallelTasks; i++){
            ScopedValue.where(scopedValue, r.nextInt() % 10).run(()-> {
                Integer tot = 0;
                    for(int k = 0; k < 100; k++) {
                        Integer v = scopedValue.get();
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
}
