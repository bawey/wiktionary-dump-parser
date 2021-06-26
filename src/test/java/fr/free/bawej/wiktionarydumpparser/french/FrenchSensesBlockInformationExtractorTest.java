package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.MarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryEntry;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexicalCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FrenchSensesBlockInformationExtractorTest {

    private FrenchSensesBlockInformationExtractor extractor = new FrenchSensesBlockInformationExtractor();

    public static class ValidHeadersArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("=== {{S|nom|fr|num=1}} ===", LexicalCategory.NOUN),
                    Arguments.of("=== {{S|nom|fr}} ===", LexicalCategory.NOUN),
                    Arguments.of("=== {{S|adjectif|fr|flexion}} ===", LexicalCategory.ADJECTIVE),
                    Arguments.of("=== {{S|locution phrase|fr}} ===", LexicalCategory.LOCUTION),
                    Arguments.of("=== {{S|adjectif|fr}} ===", LexicalCategory.ADJECTIVE),
                    Arguments.of("=== {{S|adverbe|fr}} ===", LexicalCategory.ADVERB),
                    Arguments.of("=== {{S|article indéfini|fr|flexion}} ===", LexicalCategory.DETERMINER),
                    Arguments.of("=== {{S|adjectif|fr|num=2|flexion}} ===", LexicalCategory.ADJECTIVE),
                    Arguments.of("=== {{S|nom|fr|num=1|flexion}} ===", LexicalCategory.NOUN),
                    Arguments.of("=== {{S|nom|fr|genre=m}} ===", LexicalCategory.NOUN)
            );
        }
    }


    @ParameterizedTest
    @ArgumentsSource(ValidHeadersArgumentsProvider.class)
    public void testValidHeaders(String header, LexicalCategory category) {
        MarkupBlock block = new MarkupBlock(header, "");
        assertTrue(extractor.isSupported(Locale.FRENCH, block));
        WiktionaryEntry.Builder builder = new WiktionaryEntry.Builder(Locale.FRENCH, 42, "foo");
        extractor.extractIntoEntryBuilder(block, builder);
        assertEquals(category, builder.build().getDictionaryEntries().iterator().next().getLexicalCategory());
    }
}