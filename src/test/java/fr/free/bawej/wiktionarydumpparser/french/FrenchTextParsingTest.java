package fr.free.bawej.wiktionarydumpparser.french;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class FrenchTextParsingTest {

    public static final Logger logger = LoggerFactory.getLogger(FrenchTextParsingTest.class);


    @Test
    public void testOrange(){
        URL url = this.getClass().getClassLoader().getResource("snippets/orange.fr");
        logger.info(url.toString());
    }
}
