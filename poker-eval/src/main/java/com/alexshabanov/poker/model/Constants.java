package com.alexshabanov.poker.model;

/**
 * Constants for the "Texas hold'em".
 * Each constant complies to the poker rules and hence can not be changed to not to break game.
 *
 * Note, that max number of players is 23 for 52-cards deck (23 * 2 + 5 = 51).
 */
public final class Constants {
    private Constants() {}

    /**
     * Count of cards, given to each player.
     */
    public static final int CARDS_PER_PLAYER = 2;

    /**
     * Count of cards opened in flop, should be exactly three.
     */
    public static final int FLOP_CARDS = 3;

    /**
     * Count of cards opened on each turn after the flop, should be exactly two.
     */
    public static final int AFTERFLOP_CARDS = 2;
}
