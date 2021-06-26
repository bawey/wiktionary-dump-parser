package fr.free.bawej.wiktionarydumpparser.universal.grammar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides facilities to tag and divide entries into cluster like "adjectives", "feminine nouns" or "perfect verbs" without having to come up with all the possibilities upfront.
 * As new languages are added, new combinations of {@link LexiGrammaticalFeature}s (and implementation thereof) can be registered to tag and classify the entries accordingly.
 *
 * Internally the instances of {@link LexiGrammaticalType} are reused for equivalent sets of input parameters.
 *
 * Dictionary entries can have multiple language-specific parameters: part of speech (word class), number (plural or singular), gender etc.
 *
 * This class provides mechanisms to define coarse types like "feminine noun", "plural noun", "perfect verb" or whatever comes up later on.
 * Once created, the instances will be reused for equivalent sets of input parameters.
 */
public class LexiGrammaticalType {
    private static final Logger logger = LoggerFactory.getLogger(LexiGrammaticalType.class);
    private final List<LexiGrammaticalFeature<?>> features;

    private static Map<List<LexiGrammaticalFeature<?>>, LexiGrammaticalType> registry = new HashMap<>();

    public static final LexiGrammaticalType NOUN = LexiGrammaticalType.of(LexicalCategory.NOUN);
    public static final LexiGrammaticalType VERB = LexiGrammaticalType.of(LexicalCategory.VERB);
    public static final LexiGrammaticalType ADJECTIVE = LexiGrammaticalType.of(LexicalCategory.ADJECTIVE);
    public static final LexiGrammaticalType NOUN_FEMININE = LexiGrammaticalType.of(LexicalCategory.NOUN, Gender.FEMININE);
    public static final LexiGrammaticalType NOUN_MASCULINE = LexiGrammaticalType.of(LexicalCategory.NOUN, Gender.MASCULINE);
    public static final LexiGrammaticalType NOUN_NEUTRAL = LexiGrammaticalType.of(LexicalCategory.NOUN, Gender.NEUTRAL);

    protected LexiGrammaticalType(Collection<LexiGrammaticalFeature<?>> features) {
        this.features = features.stream().collect(Collectors.toUnmodifiableList());
    }

    public boolean is(LexiGrammaticalFeature<?> feature) {
        return this.features.contains(feature);
    }

    public List<LexiGrammaticalFeature<?>> getFeatures() {
        return this.features;
    }

    public static LexiGrammaticalType of(LexiGrammaticalFeature<?>... features) {
        return getForSortedList(Arrays.stream(features).sorted().collect(Collectors.toUnmodifiableList()));
    }

    public static LexiGrammaticalType of(Collection<LexiGrammaticalFeature<?>> features){
        return getForSortedList(features.stream().sorted().collect(Collectors.toUnmodifiableList()));
    }

    private static LexiGrammaticalType getForSortedList(List<LexiGrammaticalFeature<?>> featuresList){
        logger.debug("Features list of length {}", featuresList.size());
        if (!registry.containsKey(featuresList)) {
            registry.put(featuresList, new LexiGrammaticalType(featuresList));
        }
        return registry.get(featuresList);
    }

    @Override
    public String toString() {
        return new StringBuilder("LexiGrammaticalType: ").append(this.features.stream().map(LexiGrammaticalFeature::getValueAsString).collect(Collectors.joining("-"))).toString();
    }
}
