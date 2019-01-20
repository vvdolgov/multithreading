import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FileSystemReader{
    private File file;
//    private long fileSize = 0;
    private boolean threadPerFolder = false;
    private AtomicLong along ;
    private ThreadPoolExecutor executor;
    private AtomicInteger counter;

    public FileSystemReader(File file, ThreadPoolExecutor executor) {
        this.file = file;
        this.executor = executor;
        this.along = new AtomicLong(0);

    }
    public FileSystemReader(File file, ThreadPoolExecutor executor, AtomicLong along) {
        this.file = file;
        this.executor = executor;
        this.along = along;
    }
    public FileSystemReader(File file,  AtomicLong along) {
        this.file = file;
        this.along = along;
    }

    public FileSystemReader(File file, AtomicInteger counter, AtomicLong along){
        this.file = file;
        this.counter = counter;
        this.along = along;
    }



    public FileSystemReader(File file){
        this.file = file;
        this.counter = new AtomicInteger(0);
        this.along = new AtomicLong(0);
    }

    public long getFileSize() {
        return along.get();
    }

    public FileSystemReader withThreadPerFolder(){
        this.threadPerFolder = true;
        return this;
    }

    public void calculate(){
        if(!file.isDirectory()){
            along.addAndGet(file.length());
        }
        else{
            if(file.listFiles().length>0) {
                for (File ch : file.listFiles()) {
                    if(threadPerFolder == true) {
                        FileSystemReader reader = new FileSystemReader(ch, counter, along);
                        reader.withThreadPerFolder();
                        Runnable r = () -> {
                            reader.calculate();
                            counter.decrementAndGet();
                        };
                        counter.incrementAndGet();
                        Thread t = new Thread(r);
                        t.start();
                    }
                    else{
                        FileSystemReader reader = new FileSystemReader(ch, along);
                        reader.calculate();
                    }
                }
            }
        }
    }

    public void calculateByExecutor(){
       Task task = new Task(file, along/*((length)->along.addAndGet(length))*/, executor);
        executor.submit(task);
    }

    public boolean completed(){
        return counter.get() <= 0;
    }
}
