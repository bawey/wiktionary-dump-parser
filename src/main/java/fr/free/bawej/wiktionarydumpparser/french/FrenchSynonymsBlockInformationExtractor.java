package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryEntry;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkupBlockInformationExtractor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FrenchSynonymsBlockInformationExtractor implements WiktionaryMarkupBlockInformationExtractor {
    // ==== {{S|synonymes}} ====
    @Override
    public boolean isSupported(Locale language, WiktionaryMarkupBlock block) {
        return false;
    }

    @Override
    public void extractIntoEntryBuilder(WiktionaryMarkupBlock block, WiktionaryEntry.Builder builder) {

    }
}
