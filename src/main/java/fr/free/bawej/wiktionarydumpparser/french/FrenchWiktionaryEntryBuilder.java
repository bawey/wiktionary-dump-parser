package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryEntry;

import java.util.Locale;

public class FrenchWiktionaryEntryBuilder extends WiktionaryEntry.Builder {
    public FrenchWiktionaryEntryBuilder(String title, long id) {
        super(Locale.FRENCH, id, title);
    }
}
