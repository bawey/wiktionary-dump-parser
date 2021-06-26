package fr.free.bawej.wiktionarydumpparser.universal;

import fr.free.bawej.wiktionarydumpparser.french.FrenchMarkupChunker;
import fr.free.bawej.wiktionarydumpparser.french.FrenchSensesBlockInformationExtractor;
import fr.free.bawej.wiktionarydumpparser.samples.FrenchSampleMarkup;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.Gender;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalType;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexicalCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

// TODO: should use some mocks instead and the code below should go to some integration test
//
class WiktionaryDumpConverterImplTest {
    private WiktionaryDumpConverter converter = new WiktionaryDumpConverterImpl(List.of(new FrenchMarkupChunker()), List.of(new FrenchSensesBlockInformationExtractor()));

    @Test
    public void testWhoKnowsWhat(){
        WiktionaryEntry entry = converter.convertDumpRecord(new WiktionaryDumpRecord(Locale.FRENCH, 1, "accueil", FrenchSampleMarkup.SAMPLE_M)).iterator().next();
        assertEquals(1, entry.getDictionaryEntries().size());
        DictionaryEntry dictEntry = entry.getDictionaryEntries().iterator().next();
        assertEquals(LexicalCategory.NOUN, dictEntry.getLexicalCategory());
        assertTrue(dictEntry.getGrammaticalSpecification().is(Gender.MASCULINE));
        assertEquals(2, dictEntry.getSenses().size());
    }
}