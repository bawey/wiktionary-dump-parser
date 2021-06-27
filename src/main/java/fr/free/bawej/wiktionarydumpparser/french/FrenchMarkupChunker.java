package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.MarkupChunker;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class FrenchMarkupChunker implements MarkupChunker {
    private static final Logger logger = LoggerFactory.getLogger(FrenchMarkupChunker.class);
    public static final Pattern headerPattern = Pattern.compile("(=+) \\{\\{([^\\}]*)\\}\\} (=+)");

    private List<WiktionaryMarkupBlock> heritageStack = new LinkedList<>();

    @Override
    public boolean isSupported(Locale language) {
        return Locale.FRENCH.equals(language);
    }

    @Override
    public boolean isBlockOpener(String line) {
        return headerPattern.matcher(line).find();
    }

    @Override
    public WiktionaryMarkupBlock chunk(WiktionaryMarkup markup) {
        String[] rawLines = markup.rawContent.split("\n");

        WiktionaryMarkupBlock.Builder builder = new WiktionaryMarkupBlock.Builder();

        String bufferedHeader = null;
        List<String> contents = new LinkedList<>();
        for (int i = 0; i < rawLines.length; ++i) {
            String line = rawLines[i];

            if (!isBlockOpener(line)) {
                builder.addLine(line);
            }
            if (isBlockOpener(line) || i == (rawLines.length - 1)) {
                WiktionaryMarkupBlock bufferedBlock = builder.setNextOfKin(heritageStack.isEmpty() ? null : heritageStack.get(0)).build();
                heritageStack.add(0, bufferedBlock);
                if (i < (rawLines.length - 1)) {
                    builder = new WiktionaryMarkupBlock.Builder().setRawHeader(line);
                    logger.info("Line {} should begin a new block!", line);
                }
            }
        }
        // add leftovers to structure

        return heritageStack.get(heritageStack.size() - 1);
    }
}
