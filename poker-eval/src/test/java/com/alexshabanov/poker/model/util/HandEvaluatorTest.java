package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.ReaderUtil;
import com.alexshabanov.poker.model.Hand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests hand util.
 */
public final class HandEvaluatorTest {

    @Test
    public void testPositiveStraightFlush() {
        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("8s 9h Qh Th Js Jh Kh");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraightFlush(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("9h Th Jh Qh Kh"), combinationCards);
        }

        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("2h 3c Ac Kd 2c 4c 5c");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraightFlush(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("Ac 2c 3c 4c 5c"), combinationCards);
        }

        // royal flush
        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("2s As Ks 7d Js Qs Ts");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraightFlush(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("Ts Js Qs Ks As"), combinationCards);
        }
    }

    @Test
    public void testMultipleStraightFlushes() {
        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("3d 4d 5d 6d 7d 8d 9d");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraightFlush(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("5d 6d 7d 8d 9d"), combinationCards);
        }

        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("3d 4d 5d 6d 7d 9d 8d"); // try w/transposed cards
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraightFlush(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("5d 6d 7d 8d 9d"), combinationCards);
        }
    }

    @Test
    public void testPositiveFourOfAKind() {
        final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("Qd 9h Qs Ac Qc Tc Qh");
        final List<Card> combinationCards = new ArrayList<Card>();
        assertTrue(HandEvaluator.maybeFourOfAKind(sourceCards, new SimpleHandCombinationSink() {
            @Override
            public void setBestHand(Hand hand) {
                assertEquals(0, combinationCards.size());
                combinationCards.addAll(hand.getCards());
            }
        }));

        assertEquals(ReaderUtil.cardsFromLatin1("Qd Qs Qc Qh"), combinationCards);
    }

    @Test
    public void testPositiveFullHouse() {
        final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("Qd 2h Qs 2c Qc Tc Th");
        final List<Card> combinationCards = new ArrayList<Card>();
        assertTrue(HandEvaluator.maybeFullHouse(sourceCards, new SimpleHandCombinationSink() {
            @Override
            public void setBestHand(Hand hand) {
                assertEquals(0, combinationCards.size());
                combinationCards.addAll(hand.getCards());
            }
        }));

        assertEquals(ReaderUtil.cardsFromLatin1("Tc Th Qd Qs Qc"), combinationCards);
    }

    @Test
    public void testPositiveFlush() {
        final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("Qd 3h 9h Kh Jh 8c 5h");
        final List<Card> combinationCards = new ArrayList<Card>();
        assertTrue(HandEvaluator.maybeFlush(sourceCards, new SimpleHandCombinationSink() {
            @Override
            public void setBestHand(Hand hand) {
                assertEquals(0, combinationCards.size());
                combinationCards.addAll(hand.getCards());
            }
        }));

        assertEquals(ReaderUtil.cardsFromLatin1("3h 9h Kh Jh 5h"), combinationCards);
    }

    @Test
    public void testMultipleFlushes() {
        final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("2c 5c 7c Tc Jc Kc Ac");
        final List<Card> combinationCards = new ArrayList<Card>();
        assertTrue(HandEvaluator.maybeFlush(sourceCards, new SimpleHandCombinationSink() {
            @Override
            public void setBestHand(Hand hand) {
                assertEquals(0, combinationCards.size());
                combinationCards.addAll(hand.getCards());
            }
        }));

        assertEquals(ReaderUtil.cardsFromLatin1("7c Tc Jc Kc Ac"), combinationCards);
    }

    @Test
    public void testPositiveStraight() {
        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("8s 9h Qc Th 2s Jh Kh");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraight(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("9h Th Jh Qc Kh"), combinationCards);
        }

        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("2h 3c Ac Kd Qc 4d 5s");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraight(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("Ac 2h 3c 4d 5s"), combinationCards);
        }

        // royal flush
        {
            final List<Card> sourceCards = ReaderUtil.cardsFromLatin1("2s Ah Ks 7d Js Qs Ts");
            final List<Card> combinationCards = new ArrayList<Card>();
            assertTrue(HandEvaluator.maybeStraight(sourceCards, new SimpleHandCombinationSink() {
                @Override
                public void setBestHand(Hand hand) {
                    assertEquals(0, combinationCards.size());
                    combinationCards.addAll(hand.getCards());
                }
            }));

            assertEquals(ReaderUtil.cardsFromLatin1("Ts Js Qs Ks Ah"), combinationCards);
        }
    }
}
