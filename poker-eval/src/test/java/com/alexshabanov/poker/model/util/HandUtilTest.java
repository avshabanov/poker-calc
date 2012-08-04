package com.alexshabanov.poker.model.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests hand util.
 */
public final class HandUtilTest {
    private final List<Integer> cards = Arrays.asList(0, 1, 2, 3);

    private void verifyCombinations(int headCombinationSize, List... expected) {
        final Set<List<Integer>> actualCombinations = new HashSet<List<Integer>>();
        HandUtil.iterate(cards, new HandUtil.CardCallback() {
            @Override
            public boolean process(List<Integer> cards) {
                assertTrue(actualCombinations.add(cards));
                return true;
            }
        }, headCombinationSize);

        assertEquals(new HashSet<List>(Arrays.<List>asList(expected)), actualCombinations);
    }

    @Test
    public void testIteration1() {
        verifyCombinations(1,
                Arrays.asList(0),
                Arrays.asList(1),
                Arrays.asList(2),
                Arrays.asList(3));
    }

    @Test
    public void testIteration2() {
        verifyCombinations(2,
                Arrays.asList(0, 1),
                Arrays.asList(0, 2),
                Arrays.asList(0, 3),
                Arrays.asList(1, 2),
                Arrays.asList(1, 3),
                Arrays.asList(2, 3));
    }

    @Test
    public void testIteration3() {
        verifyCombinations(3,
                Arrays.asList(0, 1, 2),
                Arrays.asList(0, 1, 3),
                Arrays.asList(0, 2, 3),
                Arrays.asList(1, 2, 3));
    }

    @Test
    public void testIteration4() {
        verifyCombinations(4,
                Arrays.asList(0, 1, 2, 3));
    }
}
