package com.sshakshin.isoknife.util;

public class Tracer {
    private Tracer() {}
    public static boolean enabled = false;
    public static void log(String source, String message) {
        if (!enabled) return;

        System.out.println(String.format("[%s]: %s", source, message));
    };
}
