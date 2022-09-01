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
        assertThrows(IllegalArgumentException.class, () -> UtilMethods.getImprovedUrl(null));
        assertThrows(IllegalArgumentException.class, () -> UtilMethods.getImprovedUrl(""));

    }

    @Test
    public void getURLPath_shouldThrowsNPE() {
        assertThrows(IllegalArgumentException.class, () -> UtilMethods.getURLPath(null));
    }

    @Test
    public void getUserFromJoinPoint_shouldThrowsNPE() {
        assertThrows(IllegalArgumentException.class, () -> UtilMethods.getUserFromJoinPoint(null));
    }
}