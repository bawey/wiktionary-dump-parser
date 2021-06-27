package fr.free.bawej.wiktionarydumpparser.universal;

import org.junit.jupiter.api.Test;

class MarkupBlockTest {

    @Test
    public void testTraversableSubtree(){
        WiktionaryMarkupBlock block = new WiktionaryMarkupBlock("== foo ==", "bar", null);
    }
}