package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        final String _7zipPath = "C:\\Program Files\\7-Zip\\7z.exe";
        final Exec7zip exec7zip = new Exec7zip(_7zipPath);
        final ArchiveParser parser = new ArchiveParser();
        final ObjectMapper objectMapper = new ObjectMapper();
        final Scanner stdin = new Scanner(System.in);

        System.out.print("Enter archive path: ");
        final String archivePath = stdin.nextLine();
        System.out.println("Listing '" + archivePath + "':");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parser.parseEntries(exec7zip.listFiles(archivePath))));
    }
}