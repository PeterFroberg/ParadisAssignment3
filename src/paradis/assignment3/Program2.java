package paradis.assignment3;

import java.util.ArrayList;
import java.util.List;

public class Program2 {
    final static int NUM_WEBPAGES = 40;
    private static List<WebPage> webPages= new ArrayList<>();
    // [You are welcome to add some variables.]

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.set(i, new WebPage(i, "http://www.site.se/page" + i + ".html"));
        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.get(i).download();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.get(i).analyze();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages.get(i).categorize();
        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages.get(i));
        }
    }

    private static void letTheMAgicHappen(WebPage webpage){
        webpage.download();
        webpage.analyze();
        webpage.categorize();
    }

    public static void main(String[] args) {
        // Initialize the list of webpages.
        initialize();

        // Start timing.
        long start = System.nanoTime();

        // Do the work.
        webPages.parallelStream().forEach(Program2::letTheMAgicHappen);
        //downloadWebPages();
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
