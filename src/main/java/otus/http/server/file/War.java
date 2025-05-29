package otus.http.server.file;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

public class War {
    public static void extract(Path warPath, Path toPath) {
        URI warUri = URI.create("jar:" + warPath.toUri());
        try (FileSystem warFs = FileSystems.newFileSystem(warUri, Collections.emptyMap())) {
            Path warRoot = warFs.getPath("/");
            Files.walkFileTree(warRoot, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetPath = toPath.resolve(warRoot.relativize(file).toString());
                    Files.createDirectories(targetPath.getParent());
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = toPath.resolve(warRoot.relativize(dir).toString());
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
