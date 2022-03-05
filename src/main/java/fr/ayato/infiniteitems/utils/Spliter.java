package fr.ayato.infiniteitems.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Spliter {

    // split a string with the ", " character and return it in a list
    public static List<String> strToList(String string) {
        List<String> list = new ArrayList<>();
        Stream.of(string)
                .map(s -> s.split(", "))
                .flatMap(Stream::of)
                .forEach(list::add);
        return list;
    }

    // Split a string with the "[" & the "]" characters and return it
    public static String stringToSplit(String string) {
        string = string.replace("[", "");
        string = string.replace("]", "");
        return string;
    }

    // return
    public static String itemPathToName(String string) {
        return Arrays.toString(string.split("items."));
    }
}
