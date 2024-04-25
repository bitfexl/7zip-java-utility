package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ArchiveParser {
    /**
     * Parses a 7zip listing to a directory tree.
     * @param _7zipEntries The entries extracted by 7zip.
     * @return A single directory entry (the archive file) with the path equal to an empty string containing the contents of the root of the archive.
     */
    public DirectoryEntry parseEntries(List<Map<String, String>> _7zipEntries) {
        final DirectoryEntry baseEntry = new DirectoryEntry("");

        for (Map<String, String> rawEntry : _7zipEntries) {
            final String[] pathParts = rawEntry.get("Path").split(Pattern.quote(File.separator));

            ArchiveEntry parentEntry = baseEntry;

            for (int i = 0; i < pathParts.length - 1; i++) {
                ArchiveEntry possibleParent = parentEntry.getContents().get(pathParts[i]);

                if (possibleParent == null) {
                    possibleParent = new DirectoryEntry(computeName(pathParts, i));
                    parentEntry.getContents().put(pathParts[i], possibleParent);
                } else if (!possibleParent.isDirectory()) {
                    throw new IllegalArgumentException("Passed entries contains file which must also be a directory. " + possibleParent.getPath());
                }

                parentEntry = possibleParent;
            }

            final ArchiveEntry entry;
            if (rawEntry.get("Folder").equals("+")) {
                entry = new DirectoryEntry(computeName(pathParts, pathParts.length - 1));
            } else {
                entry = new FileEntry(computeName(pathParts, pathParts.length - 1), Integer.parseInt(rawEntry.get("Size")));
            }
            parentEntry.getContents().putIfAbsent(pathParts[pathParts.length - 1], entry);
        }

        return baseEntry;
    }

    private String computeName(String[] pathParts, int index) {
        final StringBuilder path = new StringBuilder();

        for (int i = 0; i < index; i++) {
            path.append(pathParts[i]).append("/");
        }

        return path.append(pathParts[index]).toString();
    }
}
