package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;
import com.alexshabanov.cards.util.EncodeUtil;
import com.alexshabanov.poker.model.HandRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Hand ranking evaluation utility.
 * Rules are given here: http://www.pagat.com/poker/rules/ranking.html
 */
public final class HandUtil {
    private HandUtil() {}

    public static int STRAIGHT_FLUSH_HAND_SIZE = 5;

    /**
     * User defined callback used in {@see #iterate} method.
     */
    public interface CardCallback {

        /**
         * Processes combination of cards.
         *
         * @param cardCodes Combination of cards.
         * @return True, if the execution shall stop, false otherwise.
         */
        boolean process(List<Integer> cardCodes);
    }

    /**
     * Iterates over the possible combinations of {@see #handCardsLength} cards
     * in the cards collection {@see #cardIndexes}.
     *
     * @param cardIndexes       Cards collection (e.g. player's cards + river cards).
     * @param callback          Callback that accepts each combination {@link CardCallback}.
     * @param handCardsLength   Size of the checked combination.
     * @return True, if the iteration was interferred from the user's callback, true otherwise.
     */
    public static boolean iterate(List<Integer> cardIndexes, CardCallback callback, int handCardsLength) {
        return iterateInternal(new IterationContext(callback, cardIndexes, handCardsLength), 0, 0);
    }


    private static final class IterationContext {
        final int handCardsLength;
        final List<Integer> cardIndexes;
        final CardCallback callback;
        final int[] positions;

        private IterationContext(CardCallback callback, List<Integer> cardIndexes, int handCardsLength) {
            this.positions = new int[handCardsLength];
            this.callback = callback;
            this.cardIndexes = cardIndexes;
            this.handCardsLength = handCardsLength;
        }
    }

    private static boolean iterateInternal(IterationContext context, int pos, int step) {
        if (step == context.handCardsLength) {
            final List<Integer> result = new ArrayList<Integer>(context.positions.length);
            for (final int position : context.positions) {
                result.add(context.cardIndexes.get(position));
            }

            return context.callback.process(result);
        }

        final int newStep = step + 1;
        for (int i = pos; i < context.cardIndexes.size() - context.handCardsLength + newStep; ++i) {
            context.positions[step] = i;
            if (iterateInternal(context, i + 1, newStep)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Callback, that is invoked when the search method finds the appropriate combination.
     * {@see #maybeStraightFlush}
     */
    public interface CombinationSink {
        void setHandCode(List<Integer> combinationCardCodes);
        void setHandRank(HandRank handRank);
        void setHandValue(int value);
    }

    public static abstract class SimpleCombinationSink implements CombinationSink {

        @Override
        public void setHandCode(List<Integer> combinationCardCodes) {
            // do nothing
        }

        @Override
        public void setHandRank(HandRank handRank) {
            // do nothing
        }

        @Override
        public void setHandValue(int value) {
            // do nothing
        }
    }

    public static final class StraightCardsComparator implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            final int suitsCmpRet = lhs.getSuit().ordinal() - rhs.getSuit().ordinal();
            if (suitsCmpRet != 0) {
                return suitsCmpRet;
            }

            return lhs.getRank().ordinal() - rhs.getRank().ordinal();
        }
    }

    /**
     * Checks whether the given combination is the straight flush.
     *
     * @param sourceCardCodes   Source cards to be checked, 7 cards in Texas Hold'em.
     *                          First two cards are the player's cards, next five - cards on the table.
     * @param combinationSink   User-defined callback that is invoked when the searched combination is found
     *                          If combination sink is invoked, the method unconditionally returns true.
     * @return True, if the straight flush combination has been found in the given cards.
     */
    public static boolean maybeStraightFlush(List<Integer> sourceCardCodes, final CombinationSink combinationSink) {
        return iterate(sourceCardCodes, new CardCallback() {
            @Override
            public boolean process(List<Integer> cardCodes) {
                final List<Card> cards = EncodeUtil.fromCodes(cardCodes);
                Collections.sort(cards, new StraightCardsComparator());

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
                if (royalFlush) {
                    // Whoa, lucky bastard! Royal flush is on the table! :)
                    // restructure the outcoming cards array so that the ACE will be the last one
                    final Card ace = cards.get(0);
                    cards.remove(0);
                    cards.add(ace);
                }
                combinationSink.setHandCode(EncodeUtil.toCodes(cards));

                combinationSink.setHandRank(HandRank.STRAIGHT_FLUSH);
                combinationSink.setHandValue(-1);

                return true;
            }
        }, STRAIGHT_FLUSH_HAND_SIZE);

    }
}
