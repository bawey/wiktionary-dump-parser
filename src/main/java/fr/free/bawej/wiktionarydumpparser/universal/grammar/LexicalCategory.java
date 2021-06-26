package fr.free.bawej.wiktionarydumpparser.universal.grammar;

public class LexicalCategory extends LexiGrammaticalFeature<String> {

    public static final LexicalCategory VERB = new LexicalCategory("verb");
    public static final LexicalCategory NOUN = new LexicalCategory("noun");
    public static final LexicalCategory ADJECTIVE = new LexicalCategory("adjective");
    // apparently determiners include articles, numbers and non-qualifying adjectives
    public static final LexicalCategory DETERMINER = new LexicalCategory("determiner");
    public static final LexicalCategory PRONOUN = new LexicalCategory("pronoun");
    public static final LexicalCategory ADVERB = new LexicalCategory("adverb");
    public static final LexicalCategory PREPOSITION = new LexicalCategory("preposition");
    public static final LexicalCategory CONJUNCTION = new LexicalCategory("conjunction");
    // pseudo categories
    public static final LexicalCategory LOCUTION = new LexicalCategory("locution");
    public static final LexicalCategory PHRASE = new LexicalCategory("phrase");

    protected LexicalCategory(String value) {
        super(value);
    }


}
