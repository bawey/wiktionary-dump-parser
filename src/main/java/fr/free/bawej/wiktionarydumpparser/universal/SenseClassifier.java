package fr.free.bawej.wiktionarydumpparser.universal;

public class SenseClassifier {
    private final String value;

    protected SenseClassifier(String value) {
        this.value = value;
    }

    public static SenseClassifier UNCOUNTABLE = new SenseClassifier("uncountable");
    public static SenseClassifier ARCHAIC = new SenseClassifier("archaic");
    public static SenseClassifier VULGAR = new SenseClassifier("vulgar");

    public String getStringValue() {
        return this.value;
    }
}
