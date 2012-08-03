package com.alexshabanov.poker.model;

import com.alexshabanov.poker.model.util.PrintUtil;

/**
 * Represents a single card.
 */
public final class Card {
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
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (suit != null ? suit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + PrintUtil.asChar(rank) + PrintUtil.asLatin1Char(suit) + "]";
    }
}
