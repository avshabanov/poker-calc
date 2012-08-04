package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.EncodeUtil;
import com.alexshabanov.cards.util.ReaderUtil;
import org.junit.Test;

import java.util.*;

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
                return false;
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

    @Test
    public void testStop1() {
        final Set<List<Integer>> actualCombinations = new HashSet<List<Integer>>();

        HandUtil.iterate(cards, new HandUtil.CardCallback() {
            @Override
            public boolean process(List<Integer> cards) {
                assertTrue(actualCombinations.add(cards));
                return actualCombinations.size() > 1;
            }
        }, 1);

        final List[] expected = new List[] {Arrays.asList(0), Arrays.asList(1)};
        assertEquals(new HashSet<List>(Arrays.<List>asList(expected)), actualCombinations);
    }

    @Test
    public void testStop2() {
        final Set<List<Integer>> actualCombinations = new HashSet<List<Integer>>();

        HandUtil.iterate(cards, new HandUtil.CardCallback() {
            @Override
            public boolean process(List<Integer> cards) {
                assertTrue(actualCombinations.add(cards));
                return actualCombinations.size() > 2;
            }
        }, 1);

        final List[] expected = new List[] {Arrays.asList(0), Arrays.asList(1), Arrays.asList(2)};
        assertEquals(new HashSet<List>(Arrays.<List>asList(expected)), actualCombinations);
    }

    @Test
    public void testStraightComparator() {
        {
            final List<Card> sorted = new ArrayList<Card>(ReaderUtil.cardsFromLatin1("Ac Ad Ah As"));
            Collections.sort(sorted, new HandUtil.StraightCardsComparator());
            assertEquals(ReaderUtil.cardsFromLatin1("As Ah Ad Ac"), sorted);
        }

        {
            final List<Card> sorted = new ArrayList<Card>(ReaderUtil.cardsFromLatin1("2d Kh Jh 8h"));
            Collections.sort(sorted, new HandUtil.StraightCardsComparator());
            assertEquals(ReaderUtil.cardsFromLatin1("8h Jh Kh 2d"), sorted);
        }
    }

    @Test
    public void testPositiveStraightFlush() {
        {
            final List<Integer> cardCodes = EncodeUtil.toCodes(ReaderUtil.cardsFromLatin1("8s 9h Qh Th Js Jh Kh"));
            final List<Integer> combinationCodes = new ArrayList<Integer>();
            assertTrue(HandUtil.maybeStraightFlush(cardCodes, new HandUtil.SimpleCombinationSink() {
                @Override
                public void setHandCode(List<Integer> combinationCardCodes) {
                    assertEquals(0, combinationCodes.size());
                    combinationCodes.addAll(combinationCardCodes);
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("9h Th Jh Qh Kh"), EncodeUtil.fromCodes(combinationCodes));
        }

        {
            final List<Integer> cardCodes = EncodeUtil.toCodes(ReaderUtil.cardsFromLatin1("2h 3c Ac Kd 2c 4c 5c"));
            final List<Integer> combinationCodes = new ArrayList<Integer>();
            assertTrue(HandUtil.maybeStraightFlush(cardCodes, new HandUtil.SimpleCombinationSink() {
                @Override
                public void setHandCode(List<Integer> combinationCardCodes) {
                    assertEquals(0, combinationCodes.size());
                    combinationCodes.addAll(combinationCardCodes);
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("Ac 2c 3c 4c 5c"), EncodeUtil.fromCodes(combinationCodes));
        }

        // royal flush
        {
            final List<Integer> cardCodes = EncodeUtil.toCodes(ReaderUtil.cardsFromLatin1("2s As Ks 7d Js Qs Ts"));
            final List<Integer> combinationCodes = new ArrayList<Integer>();
            assertTrue(HandUtil.maybeStraightFlush(cardCodes, new HandUtil.SimpleCombinationSink() {
                @Override
                public void setHandCode(List<Integer> combinationCardCodes) {
                    assertEquals(0, combinationCodes.size());
                    combinationCodes.addAll(combinationCardCodes);
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("Ts Js Qs Ks As"), EncodeUtil.fromCodes(combinationCodes));
        }
    }

    @Test
    public void testPositiveFourOfAKind() {
        final List<Integer> cardCodes = EncodeUtil.toCodes(ReaderUtil.cardsFromLatin1("Qd 9h Qs Ac Qc Tc Qh"));
        final List<Integer> combinationCodes = new ArrayList<Integer>();
        assertTrue(HandUtil.maybeFourOfAKind(cardCodes, new HandUtil.SimpleCombinationSink() {
            @Override
            public void setHandCode(List<Integer> combinationCardCodes) {
                assertEquals(0, combinationCodes.size());
                combinationCodes.addAll(combinationCardCodes);
            }
        }));

        assertEquals(ReaderUtil.cardsFromLatin1("Qd Qs Qc Qh"), EncodeUtil.fromCodes(combinationCodes));
    }
}
