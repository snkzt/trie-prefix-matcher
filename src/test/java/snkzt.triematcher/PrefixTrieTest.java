package snkzt.triematcher;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    @Test
    void testTrieChildrenAreImmutableAfterConstruction() throws Exception {
        PrefixTrie trie = new PrefixTrie(List.of("foo"));

        Field rootField = PrefixTrie.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object root = rootField.get(trie);

        Field childrenField = PrefixTrie.TrieNode.class.getDeclaredField("children");
        childrenField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Character, PrefixTrie.TrieNode> rootChildren =
                (Map<Character, PrefixTrie.TrieNode>) childrenField.get(root);
        PrefixTrie.TrieNode existingNode = rootChildren.values().iterator().next();

        assertThrows(UnsupportedOperationException.class, () -> rootChildren.put('x', existingNode));
    }
}
