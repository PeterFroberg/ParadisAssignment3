package paradis.assignment3;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Program3 {
    final static int NUM_WEBPAGES = 40;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
    // [You are welcome to add some variables.]
    private static LinkedBlockingQueue<WebPage> toDownloadQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<WebPage> toAnalyzeQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<WebPage> toCategorizeQueue = new LinkedBlockingQueue<WebPage>();
    private static LinkedBlockingQueue<WebPage> toPrintQueue = new LinkedBlockingQueue<WebPage>();
    private static ExecutorService executor = ForkJoinPool.commonPool();
    private static LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
            try {
                toDownloadQueue.put((webPages[i]));
            } catch (InterruptedException e) {

            }
        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages() {
        WebPage webPage = toDownloadQueue.poll();
        if (webPage != null) {
            Runnable download = () -> {
                webPage.download();
                try {
                    toAnalyzeQueue.put(webPage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executor.submit(download);
        }
    }

    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        WebPage webPage = toAnalyzeQueue.poll();
        if (webPage != null) {
            Runnable analyze = () -> {
                webPage.analyze();
                try {
                    toCategorizeQueue.put(webPage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executor.submit(analyze);
        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        WebPage webPage = toCategorizeQueue.poll();
        if (webPage != null) {
            Runnable categorize = () -> {
                webPage.categorize();
                try {
                    toPrintQueue.put(webPage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executor.submit(categorize);
        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages[i]);
        }
    }

    private static class MyExecutor implements ExecutorService {
        boolean running;

        Thread[] threads;


        public MyExecutor(int numThreads) {
            threads = new Thread[numThreads];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread();
            }
            initiateThreads();

        }

        @Override
        public void shutdown() {
            running = false;

        }


        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
            return false;
        }

        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            return null;
        }

        @Override
        public <T> Future<T> submit(Runnable runnable, T t) {
            return null;
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void execute(Runnable runnable) {
            runnables.add(runnable);
/*            runnable.run();
            for (int i = 0; i < threads.length; i++) {
                if (threads[i] == null || !threads[i].isAlive()) {
                    threads[i] = new Thread(runnable);
                    threads[i].start();
                }
            }*/


        }

        public void initiateThreads() {
            running = true;
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    Runnable runnable = () -> {
                        System.out.println("init");
                        while (running) {

                            Runnable runnable1 = runnables.poll();
                            if (runnable1 != null)
                                runnable1.run();

                        }


                    };
                    runnable.run();

                });
                threads[i].start();

            }

        }

/*        public void runRunnables() throws InterruptedException {
            Runnable runnable = runnables.take();
            for (Thread thread : threads){
                if(!thread.isAlive()){
                    thread = new Thread(runnable).start();
                }
            }
            runnable.run();
        }*/
    }


    public static void main(String[] args) {
        // Initialize the list of webpages.
        initialize();

        // Start timing.
        long start = System.nanoTime();

        // Do the work.

        while (toPrintQueue.size() < NUM_WEBPAGES) {
            downloadWebPages();
            analyzeWebPages();
            categorizeWebPages();
        }

        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();

        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
    }
}
