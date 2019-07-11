package com.sshakshin.isoknife.util;

import sun.util.resources.cldr.om.CalendarData_om_ET;

public class AppConfig {
    public enum Mode { PARSE, MERGE }
    public enum Encoding { ASCII, EBCDIC }
    public enum IsoVariant { IPM }
    public enum Container { CLEAN, RDW, PREEDIT, FIXED1014 }

    public Mode mode = Mode.PARSE;
    public String source = null;
    public String destination = null;
    public Encoding encoding = Encoding.ASCII;
    public IsoVariant variant = IsoVariant.IPM;
    public Container container = Container.CLEAN;

    public boolean trace = false;

    public boolean isValid = true;

    private static AppConfig _instance = null;
    public static AppConfig init(String[] args) {
        _instance = new AppConfig(args);
        return _instance;
    }
    public static AppConfig get() {
        return _instance;
    }

    private AppConfig() {}
    private AppConfig(String[] args) {
        int i = 0;
        while (i < args.length) {
            switch (args[i++].toUpperCase()) {
                case "-TRACE":
                case "-T":
                    trace = true;
                    break;
                case "-MODE":
                case "-M":
                    if (i >= args.length) {
                        System.out.println("Wrong number of arguments");
                        isValid = false;
                        return;
                    }
                    switch (args[i].toUpperCase()) {
                        case "PARSE":
                            mode = Mode.PARSE;
                            break;
                        case "MERGE":
                            mode = Mode.MERGE;
                            break;
                        default:
                            System.out.println("Invalid mode: " + args[i]);
                            isValid = false;
                            return;
                    }
                    i++;
                    break;
                case "-SOURCE":
                case "-SRC":
                case "-S":
                    if (i >= args.length) {
                        System.out.println("Wrong number of arguments");
                        isValid = false;
                        return;
                    }
                    source = args[i];
                    i++;
                    break;
                case "-DESTINATION":
                case "-DST":
                case "-D":
                    if (i >= args.length) {
                        System.out.println("Wrong number of arguments");
                        isValid = false;
                        return;
                    }
                    destination = args[i];
                    i++;
                    break;
                case "-ENCODING":
                case "-ENC":
                case "-E":
                    if (i >= args.length) {
                        System.out.println("Wrong number of arguments");
                        isValid = false;
                        return;
                    }
                    switch (args[i].toUpperCase()) {
                        case "ASCII":
                            encoding = Encoding.ASCII;
                            break;
                        case "EBCDIC":
                            encoding = Encoding.EBCDIC;
                            break;
                        default:
                            System.out.println("Unknown encoding: " + args[i]);
                            isValid = false;
                            return;
                    }
                    i++;
                    break;
                case "-CONTAINER":
                case "-CONT":
                case "-C":
                    if (i >= args.length) {
                        System.out.println("Wrong number of arguments");
                        isValid = false;
                        return;
                    }
                    switch (args[i].toUpperCase()) {
                        case "CLEAN":
                            container = Container.CLEAN;
                            break;
                        case "RDW":
                            container = Container.RDW;
                            break;
                        case "PREEDIT":
                            container = Container.PREEDIT;
                            break;
                        case "FIXED1014":
                            container = Container.FIXED1014;
                            break;
                        default:
                            System.out.println("Unknown container: " + args[i]);
                            isValid = false;
                            return;
                    }
                    i++;
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
                    isValid = false;
                    return;
            }
        }
        if (source == null) {
            System.out.println("Source undefined");
            isValid = false;
            return;
        }
        if (destination == null) {
            System.out.println("Destination undefined");
            isValid = false;
            return;
        }
    }
}
