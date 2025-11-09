package snkzt.triematcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


/**
 * Main class for running the PrefixMatcher application.
 * 
 * Supports input via command-line arguments or a test input file.
 * Prefix file and input file paths are currently hard-coded for simplicity.
 * You may extend this to use environment variables if deployment requires configurable paths.
 */

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Using classpath resource so it works both in development and when running from JAR
        List<String> prefixes = PrefixConfigLoader.loadFromResource("sample_prefixes.txt");

        PrefixMatcher matcher = new PrefixMatcher(prefixes);

        List<String> inputs;
        if (args.length > 0) {
            // Use command-line arguments if provided
            inputs = List.of(args);
        } else {
            // Otherwise, read test inputs from classpath resource
            try {
                inputs = PrefixConfigLoader.loadFromResource("sample_inputs.txt");
            } catch (Exception e) {
                logger.warn("Input file not found: sample_inputs.txt");
                throw new RuntimeException("Input file not found: sample_inputs.txt", e);
            }
        }

        System.out.println("=================== Prefix Match Results ===================");
        System.out.printf("%-20s | %s%n", "Input", "Longest Matching Prefix");
        System.out.println("------------------------------------------------------------");

        for (String input : inputs) {
            // Skip empty strings as they have no meaningful prefix to match
            if (input == null || input.isEmpty()) {
                continue;
            }
            String result = matcher.findLongestPrefix(input);
            System.out.printf("%-20s | %s%n", input, result);
        }

        System.out.println("============================================================");
    }
}
