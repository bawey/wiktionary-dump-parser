package fr.free.bawej.wiktionarydumpparser.universal;

import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalType;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexicalCategory;

import java.util.*;

public class DictionaryEntry {
    private final String title;
    private final Locale language;
    private final LexicalCategory lexicalCategory;
    private final LexiGrammaticalType grammaticalSpecification;
    private final List<WiktionarySense> senses;
    private final List<Translation> translations;

    public static class Builder {
        private String title;
        private Locale language;
        private LexicalCategory lexicalCategory;
        private LexiGrammaticalType grammaticalSpecification;
        private final List<WiktionarySense> senses = new LinkedList<>();
        private final List<Translation> translations = new LinkedList<>();

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setLanguage(Locale language) {
            this.language = language;
            return this;
        }

        public Builder setLexicalCategory(LexicalCategory lexicalCategory) {
            this.lexicalCategory = lexicalCategory;
            return this;
        }

        public Builder setGrammaticalSpecification(LexiGrammaticalType grammaticalSpecification) {
            this.grammaticalSpecification = grammaticalSpecification;
            return this;
        }

        public Builder addSense(WiktionarySense sense) {
            this.senses.add(sense);
            return this;
        }

        public Builder addTranslation(Translation translation){
            this.translations.add(translation);
            return this;
        }

        public DictionaryEntry build() {
            return new DictionaryEntry(language, title, lexicalCategory, grammaticalSpecification, senses, translations);
        }

    }

    protected DictionaryEntry(Locale language, String title, LexicalCategory lexicalCategory, LexiGrammaticalType grammaticalSpecification, List<WiktionarySense> senses, List<Translation> translations) {
        this.title = title;
        this.language = language;
        this.lexicalCategory = lexicalCategory;
        this.grammaticalSpecification = grammaticalSpecification;
        this.senses = Collections.unmodifiableList(senses);
        this.translations = Collections.unmodifiableList(translations);
    }

    public String getTitle() {
        return title;
    }

    public Locale getLanguage() {
        return language;
    }

    public LexicalCategory getLexicalCategory() {
        return lexicalCategory;
    }

    public LexiGrammaticalType getGrammaticalSpecification() {
        return grammaticalSpecification;
    }

    public List<WiktionarySense> getSenses() {
        return senses;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DictionaryEntry)) return false;
        DictionaryEntry that = (DictionaryEntry) o;
        return getTitle().equals(that.getTitle()) && getLanguage().equals(that.getLanguage()) && getLexicalCategory().equals(that.getLexicalCategory()) && getGrammaticalSpecification().equals(that.getGrammaticalSpecification());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getLanguage(), getLexicalCategory(), getGrammaticalSpecification());
    }
}
