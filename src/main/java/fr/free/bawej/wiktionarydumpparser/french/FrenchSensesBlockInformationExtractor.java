package fr.free.bawej.wiktionarydumpparser.french;

import com.sun.source.tree.Tree;
import fr.free.bawej.wiktionarydumpparser.universal.*;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.Gender;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalFeature;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexiGrammaticalType;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexicalCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FrenchSensesBlockInformationExtractor implements WiktionaryMarkupBlockInformationExtractor {
    private static final Logger logger = LoggerFactory.getLogger(FrenchSensesBlockInformationExtractor.class);
    private final Pattern p = Pattern.compile("\\{\\{S\\|([^\\|]+)\\|([a-z]+)\\}\\}");
    private final Pattern definitionPrologue = Pattern.compile("'''([^']+)'''");

    private Map<String, LexicalCategory> wordClassMap = new TreeMap<>();

    public FrenchSensesBlockInformationExtractor() {
        wordClassMap.put("nom", LexicalCategory.NOUN);
        wordClassMap.put("adjectif", LexicalCategory.ADJECTIVE);
        wordClassMap.put("verbe", LexicalCategory.VERB);
    }

    /**
     * '''accueil''' {{pron|a.kœj|fr}} {{m}}
     * '''lire''' {{pron|liʁ|fr}} {{conjugaison|fr}} {{conjugaison|fr|grp=3}}
     * '''encyclopédie''' {{pron|ɑ̃.si.klɔ.pe.di|fr}} {{f}}
     * '''manga''' {{pron|mɑ̃.ɡa|fr}} {{m}}
     * === {{S|adjectif|fr|flexion}} ===
     * === {{S|locution phrase|fr}} ===
     * '''Toujours ça que les Boches n’auront pas''' {{pron|tu.ʒuʁ sa kə le bɔʃ n‿o.ʁɔ̃ pa|fr}}
     * '''fière-à-bras''' {{pron|fjɛ.ʁ‿a.bʁa|fr}} {{f}} {{équiv-pour|un homme|fier-à-bras|lang=fr}}
     * <p>
     * === {{S|adjectif|fr}} ===
     * {{fr-rég|tɛ̃.bʁik|mf=oui}}
     * '''timbrique''' {{pron|tɛ̃.bʁik|fr}} {{mf}}
     * <p>
     * === {{S|nom|fr}} ===
     * {{fr-rég|ʁa.pœʁ,ʁa.pøz|ps2=ʁa.pœ.ʁøz|pp3=ʁa.pœʁ.øz|s=rappeureuse|p=rappeureuses}}
     * '''rappeureuse'''  {{genre ?}}
     */

    @Override
    public boolean isSupported(Locale language, MarkupBlock block) {
        logger.debug("Asking if block (level {}) is supported", block.getLevel());
        // TODO: prune blocks if parent doesn't meet the requirements!
        if (block.getLevel() == 3 && language.equals(Locale.FRENCH) && block.hasHeader()) {
            String headerValue = block.getHeader().getValue();
            Matcher m = p.matcher(headerValue);
            if (m.find() && "fr".equalsIgnoreCase(m.group(2))) {
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
        String wordClass = m.group(1);
        DictionaryEntry.Builder dictEntryBuilder = DictionaryEntry.builder().setTitle(builder.getTitle()).setLanguage(builder.getLanguage());
        if (wordClassMap.containsKey(wordClass)) {
            dictEntryBuilder.setLexicalCategory(wordClassMap.get(wordClass));
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
        builder.addDictionaryEntry(dictEntryBuilder.build());
    }
}
