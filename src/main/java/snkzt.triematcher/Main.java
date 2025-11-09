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
            String displayValue = input == null ? "(null)" : input.isEmpty() ? "(empty)" : input;
            String result = matcher.findLongestPrefix(input);
            String outputValue = (input != null && !input.isEmpty() && result == null)
                    ? "Matching prefix not found"
                    : result;
            System.out.printf("%-20s | %s%n", displayValue, outputValue);
        }

        System.out.println("============================================================");
    }
}
