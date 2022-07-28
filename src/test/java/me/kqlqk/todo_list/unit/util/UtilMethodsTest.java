package me.kqlqk.todo_list.unit.util;

import me.kqlqk.todo_list.util.UtilMethods;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilMethodsTest {

    @Test
    public void getImprovedUrl() {
        String improvedUrl = UtilMethods.getImprovedUrl("http://localhost:8080/test//");

        Assertions.assertEquals("/test/", improvedUrl);
    }

    @Test
    public void random(){
        System.out.println(RandomString.make(30));
    }
}