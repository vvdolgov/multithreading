import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

public class Task implements Runnable{
    private File file;
    private long fileSize = 0;
    public Task(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        fileSize = file.length();
    }

    public long getFileSize() {
        return fileSize;
    }
}
