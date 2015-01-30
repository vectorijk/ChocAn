package database;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SQLiteInterfaceTest {

    @Test
    public void testGetLastIDinAccount() throws Exception {
        SQLiteInterface DB = new SQLiteInterface();
        assertEquals(DB.getLastIDinAccount(), 20);
    }

    @Test
    public void testInsertRoleToAccountAndRoleTable() throws Exception {

    }

    @Test
    public void testInsertRoleToAccountAndRoleTable1() throws Exception {

    }

    @Test
    public void testInsertRoleToAccountAndRoleTable2() throws Exception {

    }
}