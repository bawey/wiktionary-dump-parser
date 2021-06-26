package fr.free.bawej.wiktionarydumpparser.universal.grammar;

import java.util.Objects;

/**
 * Abstraction for a grammatical feature that can be instantiated to represent specific grammemes, eg. "MALE" being
 * one possible value for the grammatical category of gender.
 * For convenience(?), lexical categories (i.e. parts of speech, aka word classes) can also be represented as implementations of this interface.
 * @param <T>
 */
public abstract class LexiGrammaticalFeature<T> implements Comparable<LexiGrammaticalFeature<?>> {
    private final T value;

    protected LexiGrammaticalFeature(T value) {
        this.value = value;
    }

    public String getName() {
        return this.getClass().getName().toLowerCase();
    }

    public T getValue() {
        return this.value;
    }

    public String getValueAsString() {
        return this.getValue().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(this.getClass().isInstance(o))) return false;
        LexiGrammaticalFeature<T> that = this.getClass().cast(o);
        return Objects.equals(this.getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
    }

    @Override
    public int compareTo(LexiGrammaticalFeature<?> that) {
        int order = String.CASE_INSENSITIVE_ORDER.compare(this.getName(), that.getName());
        if (order == 0) {
            return String.CASE_INSENSITIVE_ORDER.compare(this.getValueAsString(), that.getValueAsString());
        }
        return order;
    }

    @Override
    public String toString() {
        return "LexiGrammaticalFeature{" +
                "value=" + value +
                '}';
    }
}
