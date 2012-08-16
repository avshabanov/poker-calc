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

package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.poker.model.Hand;
import com.alexshabanov.poker.model.HandRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the {@link Hand} interface.
 */
public final class DefaultHand implements Hand {
    private final List<Card> cards;
    private final HandRank rank;
    private final int rating;

    public DefaultHand(int rating, HandRank rank, List<Card> cards) {
        this.rating = rating;
        this.rank = rank;
        this.cards = Collections.unmodifiableList(new ArrayList<Card>(cards));
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public HandRank getRank() {
        return rank;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultHand hand = (DefaultHand) o;

        return rating == hand.rating && !(cards != null ? !cards.equals(hand.cards) : hand.cards != null) && rank == hand.rank;

    }

    @Override
    public int hashCode() {
        int result = cards != null ? cards.hashCode() : 0;
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        result = 31 * result + rating;
        return result;
    }

    @Override
    public String toString() {
        return "{cards=" + cards +
                ", rank=" + rank +
                ", rating=" + rating +
                '}';
    }
}
