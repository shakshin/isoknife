package com.sshakshin.isoknife;

import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.Tracer;

public class Main {

    public static void main(String[] args) {
        System.out.println("ISO 8583 Swiss Army Knife by Sergey V. Shakshin");
        AppConfig cfg = AppConfig.init(args);
        if (!cfg.isValid) {
            System.out.println("Application configuration is not valid");
            return;
        }
        Tracer.enabled = cfg.trace;
        Tracer.log("main", "Application initialization complete. Session started.");
    }
}