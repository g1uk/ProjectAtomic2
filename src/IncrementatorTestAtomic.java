import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IncrementatorTestAtomic {
    private static final int AMOUNT_OF_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            IncrementatorAtomic increment = new IncrementatorAtomic();

            Runnable task = () -> {
                while (increment.incrementatorAtomic()) ;
            };
            List<Thread> threads = new ArrayList<>();

            for (int j = 0; j < AMOUNT_OF_THREADS; j++) {
                Thread thread = new Thread(task);
                thread.start();
                threads.add(thread);

            }


            threads.stream().forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {

                }

            });

            if (increment.inc.get() != IncrementatorAtomic.AMOUNT_OF_ITERATIONS) {
                System.out.println("ERROR " + increment.inc.get());
                break;
            } else System.out.println("Iteration number " + i);
        }
    }
}

class IncrementatorAtomic {
    public AtomicInteger inc = new AtomicInteger(0);
    public static final int AMOUNT_OF_ITERATIONS = 1000000;

    public boolean incrementatorAtomic () {

        while (true) {
            int expect = inc.get();
            if (expect < AMOUNT_OF_ITERATIONS) {
                int update = expect + 1;
                if (inc.compareAndSet(expect, update)) {
                    return true;
                }
            } return false;
        }
    }
}

