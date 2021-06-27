package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.WiktionarySense;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class FrenchWiktionarySenses {

    public static final Pattern labelPattern = Pattern.compile("\\{\\{(.+)(?:\\|fr)\\}\\}", Pattern.UNICODE_CHARACTER_CLASS);
    public static final Pattern referencePattern = Pattern.compile("\\[\\[(\\w+)(?:#fr)?(?:\\|)?(\\w+)?\\]\\]", Pattern.UNICODE_CHARACTER_CLASS);
    public static final Pattern linePattern = Pattern.compile("^#(?:\\s)+(.*)");

    public static boolean isSenseLine(String line){
        return linePattern.matcher(line).find();
    }

    public static WiktionarySense.Builder prepareBuilderFromRawString(String content) {
        Matcher labelMatcher = labelPattern.matcher(content);
        content = labelMatcher.replaceAll((mr) -> {
            return MessageFormat.format("({0})", mr.group(1));
        });
        Matcher referenceMatcher = referencePattern.matcher(content);
        content = referenceMatcher.replaceAll(mr -> {
            return Stream.of(mr.group(2), mr.group(1)).filter(Objects::nonNull).findFirst().get();
        });
        Matcher lineMatcher = linePattern.matcher(content);
        lineMatcher.find();
        return WiktionarySense.build(lineMatcher.group(1));
    }
}
