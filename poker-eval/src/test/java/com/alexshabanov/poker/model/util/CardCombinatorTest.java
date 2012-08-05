package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.EncodeUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CardCombinatorTest {

    private final Card c0 = EncodeUtil.fromCode(0);
    private final Card c1 = EncodeUtil.fromCode(1);
    private final Card c2 = EncodeUtil.fromCode(2);
    private final Card c3 = EncodeUtil.fromCode(3);

    private final List<Card> cards = Arrays.asList(c0, c1, c2, c3);

    private void verifyCombinations(int headCombinationSize, Set<List<Card>> expected) {
        final Set<List<Card>> actualCombinations = new HashSet<List<Card>>();
        CardCombinator.iterate(cards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                assertTrue(actualCombinations.add(cards));
                return false;
            }
        }, headCombinationSize);

        assertEquals(expected, actualCombinations);
    }

    @Test
    public void testIteration1() {
        verifyCombinations(1, new HashSet<List<Card>>() {{
            add(Arrays.asList(c0));
            add(Arrays.asList(c1));
            add(Arrays.asList(c2));
            add(Arrays.asList(c3));
        }});
    }

    @Test
    public void testIteration2() {
        verifyCombinations(2, new HashSet<List<Card>>() {{
            add(Arrays.asList(c0, c1));
            add(Arrays.asList(c0, c2));
            add(Arrays.asList(c0, c3));
            add(Arrays.asList(c1, c2));
            add(Arrays.asList(c1, c3));
            add(Arrays.asList(c2, c3));
        }});
    }

    @Test
    public void testIteration3() {
        verifyCombinations(3, new HashSet<List<Card>>() {{
            add(Arrays.asList(c0, c1, c2));
            add(Arrays.asList(c0, c1, c3));
            add(Arrays.asList(c0, c2, c3));
            add(Arrays.asList(c1, c2, c3));
        }});
    }

    @Test
    public void testIteration4() {
        verifyCombinations(4, new HashSet<List<Card>>() {{
            add(Arrays.asList(c0, c1, c2, c3));
        }});
    }

    @Test
    public void testStop1() {
        final Set<List<Card>> actualCombinations = new HashSet<List<Card>>();

        CardCombinator.iterate(cards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                assertTrue(actualCombinations.add(cards));
                return actualCombinations.size() > 1;
            }
        }, 1);

        assertEquals(new HashSet<List<Card>>() {{
            add(Arrays.asList(c0));
            add(Arrays.asList(c1));
        }}, actualCombinations);
    }

    @Test
    public void testStop2() {
        final Set<List<Card>> actualCombinations = new HashSet<List<Card>>();

        CardCombinator.iterate(cards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                assertTrue(actualCombinations.add(cards));
                return actualCombinations.size() > 2;
            }
        }, 1);

        assertEquals(new HashSet<List<Card>>() {{
            add(Arrays.asList(c0));
            add(Arrays.asList(c1));
            add(Arrays.asList(c2));
        }}, actualCombinations);
    }
}
