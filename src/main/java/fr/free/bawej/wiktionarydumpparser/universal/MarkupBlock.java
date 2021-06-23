package fr.free.bawej.wiktionarydumpparser.universal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class MarkupBlock {
    private final String rawHeader;
    private final String rawContent;
    private final MarkupHeader header;
    private final MarkupBlock parent;
    private final List<MarkupBlock> children;

    public static class Builder {
        private final List<String> lines = new LinkedList<>();
        private String rawHeader;
        private MarkupBlock nextOfKin;

        public Builder setRawHeader(String rawHeader){
            this.rawHeader = rawHeader;
            return this;
        }

        public Builder addLine(String line) {
            lines.add(line);
            return this;
        }

        public Builder setNextOfKin(MarkupBlock nextOfKin){
            this.nextOfKin = nextOfKin;
            return this;
        }

        public MarkupBlock build(){
            return new MarkupBlock(this.rawHeader, this.lines.stream().collect(Collectors.joining("\n")), this.nextOfKin);
        }
    }

    public MarkupBlock(String rawHeader, String rawContent, MarkupBlock nextOfKin) {
        this.rawHeader = rawHeader;
        this.header = rawHeader!= null ? createHeader(rawHeader) : null;
        this.rawContent = rawContent;
        while(nextOfKin != null && nextOfKin.getLevel() >= this.getLevel()){
            nextOfKin = nextOfKin.getParent();
        }
        this.parent = nextOfKin;
        if (this.parent != null) {
            nextOfKin.addChild(this);
        }
        this.children = new LinkedList<>();
    }

    public List<MarkupBlock> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    protected void addChild(MarkupBlock child) {
        this.children.add(child);
    }

    public int getLevel() {
        return header != null ? header.getLevel() : 0;
    }

    public MarkupBlock getParent(){
        return this.parent;
    }

    public String getRawContent() {
        return rawContent;
    }

    public MarkupHeader getHeader(){
        return this.header;
    }

    public boolean hasHeader() {
        // TODO: perhaps the header should be Optional<> if it isn't always present
        return this.header != null;
    }


    /**
     * Override it if need be to create headers using different rules
     * @param rawHeader
     * @return
     */
    protected MarkupHeader createHeader(String rawHeader){
        return new MarkupHeader(rawHeader);
    }
}
