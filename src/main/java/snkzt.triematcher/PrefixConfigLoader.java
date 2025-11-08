package snkzt.triematcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class PrefixConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(PrefixConfigLoader.class);

    private PrefixConfigLoader() {}

    /**
     * Loads a list of prefixes from a file.
     * Trims whitespace and ignores empty lines.
     */
    public static List<String> loadFromFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            logger.warn("Prefix file not found: {}", path);
            throw new IOException("Prefix file not found: " + path);
        }

        try (var lines = Files.lines(path)) {
            List<String> prefixes = lines.map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());
            logger.info("Loaded {} prefixes from {}", prefixes.size(), path);
            return prefixes;
        }
    }
}
