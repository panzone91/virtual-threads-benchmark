package it.panzone;

import org.openjdk.jmh.annotations.Benchmark;

public class VirtualThreadsStartup extends VirtualThreadsBenchmark{

    @Benchmark
    public void threadStartup() {
        Runnable r = () -> {};

        for (int i = 0; i < 5000; i++){
            if (useVirtualThread){
                Thread.startVirtualThread(r);
            } else {
                Thread t = new Thread(r);
                t.start();
            }
        }
    }
}
