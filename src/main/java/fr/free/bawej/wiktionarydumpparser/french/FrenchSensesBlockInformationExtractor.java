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
import java.util.stream.Stream;

import static fr.free.bawej.wiktionarydumpparser.french.FrenchWiktionarySenses.isSenseLine;

@Component
public class FrenchSensesBlockInformationExtractor implements WiktionaryMarkupBlockInformationExtractor {
    private static final Logger logger = LoggerFactory.getLogger(FrenchSensesBlockInformationExtractor.class);
    //    private final Pattern p = Pattern.compile("\\{\\{S\\|([^\\|]+)\\|([a-z]+)\\}\\}");
    private final Pattern p = Pattern.compile("\\{\\{S\\|([^\\}]+)\\}\\}");
    private final Pattern definitionPrologue = Pattern.compile("'''([^']+)'''");

    private static class HeaderMacroTokenizer {
        private static final Map<String, LexicalCategory> wordClassMap = new TreeMap<>();

        static {
            wordClassMap.put("nom", LexicalCategory.NOUN);
            wordClassMap.put("adjectif", LexicalCategory.ADJECTIVE);
            wordClassMap.put("adj", LexicalCategory.ADJECTIVE);
            wordClassMap.put("verbe", LexicalCategory.VERB);
            wordClassMap.put("verb", LexicalCategory.VERB);
            wordClassMap.put("adverbe", LexicalCategory.ADVERB);
            wordClassMap.put("adv", LexicalCategory.ADVERB);
            wordClassMap.put("locution", LexicalCategory.LOCUTION);
            wordClassMap.put("phrase", LexicalCategory.PHRASE);
            wordClassMap.put("article", LexicalCategory.DETERMINER);
            wordClassMap.put("pronom", LexicalCategory.PRONOUN);
        }

        private final List<String> tokens;

        private HeaderMacroTokenizer(String header) {
            tokens = Arrays.stream(header.split("\\|")).map(String::toLowerCase).collect(Collectors.toUnmodifiableList());

        }

        public static HeaderMacroTokenizer of(String header) {
            // factory for potential caching of headers as they
            return new HeaderMacroTokenizer(header);
        }

        public boolean isFrench() {
            return tokens.stream().filter(e -> "fr".equalsIgnoreCase(e)).findAny().isPresent();
        }

        public Optional<LexicalCategory> getLexicalCategory() {
            String token = tokens.get(0);
            // the best case scenario: the first token is directly mapped
            if (wordClassMap.containsKey(token)) {
                return Optional.of(wordClassMap.get(token));
            }
            // second-best scenario: the first token has a mapped substring
            int minChars = 3;
            return Stream.iterate(minChars, e -> e + 1).limit(token.length() - minChars)
                    .map(e -> token.substring(0, e)).map(e -> wordClassMap.get(e)).filter(Objects::nonNull).findAny();
        }
    }

    @Override
    public boolean isSupported(Locale language, WiktionaryMarkupBlock block) {
        logger.debug("Asking if block (level {}) is supported", block.getLevel());
        // TODO: prune blocks if parent doesn't meet the requirements!
        if (block.getLevel() == 3 && language.equals(Locale.FRENCH) && block.hasHeader()) {
            String headerValue = block.getHeader().getValue();
            Matcher m = p.matcher(headerValue);
            if (m.find() && HeaderMacroTokenizer.of(m.group(1)).isFrench()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void extractIntoEntryBuilder(WiktionaryMarkupBlock block, WiktionaryEntry.Builder builder) {
        // oversimplified implementation surely!

        Matcher m = p.matcher(block.getHeader().getValue());
        m.find();
        HeaderMacroTokenizer tokenizer = HeaderMacroTokenizer.of(m.group(1));
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
            } else if (isSenseLine(line)) {
                WiktionarySense.Builder senseBuilder = FrenchWiktionarySenses.prepareBuilderFromRawString(line);
                dictEntryBuilder.addSense(senseBuilder.build());
            }
        }
        dictEntryBuilder.setGrammaticalSpecification(LexiGrammaticalType.of(traits));
        builder.addDictionaryEntryBuilder(dictEntryBuilder);
    }
}
