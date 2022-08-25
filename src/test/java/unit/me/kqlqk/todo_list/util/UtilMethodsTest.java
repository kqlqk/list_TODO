package unit.me.kqlqk.todo_list.util;

import me.kqlqk.todo_list.util.UtilMethods;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilMethodsTest {

    @Test
    public void getImprovedUrl_shouldGetImprovedUrl() {
        String improvedUrl = UtilMethods.getImprovedUrl("http:///localhost:8080//test//");

        assertEquals("/test/", improvedUrl);
    }

    @Test
    public void getImprovedUrl_shouldThrowsNPE() {
        assertThrows(NullPointerException.class, () -> UtilMethods.getImprovedUrl(null));
        assertThrows(NullPointerException.class, () -> UtilMethods.getImprovedUrl(""));

    }

    @Test
    public void getURLPath_shouldThrowsNPE() {
        assertThrows(NullPointerException.class, () -> UtilMethods.getURLPath(null, new String[]{"1"}));
    }

    @Test
    public void getUserFromJoinPoint_shouldThrowsNPE() {
        assertThrows(NullPointerException.class, () -> UtilMethods.getUserFromJoinPoint(null));
    }
}