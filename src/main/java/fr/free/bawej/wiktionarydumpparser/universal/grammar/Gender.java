package fr.free.bawej.wiktionarydumpparser.universal.grammar;

public class Gender extends LexiGrammaticalFeature<String> {

    public static final Gender MASCULINE = new Gender("masculine");
    public static final Gender FEMININE = new Gender("feminine");
    public static final Gender NEUTRAL = new Gender("neutral");

    protected Gender(String value) {
        super(value);
    }
}
