package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.ReaderUtil;
import com.alexshabanov.poker.model.Hand;
import com.alexshabanov.poker.model.HandRank;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests hand util.
 */
public final class HandEvaluatorTest {

    private interface Combination {
        List<Card> maybe(List<Card> sourceCards);
    }

    private static abstract class AbstractCombination implements Combination {
        @Override
        public final List<Card> maybe(List<Card> sourceCards) {
            final List<Card> combinationCards = new ArrayList<Card>();
            if (!maybeCombination(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            })) {
                return null;
            }

            return combinationCards;
        }

        protected abstract boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink);
    }

    private static final class StraightFlushCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeStraightFlush(sourceCards, sink);
        }
    }

    private static final class FourOfAKindCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeFourOfAKind(sourceCards, sink);
        }
    }

    private static final class FullHouseCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeFullHouse(sourceCards, sink);
        }
    }

    private static final class FlushCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeFlush(sourceCards, sink);
        }
    }

    private static final class StraightCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeStraight(sourceCards, sink);
        }
    }

    private static final class ThreeOfAKindCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeThreeOfAKind(sourceCards, sink);
        }
    }

    private static final class TwoPairsCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybeTwoPairs(sourceCards, sink);
        }
    }

    private static final class PairCombination extends AbstractCombination {
        @Override
        protected boolean maybeCombination(List<Card> sourceCards, HandCombinationSink sink) {
            return HandEvaluator.maybePair(sourceCards, sink);
        }
    }


    private static void assertCombination(Combination combination,
                                          String sourceCardsLatin1,
                                          String expectedCardsLatin1) {
        final List<Card> sourceCards = ReaderUtil.cardsFromLatin1(sourceCardsLatin1);
        final List<Card> expectedCards = ReaderUtil.cardsFromLatin1(expectedCardsLatin1);
        assertEquals(expectedCards, combination.maybe(sourceCards));
    }



    @Test
    public void testPositiveStraightFlush() {
        assertCombination(new StraightFlushCombination(), "8s 9h Qh Th Js Jh Kh", "9h Th Jh Qh Kh");
        assertCombination(new StraightFlushCombination(), "2h 3c Ac Kd 2c 4c 5c", "Ac 2c 3c 4c 5c");
        // royal flush
        assertCombination(new StraightFlushCombination(), "2s As Ks 7d Js Qs Ts", "Ts Js Qs Ks As");
    }

    @Test
    public void testMultipleStraightFlushes() {
        assertCombination(new StraightFlushCombination(), "3d 4d 5d 6d 7d 8d 9d", "5d 6d 7d 8d 9d");
        assertCombination(new StraightFlushCombination(), "3d 4d 5d 6d 7d 9d 8d", "5d 6d 7d 8d 9d"); // transposed
        assertCombination(new StraightFlushCombination(), "8d 4d 7d 6d 5d 3d 9d", "5d 6d 7d 8d 9d"); // transposed
    }

    @Test
    public void testPositiveFourOfAKind() {
        assertCombination(new FourOfAKindCombination(), "Qd 9h Qs Ac Qc Tc Qh", "Qd Qs Qc Qh");
    }

    @Test
    public void testPositiveFullHouse() {
        assertCombination(new FullHouseCombination(), "Qd 2h Qs 2c Qc Tc Th", "Tc Th Qd Qs Qc");
    }

    @Test
    public void testPositiveFlush() {
        assertCombination(new FlushCombination(), "Qd 3h 9h Kh Jh 8c 5h", "3h 9h Kh Jh 5h");
    }

    @Test
    public void testMultipleFlushes() {
        assertCombination(new FlushCombination(), "2c 5c 7c Tc Jc Kc Ac", "7c Tc Jc Kc Ac");
    }

    @Test
    public void testPositiveStraight() {
        assertCombination(new StraightCombination(), "8s 9h Qc Th 2s Jh Kh", "9h Th Jh Qc Kh");
        assertCombination(new StraightCombination(), "2h 3c Ac Kd Qc 4d 5s", "Ac 2h 3c 4d 5s");
        assertCombination(new StraightCombination(), "2s Ah Ks 7d Js Qs Ts", "Ts Js Qs Ks Ah");
    }

    @Test
    public void testPositiveThreeOfAKind() {
        assertCombination(new ThreeOfAKindCombination(), "Qd 3h 9d Qh Jc Qc 5c", "Qd Qh Qc");
    }

    @Test
    public void testPositiveTwoPairs() {
        assertCombination(new TwoPairsCombination(), "Qd 3h 9d 5h Jc Qc 5c", "5h 5c Qd Qc");
    }

    @Test
    public void testPositivePair() {
        assertCombination(new PairCombination(), "Qd 3h 9d 5h Jc Qc 5c", "Qd Qc");
        assertCombination(new PairCombination(), "Qd 3h 9d 5h Jc Ac 5c", "5h 5c");
    }

    private static Hand evalHand(String sourceCardsLatin1) {
        return HandEvaluator.evaluate(ReaderUtil.cardsFromLatin1(sourceCardsLatin1));
    }

    private static void assertHandRank(Hand hand, HandRank expectedRank, String expectedCardsLatin1) {
        assertEquals(expectedRank, hand.getRank());
        assertEquals(ReaderUtil.cardsFromLatin1(expectedCardsLatin1), hand.getCards());
    }

    @Test
    public void testStraightFlushRating() {
        final Hand hand1 = evalHand("3s 4s 5s 2s As Qs Ks");
        final Hand hand2 = evalHand("4d 5d 6d 7d 8d 9d Ts");
        final Hand hand3 = evalHand("Kc Jc As 9c 9s Tc Qc");
        final Hand hand4 = evalHand("Th 5d Jh 7s Qh Kh Ah"); // royal flush

        assertHandRank(hand1, HandRank.STRAIGHT_FLUSH, "As 2s 3s 4s 5s");
        assertHandRank(hand2, HandRank.STRAIGHT_FLUSH, "5d 6d 7d 8d 9d");
        assertHandRank(hand3, HandRank.STRAIGHT_FLUSH, "9c Tc Jc Qc Kc");
        assertHandRank(hand4, HandRank.STRAIGHT_FLUSH, "Th Jh Qh Kh Ah");

        assertTrue(hand4.getRating() > hand3.getRating() && hand3.getRating() > hand2.getRating() &&
                hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testFourOfAKindRating() {
        final Hand hand1 = evalHand("2d 3h 9d 2h Jc 2c 2s");
        final Hand hand2 = evalHand("Kd 3h 9d Kh Kc Qc Ks");
        final Hand hand3 = evalHand("As 3h 9d Ah Jc Ac Ad");

        assertHandRank(hand1, HandRank.FOUR_OF_A_KIND, "2d 2h 2c 2s");
        assertHandRank(hand2, HandRank.FOUR_OF_A_KIND, "Kd Kh Kc Ks");
        assertHandRank(hand3, HandRank.FOUR_OF_A_KIND, "As Ah Ac Ad");

        assertTrue(hand3.getRating() > hand2.getRating() && hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testFullHouseRating() {
        final Hand hand1 = evalHand("Jc Ac Jd Ks 2c 2h 2s");
        final Hand hand2 = evalHand("2h 2d 2c 7s Ks Ah Ac");
        final Hand hand3 = evalHand("Kd Kc Qc 3s Qh 3h Qs");
        final Hand hand4 = evalHand("Jd Js Kc 2s Jc Kd Ks");
        final Hand hand5 = evalHand("2h 2d 2c 7s As Ah Ac");
        final Hand hand6 = evalHand("Kh Kd Kc 7s As Ah Ac");

        assertHandRank(hand1, HandRank.FULL_HOUSE, "2c 2h 2s Jc Jd");
        assertHandRank(hand2, HandRank.FULL_HOUSE, "Ah Ac 2h 2d 2c");
        assertHandRank(hand3, HandRank.FULL_HOUSE, "Qc Qh Qs Kd Kc");
        assertHandRank(hand4, HandRank.FULL_HOUSE, "Jd Js Kc Kd Ks");
        assertHandRank(hand5, HandRank.FULL_HOUSE, "As Ah Ac 2h 2d");
        assertHandRank(hand6, HandRank.FULL_HOUSE, "As Ah Ac Kh Kd");

        assertTrue(hand6.getRating() > hand5.getRating() && hand5.getRating() > hand4.getRating() &&
                hand4.getRating() > hand3.getRating() && hand3.getRating() > hand2.getRating() &&
                hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testFlushRating() {
        final Hand hand1 = evalHand("2h 3h 5h 6h 7c 8h 9s");
        final Hand hand2 = evalHand("2h 4h 5h 6h 7c 8h 9s");
        final Hand hand3 = evalHand("4h 5h 6h 8h 9s Th Jc");
        final Hand hand4 = evalHand("4d Qs Qc 6d 8d Td Qd");
        final Hand hand5 = evalHand("4s 6s 8s 9d Js Qs Kd");
        final Hand hand6 = evalHand("9c Ts Jc Qc Kc As Ac");

        assertHandRank(hand1, HandRank.FLUSH, "2h 3h 5h 6h 8h");
        assertHandRank(hand2, HandRank.FLUSH, "2h 4h 5h 6h 8h");
        assertHandRank(hand3, HandRank.FLUSH, "4h 5h 6h 8h Th");
        assertHandRank(hand4, HandRank.FLUSH, "4d 6d 8d Td Qd");
        assertHandRank(hand5, HandRank.FLUSH, "4s 6s 8s Js Qs");
        assertHandRank(hand6, HandRank.FLUSH, "9c Jc Qc Kc Ac");

        assertTrue(hand6.getRating() > hand5.getRating() && hand5.getRating() > hand4.getRating() &&
                hand4.getRating() > hand3.getRating() && hand3.getRating() > hand2.getRating() &&
                hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testStraightRating() {
        final Hand hand1 = evalHand("As Ac Ad 2s 3s 4h 5c");
        final Hand hand2 = evalHand("2c 2d 3c 3d 4h 5h 6h");
        final Hand hand3 = evalHand("9s Td Jd Qd Th Kc Kd");
        final Hand hand4 = evalHand("Tc Js Qd Kh 9s Jc Ah");

        assertHandRank(hand1, HandRank.STRAIGHT, "As 2s 3s 4h 5c");
        assertHandRank(hand2, HandRank.STRAIGHT, "2c 3c 4h 5h 6h");
        assertHandRank(hand3, HandRank.STRAIGHT, "9s Td Jd Qd Kc");
        assertHandRank(hand4, HandRank.STRAIGHT, "Tc Js Qd Kh Ah");

        assertTrue(hand4.getRating() > hand3.getRating() && hand3.getRating() > hand2.getRating() &&
                hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testThreeOfAKindRating() {
        final Hand hand1 = evalHand("2s 3c 2d 8d Tc 2c As");
        final Hand hand2 = evalHand("Qc Ad Qd Ks Qh Tc 2s");
        final Hand hand3 = evalHand("Kh Jd Kc Kd 5h 8s 9s");
        final Hand hand4 = evalHand("As Ad 2h Ah 3s 4d 9c");

        assertHandRank(hand1, HandRank.THREE_OF_A_KIND, "2s 2d 2c");
        assertHandRank(hand2, HandRank.THREE_OF_A_KIND, "Qc Qd Qh");
        assertHandRank(hand3, HandRank.THREE_OF_A_KIND, "Kh Kc Kd");
        assertHandRank(hand4, HandRank.THREE_OF_A_KIND, "As Ad Ah");

        assertTrue(hand4.getRating() > hand3.getRating() && hand3.getRating() > hand2.getRating() &&
                hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testTwoPairsRating() {
        final Hand hand1 = evalHand("Qd 3h 9d 5h Jc Qc 5c");
        final Hand hand2 = evalHand("Qd 3h 9d 6h Jc Qc 6c");
        final Hand hand3 = evalHand("As 3h 9d 5h Jc Ac 5c");

        assertHandRank(hand1, HandRank.TWO_PAIRS, "5h 5c Qd Qc");
        assertHandRank(hand2, HandRank.TWO_PAIRS, "6h 6c Qd Qc");
        assertHandRank(hand3, HandRank.TWO_PAIRS, "As Ac 5h 5c");

        assertTrue(hand3.getRating() > hand2.getRating() && hand2.getRating() > hand1.getRating());
    }

    @Test
    public void testHighCardRating() {
        final Hand hand1 = evalHand("3h 5d 6s 8d Tc Jc Kh Ah");
        final Hand hand2 = evalHand("2h Td 6s 8d 9c Jc Kh Ah");
        final Hand hand3 = evalHand("3h Ad 2s 7d 8c 9c Qh Kh");

        assertHandRank(hand1, HandRank.HIGH_CARD, "3h 5d");
        assertHandRank(hand2, HandRank.HIGH_CARD, "2h Td");
        assertHandRank(hand3, HandRank.HIGH_CARD, "3h Ad");

        assertTrue(hand3.getRating() > hand2.getRating() && hand2.getRating() > hand1.getRating());
    }
}
