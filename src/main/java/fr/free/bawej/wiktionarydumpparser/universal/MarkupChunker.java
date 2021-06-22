package fr.free.bawej.wiktionarydumpparser.universal;


import java.util.Locale;

public interface MarkupChunker {
    public boolean isSupported(Locale language);
    public boolean isBlockOpener(String line);
    public MarkupBlock chunk(WiktionaryMarkup markup);
}
