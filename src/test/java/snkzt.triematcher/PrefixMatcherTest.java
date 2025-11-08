package snkzt.triematcher;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PrefixMatcherTest {

    @Test
    void testFindLongestPrefix() {
        PrefixMatcher matcher = new PrefixMatcher(List.of("foo", "tru", "true"));
        assertEquals("true", matcher.findLongestPrefix("truecaller"));
        assertEquals("tru", matcher.findLongestPrefix("trumpet"));
        assertNull(matcher.findLongestPrefix("bar"));
    }
}
