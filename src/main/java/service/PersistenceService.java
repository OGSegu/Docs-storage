package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class PersistenceService {

    private static final Path UPLOADED_DIR = Path.of("./data");

    public PersistenceService() throws IOException {
        if (Files.notExists(UPLOADED_DIR)) {
            Files.createDirectories(UPLOADED_DIR);
        }
    }

    public File store(String fileName, InputStream fileContent) throws IOException {
        long timestamp = System.currentTimeMillis();
        Path file = UPLOADED_DIR.resolve(timestamp + "-" + fileName);
        Files.copy(fileContent, file);
        return file.toFile();
    }


}
