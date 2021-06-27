package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class WiktionaryMarkupBlock {
    private final String rawHeader;
    private final String rawContent;
    private final WiktionaryMarkupBlockHeader header;
    private final WiktionaryMarkupBlock parent;
    private final List<WiktionaryMarkupBlock> children;

    public static class Builder {
        private final List<String> lines = new LinkedList<>();
        private String rawHeader;
        private WiktionaryMarkupBlock nextOfKin;

        public Builder setRawHeader(String rawHeader) {
            this.rawHeader = rawHeader;
            return this;
        }

        public Builder addLine(String line) {
            lines.add(line);
            return this;
        }

        public Builder setNextOfKin(WiktionaryMarkupBlock nextOfKin) {
            this.nextOfKin = nextOfKin;
            return this;
        }

        public WiktionaryMarkupBlock build() {
            return new WiktionaryMarkupBlock(this.rawHeader, this.lines.stream().collect(Collectors.joining("\n")), this.nextOfKin);
        }
    }

    public WiktionaryMarkupBlock(String rawHeader, String rawContent){
        this(rawHeader, rawContent, null);
    }

    public WiktionaryMarkupBlock(String rawHeader, String rawContent, WiktionaryMarkupBlock nextOfKin) {
        this.rawHeader = rawHeader;
        this.header = rawHeader != null ? createHeader(rawHeader) : null;
        this.rawContent = rawContent;
        while (nextOfKin != null && nextOfKin.getLevel() >= this.getLevel()) {
            nextOfKin = nextOfKin.getParent();
        }
        this.parent = nextOfKin;
        if (this.parent != null) {
            nextOfKin.addChild(this);
        }
        this.children = new LinkedList<>();
    }

    public List<WiktionaryMarkupBlock> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    protected void addChild(WiktionaryMarkupBlock child) {
        this.children.add(child);
    }

    public int getLevel() {
        return header != null ? header.getLevel() : 0;
    }

    public WiktionaryMarkupBlock getParent() {
        return this.parent;
    }

    public String getRawContent() {
        return rawContent;
    }

    public WiktionaryMarkupBlockHeader getHeader() {
        return this.header;
    }

    public boolean hasHeader() {
        // TODO: perhaps the header should be Optional<> if it isn't always present
        return this.header != null;
    }

    public List<WiktionaryMarkupBlock> getPreorderSubtree() {
        List<WiktionaryMarkupBlock> list = new LinkedList<>();
        list.add(this);
        this.getChildren().stream().map(WiktionaryMarkupBlock::getPreorderSubtree).forEach(list::addAll);
        return list;
    }

    /**
     * Override it if need be to create headers using different rules
     *
     * @param rawHeader
     * @return
     */
    protected WiktionaryMarkupBlockHeader createHeader(String rawHeader) {
        return new WiktionaryMarkupBlockHeader(rawHeader);
    }
}
