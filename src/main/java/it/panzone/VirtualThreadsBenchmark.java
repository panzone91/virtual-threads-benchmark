package it.panzone;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@BenchmarkMode(Mode.SingleShotTime)
@Fork(value = 1, warmups = 1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public abstract class VirtualThreadsBenchmark {

    @Param({"true", "false"})
    protected boolean useVirtualThread;

    protected Consumer<Runnable> runner;

    @Setup
    public void init(){
        this.runner = getRunner();
    }

    protected Consumer<Runnable> getRunner(){
        if(useVirtualThread){
            return Thread::startVirtualThread;
        } else {
            return Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors())::execute;
        }
    }
}
