package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.MarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryEntry;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkupBlockInformationExtractor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FrenchTranslationsBlockInformationExtractor implements WiktionaryMarkupBlockInformationExtractor {
    @Override
    public boolean isSupported(Locale language, MarkupBlock block) {
        return false;
    }

    @Override
    public void extractIntoEntryBuilder(MarkupBlock block, WiktionaryEntry.Builder builder) {

    }
}
