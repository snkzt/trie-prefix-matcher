package snkzt.triematcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * Main class for demoing the PrefixMatcher.
 * 
 * Supports input via command-line arguments or a test input file.
 * Prefix file and input file paths are currently hard-coded for simplicity.
 * You may extend this to use environment variables if deployment requires configurable paths.
 */

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Prefix file path (can be made configurable via env or system properties if needed)
        Path prefixFile = Path.of("src/main/resources/sample_prefixes.txt");
        List prefixes = PrefixConfigLoader.loadFromFile(prefixFile);
        logger.info("Loaded {} prefixes from {}", prefixes.size(), prefixFile);

        PrefixMatcher matcher = new PrefixMatcher(prefixes);

        List<String> inputs;
        if (args.length > 0) {
            // Use command-line arguments if provided
            inputs = List.of(args);
        } else {
            // Otherwise, read test inputs from file
            Path inputFile = Path.of("src/main/resources/sample_inputs.txt");
            if (!Files.exists(inputFile)) {
                logger.warn("Input file not found: {}", inputFile);
                throw new RuntimeException("Input file not found: " + inputFile);
            }
            inputs = Files.readAllLines(inputFile);
        }

        for (String input : inputs) {
            String result = matcher.findLongestPrefix(input);
            logger.info("input={} -> longestPrefix={}", input, result);
        }   
    }
}
