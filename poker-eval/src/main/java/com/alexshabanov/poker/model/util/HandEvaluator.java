package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;
import com.alexshabanov.poker.model.Hand;
import com.alexshabanov.poker.model.HandRank;

import javax.xml.ws.Holder;
import java.util.*;

/**
 * Hand ranking evaluation utility.
 * Rules are given here: http://www.pagat.com/poker/rules/ranking.html
 */
public final class HandEvaluator {
    private HandEvaluator() {}

    public static final int STRAIGHT_FLUSH_HAND_SIZE = 5;
    public static final int FOUR_OF_A_KIND_HAND_SIZE = 4;
    public static final int FULL_HOUSE_HAND_SIZE = 5;
    public static final int FLUSH_HAND_SIZE = 5;
    public static final int STRAIGHT_HAND_SIZE = 5;

    private static final Map<Rank, Integer> RATING_FLAG_MAP = new HashMap<Rank, Integer>();
    static {
        RATING_FLAG_MAP.put(Rank.TWO, 0x00000001);
        RATING_FLAG_MAP.put(Rank.THREE, 0x00000002);
        RATING_FLAG_MAP.put(Rank.FOUR, 0x00000004);
        RATING_FLAG_MAP.put(Rank.FIVE, 0x00000008);
        RATING_FLAG_MAP.put(Rank.SIX, 0x00000010);
        RATING_FLAG_MAP.put(Rank.SEVEN, 0x00000020);
        RATING_FLAG_MAP.put(Rank.EIGHT, 0x00000040);
        RATING_FLAG_MAP.put(Rank.NINE, 0x00000080);
        RATING_FLAG_MAP.put(Rank.TEN, 0x00000100);
        RATING_FLAG_MAP.put(Rank.JACK, 0x00000200);
        RATING_FLAG_MAP.put(Rank.QUEEN, 0x00000400);
        RATING_FLAG_MAP.put(Rank.KING, 0x00000800);
        RATING_FLAG_MAP.put(Rank.ACE, 0x00001000);
    }

    private static boolean provideBestHand(Holder<Hand> handHolder, HandCombinationSink handCombinationSink) {
        if (handHolder.value == null) {
            return false;
        }

        handCombinationSink.setBestHand(handHolder.value);
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
        final Holder<Hand> bestHandHolder = new Holder<Hand>();

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
                int rating = 0;
                for (final Card card : cards) {
                    rating |= RATING_FLAG_MAP.get(card.getRank());
                }

                if (royalFlush) {
                    // Whoa, lucky bastard! Royal flush is on the table! :)
                    // restructure the outcoming cards array so that the ACE will be the last one
                    final Card ace = cards.get(0);
                    cards.remove(0);
                    cards.add(ace);
                } else {
                    // wheel combination
                    // the ACE rating considered as lowest one (zero) for straight flush when ACE is a leading card
                    rating &= ~RATING_FLAG_MAP.get(Rank.ACE);
                }

                if (bestHandHolder.value == null || bestHandHolder.value.getRating() < rating) {
                    bestHandHolder.value = new HandImpl(rating, HandRank.STRAIGHT_FLUSH, cards);
                }

                return false; // search should continue as there might be multiple flushes per hand
            }
        }, STRAIGHT_FLUSH_HAND_SIZE);

        return provideBestHand(bestHandHolder, handCombinationSink);
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
        final Holder<Hand> bestHandHolder = new Holder<Hand>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                Collections.sort(cards, new RankComparator());

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
                    threeCardRankRating = RATING_FLAG_MAP.get(firstRank);
                    twoCardRankRating = RATING_FLAG_MAP.get(lastRank);
                } else if (midRank == lastRank) {
                    // tailing three
                    threeCardRankRating = RATING_FLAG_MAP.get(lastRank);
                    twoCardRankRating = RATING_FLAG_MAP.get(firstRank);
                } else {
                    return false;
                }

                final int rating = threeCardRankRating * FULL_HOUSE_THREE_CARD_WEIGHT + twoCardRankRating;
                if (bestHandHolder.value == null || bestHandHolder.value.getRating() < rating) {
                    bestHandHolder.value = new HandImpl(rating, HandRank.FULL_HOUSE, cards);
                }

                return false; // there might be multiple full houses
            }
        }, FULL_HOUSE_HAND_SIZE);

        return provideBestHand(bestHandHolder, handCombinationSink);
    }

    private static final int FULL_HOUSE_THREE_CARD_WEIGHT = 0x1000;

    /**
     * Card comparator, that takes into an account only the card ranks.
     */
    private static final class RankComparator implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            return lhs.getRank().ordinal() - rhs.getRank().ordinal();
        }
    }

    public static boolean maybeFlush(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        final Holder<Hand> bestHandHolder = new Holder<Hand>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                Suit suit = null;
                int rating = 0;
                for (final Card card : cards) {
                    if (suit == null) {
                        suit = card.getSuit();
                    } else if (card.getSuit() != suit) {
                        return false;
                    }

                    rating = rating | RATING_FLAG_MAP.get(card.getRank());
                }
                assert suit != null;

                if (bestHandHolder.value == null || bestHandHolder.value.getRating() < rating) {
                    bestHandHolder.value = new HandImpl(rating, HandRank.FLUSH, cards);
                }

                return false; // there might be multiple flushes
            }
        }, FLUSH_HAND_SIZE);

        return provideBestHand(bestHandHolder, handCombinationSink);
    }

    public static boolean maybeStraight(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        final Holder<Hand> bestHandHolder = new Holder<Hand>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                Collections.sort(cards, new RankComparator());

                int prevRankOrdinal = -1;
                boolean aceFollowsKing = false;
                for (final Card card : cards) {
                    final int newRankOrdinal = card.getRank().ordinal();

                    if (prevRankOrdinal >= 0 && (prevRankOrdinal + 1) != newRankOrdinal) {
                        // test for combination where ACE follows KING, if so -
                        // current card (which is second one) should be TEN, last card should be KING
                        if (prevRankOrdinal != Rank.ACE.ordinal() || card.getRank() != Rank.TEN) {
                            return false;
                        }

                        aceFollowsKing = true;
                    }

                    prevRankOrdinal = newRankOrdinal;
                }

                // ok, this is the straight
                int rating = 0;
                for (final Card card : cards) {
                    rating |= RATING_FLAG_MAP.get(card.getRank());
                }

                if (aceFollowsKing) {
                    final Card ace = cards.get(0);
                    cards.remove(0);
                    cards.add(ace);
                } else {
                    // wheel combination
                    // the ACE rating considered as lowest one (zero) for straight flush when ACE is a leading card
                    rating &= ~RATING_FLAG_MAP.get(Rank.ACE);
                }

                if (bestHandHolder.value == null || bestHandHolder.value.getRating() < rating) {
                    bestHandHolder.value = new HandImpl(rating, HandRank.STRAIGHT_FLUSH, cards);
                }

                return false;
            }
        }, STRAIGHT_HAND_SIZE);

        return provideBestHand(bestHandHolder, handCombinationSink);
    }

    public static boolean maybeThreeOfAKind(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        final Holder<Hand> bestHandHolder = new Holder<Hand>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                // TODO: implement
                return false;
            }
        }, -1);

        return provideBestHand(bestHandHolder, handCombinationSink);
    }

    public static boolean maybePair(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        final Holder<Hand> bestHandHolder = new Holder<Hand>();

        CardCombinator.iterate(sourceCards, new CardCombinationCallback() {
            @Override
            public boolean process(List<Card> cards) {
                // TODO: implement
                return false;
            }
        }, -1);

        return provideBestHand(bestHandHolder, handCombinationSink);
    }

    public static void highCard(List<Card> sourceCards, HandCombinationSink handCombinationSink) {
        // TODO: implement
    }

    public static Hand evaluate(List<Card> sourceCards) {
        final Holder<Hand> handHolder = new Holder<Hand>();
        final HandCombinationSink handCombinationSink = new HandCombinationSink() {
            @Override
            public void setBestHand(Hand hand) {
                assert handHolder.value == null;
                handHolder.value = hand;
            }
        };

        do {
            if (maybeStraightFlush(sourceCards, handCombinationSink)) {
                break;
            }

            if (maybeFourOfAKind(sourceCards, handCombinationSink)) {
                break;
            }

            if (maybeFullHouse(sourceCards, handCombinationSink)) {
                break;
            }

            if (maybeFlush(sourceCards, handCombinationSink)) {
                break;
            }

            if (maybeStraight(sourceCards, handCombinationSink)) {
                break;
            }

            if (maybeThreeOfAKind(sourceCards, handCombinationSink)) {
                break;
            }

            if (maybePair(sourceCards, handCombinationSink)) {
                break;
            }

            highCard(sourceCards, handCombinationSink);
        } while (false);

        assert handHolder.value != null;
        return handHolder.value;
    }


    private static final class HandImpl implements Hand {
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
    }
}
