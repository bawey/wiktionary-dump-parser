package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.MarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.MarkupHeader;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkup;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static fr.free.bawej.wiktionarydumpparser.samples.FrenchSampleMarkup.SAMPLE_M;
import static fr.free.bawej.wiktionarydumpparser.samples.FrenchSampleMarkup.SAMPLE_S;
import static org.junit.jupiter.api.Assertions.*;

class FrenchMarkupChunkerTest {

    private FrenchMarkupChunker chunker = new FrenchMarkupChunker();

    @Test
    public void testSimpleChunking() {
        WiktionaryMarkup markup = new WiktionaryMarkup(SAMPLE_S, Locale.FRENCH);
        MarkupBlock root = chunker.chunk(markup);
        assertNotNull(root);
        List<MarkupBlock> subRootBlocks = root.getChildren();
        assertEquals(1, subRootBlocks.size());
        assertEquals("{{langue|fr}}", subRootBlocks.iterator().next().getHeader().getValue());
        List<MarkupBlock> leaves = subRootBlocks.iterator().next().getChildren();
        assertEquals(2, leaves.size());
        assertEquals(List.of("{{S|étymologie}}", "{{S|nom|fr}}"), leaves.stream().map(MarkupBlock::getHeader).map(MarkupHeader::getValue).collect(Collectors.toList()));
    }

    @Test
    public void testLargerSample() {
        WiktionaryMarkup markup = new WiktionaryMarkup(SAMPLE_M, Locale.FRENCH);
        MarkupBlock root = chunker.chunk(markup);
        assertNotNull(root);
        List<MarkupBlock> children = root.getChildren();
        assertEquals(List.of("{{langue|fr}}"), children.stream().map(MarkupBlock::getHeader).map(MarkupHeader::getValue).collect(Collectors.toList()));
        children = children.iterator().next().getChildren();
        assertEquals(List.of("{{S|étymologie}}", "{{S|nom|fr}}", "{{S|prononciation}}"), children.stream().map(MarkupBlock::getHeader).map(MarkupHeader::getValue).collect(Collectors.toList()));
        children = children.get(1).getChildren();
        assertEquals(List.of("{{S|dérivés}}", "{{S|traductions}}"), children.stream().map(MarkupBlock::getHeader).map(MarkupHeader::getValue).collect(Collectors.toList()));
    }
}