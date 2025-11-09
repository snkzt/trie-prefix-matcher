package snkzt.triematcher;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class PrefixConfigLoaderTest {

    @Test
    void testLoadFromFile_trimsAndIgnoresEmptyLines() throws Exception {
        // Create a temporary file for testing
        Path tempFile = Files.createTempFile("prefixes", ".txt");
        Files.write(tempFile, List.of(" foo ", "", "bar", "   ", "baz"));

        List<String> loaded = PrefixConfigLoader.loadFromFile(tempFile);

        // Verify trimming and filtering
        assertEquals(3, loaded.size());
        assertEquals(List.of("foo", "bar", "baz"), loaded);

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testLoadFromFile_emptyFile() throws Exception {
        Path tempFile = Files.createTempFile("empty", ".txt");
        Files.write(tempFile, List.of());

        List<String> loaded = PrefixConfigLoader.loadFromFile(tempFile);
        assertTrue(loaded.isEmpty());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testLoadFromFile_fileNotFound() {
        Path missingFile = Path.of("nonexistent.txt");
        assertThrows(Exception.class, () -> PrefixConfigLoader.loadFromFile(missingFile));
    }
}
