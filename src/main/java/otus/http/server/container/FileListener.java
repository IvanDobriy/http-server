package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class FileListener {
    public interface Callback {
        void execute(Path path);
    }

    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final ExecutorService executorService;
    private final WatchService watchService;
    private final Path path;
    private Callback onCreate = null;
    private Callback onDelete = null;

    public FileListener(Path path) {
        try {
            Objects.requireNonNull(path);
            executorService = Executors.newSingleThreadExecutor();
            watchService = FileSystems.getDefault().newWatchService();
            this.path = path;
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnCreate(Callback callback) {
        this.onCreate = callback;
    }

    public void setOnDelete(Callback callback) {
        this.onDelete = callback;
    }

    public void run() {
        executorService.execute(() -> {
            while (!executorService.isShutdown()) {
                try {
                    final var key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        final var context = (Path) event.context();
                        if (event.kind() == ENTRY_CREATE && onCreate != null) {
                            onCreate.execute(path.resolve(context));
                        } else if (event.kind() == ENTRY_DELETE && onDelete != null) {
                            onDelete.execute(path.resolve(context));
                        }
                    }
                    key.reset();
                } catch (Exception e) {
                    logger.warn("unhandled exception: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
                }
            }
        });
    }

    public void stop() {
        executorService.shutdownNow();
    }
}
