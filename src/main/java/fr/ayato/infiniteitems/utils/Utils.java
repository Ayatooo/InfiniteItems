package fr.ayato.infiniteitems.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<String> replaceLore(List<String> lore, String pHolder, String replacer) {
        List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            if (s.contains(pHolder)) {
                s = s.replace(pHolder, replacer);
            }
            newLore.add(s);
        }
        return newLore;
    }

    //get the actual date format dd/mm/yyyy
    public static String getDate() {
        String date = "";
        int day = java.time.LocalDate.now().getDayOfMonth();
        int month = java.time.LocalDate.now().getMonthValue();
        int year = java.time.LocalDate.now().getYear();
        date = day + "/" + month + "/" + year;
        return date;
    }

}
