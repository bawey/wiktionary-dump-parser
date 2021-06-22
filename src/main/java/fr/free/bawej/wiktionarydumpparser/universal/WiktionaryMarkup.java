package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Locale;

public class WiktionaryMarkup {
    public final String rawContent;
    public final Locale language;

    public WiktionaryMarkup(String rawContent, Locale language) {
        this.rawContent = rawContent;
        this.language = language;
    }
}
