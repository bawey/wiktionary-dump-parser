package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Locale;

/**
 * A thin wrapper for entries extracted from a wiktionary dump
 */
public record WiktionaryDumpRecord(Locale language, long id, String title, WiktionaryMarkup markup) {
    public WiktionaryDumpRecord(Locale language, long id, String title, String rawMarkupString) {
        this(language, id, title, new WiktionaryMarkup(rawMarkupString, language));
    }
}
