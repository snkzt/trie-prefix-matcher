package snkzt.triematcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PrefixTrie built on a Trie (prefix tree) for efficient prefix lookups.
 *
 * Thread-safety: the trie is built at construction time and then read-only.
 * Multiple threads can call `findLongestPrefix` concurrently.
 * Optimised for alphanumeric character set (0-9, A-Z, a-z).
 */
public final class PrefixTrie {
    private static final Logger logger = LoggerFactory.getLogger(PrefixTrie.class);
    private final TrieNode root;

    public PrefixTrie(Collection<String> prefixes) {
        Objects.requireNonNull(prefixes);
        this.root = new TrieNode();
        for (String p : prefixes) {
            if (p != null && !p.trim().isEmpty()) {
                insert(p.trim());
            }
        }
        root.freeze(); // make immutable after construction
        logger.debug("PrefixTrie constructed with {} prefixes", prefixes.size());
    }

    private void insert(String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            cur = cur.children.computeIfAbsent(c, k -> new TrieNode());
        }
        cur.isWord = true;
    }

    /**
     * Returns the longest prefix matching the input, or null if none.
     */
    public String findLongestPrefix(String input) {
        if (input == null || input.isEmpty()) return null;
        TrieNode cur = root;
        int lastMatchIndex = -1;
        for (int i = 0; i < input.length(); i++) {
            cur = cur.children.get(input.charAt(i));
            if (cur == null) break;
            if (cur.isWord) lastMatchIndex = i;
        }
        return lastMatchIndex == -1 ? null : input.substring(0, lastMatchIndex + 1);
    }

    /**
     * Node in the Trie. isWord indicates end of a prefix.
     */
    static final class TrieNode {
        final Map<Character, TrieNode> children = new HashMap<>();
        boolean isWord = false;

        /**
         * Recursively freezes children nodes to make the Trie immutable.
         */
        void freeze() {
            children.replaceAll((k, v) -> { v.freeze(); return v; });
        }
    }
}
