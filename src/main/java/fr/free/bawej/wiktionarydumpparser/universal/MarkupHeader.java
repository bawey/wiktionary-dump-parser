package fr.free.bawej.wiktionarydumpparser.universal;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkupHeader {
    public static final Pattern headerPattern = Pattern.compile("([=]+)(?:\s)([^=]*)(?:\s)([=]+)");
    private final String rawHeader;
    private final String value;
    private final int level;

    public MarkupHeader(String rawHeader) {
        this.rawHeader = rawHeader;
        Matcher m = headerPattern.matcher(rawHeader);
        if (!m.find()){
            throw new IllegalArgumentException(MessageFormat.format("Illegal header line: \"{0}\"", rawHeader));
        }
        level = m.group(1).length();
        value = m.group(2);
    }

    public int getLevel() {
        return this.level;
    }

    public String getValue(){
        return this.value;
    }
}
