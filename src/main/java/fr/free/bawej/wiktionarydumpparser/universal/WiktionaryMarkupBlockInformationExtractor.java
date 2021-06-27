package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Locale;

public interface WiktionaryMarkupBlockInformationExtractor {
    public boolean isSupported(Locale language, WiktionaryMarkupBlock block);
    public void extractIntoEntryBuilder(WiktionaryMarkupBlock block, WiktionaryEntry.Builder builder);
}
