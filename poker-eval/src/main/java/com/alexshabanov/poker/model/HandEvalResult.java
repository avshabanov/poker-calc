package com.alexshabanov.poker.model;

import java.util.List;

/**
 * Combination of card that forms a combination.
 */
public final class HandEvalResult {
    private final HandRank handRank;
    private final List<Integer> cards;
    private final int ranking;

    public HandEvalResult(int ranking, List<Integer> cards, HandRank handRank) {
        this.ranking = ranking;
        this.cards = cards;
        this.handRank = handRank;
    }

    public HandRank getHandRank() {
        return handRank;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public int getRanking() {
        return ranking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HandEvalResult that = (HandEvalResult) o;

        return ranking == that.ranking && !(cards != null ? !cards.equals(that.cards) : that.cards != null) &&
                handRank == that.handRank;
    }

    @Override
    public int hashCode() {
        int result = handRank != null ? handRank.hashCode() : 0;
        result = 31 * result + (cards != null ? cards.hashCode() : 0);
        result = 31 * result + ranking;
        return result;
    }

    @Override
    public String toString() {
        return "HandEvalResult{" +
                "handRank=" + handRank +
                ", cards=" + cards +
                ", ranking=" + ranking +
                '}';
    }
}
