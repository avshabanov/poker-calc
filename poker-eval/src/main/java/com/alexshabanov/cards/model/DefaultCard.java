/*
 * Copyright 2012 Alexander Shabanov - http://alexshabanov.com.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexshabanov.cards.model;

import com.alexshabanov.cards.util.PrintUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the {@link Card} interface.
 */
public final class DefaultCard implements Serializable, Card {
    private static final long serialVersionUID = 1L;

    private final Rank rank;
    private final Suit suit;

    private DefaultCard(Suit suit, Rank rank) {
        if (suit == null) {
            throw new NullPointerException("suit");
        }

        if (rank == null) {
            throw new NullPointerException("rank");
        }
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Max index of a card. Equals to a size of a deck (52 for poker).
     */
    public static final int MAX_CARD_CODE = Suit.values().length * Rank.values().length;

    private static final DefaultCard[] DEFAULT_CARDS;

    static {
        DEFAULT_CARDS = new DefaultCard[MAX_CARD_CODE];
        final Rank[] ranks = Rank.values();
        final Suit[] suits = Suit.values();

        for (int cardCode = 0; cardCode < MAX_CARD_CODE; ++cardCode) {

            final int suitId = cardCode / ranks.length;
            final int rankId = cardCode - suitId * ranks.length;

            DEFAULT_CARDS[cardCode] = new DefaultCard(suits[suitId], ranks[rankId]);
        }
    }

    /**
     * @return The unsorted deck contents where cards sorted by ranks and grouped by suits.
     */
    public static List<? extends Card> deck() {
        return Collections.unmodifiableList(Arrays.asList(DEFAULT_CARDS));
    }

    /**
     * Returns card that corresponds to the given card code.
     * <p>
     * Each card may be represented as an integer number from 0 to 51.
     * If card code is X, then card rank is [X/Rmax], where Rmax is count of card ranks,
     * card suit is X-[X/Rmax], where
     * [Y] - is taking an integer part from the real number Y by throwing away it's fraction part.
     * </p>
     *
     * @param cardCode Card code, integer between zero inclusive and {@link #MAX_CARD_CODE} exclusive.
     * @return Non-null card instance.
     */
    public static Card valueOf(int cardCode) {
        if (cardCode < 0 || cardCode >= MAX_CARD_CODE) {
            throw new IllegalArgumentException("Illegal card code: " + cardCode + ", expected to be between 0 " +
                    "inclusive and " + MAX_CARD_CODE + " exclusive");
        }

        return DEFAULT_CARDS[cardCode];
    }

    public static Rank rankFromCode(int code) {
        final Rank[] ranks = Rank.values();
        final int suitId = code / ranks.length;
        final int rankId = code - suitId * ranks.length;
        return ranks[rankId];
    }

    public static Suit suitFromCode(int code) {
        final Rank[] ranks = Rank.values();
        final int suitId = code / ranks.length;
        return Suit.values()[suitId];
    }

    /**
     * Returns card that corresponds to the given suit and rank.
     *
     * @param suit Non-null suit.
     * @param rank Non-null rank.
     * @return Non-null card instance.
     * @see #valueOf(int)
     */
    public static Card valueOf(Suit suit, Rank rank) {
        if (suit == null) {
            throw new NullPointerException("suit");
        }

        if (rank == null) {
            throw new NullPointerException("rank");
        }

        return valueOf(suit.ordinal() * Rank.values().length + rank.ordinal());
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public int getCode() {
        return suit.ordinal() * Rank.values().length + rank.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Card card = (Card) o;

        return rank == card.getRank() && suit == card.getSuit();

    }

    @Override
    public int hashCode() {
        return rank.hashCode() * 31 + suit.hashCode();
    }

    @Override
    public String toString() {
        return "[" + PrintUtil.asChar(rank) + PrintUtil.asLatin1Char(suit) + "]";
    }

    /**
     * Standard straight comparator: first compares suits, then compares ranks.
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Card other) {
        final int suitsCmpRet = this.getSuit().ordinal() - other.getSuit().ordinal();
        if (suitsCmpRet != 0) {
            return suitsCmpRet;
        }

        return this.getRank().ordinal() - other.getRank().ordinal();
    }
}
