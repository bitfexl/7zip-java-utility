package org.example;

import java.util.Map;

public interface ArchiveEntry {
    /**
     * Get the full path of the entry. It is normalized to ues '/' as the file separator.
     * @return The path of the file or directory (path in zip).
     */
    String getPath();

    /**
     * Test if the entry is a directory.
     * @return true: entry is a directory, false: entry is a file;
     */
    boolean isDirectory();

    /**
     * Get the contents of the directory. Can only be used if a directory.
     * @return A modifiable map of entries within this directory mapped by the file or directory name (not path).
     * @throws UnsupportedOperationException If this entry is a file.
     */
    Map<String, ArchiveEntry> getContents();

    /**
     * Get the size. Either the size of the file or the total size of the directory.
     * @return The total unpacked size in bytes.
     */
    int getSize();
}
