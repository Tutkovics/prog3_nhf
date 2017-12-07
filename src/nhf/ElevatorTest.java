package nhf;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class ElevatorTest {
    Elevator el;
    @Before
    public void setUp() throws Exception {
        el = new Elevator("Name",20,0,18,0.3, new JTextArea());
        el.donePassengers = 10;
    }

    @Test
    public void cleanAttributes() throws Exception {
        el.cleanAttributes();
        assertEquals(0, el.donePassengers, 0);
    }

    @Test
    public void isBetween() throws Exception {
        assertTrue(el.isBetween(9,3,5));
        assertFalse(el.isBetween(9,3,2));
    }

}