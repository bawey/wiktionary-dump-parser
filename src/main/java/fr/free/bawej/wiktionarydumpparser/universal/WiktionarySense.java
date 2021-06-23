package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class WiktionarySense {
    private final String content;
    private final Map<String, String> relatedTerms = new TreeMap<>();

    public WiktionarySense(String content) {
        this.content = content;
    }

    public void addRelation(String from, String to) {
        relatedTerms.put(from.toLowerCase(), to);
    }

    public Optional<String> getRelation(String word) {
        return Optional.ofNullable(relatedTerms.get(word.toLowerCase()));
    }

    public Collection<String> getRelatedTerms(){
        return this.relatedTerms.values().stream().collect(Collectors.toList());
    }
}
