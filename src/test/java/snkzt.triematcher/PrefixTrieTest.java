package snkzt.triematcher;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PrefixTrieTest {

    @Test
    void testLongestPrefix() {
        PrefixTrie trie = new PrefixTrie(List.of("foo", "tru", "true"));

        assertEquals("true", trie.findLongestPrefix("truecaller"));
        assertEquals("tru", trie.findLongestPrefix("truancy"));
        assertEquals("foo", trie.findLongestPrefix("foobar"));
        assertNull(trie.findLongestPrefix("unknown"));
    }

    @Test
    void testEmptyAndNullInputs() {
        PrefixTrie trie = new PrefixTrie(List.of()); // empty prefix list

        assertNull(trie.findLongestPrefix(""));
        assertNull(trie.findLongestPrefix(null));
    }
}
