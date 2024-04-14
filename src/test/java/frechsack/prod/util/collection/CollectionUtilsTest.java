package frechsack.prod.util.collection;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionUtilsTest extends TestCase {

    @Test
    public void testHighestFrequency() {
        final var elements = new ArrayList<Integer>();
        elements.add(null);
        elements.add(1);
        elements.add(1);
        elements.add(3);
        elements.add(3);
        elements.add(8);
        elements.add(8);
        elements.add(null);
        elements.add(null);
        Assert.assertEquals(null, CollectionUtils.highestFrequency(elements));
    }

    @Test
    public void testHighestFrequencyExcludeNull() {
        final var elements = new ArrayList<Integer>();
        elements.add(null);
        elements.add(1);
        elements.add(1);
        elements.add(3);
        elements.add(3);
        elements.add(8);
        elements.add(8);
        elements.add(null);
        elements.add(null);
        elements.add(null);
        Assert.assertEquals(1,(int) CollectionUtils.highestFrequencyExcludeNull(elements));
    }
}