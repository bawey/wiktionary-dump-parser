package fr.free.bawej.wiktionarydumpparser.universal;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiktionaryMarkupBlockHeader {
    // can accommodate stuff like "== stuff ==" or "== {{S|foo|bar=13}} =="
    public static final Pattern headerPattern = Pattern.compile("([=]+)(?:\s)([^=]*|\\{\\{[^\\}]*\\}\\})(?:\s)([=]+)");
    private final String rawHeader;
    private final String value;
    private final int level;

    public WiktionaryMarkupBlockHeader(String rawHeader) {
        this.rawHeader = rawHeader;
        Matcher m = headerPattern.matcher(rawHeader);
        if (!m.find() || m.group(1).length() != m.group(3).length()) {
            throw new IllegalArgumentException(MessageFormat.format("Illegal header line: \"{0}\"", rawHeader));
        }
        level = m.group(1).length();
        value = m.group(2);
    }

    public int getLevel() {
        return this.level;
    }

    public String getValue() {
        return this.value;
    }
}
