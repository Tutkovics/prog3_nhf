package sample;//package sample;

import org.junit.Assert;
import org.junit.Test;
import sample.Call;
import sample.Timer;

import static org.junit.Assert.*;

public class CallTest {
    Call call1;
    Call call2;
    Call call3;

    @org.junit.Before
    public void setUp() throws Exception {
        Timer timer = new Timer(12,15,23);
        call1 = new Call(10, 15, timer);
        call2 = new Call(10, 15, timer);
        call3 = new Call(10, 16, timer);
    }

    @Test
    public void testConstuctor(){
        //Assert.assertEquals(0,call1.id, 0);
        Assert.assertEquals(10, call1.from, 0);
        Assert.assertEquals(15, call1.to,  0);
        Assert.assertEquals(12, call1.timer.hh,0);
        Assert.assertEquals(15, call1.timer.mm,0);
        Assert.assertEquals(23, call1.timer.ss, 0);
    }

    @org.junit.Test
    public void compareToTest() throws Exception {
        Assert.assertEquals(0, call1.compareTo(call2), 0);
        Assert.assertEquals(-1, call1.compareTo(call3), 0);
    }

}