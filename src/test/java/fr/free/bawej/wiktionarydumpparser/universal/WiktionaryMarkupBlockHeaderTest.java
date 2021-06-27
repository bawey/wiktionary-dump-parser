package fr.free.bawej.wiktionarydumpparser.universal;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WiktionaryMarkupBlockHeaderTest {

    public static class ValidBlockHeadersProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("== FOO ==", 2, "FOO"),
                    Arguments.of("=== FooBar ===", 3, "FooBar"),
                    Arguments.of("= {{lang=francais}} =", 1, "{{lang=francais}}"),
                    Arguments.of("=== {{S|verbe|fr|flexion|arg=14}} ===", 3, "{{S|verbe|fr|flexion|arg=14}}"));
        }
    }

    public static class InvalidBlockHeadersProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(Arguments.of("==FOO=="), Arguments.of("== FooBar ==="));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ValidBlockHeadersProvider.class)
    public void testValidBlockHeaders(String raw, int expectedLevel, String expectedValue) {
        WiktionaryMarkupBlockHeader header = new WiktionaryMarkupBlockHeader(raw);
        assertEquals(expectedLevel, header.getLevel());
        assertEquals(expectedValue, header.getValue());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidBlockHeadersProvider.class)
    public void testInvalidBlockHeaders(String raw) {
        assertThrows(IllegalArgumentException.class, () -> new WiktionaryMarkupBlockHeader(raw));
    }
}