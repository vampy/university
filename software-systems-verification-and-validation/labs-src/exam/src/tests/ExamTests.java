package tests;

import exam.Main;

import exam.ValueException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ExamTests
{
    protected Main main;

    @org.junit.Before
    public void setUp() throws Exception
    {
        main = new Main();
    }

    @Test
    public void testThrows() // tc 1
    {
        ArrayList<Integer> t = new ArrayList<>();

        try
        {
            main.replaceAllFromIntervalWithValue(t, 100, 0, 10);
            Assert.assertTrue(false);
        }
        catch (ValueException e)
        {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testOneItem() // tc 2
    {
        ArrayList<Integer> t = new ArrayList<>();
        t.add(100);

        try
        {
            main.replaceAllFromIntervalWithValue(t, 0, 10, 5);
            Assert.assertEquals((int)t.get(0), 5);
        }
        catch (ValueException e)
        {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testEmpty() // tc 3
    {
        ArrayList<Integer> t = new ArrayList<>();

        try
        {
            main.replaceAllFromIntervalWithValue(t, 0, 10, 5);
            Assert.assertTrue(t.isEmpty());
        }
        catch (ValueException e)
        {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testTwoItemsTrue() // tc 4
    {
        ArrayList<Integer> t = new ArrayList<>();
        t.add(100);
        t.add(100);

        try
        {
            main.replaceAllFromIntervalWithValue(t, 0, 10, 5);
            Assert.assertEquals((int)t.get(0), 5);
            Assert.assertEquals((int)t.get(1), 5);
        }
        catch (ValueException e)
        {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testTwoItemsFalse() // tc 5
    {
        ArrayList<Integer> t = new ArrayList<>();
        t.add(100);
        t.add(100);

        try
        {
            main.replaceAllFromIntervalWithValue(t, 50, 150, 5);
            Assert.assertEquals((int)t.get(0), 100);
            Assert.assertEquals((int)t.get(1), 100);
        }
        catch (ValueException e)
        {
            Assert.assertTrue(false);
        }
    }
}
