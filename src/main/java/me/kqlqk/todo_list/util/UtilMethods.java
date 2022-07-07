package me.kqlqk.todo_list.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UtilMethods {

    public static String getImprovedUrl(String url){
        StringBuilder improvedUrl = new StringBuilder();

        List<String> words = new ArrayList<>(List.of(url.split("/")));

        for (String word : words) {
            if (Objects.equals(word, "")) {
                continue;
            }
            improvedUrl.append(word).append("/");
        }

        return improvedUrl.toString();
    }

}
