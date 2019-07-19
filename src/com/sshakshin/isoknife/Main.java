package com.sshakshin.isoknife;

import com.sshakshin.isoknife.containers.Container;
import com.sshakshin.isoknife.iso8583.IsoFile;
import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.Tracer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    private static void printHelp() {
        System.out.println("\n" +
                "Command line options:\n" +
                "\n" +
                "   -m <working mode> (parse | merge)\n" +
                "   -mode\n" +
                "\n" +
                "   -t\n" +
                "   -trace\n" +
                "\n" +
                "   -s <path to source file>\n" +
                "   -src\n" +
                "   -source\n" +
                "\n" +
                "   -d <path to destination file>\n" +
                "   -dst\n" +
                "   -destination\n" +
                "\n" +
                "   -c <container type> (clean | RDW | PreEdit | Fixed1014 )\n" +
                "   -cont\n" +
                "   -container\n" +
                "\n" +
                "   -e <data encoding> (ACSII | EBCDIC)\n" +
                "   -enc\n" +
                "   -encoding\n" +
                "\n" +
                "In PARSE mode ISO messages wil be parsed from ISO file and written to XML.\n" +
                "In MERGE mode ISO messages will be loaded from XML file and written to ISO.\n" +
                "\n" +
                "This tool could be used to transcode ISO files between different formats/encodings or to edit messages data.\n" +
                "Only Mastercard IPM files are supported at the moment."
        );
    }

    public static void main(String[] args) {
        System.out.println("ISO 8583 Swiss Army Knife by Sergey V. Shakshin");
        AppConfig cfg = AppConfig.init(args);
        if (!cfg.isValid) {
            System.out.println("Application configuration is not valid");
            printHelp();
            return;
        }
        Tracer.enabled = cfg.trace;

        IsoFile file = null;
        try {
            switch (cfg.mode) {
                case PARSE:
                    file = IsoFile.parse(Container.getInputStream());
                    file.save(cfg.destination);
                    break;
                case MERGE:
                    file = IsoFile.load(cfg.source);
                    file.merge(Container.getOutputStream());
                    break;

            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Source file not found.");
            return;
        } catch (IOException ioe) {
            System.out.println("Error while reading source file: " + ioe.getMessage());
        }

        try {
            switch (cfg.mode) {
                case PARSE:
                    file.save(cfg.destination);
                    break;
                case MERGE:
                    file.merge(Container.getOutputStream());
                    break;

            }
        } catch (Exception e) {
            System.out.println("Error while writing destination file: " + e.getMessage());
        }
    }
}