package otus.http.server.file;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class War {
    public static void extract(Path warPath, Path toPath) {
        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(warPath));
             TarArchiveInputStream tar = new TarArchiveInputStream(inputStream)) {
            ArchiveEntry entry;
            while ((entry = tar.getNextEntry()) != null) {
                Path extractTo = toPath.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(extractTo);
                } else {
                    Files.copy(tar, extractTo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
