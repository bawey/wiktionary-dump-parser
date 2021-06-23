package fr.free.bawej.wiktionarydumpparser.universal;

import fr.free.bawej.wiktionarydumpparser.french.FrenchMarkupChunker;
import fr.free.bawej.wiktionarydumpparser.french.FrenchSensesBlockInformationExtractor;
import fr.free.bawej.wiktionarydumpparser.samples.FrenchSampleMarkup;
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
        WiktionaryEntry entry = converter.convertDumpRecord(new WiktionaryDumpRecord(Locale.FRENCH, 1, "accueil", FrenchSampleMarkup.SAMPLE_M));
        assertNotNull(entry.getSenses());
        assertEquals(2, entry.getSenses().size());
    }
}