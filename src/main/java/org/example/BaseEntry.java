package org.example;

public abstract class BaseEntry implements ArchiveEntry {
    private final String path;

    public BaseEntry(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
