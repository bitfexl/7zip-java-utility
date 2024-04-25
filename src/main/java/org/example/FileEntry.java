package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class FileEntry extends BaseEntry {
    private final int size;

    public FileEntry(String path, int size) {
        super(path);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    @JsonIgnore
    public Map<String, ArchiveEntry> getContents() {
        throw new UnsupportedOperationException();
    }
}
