package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.MarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryEntry;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkupBlockInformationExtractor;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionarySense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrenchSensesBlockInformationExtractor implements WiktionaryMarkupBlockInformationExtractor {
    private static final Logger logger = LoggerFactory.getLogger(FrenchSensesBlockInformationExtractor.class);

    private final Pattern p = Pattern.compile("\\{\\{S\\|([^\\|]+)\\|([a-z]+)\\}\\}");

    @Override
    public boolean isSupported(Locale language, MarkupBlock block) {
        logger.debug("Asking if block is supported");
        if (language.equals(Locale.FRENCH) && block.hasHeader()) {
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
        logger.debug("Extracting info from block...");
        for (String line : block.getRawContent().split("\n")){
            if (line.startsWith("# ")){
                builder.addSense(new WiktionarySense(line.substring(2)));
            }
        }
    }
}
