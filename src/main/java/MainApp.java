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
            Thread thread = new Thread(r, "One thread for all");
            System.out.println(thread);
            thread.start();
            thread.join();
            System.out.println("Total size is " + reader.getFileSize() + " byte");
            System.out.println("-----------------------------------------------------------");
            FileSystemReader reader1 = new FileSystemReader(new File("C:\\a")).withThreadPerFolder();
            reader1.calculate();
            do{
                Thread.sleep(1000);
            }while(!reader1.completed());
            System.out.println("Total size is " + reader1.getFileSize() + " byte");
            System.out.println("-----------------------------------------------------------");
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(10,20,1, TimeUnit.MINUTES, queue);

            final FileSystemReader reader2 = new FileSystemReader(new File("C:\\a"),executor);
            r = () -> {reader2.calculateByExecutor();};
            thread = new Thread(r, "By ThreadPoolExecutor");
            System.out.println(thread);
            thread.start();
            thread.join(500);
            while(executor.getActiveCount()!= 0){

            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("Total size is " + reader2.getFileSize() + " byte");
            System.out.println("-----------------------------------------------------------");
            //executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
