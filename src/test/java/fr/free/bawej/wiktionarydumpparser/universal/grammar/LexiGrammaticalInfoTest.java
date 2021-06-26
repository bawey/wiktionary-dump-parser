package fr.free.bawej.wiktionarydumpparser.universal.grammar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexiGrammaticalInfoTest {

    @Test
    public void testInstantiation(){
        LexiGrammaticalType lgi = LexiGrammaticalType.of(LexicalCategory.NOUN, Gender.MASCULINE);
        assertNotNull(lgi);
        assertTrue(lgi.is(LexicalCategory.NOUN));
        assertTrue(lgi.is(Gender.MASCULINE));
        assertFalse(lgi.is(Number.PLURAL));

    }

    @Test
    public void testEquality(){
        assertEquals(LexiGrammaticalType.NOUN, LexiGrammaticalType.of(LexicalCategory.NOUN));
        assertEquals(LexiGrammaticalType.NOUN_MASCULINE, LexiGrammaticalType.of(Gender.MASCULINE, LexicalCategory.NOUN));
        assertSame(LexiGrammaticalType.NOUN, LexiGrammaticalType.of(LexicalCategory.NOUN));
        assertSame(LexiGrammaticalType.NOUN_MASCULINE, LexiGrammaticalType.of(Gender.MASCULINE, LexicalCategory.NOUN));
    }

    @Test
    public void testAreNotEqual(){
        assertNotEquals(LexiGrammaticalType.NOUN, LexiGrammaticalType.NOUN_NEUTRAL);
    }
}