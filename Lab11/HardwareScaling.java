public class HardwareScaling {

    static class MathTask implements Runnable {
        private final int iterations = 10_000_000;

        @Override
        public void run() {
            double result = 0;
            for (int i = 0; i < iterations; i++) {
                result += Math.pow(i, 3) + (i * i);
            }
            if (result == -1) System.out.println(result);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("--- Hardware Awareness Report ---");
        System.out.println("Available Logical Processors: " + coreCount);
        System.out.println("---------------------------------\n");

        System.out.println("Starting Single-Threaded Test...");
        long startSingle = System.currentTimeMillis();
        runWorkload(1);
        long endSingle = System.currentTimeMillis();
        long durationSingle = endSingle - startSingle;
        System.out.println("Finished in: " + durationSingle + "ms\n");

        System.out.println("Starting Multi-Threaded Test (" + coreCount + " threads)...");
        long startMulti = System.currentTimeMillis();
        runWorkload(coreCount);
        long endMulti = System.currentTimeMillis();
        long durationMulti = endMulti - startMulti;
        System.out.println("Finished in: " + durationMulti + "ms\n");

        double speedup = (double) durationSingle / durationMulti;
        System.out.println("--- Results ---");
        System.out.printf("Hardware Scaling Efficiency: %.2fx faster\n", speedup);
    }


    private static void runWorkload(int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MathTask());
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }
}