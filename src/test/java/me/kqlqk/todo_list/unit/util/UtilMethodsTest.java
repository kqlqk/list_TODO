package me.kqlqk.todo_list.unit.util;

import me.kqlqk.todo_list.util.UtilMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilMethodsTest {

    @Test
    public void getImprovedUrl() {
        String improvedUrl = UtilMethods.getImprovedUrl("localhost:8080//test/////dsad/r321//213");

        Assertions.assertEquals("localhost:8080/test/dsad/r321/213/", improvedUrl);
    }
}