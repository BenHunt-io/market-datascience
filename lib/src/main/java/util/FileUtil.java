package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

        public static File createFileIfNotExist(Path filePath){
            try {
                Files.deleteIfExists(filePath);
                return Files.createFile(filePath).toFile();
            } catch (IOException e) {
                System.out.println("error creating directory: " + filePath);
                throw new RuntimeException(e);
            }
    
        }
    
}
