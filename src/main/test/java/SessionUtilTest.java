import me.kqlqk.springBootApp.models.User;
import me.kqlqk.springBootApp.util.SessionUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SessionUtilTest extends SessionUtil {
    @Test
    public void queryShouldReturnAnUser(){
        openTransactionSession();
        List<User> users = getSession().createQuery("from User where id = 83", User.class).list();
        closeTransactionSession();

        Assert.assertEquals("kononchuk99999@gmail.com",users.get(0).getEmail());
        Assert.assertEquals("kqlqk",users.get(0).getLogin());
    }
}
