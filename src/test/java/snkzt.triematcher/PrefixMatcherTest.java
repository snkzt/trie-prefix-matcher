package snkzt.triematcher;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class PrefixMatcherTest {

    @Test
    void testFindLongestPrefix() {
        PrefixMatcher matcher = new PrefixMatcher(List.of("foo", "tru", "true"));
        assertEquals("true", matcher.findLongestPrefix("truecaller"));
        assertEquals("tru", matcher.findLongestPrefix("trumpet"));
        assertNull(matcher.findLongestPrefix("bar"));
    }

     @Test
    void mainPrintsEmptyAndNotFoundRows() throws Exception {
        String[] args = {"foo", "", "bar"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try (PrintStream print = new PrintStream(out)) {
            System.setOut(print);
            Main.main(args);
        } finally {
            System.setOut(original);
        }

        String output = out.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("(empty)"));
        assertTrue(output.contains("Matching prefix not found"));
        assertTrue(output.contains("foo"));
    }
}
