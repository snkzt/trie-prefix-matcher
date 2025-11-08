package snkzt.triematcher;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Public-facing class that builds the Trie from configuration and exposes the match method.
 */
public class PrefixMatcher {
    private static final Logger logger = LoggerFactory.getLogger(PrefixMatcher.class);

    private final PrefixTrie trie;

    public PrefixMatcher(List<String> prefixes) {
        this.trie = new PrefixTrie(prefixes); // build trie at construction
        logger.info("PrefixMatcher initialized with {} prefixes", prefixes.size());
    }

    public String findLongestPrefix(String input) {
        return trie.findLongestPrefix(input);
    }
}
