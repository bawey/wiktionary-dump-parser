package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.*;
import java.util.stream.Collectors;

public class WiktionarySense {
    private final String content;
    private final List<SenseClassifier> classifiers;
    private final Map<String, WiktionaryReference> references;
    private final List<String> examples;

    public static class Builder {
        private final String content;
        private final List<SenseClassifier> classifiers = new LinkedList<>();
        private final List<WiktionaryReference> references = new LinkedList<>();
        private final List<String> examples = new LinkedList<>();

        public Builder(String content) {
            this.content = content;
        }

        public Builder addClassifier(SenseClassifier classifier) {
            this.classifiers.add(classifier);
            return this;
        }

        public Builder addReference(WiktionaryReference reference) {
            references.add(reference);
            return this;
        }

        public Builder addExample(String example) {
            examples.add(example);
            return this;
        }

        public WiktionarySense build() {
            return new WiktionarySense(this.content, this.classifiers, this.references, this.examples);
        }
    }

    private WiktionarySense(String content, Collection<SenseClassifier> classifiers, Collection<WiktionaryReference> references, Collection<String> examples) {
        this.content = content;
        this.classifiers = classifiers.stream().collect(Collectors.toUnmodifiableList());
        this.references = references.stream().collect(Collectors.toUnmodifiableMap(WiktionaryReference::getSourceTerm, e -> e));
        this.examples = examples.stream().collect(Collectors.toUnmodifiableList());
    }

    public List<SenseClassifier> getClassifiers() {
        return this.classifiers;
    }

    public List<WiktionaryReference> getReferences() {
        return this.references.values().stream().collect(Collectors.toUnmodifiableList());
    }

    public String getContent() {
        return content;
    }

    public static Builder build(String content){
        return new Builder(content);
    }
}
