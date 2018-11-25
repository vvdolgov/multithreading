import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class FileSystemReader{
    private File file;
    private long fileSize = 0;
    private boolean threadPerFolder = false;
    private AtomicLong along = new AtomicLong(fileSize);
    private ThreadPoolExecutor executor;

    public FileSystemReader(File file, ThreadPoolExecutor executor) {
        this.file = file;
        this.executor = executor;
    }

    public FileSystemReader(File file){
        this.file = file;
    }

    public long getFileSize() {
        return fileSize;
    }

    public FileSystemReader withThreadPerFolder(){
        this.threadPerFolder = true;
        return this;
    }

    public void calculate(){
        if(!file.isDirectory()){
            if(along.compareAndSet(fileSize, fileSize + (file.length()))) {
                fileSize += (file.length());
            }
        }
        else{
            if(file.listFiles().length>0) {
                for (File ch : file.listFiles()) {
                    FileSystemReader reader = new FileSystemReader(ch);
                    if(threadPerFolder == true) {
                        reader.withThreadPerFolder();
                        Runnable r = () -> {
                            reader.calculate();
                            if(along.compareAndSet(fileSize, fileSize + (reader.getFileSize()))) {
                                fileSize += (reader.getFileSize());
                            }
                        };
                        Thread t = new Thread(r);
                        t.start();
                    }
                    else{
                        reader.calculate();
                        fileSize += (reader.getFileSize());
                    }
                }
            }
        }
    }

    public void calculateByExecutor(){
        if(!file.isDirectory()){
            Task task = new Task(file);
            executor.execute(task);
            if(along.compareAndSet(fileSize, fileSize + task.getFileSize())) {
                fileSize += task.getFileSize();
            }
        }
        else{
            for (File ch : file.listFiles()){
                FileSystemReader reader = new FileSystemReader(ch, executor);
                reader.calculateByExecutor();
            }
        }
    }
}
