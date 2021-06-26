package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Locale;

public class Translation {
    private final String disambiguation;
    private final Locale destinationLanguage;
    private final String value;

    public Translation(Locale destinationLanguage, String value, String disambiguation) {
        this.disambiguation = disambiguation;
        this.destinationLanguage = destinationLanguage;
        this.value = value;
    }
}
