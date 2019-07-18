package com.sshakshin.isoknife;

import com.sshakshin.isoknife.containers.Container;
import com.sshakshin.isoknife.iso8583.IsoFile;
import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.Tracer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("ISO 8583 Swiss Army Knife by Sergey V. Shakshin");
        AppConfig cfg = AppConfig.init(args);
        if (!cfg.isValid) {
            System.out.println("Application configuration is not valid");
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
                    break;

            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Source file not found.");
            return;
        } catch (IOException ioe) {
            System.out.println("Error while reading source file: " + ioe.getMessage());
        }


    }
}