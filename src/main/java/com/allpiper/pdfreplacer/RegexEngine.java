package com.allpiper.pdfreplacer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public class RegexEngine {

    static Map<String, Pattern> compiled = new HashMap<>();
    private static Pattern pattern;

    public static boolean matches(String searchText, String text) {
        pattern = compiled.get(searchText);
        if (pattern != null) {
            return pattern.matcher(text).matches();
        }
        Pattern compile = Pattern.compile(searchText);
        compiled.put(searchText, compile);
        return compile.matcher(text).matches();
    }
}
