package fr.ayato.infiniteitems.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Spliter {

    public static List<String> strToList(String string) {
        List<String> list = new ArrayList<>();
        Stream.of(string)
                .map(s -> s.split(", "))
                .flatMap(Stream::of)
                .forEach(list::add);
        return list;
    }

}
