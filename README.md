# Trie Prefix Matcher

## Overview
This project implements an efficient prefix-matching service using a **Trie (prefix tree)** data structure.  
Given a large list of string prefixes, it can quickly find the **longest prefix** that matches a given input string.
A Trie for O(L) lookups where L is the length of the input string, and builds the Trie once from the prefix list.
The Trie is immutable after creation so it is safe for concurrent lookups.

Example:
- Prefixes: `foo`, `tru`, `true`
- Input: `truecaller`
- Output: `true`


## Features
- Fast lookup using Trie (O(L) per lookup, where L = input length)
- Clean, testable, modular design
- Thread-safe after construction (immutable Trie)
- Robust error handling and clear separation of concerns
- Includes both unit and integration tests


## Character Set Assumptions and Extensibility
The provided sample prefixes are **alphanumeric** only:
- Digits: `0–9`
- Uppercase: `A–Z`
- Lowercase: `a–z`
- One prefix per line, newline-separated.

### Implications
- Trie uses Map<Character, TrieNode> to efficiently handle the 62-character alphanumeric space.
- The structure is mutable during construction, but functionally immutable once built — ensuring thread-safe, read-only lookups.
- No special handling or Unicode normalization is required.

### Memory Optimization Note
For known alphanumeric character sets, a more memory-efficient implementation could store child nodes in a compact array of size 62 instead of a HashMap.
However, for the current dataset (~264k prefixes), using a HashMap is sufficient and provides a good balance of readability, maintainability, and performance.

### Extending for more character types
If prefixes or inputs include punctuation, Unicode, or spaces:
1. No code change is strictly required (Java `char` supports Unicode).
2. For large Unicode sets, consider:
   - Using `TreeMap<Character, Node>` for ordered traversal.
   - Adding normalization (e.g. `Normalizer` for UTF-8).
   - Preprocessing input (e.g., using Normalizer for UTF-8) to reduce character diversity.
3. For extreme multilingual datasets, review performance (larger branching factor).
The current PrefixTrie is fully optimised for alphanumeric datasets but can be easily adapted for wider character sets if needed.


## Key Engineering Decisions and Best Practices Applied

| Aspect | Decision / Practice | Rationale |
| --- | --- | --- |
| Algorithmic correctness & performance | Trie ensures O(L) lookup | Efficient for large prefix sets (~264k prefixes), avoids linear scans over the prefix list for each query |
| Separation of concerns | Loader, Matcher, Trie, and Main are separate classes | Improves maintainability, testability, and makes code easier to extend |
| Thread-safety & immutability | Trie is immutable after construction; PrefixMatcher delegates to it | Safe for concurrent access without locks; predictable behavior; multiple threads can safely call `findLongestPrefix` |
| Concurrency handling | Trie insertion occurs only in constructor; read-only thereafter | Ensures no race conditions; no synchronization overhead needed |
| Logging & debugging | SLF4J logging added for construction metrics and file loads | Provides visibility into the system while keeping production logs clean and optional |
| Simplicity & readability | Small, focused methods and concise classes | Easier to read, reason about, and maintain; aligns with KISS principle |
| Testability | Unit & integration tests using JUnit 5 | Covers expected behavior, edge cases (null/empty inputs), and integration with file-based configuration |
| Error handling | Null and empty checks, skipped empty lines in config loader | Ensures robust and predictable behavior, avoids runtime exceptions |
| Packaging | Standard Maven project layout | Simplifies build, test, and deployment; compatible with CI/CD pipelines |


## Build and Run

### 1.Compile
```
mvn compile
```
### 2.Run all tests
```
mvn test
```
### 3. Running the Demo with Main.java

The `Main.java` class can read input strings in two ways:
1. Command-line arguments – pass the input strings directly when running the program. For example:
```
mvn exec:java -Dexec.mainClass=snkzt.triematcher.Main -Dexec.args="truecaller truancy fooBar unknown"
```
2. Input file – if no command-line arguments are provided, `Main.java` will read inputs from a file located at `src/main/resources/sample_inputs.txt`. Each input should be on a separate line.
Ensure this file exists before running, or the program will throw a `RuntimeException`.

The prefix list is read from `src/main/resources/sample_prefixes.txt`.
Both paths are currently hard-coded for simplicity, but they could be made configurable using environment variables or system properties in a production deployment.


Expected output:
```
input=truecaller -> longestPrefix=true
input=truancy -> longestPrefix=tru
input=fooBar -> longestPrefix=foo
input=unknown -> longestPrefix=null
```

## Project Structure
```
trie-prefix-matcher/
├── README.md
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── snkzt.triematcher
│   │   │       ├── Main.java
│   │   │       ├── PrefixMatcher.java
│   │   │       ├── PrefixTrie.java
│   │   │       └── PrefixConfigLoader.java
│   │   └── resources
│           ├── sample_prefixes.txt   (used by both main code and tests)
│   │       └── sample_inputs.txt (used by Main.java if no args provided)
│   └── test
│       └── java
│           └── snkzt.triematcher
│               ├── PrefixMatcherTest.java
│               ├── PrefixTrieTest.java
│               ├── PrefixConfigLoaderTest.java
│               └── PrefixMatcherIntegrationTest.java
```