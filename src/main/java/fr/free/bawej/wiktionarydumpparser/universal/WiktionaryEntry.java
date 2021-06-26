package fr.free.bawej.wiktionarydumpparser.universal;

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
        private final List<DictionaryEntry.Builder> dictEntryBuilders = new LinkedList<>();

        public Builder(Locale language, long id, String title) {
            this.title = title;
            this.language = language;
            this.id = id;
        }

        public Builder addDictionaryEntryBuilder(DictionaryEntry.Builder entryBuilder) {
            dictEntryBuilders.add(entryBuilder);
            return this;
        }

        public WiktionaryEntry build() {
            return new WiktionaryEntry(this.language, this.id, this.title, this.dictEntryBuilders.stream().map(DictionaryEntry.Builder::build).collect(Collectors.toUnmodifiableList()));
        }

        public String getTitle() {
            return title;
        }

        public Locale getLanguage() {
            return language;
        }

        public Iterator<DictionaryEntry.Builder> getDictEntryBuildersIterator(){
            return Collections.unmodifiableList(dictEntryBuilders).iterator();
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
