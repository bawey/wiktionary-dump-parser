package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Locale;

public interface WiktionaryMarkupBlockInformationExtractor {
    public boolean isSupported(Locale language, MarkupBlock block);
    public void extractIntoEntryBuilder(MarkupBlock block, WiktionaryEntry.Builder builder);
}
