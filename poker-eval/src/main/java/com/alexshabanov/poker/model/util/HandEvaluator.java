package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;
import com.alexshabanov.poker.model.Hand;
import com.alexshabanov.poker.model.HandRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Hand ranking evaluation utility.
 * Rules are given here: http://www.pagat.com/poker/rules/ranking.html
 */
public final class HandEvaluator {
    private HandEvaluator() {}

    public static final int STRAIGHT_FLUSH_HAND_SIZE = 5;
    public static final int FOUR_OF_A_KIND_HAND_SIZE = 4;
    public static final int FULL_HOUSE_HAND_SIZE = 5;

    private static boolean provideBestHand(List<HandImpl> hands, HandCombinationSink handCombinationSink) {
        if (hands.isEmpty()) {
            return false;
        }

        // best hand is the last one
        Collections.sort(hands);
        handCombinationSink.setBestHand(hands.get(hands.size() - 1));
        return true;
    }

    /**
     * Checks whether the given combination is the straight flush.
     *
     * @param sourceCards       Source cards to be checked, 7 cards in Texas Hold'em.
     *                          First two cards should be the player's cards, next five - cards on the table.
     * @param handCombinationSink   User-defined callback that is invoked when the searched combination is found
     *                          If combination sink is invoked, the method unconditionally returns true.
     * @return True, if the straight flush combination has been found in the given cards.
     */
    public static boolean maybeStraightFlush(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        final List<HandImpl> hands = new ArrayList<HandImpl>();

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

        return provideBestHand(hands, handCombinationSink);
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

    public static boolean maybeFullHouse(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        final List<HandImpl> hands = new ArrayList<HandImpl>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                Collections.sort(cards, new RankComparator());

                int maxRank = -1;
                // first two and last two cards must be identical
                Rank firstRank = cards.get(0).getRank();
                if (firstRank != cards.get(1).getRank()) {
                    return false;
                }

                Rank lastRank = cards.get(4).getRank();
                if (lastRank != cards.get(3).getRank()) {
                    return false;
                }

                Rank midRank = cards.get(2).getRank();
                final int threeCardRankRating;
                final int twoCardRankRating;
                if (midRank == firstRank) {
                    // leading three
                    threeCardRankRating = getStraightRankRating(firstRank);
                    twoCardRankRating = getStraightRankRating(lastRank);
                } else if (midRank == lastRank) {
                    // tailing three
                    threeCardRankRating = getStraightRankRating(lastRank);
                    twoCardRankRating = getStraightRankRating(firstRank);
                } else {
                    return false;
                }


                hands.add(new HandImpl(threeCardRankRating * FULL_HOUSE_THREE_CARD_WEIGHT + twoCardRankRating,
                        HandRank.FULL_HOUSE,
                        cards));
                return false; // there might be multiple full houses
            }
        }, FULL_HOUSE_HAND_SIZE);

        return provideBestHand(hands, handCombinationSink);
    }

    private static final int FULL_HOUSE_THREE_CARD_WEIGHT = 100;

    private static int getStraightRankRating(Rank rank) {
        if (rank == Rank.ACE) {
            return Rank.KING.ordinal() + 1;
        } else {
            return rank.ordinal();
        }
    }


    /**
     * Card comparator, that takes into an account only the card ranks.
     */
    private static final class RankComparator implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            return lhs.getRank().ordinal() - rhs.getRank().ordinal();
        }
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
