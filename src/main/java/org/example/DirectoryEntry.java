package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

public class DirectoryEntry extends BaseEntry {
    private final Map<String, ArchiveEntry> contents = new HashMap<>();

    public DirectoryEntry(String path) {
        super(path);
    }

    @Override
    public int getSize() {
        int size = 0;
        for (ArchiveEntry entry : contents.values()) {
            size += entry.getSize();
        }
        return size;
    }

    @Override
    public Map<String, ArchiveEntry> getContents() {
        return contents;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
