package com.alexshabanov.cards.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents card encoding utility.
 * Each card may be represented as an integer number from 0 to 51.
 * If card code is X, then card rank is [X/Rmax], where Rmax is count of card ranks,
 * card suit is X-[X/Rmax], where
 * [Y] - is taking an integer part from the real number Y by throwing away it's fraction part.
 */
public final class EncodeUtil {

    public static final int MAX_CARD_CODE = Suit.values().length * Rank.values().length;

    public static Card fromCode(int code) {
        if (code < 0 || code >= MAX_CARD_CODE) {
            throw new IllegalArgumentException("Illegal card code: " + code + ", expected to be between 0 " +
                    "inclusive and " + MAX_CARD_CODE + " exclusive");
        }
        final Rank[] ranks = Rank.values();
        final Suit[] suits = Suit.values();

        final int suitId = code / ranks.length;
        final int rankId = code - suitId * ranks.length;

        return new Card(suits[suitId], ranks[rankId]);
    }

    public static List<Card> fromCodes(List<Integer> codes) {
        final List<Card> result = new ArrayList<Card>(codes.size());
        for (final int code : codes) {
            result.add(fromCode(code));
        }

        return result;
    }

    public static int toCode(Card card) {
        return card.getSuit().ordinal() * Rank.values().length + card.getRank().ordinal();
    }

    public static List<Integer> toCodes(List<Card> cards) {
        final List<Integer> result = new ArrayList<Integer>(cards.size());
        for (final Card card : cards) {
            result.add(toCode(card));
        }
        return result;
    }
}
