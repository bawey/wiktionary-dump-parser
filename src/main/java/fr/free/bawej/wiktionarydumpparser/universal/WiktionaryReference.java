package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Locale;

public class WiktionaryReference {
    private final Locale sourceLanguage;
    private final Locale targetLanguage;
    private final String sourceTerm;
    private final String targetTerm;

    private WiktionaryReference(Locale sourceLanguage, Locale targetLanguage, String sourceTerm, String targetTerm) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.sourceTerm = sourceTerm;
        this.targetTerm = targetTerm;
    }

    /**
     * Can represent a translation from one language to another.
     *
     * @param sourceLanguage
     * @param targetLanguage
     * @param sourceTerm
     * @param targetTerm
     * @return
     */
    public static WiktionaryReference translation(Locale sourceLanguage, Locale targetLanguage, String sourceTerm, String targetTerm) {
        return new WiktionaryReference(sourceLanguage, targetLanguage, sourceTerm, targetTerm);
    }

    /**
     * Represents a reference to a word in identical form as it appears in the referring context.
     *
     * @param language
     * @param term
     * @return
     */
    public static WiktionaryReference direct(Locale language, String term) {
        return new WiktionaryReference(language, language, term, term);
    }

    /**
     * Represents a reference between inflected form of a word and its basic form
     *
     * @param language
     * @param sourceTerm
     * @param targetTerm
     * @return
     */
    public static WiktionaryReference inflection(Locale language, String sourceTerm, String targetTerm) {
        return new WiktionaryReference(language, language, sourceTerm, targetTerm);
    }

    public Locale getSourceLanguage() {
        return sourceLanguage;
    }

    public Locale getTargetLanguage() {
        return targetLanguage;
    }

    public String getSourceTerm() {
        return sourceTerm;
    }

    public String getTargetTerm() {
        return targetTerm;
    }
}
