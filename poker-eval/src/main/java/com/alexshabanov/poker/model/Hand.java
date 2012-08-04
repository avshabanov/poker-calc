package com.alexshabanov.poker.model;

import java.util.List;

/**
 * Represents player's hand.
 */
public interface Hand {
    List<Integer> getCardCodes();
    HandRank getRank();
    int getRating();
}
