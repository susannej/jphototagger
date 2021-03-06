package org.jphototagger.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Elmar Baumann
 */
public class CollectionUtilTest {

    /**
     * Test of binaryInsert method, of class CollectionUtil.
     */
    @Test
    public void testBinaryInsert() {
        LinkedList<String> list = new LinkedList<>();
        String el2 = "Birne";

        CollectionUtil.binaryInsert(list, el2);
        assertTrue(list.size() == 1);
        assertEquals(el2, list.get(0));

        String el1 = "Apfel";

        CollectionUtil.binaryInsert(list, el1);
        assertTrue(Arrays.equals(list.toArray(), new Object[]{el1, el2}));

        String el3 = "Zitrone";

        CollectionUtil.binaryInsert(list, el3);
        assertTrue(Arrays.equals(list.toArray(),
                new Object[]{el1, el2, el3}));
        list.clear();
        CollectionUtil.binaryInsert(list, el1);
        CollectionUtil.binaryInsert(list, el3);
        assertTrue(Arrays.equals(list.toArray(), new Object[]{el1, el3}));
        list.clear();
        CollectionUtil.binaryInsert(list, el1);
        CollectionUtil.binaryInsert(list, el2);
        CollectionUtil.binaryInsert(list, el1);
        assertTrue(Arrays.equals(list.toArray(),
                new Object[]{el1, el1, el2}));
        list.clear();
        CollectionUtil.binaryInsert(list, el2);
        CollectionUtil.binaryInsert(list, el1);
        CollectionUtil.binaryInsert(list, el1);
        CollectionUtil.binaryInsert(list, el2);
        assertTrue(Arrays.equals(list.toArray(), new Object[]{el1, el1, el2,
                    el2}));
    }

    /**
     * Test of integerTokenToArray method, of class ArrayUtil.
     */
    @Test
    public void testIntegerTokenToList() {
        String string = "1,125,7";
        String delimiter = ",";
        List<Integer> expResult = new ArrayList<>();

        expResult.add(1);
        expResult.add(125);
        expResult.add(7);

        List<Integer> result = CollectionUtil.integerTokenToList(string,
                delimiter);

        assertEquals(expResult, result);
        string = "-1,42,29:33,72";
        delimiter = ":,";
        expResult = new ArrayList<>();
        expResult.add(-1);
        expResult.add(42);
        expResult.add(29);
        expResult.add(33);
        expResult.add(72);
        result = CollectionUtil.integerTokenToList(string, delimiter);
        assertEquals(expResult, result);
        string = "-1";
        delimiter = ",";
        expResult = new ArrayList<>();
        expResult.add(-1);
        result = CollectionUtil.integerTokenToList(string, delimiter);
        assertEquals(expResult, result);
        string = "";
        delimiter = ",";
        expResult = new ArrayList<>();
        result = CollectionUtil.integerTokenToList(string, delimiter);
        assertEquals(expResult, result);

        try {
            string = "12,Peter";
            delimiter = ",";
            expResult = new ArrayList<>();
            result = CollectionUtil.integerTokenToList(string, delimiter);
            fail("no NumberFormatException");
        } catch (NumberFormatException ex) {
            // ok
        }

        try {
            CollectionUtil.integerTokenToList(null, "");
            fail("NullpointerException was not thrown");
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            CollectionUtil.integerTokenToList("", null);
            fail("NullpointerException was not thrown");
        } catch (NullPointerException ex) {
            // ok
        }
    }

    /**
     * Test of stringTokenToList method, of class ArrayUtil.
     */
    @Test
    public void testStringTokenToList() {
        String string = "anton,berta,cäsar,wilhelm";
        String delimiter = ",:";
        List<String> expResult = new ArrayList<>();

        expResult.add("anton");
        expResult.add("berta");
        expResult.add("cäsar");
        expResult.add("wilhelm");

        List<String> result = CollectionUtil.stringTokenToList(string, delimiter);

        assertEquals(expResult, result);
        string = "anton,berta,cäsar:wilhelm";
        delimiter = ",:";
        expResult = new ArrayList<>();
        expResult.add("anton");
        expResult.add("berta");
        expResult.add("cäsar");
        expResult.add("wilhelm");
        result = CollectionUtil.stringTokenToList(string, delimiter);
        assertEquals(expResult, result);
        string = "anton:berta::cäsar:wilhelm";
        delimiter = ",:";
        expResult = new ArrayList<>();
        expResult.add("anton");
        expResult.add("berta");
        expResult.add("cäsar");
        expResult.add("wilhelm");
        result = CollectionUtil.stringTokenToList(string, delimiter);
        assertEquals(expResult, result);
        string = "anton";
        delimiter = ",";
        expResult = new ArrayList<>();
        expResult.add(string);
        result = CollectionUtil.stringTokenToList(string, delimiter);
        assertEquals(expResult, result);
        string = "anton,berta,cäsar,:wilhelm";
        delimiter = "";
        expResult = new ArrayList<>();
        expResult.add(string);
        result = CollectionUtil.stringTokenToList(string, delimiter);
        assertEquals(expResult, result);
        string = "";
        delimiter = ",";
        expResult = new ArrayList<>();
        result = CollectionUtil.stringTokenToList(string, delimiter);
        assertEquals(expResult, result);

        try {
            CollectionUtil.stringTokenToList(null, "");
            fail("NullpointerException was not thrown");
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            CollectionUtil.stringTokenToList("", null);
            fail("NullpointerException was not thrown");
        } catch (NullPointerException ex) {
            // ok
        }
    }

    @Test
    public void testGetFirstElement() {
        Collection<String> strings = Collections.emptyList();
        String firstElement = CollectionUtil.getFirstElement(strings);
        String idefix = "Idefix";
        String pluto = "Pluto";

        assertNull(firstElement);
        strings = Arrays.asList(idefix);
        firstElement = CollectionUtil.getFirstElement(strings);
        assertEquals(idefix, firstElement);
        strings = Arrays.asList(pluto, idefix);
        firstElement = CollectionUtil.getFirstElement(strings);
        assertEquals(pluto, firstElement);
    }

    @Test
    public void testContainsStringIgnoreCase() {
        Collection<String> strings = Collections.emptyList();
        String string = "bla";
        boolean result = CollectionUtil.containsStringIgnoreCase(strings, string);

        assertFalse(result);

        strings = Arrays.asList("Bla");
        result = CollectionUtil.containsStringIgnoreCase(strings, string);
        assertTrue(result);

        strings = Arrays.asList("1", "bla", "2");
        result = CollectionUtil.containsStringIgnoreCase(strings, string);
        assertTrue(result);

        strings = Arrays.asList("1", "blubb", "2");
        result = CollectionUtil.containsStringIgnoreCase(strings, string);
        assertFalse(result);
    }
}
