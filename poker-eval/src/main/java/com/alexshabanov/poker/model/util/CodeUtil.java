package com.alexshabanov.poker.model.util;

import com.alexshabanov.poker.model.Card;
import com.alexshabanov.poker.model.Rank;
import com.alexshabanov.poker.model.Suit;

/**
 * Represents card encoding utility.
 */
public final class CodeUtil {

    public static final int MAX_CARD_CODE = Suit.values().length * Rank.values().length;

    public static final int[] INITIAL_CARDS = new int[MAX_CARD_CODE];

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


}
