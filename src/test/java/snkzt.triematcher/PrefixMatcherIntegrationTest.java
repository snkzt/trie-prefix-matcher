package com.example.triematcher;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class PrefixMatcherIntegrationTest {
    @Test
    void testIntegrationWithFileLoad() throws IOException {
        Path file = Path.of("src/main/resources/sample_prefixes.txt");
        List<String> prefixes = PrefixConfigLoader.loadFromFile(file);
        PrefixMatcher matcher = new PrefixMatcher(prefixes);

        // Example test
        assertEquals("KAWeq", matcher.findLongestPrefix("KAWeqXYZ"));
        assertNull(matcher.findLongestPrefix("zzzzzz"));
    }
}
