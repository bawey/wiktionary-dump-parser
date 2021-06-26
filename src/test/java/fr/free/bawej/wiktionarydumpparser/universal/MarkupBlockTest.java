package fr.free.bawej.wiktionarydumpparser.universal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkupBlockTest {

    @Test
    public void testTraversableSubtree(){
        MarkupBlock block = new MarkupBlock("== foo ==", "bar", null);
    }
}