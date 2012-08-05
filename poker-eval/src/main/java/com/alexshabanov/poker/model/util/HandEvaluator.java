package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;
import com.alexshabanov.poker.model.Hand;
import com.alexshabanov.poker.model.HandRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hand ranking evaluation utility.
 * Rules are given here: http://www.pagat.com/poker/rules/ranking.html
 */
public final class HandEvaluator {
    private HandEvaluator() {}

    public static final int STRAIGHT_FLUSH_HAND_SIZE = 5;
    public static final int FOUR_OF_A_KIND_HAND_SIZE = 4;

    /**
     * Checks whether the given combination is the straight flush.
     *
     * @param sourceCards       Source cards to be checked, 7 cards in Texas Hold'em.
     *                          First two cards should be the player's cards, next five - cards on the table.
     * @param handCombinationSink   User-defined callback that is invoked when the searched combination is found
     *                          If combination sink is invoked, the method unconditionally returns true.
     * @return True, if the straight flush combination has been found in the given cards.
     */
    public static boolean maybeStraightFlush(List<Card> sourceCards, final HandCombinationSink handCombinationSink) {
        final List<Hand> hands = new ArrayList<Hand>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                Collections.sort(cards);

                Suit currentSuit = null;
                int prevRankOrdinal = -1;
                boolean royalFlush = false; // Identifies whether ACE follows KING or Royal Flush flag
                for (final Card card : cards) {
                    final int newRankOrdinal = card.getRank().ordinal();

                    if (currentSuit == null) {
                        currentSuit = card.getSuit();
                    } else {
                        if (card.getSuit() != currentSuit) {
                            // suit mismatch
                            return false;
                        }

                        if ((prevRankOrdinal + 1) != newRankOrdinal) {
                            // test for combination where ACE follows KING, if so -
                            // current card (which is second one) should be TEN, last card should be KING
                            if (prevRankOrdinal != Rank.ACE.ordinal() || card.getRank() != Rank.TEN) {
                                return false;
                            }

                            royalFlush = true;
                        }
                    }

                    prevRankOrdinal = newRankOrdinal;
                }

                // ok, this is the straight flush
                final int rating;
                if (royalFlush) {
                    // Whoa, lucky bastard! Royal flush is on the table! :)
                    // restructure the outcoming cards array so that the ACE will be the last one
                    final Card ace = cards.get(0);
                    cards.remove(0);
                    cards.add(ace);
                    rating = Rank.KING.ordinal() + 1;
                } else {
                    rating = cards.get(cards.size() - 1).getRank().ordinal();
                }
                hands.add(new HandImpl(rating, HandRank.STRAIGHT_FLUSH, cards));
                return false; // search should continue as there might be multiple flushes per hand
            }
        }, STRAIGHT_FLUSH_HAND_SIZE);

        if (hands.isEmpty()) {
            return false;
        }

        // best hand is the last one
        handCombinationSink.setBestHand(hands.get(hands.size() - 1));
        return true;
    }

    public static boolean maybeFourOfAKind(List<Card> sourceCards, final HandCombinationSink handCombinationSink) {
        return CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                Rank rank = null;
                for (final Card card : cards) {
                    if (rank == null) {
                        rank = card.getRank();
                    } else if (rank != card.getRank()) {
                        return false;
                    }
                }
                assert rank != null;

                // of, this is the four of a kind
                handCombinationSink.setBestHand(new HandImpl(rank.ordinal(), HandRank.FOUR_OF_A_KIND, cards));

                return true; // the followup search does not make any sense, since only one four-of-a-kind may occur.
            }
        }, FOUR_OF_A_KIND_HAND_SIZE);
    }



    private static final class HandImpl implements Hand, Comparable<Hand> {
        private final List<Card> cards;
        private final HandRank rank;
        private final int rating;

        public HandImpl(int rating, HandRank rank, List<Card> cards) {
            this.rating = rating;
            this.rank = rank;
            this.cards = Collections.unmodifiableList(new ArrayList<Card>(cards));
        }

        @Override
        public List<Card> getCards() {
            return cards;
        }

        @Override
        public HandRank getRank() {
            return rank;
        }

        @Override
        public int getRating() {
            return rating;
        }

        @Override
        public int compareTo(Hand o) {
            return this.rating - o.getRating();
        }
    }
}
