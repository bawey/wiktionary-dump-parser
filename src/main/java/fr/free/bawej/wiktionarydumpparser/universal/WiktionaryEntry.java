package fr.free.bawej.wiktionarydumpparser.universal;

import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalType;

import java.util.*;
import java.util.stream.Collectors;

public class WiktionaryEntry {
    private final String title;
    private final long id;
    private final Locale language;
    private final List<DictionaryEntry> dictionaryEntries;

    public static class Builder {
        private final String title;
        private final Locale language;
        private final long id;
        private final List<DictionaryEntry> dictionaryEntries = new LinkedList<>();

        public Builder(Locale language, long id, String title) {
            this.title = title;
            this.language = language;
            this.id = id;
        }

        public Builder addDictionaryEntry(DictionaryEntry entry) {
            dictionaryEntries.add(entry);
            return this;
        }

        public WiktionaryEntry build() {
            return new WiktionaryEntry(this.language, this.id, this.title, this.dictionaryEntries);
        }

        public String getTitle() {
            return title;
        }

        public Locale getLanguage() {
            return language;
        }
    }

    private WiktionaryEntry(Locale language, long id, String title, List<DictionaryEntry> dictionaryEntries) {
        this.title = title;
        this.language = language;
        this.id = id;
        this.dictionaryEntries = Collections.unmodifiableList(dictionaryEntries);
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public Locale getLanguage() {
        return language;
    }

    public List<DictionaryEntry> getDictionaryEntries() {
        return this.dictionaryEntries;
    }
}
