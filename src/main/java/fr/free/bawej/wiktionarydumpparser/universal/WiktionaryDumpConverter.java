package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public interface WiktionaryDumpConverter {
    public Locale getLanguage();

    public Iterable<WiktionaryEntry> convertDumpRecords(Collection<WiktionaryDumpRecord> records);

    public default Iterable<WiktionaryEntry> convertDumpRecord(WiktionaryDumpRecord record) {
        return this.convertDumpRecords(Collections.singletonList(record));
    }
}
