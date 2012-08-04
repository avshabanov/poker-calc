package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.EncodeUtil;
import com.alexshabanov.poker.model.HandEvalResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Hand ranking evaluation utility.
 * Rules are given here: http://www.pagat.com/poker/rules/ranking.html
 */
public final class HandUtil {
    private HandUtil() {}

    public static final int MIN_HAND_CARDS_LENGTH = 5;

    public static HandEvalResult evaluate(List<Integer> sourceCardCodes) {
        if (sourceCardCodes.size() < MIN_HAND_CARDS_LENGTH) {
            throw new IllegalArgumentException("Too few hand cards in " + sourceCardCodes);
        }

        final List<Card> sourceCards = EncodeUtil.fromCodes(sourceCardCodes);

        return null;
    }

    public static interface CardCallback {
        boolean process(List<Integer> cards);
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
            if (!iterateInternal(context, i + 1, newStep)) {
                return false;
            }
        }

        return true;
    }


    public static boolean iterate(List<Integer> cardIndexes, CardCallback callback, int handCardsLength) {
        return iterateInternal(new IterationContext(callback, cardIndexes, handCardsLength), 0, 0);
    }

    public static List<Card> maybeStraightFlush(List<Card> sourceCards) {

        return null;
    }
}
