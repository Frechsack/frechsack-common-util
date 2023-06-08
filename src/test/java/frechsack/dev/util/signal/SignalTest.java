package frechsack.dev.util.signal;

import org.junit.Assert;
import org.junit.Test;

public class SignalTest {


    @Test
    public void expressionTest() throws InterruptedException {
        var width = Signal.ofInt(1920);
        var height = Signal.ofInt(1080);
        var pixelCount = Signal.pipeOfInt(() -> width.getAsInt() * height.getAsInt(), width, height).build();
        var pixelCountI = Signal.pipeOfInt(() -> pixelCount.getAsInt() / 2, pixelCount).build();

        Assert.assertEquals(1080, height.getAsInt());
        Assert.assertEquals(1920, width.getAsInt());
        Assert.assertEquals(width.getAsInt() * height.getAsInt(), pixelCount.getAsInt());

        width.set(720);
        Assert.assertEquals(720, width.getAsInt());

        Thread.sleep(200);
        Assert.assertEquals(720 * 1080 / 2, pixelCountI.getAsInt());

    }
}
