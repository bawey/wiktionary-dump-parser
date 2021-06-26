package fr.free.bawej.wiktionarydumpparser.universal.grammar;

public class Number extends LexiGrammaticalFeature<String> {

    public static final Number SINGULAR = new Number("singular");
    public static final Number PLURAL = new Number("plural");

    protected Number(String value) {
        super(value);
    }
}
