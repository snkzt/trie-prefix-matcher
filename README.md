# Trie Prefix Matcher


## Overview
This project implements an efficient prefix-matching service using a **Trie (prefix tree)** data structure.  
Given a large list of string prefixes, it can quickly find the **longest prefix** that matches a given input string.
A Trie for O(L) lookups where L is the length of the input string, and builds the Trie once from the prefix list.
The Trie is immutable after creation so it is safe for concurrent lookups.

#### Example:
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
- No special handling or Unicode normalisation is required.

### Memory Optimisation Note
For known alphanumeric character sets, a more memory-efficient implementation could store child nodes in a compact array of size 62 instead of a HashMap.
However, for the current dataset (~264k prefixes), using a HashMap is sufficient and provides a good balance of readability, maintainability and performance.

### Extending for more character types
If prefixes or inputs include punctuation, Unicode, or spaces:
1. No code change is strictly required (Java `char` supports Unicode).
2. For large or highly varied Unicode sets, consider normalising inputs with `java.text.Normalizer` or other pre-processing steps, and ensure the data structure (e.g., switching to `TreeMap`) still meets your ordering or performance needs.
3. For extremely multilingual datasets, review performance (larger branching factor).
The current PrefixTrie is fully optimised for alphanumeric datasets but can be easily adapted for wider character sets if needed.
4. The prefix list is loaded from the classpath resource `sample_prefixes.txt` (located in `src/main/resources/` at build time). Both inputs and prefixes are loaded from classpath resources, which works both in development and when running from a JAR. They could be made configurable using environment variables or system properties in a production deployment.


## Key Engineering Decisions and Best Practices Applied

| Aspect | Decision / Practice | Rationale |
| --- | --- | --- |
| Algorithmic correctness & performance | Trie ensures O(L) lookup | Efficient for large prefix sets (~264k prefixes), avoids linear scans over the prefix list for each query |
| Separation of concerns | Loader, Matcher, Trie, and Main are separate classes | Improves maintainability, testability, and makes code easier to extend |
| Thread-safety & immutability | Trie nodes are frozen with `Map.copyOf` after construction; PrefixMatcher only exposes read operations | Immutability enforced at runtime, so concurrent lookups are safe and accidental mutations fail fast |
| Concurrency handling | Trie is built once in the constructor and then read-only | Eliminates the need for locks while guaranteeing deterministic behaviour |
| Logging & debugging | SLF4J logging added for construction metrics and file loads | Provides visibility into the system while keeping production logs clean and optional |
| Simplicity & readability | Small, focused methods and concise classes | Easier to read, reason about, and maintain; aligns with KISS principle |
| Testability | Unit & integration tests using JUnit 5 | Covers expected behaviour, edge cases (null/empty inputs), CLI formatting, and configuration loading |
| Error handling | Configuration loaders trim blanks and force UTF-8; CLI outputs `(empty)` or “Matching prefix not found” for clarity | Handles malformed data gracefully and keeps user-facing output explicit |
| CI automation | GitHub Actions workflow runs `./mvnw test` on pushes/PRs | Guarantees remote validation of every change before merge |
| Packaging | Standard Maven project layout | Simplifies build, test, and deployment; compatible with CI/CD pipelines |


## Prerequisites
- **Java 17** or later installed on your system.
- The project includes the **Maven Wrapper**, so you do **not** need Maven installed globally.


## Build and Run
***Note:** The following commands should be run from the project root directory*.

### Build the JAR
Build an executable JAR with all dependencies included:
```
# Linux/macOS
./mvnw -q clean package

# Windows
mvnw.cmd -q clean package
```
When to run:
- Rebuild the JAR whenever you modify the source code. 
- This command:
    - Cleans previous build artifacts
    - Compiles the code
    - Runs all tests
    - Packages everything into `target/trie-prefix-matcher-1.0.0.jar` - a standalone, executable JAR file
- If you only need to compile without running tests or packaging, you can use `./mvnw compile` instead.

### Run all tests
```
# Linux/macOS
./mvnw test

# Windows
mvnw.cmd test
```
This command compiles the code and runs all unit and integration tests. Note that `./mvnw -q clean package` (section Build the JAR) also runs tests as part of the build process.

### Run the JAR
The `Main.java` class can read input strings in two ways:

**1. Command-line arguments**
- Pass input strings as separate arguments:
    ```
    java -jar target/trie-prefix-matcher-1.0.0.jar truecaller "" KT528oxLERGI fooBar unknown KAWeqIXYZ
    ```

- Each argument is treated as a separate input string. If an argument contains spaces, quote just that argument:
    ```
    java -jar target/trie-prefix-matcher-1.0.0.jar "true caller" KT528oxLERGI fooBar
    ```

**2. Input file**: If no command-line arguments are provided, the program reads inputs from the classpath resource `sample_inputs.txt` (one input per line, located in `src/main/resources/` at build time):
```
java -jar target/trie-prefix-matcher-1.0.0.jar
```

#### Expected output:
```
=================== Prefix Match Results ===================
Input                | Longest Matching Prefix
------------------------------------------------------------
truecaller           | truecal
(empty)              | null
KT528oxLERGI         | KT528oxL
fooBar               | Matching prefix not found
unknown              | Matching prefix not found
KAWeqIXYZ            | KAWeqI
============================================================
```

***Notes:***
- Clean output (no Maven messages) - only essential results are printed
- Non-empty inputs with no match render `Matching prefix not found`; empty strings appear as `(empty)` for readability
- All dependencies are included in the JAR - only Java is required at runtime
- For very large input sets, redirect output to a file:
    ```
    # For input file
    java -jar target/trie-prefix-matcher-1.0.0.jar > results.txt

    # For command-line arguments
    java -jar target/trie-prefix-matcher-1.0.0.jar truecaller "" KT528oxLERGI fooBar unknown KAWeqIXYZ > results.txt
    ```


## Ignored Files

The following files and directories are excluded from version control (see `.gitignore`):
- `target/` - Maven build output directory (compiled classes, JAR files, test reports)
- `results.txt` - Output file that may be generated when redirecting program output
- `dependency-reduced-pom.xml` - Generated by maven-shade-plugin during JAR packaging

These are build artifacts and generated files that should not be committed to the repository.

## Class Relationships
```
Main
  └─> PrefixConfigLoader (loads prefixes/inputs)
  └─> PrefixMatcher
        └─> PrefixTrie (does the actual matching)
```

## Project Structure
```
trie-prefix-matcher/
├── README.md
├── pom.xml
├── mvnw, mvnw.cmd (Maven Wrapper scripts)
├── .mvn/          (Maven Wrapper configuration)
├── .github/ 
│   └── workflows
│       └── ci.yml (GitHub Actions workflow for CI)
├── .gitignore
├── src
│   ├── main
│   │   ├── java
│   │   │   └── snkzt.triematcher
│   │   │       ├── Main.java
│   │   │       ├── PrefixMatcher.java
│   │   │       ├── PrefixTrie.java
│   │   │       └── PrefixConfigLoader.java
│   │   └── resources
│   │       ├── sample_prefixes.txt
│   │       ├── sample_inputs.txt
│   │       └── simplelogger.properties (configuration file for SLF4J’s SimpleLogger)
│   └── test
│       └── java
│           └── snkzt.triematcher
│               ├── PrefixMatcherTest.java
│               ├── PrefixTrieTest.java
│               ├── PrefixConfigLoaderTest.java
│               └── PrefixMatcherIntegrationTest.java
```