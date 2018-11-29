import java.io.File;
import java.util.function.Consumer;

public class Task implements Runnable{
    private File file;
    private Consumer<Long> consumer;
    private long fileSize = 0;
    public Task(File file, Consumer<Long> consumer) {
        this.file = file;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        fileSize = file.length();
        consumer.accept(fileSize);
    }

    public long getFileSize() {
        return fileSize;
    }

}
