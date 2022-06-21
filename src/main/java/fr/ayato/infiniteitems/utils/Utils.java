package fr.ayato.infiniteitems.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<String> replaceLore(List<String> lore, String pHolder, String replacer) {

        List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            if (s.contains(pHolder)) {
                System.out.println("CONTIENT");
                s = s.replace(pHolder, replacer);
            }
            newLore.add(s);
        }
        return lore;
    }

}
