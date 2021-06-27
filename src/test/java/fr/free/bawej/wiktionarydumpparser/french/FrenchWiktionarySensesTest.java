package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.WiktionarySense;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FrenchWiktionarySensesTest {
    String foo = "{{désuet|fr}} [[oranger#fr|Oranger]].";

    public static class SenseSamplesProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("# {{fooTag|fr}} Rien.", "(fooTag) Rien."),
                    Arguments.of("# {{désuet|fr}} [[oranger#fr|Oranger]].", "(désuet) Oranger."),
                    Arguments.of("# {{agrumes|fr}} [[fruit#fr|Fruit]] de l’[[oranger]], [[agrume#fr]] de couleur orangée ...",
                            "(agrumes) Fruit de l’oranger, agrume de couleur orangée ..."),
                    Arguments.of("# [[appréhender|Appréhender]] par l’[[esprit]] ou la [[connaissance]] ; [[connaître]] de façon certaine.",
                            "Appréhender par l’esprit ou la connaissance ; connaître de façon certaine.")
            );
        }
    }

    public static class WrongSenseSamplesProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("#* ''Fleur d’'''orange''','' fleur d’oranger."),
                    Arguments.of("#* ''C’est ainsi que vers la fin de l’hiver, comme chaque année, garçons et filles, durant un mois, furent occupés à la cueillette des '''oranges''' dans le domaine du pacha.'' {{source|{{w|Out-el-Kouloub}}, « Zariffa », dans ''Trois Contes de l’Amour et de la Mort'', 1940}}")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(SenseSamplesProvider.class)
    public void testSenseExtraction(String raw, String expectedContent) {
        WiktionarySense sense = FrenchWiktionarySenses.prepareBuilderFromRawString(raw).build();
        assertEquals(expectedContent, sense.getContent());
    }

    @ParameterizedTest
    @ArgumentsSource(SenseSamplesProvider.class)
    public void testCheckingLegitSenseLines(String rawLine) {
        assertTrue(FrenchWiktionarySenses.isSenseLine(rawLine));
    }

    @ParameterizedTest
    @ArgumentsSource(WrongSenseSamplesProvider.class)
    public void testDetectingWrongSenseLines(String line) {
        assertFalse(FrenchWiktionarySenses.isSenseLine(line));
    }

}