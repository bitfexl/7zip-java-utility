package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Exec7zip {
    private final String _7zipPath;

    public Exec7zip(String _7zipPath) {
        this._7zipPath = _7zipPath;
    }

    public List<Map<String, String>> listFiles(String archivePath) {
        final Process _7zip = start7zip(List.of("l", "-slt", "-ba", archivePath));
        final List<String> output = readAllLines(_7zip);
        exit(_7zip);

        final List<Map<String, String>> entries = new ArrayList<>();
        Map<String, String> currentEntry = new HashMap<>();

        for (String property : output) {
            if (property.isEmpty()) {
                entries.add(currentEntry);
                currentEntry = new HashMap<>();
                continue;
            }

            final String[] parts = property.split(Pattern.quote("="), 2);
            if (parts.length != 2) {
                throw new RuntimeException("7zip output could be parsed, expected key value pair listing files.");
            }

            currentEntry.put(parts[0].trim(), parts[1].trim());
        }

        if (!currentEntry.isEmpty()) {
            entries.add(currentEntry);
        }

        return entries;
    }

    private List<String> readAllLines(Process process) {
        final List<String> lines = new ArrayList<>();

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String lastLine;
            do {
                lastLine = in.readLine();
                if (lastLine != null) {
                    lines.add(lastLine);
                }
            } while (lastLine != null);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading from process.", ex);
        }

        return lines;
    }

    private void exit(Process process) {
        final int exitValue;

        try {
            exitValue = process.onExit().get().exitValue();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }

        if (exitValue != 0) {
            throw new RuntimeException("Process exited with code " + exitValue + ".");
        }
    }

    private Process start7zip(List<String> arguments) {
        return start7zip(null, arguments);
    }

    private Process start7zip(String workingDirectory, List<String> arguments) {
        final List<String> command = new ArrayList<>(List.of(_7zipPath));
        command.addAll(arguments);
        final ProcessBuilder builder = new ProcessBuilder(command);
        if (workingDirectory != null) {
            builder.directory(new File(workingDirectory));
        }
        try {
            return builder.start();
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Faulty arguments.", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to start 7zip.", ex);
        }
    }
}
