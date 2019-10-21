package com.allpiper.pdfreplacer;

/** @author Klaus Pfeiffer - klaus@allpiper.com */
public enum MatchMode {
    CONTAINS {
        @Override
        public boolean matches(String searchText, String text) {
            return text.contains(searchText);
        }
    },
    EQUALS {
        @Override
        public boolean matches(String searchText, String text) {
            return text.equals(searchText);
        }
    },
    TRIM_EQUALS {
        @Override
        public boolean matches(String searchText, String text) {
            return text.trim().equals(searchText);
        }
    },
    EQUALS_IGNORE_CASE {
        @Override
        public boolean matches(String searchText, String text) {
            return text.equalsIgnoreCase(searchText);
        }
    },
    TRIM_EQUALS_IGNORE_CASE {
        @Override
        public boolean matches(String searchText, String text) {
            return text.trim().equalsIgnoreCase(searchText);
        }
    },
    REGEX {
        @Override
        public boolean matches(String searchText, String text) {
            return RegexEngine.matches(searchText, text);
        }
    };

    public abstract boolean matches(String searchText, String text);
}
