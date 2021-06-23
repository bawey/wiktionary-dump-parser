package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.*;

public class WiktionaryEntry {
    private final String title;
    private final long id;
    private final Locale language;
    private final ArrayList<WiktionarySense> senses = new ArrayList<>();

    public static class Builder {
        private final String title;
        private final Locale language;
        private final long id;
        private final List<WiktionarySense> senses = new LinkedList<>();

        public Builder(Locale language, long id, String title) {
            this.title = title;
            this.language = language;
            this.id = id;
        }

        public List<WiktionarySense> getSenses() {
            return senses;
        }

        public void addSense(WiktionarySense sense) {
            senses.add(sense);
        }

        public WiktionaryEntry build() {
            return new WiktionaryEntry(this.language, this.id, this.title, this.senses);
        }
    }

    private WiktionaryEntry(Locale language, long id, String title) {
        this.title = title;
        this.language = language;
        this.id = id;
    }

    private WiktionaryEntry(Locale language, long id, String title, List<WiktionarySense> senses) {
        this(language, id, title);
        this.senses.ensureCapacity(senses.size());
        this.senses.addAll(senses);
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

    public List<WiktionarySense> getSenses() {
        return Collections.unmodifiableList(this.senses);
    }
}
