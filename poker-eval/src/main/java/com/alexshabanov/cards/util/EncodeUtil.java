package com.alexshabanov.cards.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.DefaultCard;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents card encoding utility.
 */
public final class EncodeUtil {

    private EncodeUtil() {}

    public static List<Card> fromCodes(List<Integer> codes) {
        final List<Card> result = new ArrayList<Card>(codes.size());
        for (final int code : codes) {
            result.add(DefaultCard.valueOf(code));
        }

        return result;
    }

    public static List<Integer> toCodes(List<Card> cards) {
        final List<Integer> result = new ArrayList<Integer>(cards.size());
        for (final Card card : cards) {
            result.add(card.getCode());
        }
        return result;
    }
}
