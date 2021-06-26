package fr.free.bawej.wiktionarydumpparser.universal.grammar;

public class LexicalCategory extends LexiGrammaticalFeature<String> {

    public static final LexicalCategory VERB = new LexicalCategory("verb");
    public static final LexicalCategory NOUN = new LexicalCategory("noun");
    public static final LexicalCategory ADJECTIVE = new LexicalCategory("adjective");
    public static final LexicalCategory DETERMINER = new LexicalCategory("determiner");
    public static final LexicalCategory PRONOUN = new LexicalCategory("pronoun");
    public static final LexicalCategory ADVERB = new LexicalCategory("adverb");
    public static final LexicalCategory PREPOSITION = new LexicalCategory("preposition");
    public static final LexicalCategory CONJUNCTION = new LexicalCategory("conjunction");

    protected LexicalCategory(String value) {
        super(value);
    }
}
