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
}
