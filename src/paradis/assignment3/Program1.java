package paradis.assignment3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Program1 {
    final static int NUM_WEBPAGES = 40;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
    // [You are welcome to add some variables.]
    private static BlockingQueue<WebPage> toDownloadQueue = new ArrayBlockingQueue<WebPage>(NUM_WEBPAGES);
    private static BlockingQueue<WebPage> toAnalyzeQueue = new ArrayBlockingQueue<WebPage>(NUM_WEBPAGES);
    private static BlockingQueue<WebPage> toCategorizeQueue = new ArrayBlockingQueue<WebPage>(NUM_WEBPAGES);
    private static ExecutorService executor = ForkJoinPool.commonPool();

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

        while (true) {
            Runnable download = () -> {
                try {
                    WebPage webPage = toDownloadQueue.take();
                    webPage.download();
                    toAnalyzeQueue.add(webPage);

                } catch (InterruptedException e) {
                    System.out.printf(e.getMessage());
                }
            };
            //WebPage webPageToDownLoad = toDownloadQueue.take();
            //webPageToDownLoad.download();
            executor.submit(download);
        }

        //webPages[i].download();


    }

    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].analyze();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].categorize();
        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages[i]);
        }
    }

    public static void main(String[] args) {


        // Initialize the list of webpages.
        initialize();

        // Start timing.
        long start = System.nanoTime();

        // Do the work.

        downloadWebPages();

        //analyzeWebPages();
        //categorizeWebPages();

        // Stop timing.
        long stop = System.nanoTime();

        // Present the result.
        presentResult();

        // Present the execution time.
        System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
    }


}
