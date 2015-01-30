package Format;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class formatNumberTest {

    @Test
    public void testID() throws Exception {
        assertEquals("ID", "900000123", formatNumber.ID(123));
    }

    @Test
    public void testServiceCode() throws Exception {
        assertEquals("Service Code", formatNumber.serviceCode(234), "600234");
    }
}