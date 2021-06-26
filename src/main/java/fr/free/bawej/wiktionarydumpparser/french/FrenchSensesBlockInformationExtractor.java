package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.*;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.Gender;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalFeature;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalType;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexicalCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FrenchSensesBlockInformationExtractor implements WiktionaryMarkupBlockInformationExtractor {
    private static final Logger logger = LoggerFactory.getLogger(FrenchSensesBlockInformationExtractor.class);
    //    private final Pattern p = Pattern.compile("\\{\\{S\\|([^\\|]+)\\|([a-z]+)\\}\\}");
    private final Pattern p = Pattern.compile("\\{\\{S\\|([^\\}]+)\\}\\}");
    private final Pattern definitionPrologue = Pattern.compile("'''([^']+)'''");

    private static class HeaderArgsTokenizer {
        private static final Map<String, LexicalCategory> wordClassMap = new TreeMap<>();

        static {
            wordClassMap.put("nom", LexicalCategory.NOUN);
            wordClassMap.put("adjectif", LexicalCategory.ADJECTIVE);
            wordClassMap.put("verbe", LexicalCategory.VERB);
        }

        private final List<String> tokens;

        private HeaderArgsTokenizer(String header) {
            tokens = Arrays.stream(header.split("\\|")).map(String::toLowerCase).collect(Collectors.toUnmodifiableList());

        }

        public static HeaderArgsTokenizer of(String header) {
            // factory for potential caching of headers as they
            return new HeaderArgsTokenizer(header);
        }

        public boolean isFrench() {
            return tokens.stream().filter(e -> "fr".equalsIgnoreCase(e)).findAny().isPresent();
        }

        public Optional<LexicalCategory> getLexicalCategory() {
            for (String key : wordClassMap.keySet()) {
                if (tokens.contains(key)) {
                    return Optional.of(wordClassMap.get(key));
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public boolean isSupported(Locale language, MarkupBlock block) {
        logger.debug("Asking if block (level {}) is supported", block.getLevel());
        // TODO: prune blocks if parent doesn't meet the requirements!
        if (block.getLevel() == 3 && language.equals(Locale.FRENCH) && block.hasHeader()) {
            String headerValue = block.getHeader().getValue();
            Matcher m = p.matcher(headerValue);
            if (m.find() && HeaderArgsTokenizer.of(m.group(1)).isFrench()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void extractIntoEntryBuilder(MarkupBlock block, WiktionaryEntry.Builder builder) {
        // oversimplified implementation surely!

        Matcher m = p.matcher(block.getHeader().getValue());
        m.find();
        HeaderArgsTokenizer tokenizer = HeaderArgsTokenizer.of(m.group(1));
        String wordClass = m.group(1);
        DictionaryEntry.Builder dictEntryBuilder = DictionaryEntry.builder().setTitle(builder.getTitle()).setLanguage(builder.getLanguage());
        if (tokenizer.getLexicalCategory().isPresent()) {
            dictEntryBuilder.setLexicalCategory(tokenizer.getLexicalCategory().get());
        }

        logger.debug("Extracting info from block...");
        List<LexiGrammaticalFeature<?>> traits = new LinkedList<>();
        for (String line : block.getRawContent().split("\n")) {
            if (line.startsWith("'''")) {
                if (line.contains("{{m}}")) {
                    traits.add(Gender.MASCULINE);
                } else if (line.contains("{{f}}")) {
                    traits.add(Gender.FEMININE);
                } else if (line.contains("{{n}}")) {
                    traits.add(Gender.NEUTRAL);
                }
            } else if (line.startsWith("# ")) {
                dictEntryBuilder.addSense(new WiktionarySense(line.substring(2)));
            }
        }
        dictEntryBuilder.setGrammaticalSpecification(LexiGrammaticalType.of(traits));
        builder.addDictionaryEntryBuilder(dictEntryBuilder);
    }
}
