package sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class TimerTest {
    Timer timer1;

    @Before
    public void setUp() throws Exception {
        timer1 = new Timer(11,59,59);
    }

    @Test(expected=InvalidParameterException.class)
    public void wrongParameterGiven(){
        new Timer(12,60,59);
    }

    @Test
    public void equalTest(){
        boolean equal = timer1.equals(new Timer(11,43,59));
        Assert.assertTrue(equal);
    }

    @Test
    public void addSecondTest(){
        Timer expected = new Timer(12,0,0);
        // add a second to timer1
        timer1.addSecond();
        boolean equal = timer1.equals(expected);
        Assert.assertTrue(equal);
    }

}