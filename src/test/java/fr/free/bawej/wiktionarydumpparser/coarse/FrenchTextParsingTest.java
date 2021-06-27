package fr.free.bawej.wiktionarydumpparser.coarse;

import fr.free.bawej.wiktionarydumpparser.universal.*;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.Gender;
import fr.free.bawej.wiktionarydumpparser.universal.grammar.LexicalCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FrenchTextParsingTest {

    public static final Logger logger = LoggerFactory.getLogger(FrenchTextParsingTest.class);

    @Autowired
    WiktionaryDumpConverter converter;

    private static final String orangeSample = readSample("orange.fr");
    private static final String savoirSample = readSample("savoir.fr");
    private static final String voileSample = readSample("voile.fr");

    public static class SamplesProvider implements ArgumentsProvider{
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(orangeSample, List.of("orange", ": L’ancien français ''pomme (d’)orange'' est le calque de l’{{calque|it|fr|mot=melarancia|sens=orange}}, ''{{lien|pomme|lang=fro}}'' en ancien français signifiait « [[fruit]] »), ce qui explique la forme du mot dans plusieurs langues (''[[pomeranč]]'' en tchèque, ''[[pomarańcza]]'' en polonais).")),
                    Arguments.of(savoirSample, List.of("savoir")),
                    Arguments.of(voileSample, List.of("voile"))
            );
        }
    }

    private static String readSample(String filename) {
        URL url = FrenchTextParsingTest.class.getClassLoader().getResource(MessageFormat.format("snippets/{0}", filename));
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(url.getFile()))) {
            for (String line; (line = reader.readLine()) != null; buffer.append(line).append("\n")) ;
        } catch (Exception oops) {
            logger.error("Oh NO!", oops);
        }
        return buffer.toString();
    }

    @ParameterizedTest
    @ArgumentsSource(SamplesProvider.class)
    public void testSamplesLoaded(String sample, List<String> substrings) {
        assertNotNull(sample);
        assertFalse(sample.isBlank());
        for (String substring : substrings){
            assertTrue(sample.contains(substring));
        }
    }

    @Test
    public void testOrange() {
        logger.debug("Loaded markup for «orange»: {} characters", orangeSample.length());
        WiktionaryDumpRecord record = new WiktionaryDumpRecord(Locale.FRENCH, 42, "orange", orangeSample);
        WiktionaryEntry entry = converter.convertDumpRecords(List.of(record)).iterator().next();
        assertNotNull(entry);
        // every wiki page contains one or more dictionary entries
        List<DictionaryEntry> orangeDefinitions = entry.getDictionaryEntries();
        assertEquals(4, orangeDefinitions.size());
        // first should be a fruit, noun, feminine...
        DictionaryEntry fruit = orangeDefinitions.get(0);
        assertEquals(Locale.FRENCH, fruit.getLanguage());
        assertEquals(LexicalCategory.NOUN, fruit.getLexicalCategory());
        assertTrue(fruit.getGrammaticalSpecification().is(Gender.FEMININE));
        assertEquals(2, fruit.getSenses().size(), "obtained: \n" + fruit.getSenses().stream().map(WiktionarySense::getContent).collect(Collectors.joining("\n")));
        assertTrue(fruit.getSenses().get(0).getContent().contains("agrume"));
        assertTrue(fruit.getSenses().get(1).getContent().contains("Oranger"));
        // second should be color, noun, masculine
        DictionaryEntry color = orangeDefinitions.get(1);
        assertEquals(Locale.FRENCH, color.getLanguage());
        assertEquals(LexicalCategory.NOUN, color.getLexicalCategory());
        assertTrue(color.getGrammaticalSpecification().is(Gender.MASCULINE));
        assertTrue(color.getSenses().iterator().next().getContent().contains("Couleur"));
        // third would be an adjective, color-related
        DictionaryEntry adjective = orangeDefinitions.get(2);
        assertEquals(Locale.FRENCH, adjective.getLanguage());
        assertEquals(LexicalCategory.ADJECTIVE, adjective.getLexicalCategory());
        // then a flexion of a verb
        DictionaryEntry strayVerb = orangeDefinitions.get(3);
        assertEquals(Locale.FRENCH, strayVerb.getLanguage());
        assertEquals(LexicalCategory.VERB, strayVerb.getLexicalCategory());
    }

    @Test
    public void testVoile() {
        WiktionaryDumpRecord record = new WiktionaryDumpRecord(Locale.FRENCH, 44, "voile", voileSample);
        WiktionaryEntry entry = converter.convertDumpRecords(List.of(record)).iterator().next();
        assertNotNull(entry);
    }

    @Test
    public void testSavoir() {
        WiktionaryDumpRecord record = new WiktionaryDumpRecord(Locale.FRENCH, 43, "savoir", savoirSample);
        WiktionaryEntry entry = converter.convertDumpRecords(List.of(record)).iterator().next();
        assertNotNull(entry);
    }
}
