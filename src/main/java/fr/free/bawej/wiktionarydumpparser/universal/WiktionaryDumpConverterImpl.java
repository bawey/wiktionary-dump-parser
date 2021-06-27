package fr.free.bawej.wiktionarydumpparser.universal;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WiktionaryDumpConverterImpl implements WiktionaryDumpConverter {

    private final List<MarkupChunker> chunkers;
    private final List<WiktionaryMarkupBlockInformationExtractor> blockExtractors;

    public WiktionaryDumpConverterImpl(List<MarkupChunker> chunkers, List<WiktionaryMarkupBlockInformationExtractor> blockExtractors) {
        this.chunkers = chunkers;
        this.blockExtractors = blockExtractors;
    }

    private Optional<MarkupChunker> getChunkerForLanguage(Locale language) {
        return this.chunkers.stream().filter(c -> c.isSupported(language)).findAny();
    }

    // TODO: use some caching to store likely handlers by language and block's header - if need be
    private Optional<WiktionaryMarkupBlockInformationExtractor> getExtractorForLanguageAndBlock(Locale language, WiktionaryMarkupBlock block) {
        return this.blockExtractors.stream().filter(e -> e.isSupported(language, block)).findAny();
    }

    @Override
    public Locale getLanguage() {
        return null;
    }

    @Override
    public Iterable<WiktionaryEntry> convertDumpRecords(Collection<WiktionaryDumpRecord> records) {
        List<WiktionaryEntry> result = new ArrayList<>(records.size());

        for (WiktionaryDumpRecord dumpRecord : records) {
            WiktionaryEntry.Builder builder = new WiktionaryEntry.Builder(dumpRecord.language(), dumpRecord.id(), dumpRecord.title());
            MarkupChunker chunker = getChunkerForLanguage(dumpRecord.language()).orElseThrow();
            WiktionaryMarkupBlock root = chunker.chunk(dumpRecord.markup());

            root.getPreorderSubtree().stream().forEach(b -> {
                this.getExtractorForLanguageAndBlock(dumpRecord.language(), b).stream().forEach(e -> e.extractIntoEntryBuilder(b, builder));
            });

            result.add(builder.build());


        }
        return result;
    }

}
