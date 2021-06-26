package fr.free.bawej.wiktionarydumpparser.coarse;

import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryDumpConverter;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryDumpRecord;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FrenchTextParsingTest {

    public static final Logger logger = LoggerFactory.getLogger(FrenchTextParsingTest.class);

    @Autowired
    WiktionaryDumpConverter converter;

    private static final String orangeSample = readSample("orange.fr");
    private static final String savoirSample = readSample("savoir.fr");
    ;
    private static final String voileSample = readSample("voile.fr");

    private static Stream<String> getSamples() {
        return Stream.of(orangeSample, savoirSample, voileSample);
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
    @MethodSource("getSamples")
    public void testSamplesLoaded(String sample) {
        assertNotNull(sample);
        assertFalse(sample.isBlank());
    }

    @Test
    public void testOrange() {
        logger.debug("Loaded markup for «orange»: {} characters", orangeSample.length());
        WiktionaryDumpRecord record = new WiktionaryDumpRecord(Locale.FRENCH, 42, "orange", orangeSample);
        WiktionaryEntry entry = converter.convertDumpRecords(List.of(record)).iterator().next();
        assertNotNull(entry);
    }

    @Test
    public void testVoile() {
        WiktionaryDumpRecord record = new WiktionaryDumpRecord(Locale.FRENCH, 42, "orange", voileSample);
        WiktionaryEntry entry = converter.convertDumpRecords(List.of(record)).iterator().next();
        assertNotNull(entry);
    }

    @Test
    public void testSavoir() {
        WiktionaryDumpRecord record = new WiktionaryDumpRecord(Locale.FRENCH, 42, "orange", savoirSample);
        WiktionaryEntry entry = converter.convertDumpRecords(List.of(record)).iterator().next();
        assertNotNull(entry);
    }
}
