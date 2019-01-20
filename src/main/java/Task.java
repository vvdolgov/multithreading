import java.io.File;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class Task implements Runnable{
    private File file;
    private Consumer<Long> consumer;
    private long fileSize = 0;
    ThreadPoolExecutor executor;
    AtomicLong along;
    public Task(File file, AtomicLong along,/*Consumer<Long> consumer,*/ ThreadPoolExecutor executor) {
        this.file = file;
        //this.consumer = consumer;
        this.executor = executor;
        this.along = along;
    }

    @Override
    public void run() {
        if(!file.isDirectory()){
            fileSize = file.length();
            along.addAndGet(fileSize);
            //System.out.println(along.get());
        }
        else{
            for (File ch : file.listFiles()){
                Task task = new Task(ch, along, executor);
                executor.submit(task);
            }
        }
    }

    public long getFileSize() {
        return fileSize;
    }

}
