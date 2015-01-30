package Person;

import junit.framework.TestCase;
import org.junit.Test;

public class RoleTest extends TestCase {
    Role role1 = new Role();

    @Test
    public void testGetName() throws Exception {
        assertEquals("anonymous", role1.name);
    }
}