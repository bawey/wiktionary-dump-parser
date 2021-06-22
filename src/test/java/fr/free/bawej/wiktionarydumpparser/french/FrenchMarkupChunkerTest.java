package fr.free.bawej.wiktionarydumpparser.french;

import fr.free.bawej.wiktionarydumpparser.universal.MarkupBlock;
import fr.free.bawej.wiktionarydumpparser.universal.MarkupHeader;
import fr.free.bawej.wiktionarydumpparser.universal.WiktionaryMarkup;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FrenchMarkupChunkerTest {

    private FrenchMarkupChunker chunker = new FrenchMarkupChunker();

    public static String SAMPLE_M = """                                   
            {{voir|Accueil}}

            == {{langue|fr}} ==
            === {{S|étymologie}} ===
            : {{siècle|XII}} {{déverbal|de=accueillir|lang=fr|m=1}}.

            === {{S|nom|fr}} ===
            {{fr-rég|a.kœj}}
            '''accueil''' {{pron|a.kœj|fr}} {{m}}
            # [[cérémonie|Cérémonie]] ou [[prestation]] réservée à un nouvel [[arrivant]], consistant généralement à lui [[souhaiter]] la [[bienvenue]] et à l’aider dans son [[intégration]] ou ses [[démarche]]s.
            # Manière dont une [[œuvre]] a été acceptée lors de sa sortie par le [[public]] et les [[critique]]s.

            ==== {{S|dérivés}} ====
            {{(}}
            * [[accueil de loisirs]]
            * [[station d’accueil]]
            {{)}}
                        
            ==== {{S|traductions}} ====
            {{trad-début|Cérémonie|1}}
            * {{T|en}} : {{trad+|en|welcome}} {{vieilli|nocat=1}}, {{trad+|en|reception}}, {{trad+|en|acceptance}}
            {{trad-fin}}
            {{trad-début|Lieu|2}}
            * {{T|en}} : {{trad+|en|reception}} (''desk'' = d’une société), {{trad+|en|courtesy}} (''counter, booth, desk'') {{États-Unis|nocat=1}}, {{trad+|en|customer service}} (''desk, booth, center'') {{États-Unis|nocat=1}}}
            {{trad-fin}}
            {{trad-début|Fait d’accueillir|3}}
            * {{T|en}} : {{trad+|en|reception}}
            {{cf|accueillir}}
            {{trad-fin}}
                        
            ===== {{S|traductions à trier}} =====
            {{trad-début}}
            * {{T|nl|trier}} : {{trad+|nl|ontvangst}}, {{trad+|nl|acceptatie}}, {{trad+|nl|aanneming}}, {{trad+|nl|aanvaarding}}
            {{trad-fin}}
                        
            === {{S|prononciation}} ===
            * {{pron-rimes|a.kœj|fr}}
            """;
    public static String SAMPLE_S = """
            == {{langue|fr}} ==
            === {{S|étymologie}} ===
            : {{siècle|XII}} {{déverbal|de=accueillir|lang=fr|m=1}}.
                   
            === {{S|nom|fr}} ===
            {{fr-rég|a.kœj}}
            '''accueil''' {{pron|a.kœj|fr}} {{m}}
            # [[cérémonie|Cérémonie]] ou [[prestation]] réservée à un nouvel [[arrivant]], consistant généralement à lui [[souhaiter]] la [[bienvenue]] et à l’aider dans son [[intégration]] ou ses [[démarche]]s.
                 """;

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