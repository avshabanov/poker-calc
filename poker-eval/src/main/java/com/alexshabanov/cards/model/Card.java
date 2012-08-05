package com.alexshabanov.cards.model;

import com.alexshabanov.cards.util.PrintUtil;

import java.io.Serializable;

/**
 * Represents a single card.
 */
public final class Card implements Serializable, Comparable<Card> {
    private static final long serialVersionUID = 1L;

    private final Rank rank;
    private final Suit suit;

    public Card(Suit suit, Rank rank) {
        if (suit == null) {
            throw new NullPointerException("suit");
        }

        if (rank == null) {
            throw new NullPointerException("rank");
        }
        this.suit = suit;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
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

        return rank == card.rank && suit == card.suit;

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
