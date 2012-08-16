package com.alexshabanov.cards.model;

/**
 * Represents a single card.
 */
public interface Card extends Comparable<Card> {

    /**
     * @return Card's rank, not null.
     */
    Rank getRank();

    /**
     * @return Card's suit, not null.
     */
    Suit getSuit();

    /**
     * @return Unique integer value, that represents rank and suit fold in the code value.
     */
    int getCode();
}
