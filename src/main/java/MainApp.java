import java.io.File;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainApp {
    public static void main(String[] args) {
        try {
            final FileSystemReader reader = new FileSystemReader(new File("C:\\a"));
            Runnable r = () -> {reader.calculate();};
            long initTime = System.currentTimeMillis();
            Thread thread = new Thread(r, "One thread for all");
            System.out.println(thread);
            thread.start();
            thread.join();
            long endTime = System.currentTimeMillis();
            //System.out.println("Total size is " + reader.getFileSize()/(1024*1024) + " Mb");
            System.out.println("Total size is " + reader.getFileSize() + " byte");
            System.out.println("-----------------------------------------------------------");
            final FileSystemReader reader1 = new FileSystemReader(new File("C:\\a")).withThreadPerFolder();
            r = () -> {reader1.calculate();};
            thread = new Thread(r, "One thread per folder");
            System.out.println(thread);
            thread.start();
            thread.join();
//            System.out.println("Total size is " + reader.getFileSize()/(1024*1024) + " Mb");

            System.out.println("Total size is " + reader.getFileSize() + " byte");
            System.out.println("-----------------------------------------------------------");
            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(10,20,1, TimeUnit.MINUTES, queue);

            final FileSystemReader reader2 = new FileSystemReader(new File("C:\\a"),executor);
            r = () -> {reader2.calculateByExecutor();};
            thread = new Thread(r, "By ThreadPoolExecutor");
            System.out.println(thread);
            thread.start();
            thread.join();
//            System.out.println("Total size is " + reader.getFileSize()/(1024*1024) + " Mb");
            executor.shutdown();
            System.out.println("Total size is " + reader.getFileSize() + " byte");
            System.out.println("-----------------------------------------------------------");

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //использовать AtomicLong
        // LinkedBlockingQueue
    }
}
