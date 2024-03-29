package unit.me.kqlqk.todo_list.util;

import me.kqlqk.todo_list.util.UtilCookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UtilCookieTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Cookie cookie;

    @Test
    public void createOrUpdateCookie_shouldCreateOrUpdateCookie() {
        UtilCookie.createOrUpdateOrDeleteCookie("anyName", "anyValue", 10, request, response);
    }

    @Test
    public void createOrUpdateCookie_shouldThrowsAllExceptions() {
        assertThrows(IllegalArgumentException.class,
                () -> UtilCookie.createOrUpdateOrDeleteCookie(null, "anyValue", 10, request, response));

        assertThrows(IllegalArgumentException.class,
                () -> UtilCookie.createOrUpdateOrDeleteCookie("anyName", "anyValue", -3, request, response));

        assertThrows(IllegalArgumentException.class,
                () -> UtilCookie.createOrUpdateOrDeleteCookie("anyName", "anyValue", 10, null, response));

        assertThrows(IllegalArgumentException.class,
                () -> UtilCookie.createOrUpdateOrDeleteCookie("anyName", "anyValue", 10, request, null));
    }

    @Test
    public void getCookieByName_shouldGetCookie() {
        doReturn("anyName").when(cookie).getName();
        doReturn(new Cookie[]{cookie}).when(request).getCookies();

        Cookie cookie = UtilCookie.getCookieByName("anyName", request);

        assertThat(cookie.getName()).isEqualTo(this.cookie.getName());
    }

    @Test
    public void getCookieByName_shouldThrowsAllExceptions(){
        assertThrows(IllegalArgumentException.class,
                () -> UtilCookie.getCookieByName(null, request));

        assertThrows(IllegalArgumentException.class,
                () -> UtilCookie.getCookieByName("anyName", null));
    }

    @Test
    public void isCookieExistsByName_shouldCheckIfCookieExistsByName() {
        assertThat(UtilCookie.isCookieExistsByName("anyName", request)).isFalse();

        doReturn("anyName").when(cookie).getName();
        doReturn(new Cookie[]{cookie}).when(request).getCookies();
        assertThat(UtilCookie.isCookieExistsByName("anyName", request)).isTrue();
    }
}