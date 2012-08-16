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

package com.alexshabanov.sampleapp;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.EncodeUtil;
import com.alexshabanov.cards.util.PrintUtil;
import com.alexshabanov.poker.model.Constants;
import com.alexshabanov.poker.model.Hand;
import com.alexshabanov.poker.model.util.HandEvaluator;
import com.alexshabanov.poker.service.DealerService;
import com.alexshabanov.poker.service.support.DefaultDealerService;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrates how hands might be evaluated.
 */
@Ignore
public final class EvalHandSampleApp {

    /**
     * Count of game rounds.
     */
    private static final int NUM_GAMES = 3;

    /**
     * Count of participated players.
     */
    private static final int NUM_PLAYERS = 5;

    private static class Player {
        final Hand bestHand;
        final List<Card> cards;

        private Player(List<Card> cards, Hand bestHand) {
            this.cards = cards;
            this.bestHand = bestHand;
        }
    }

    private static String asString(List<Card> cards) {
        final StringBuilder builder = new StringBuilder();
        for (final Card card : cards) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(PrintUtil.asUtfString(card));
        }

        return builder.toString();
    }

    private static void gameRound(DealerService dealerService) {
        // consider cards disposition as follows:
        // first cards [0..NUM_PLAYERS * 2 - 1] - player cards (two for each players)
        // [NUM_PLAYERS * 2..NUM_PLAYERS * 2 + 5] - river cards
        final List<Card> cards = dealerService.deal();
        final int riverIndex = NUM_PLAYERS * Constants.CARDS_PER_PLAYER;
        final int riverCount = Constants.FLOP_CARDS + Constants.AFTERFLOP_CARDS;
        final List<Card> riverCards = Collections.unmodifiableList(cards.subList(riverIndex, riverIndex + riverCount));

        System.out.println("River cards are: " + asString(riverCards));

        final List<Player> players = new ArrayList<Player>(NUM_PLAYERS);
        for (int i = 0; i < NUM_PLAYERS; ++i) {
            final List<Card> playerCards = new ArrayList<Card>(Constants.CARDS_PER_PLAYER + riverCount);
            playerCards.add(cards.get(i * 2));
            playerCards.add(cards.get(i * 2 + 1));
            playerCards.addAll(riverCards);

            final Player player = new Player(playerCards, HandEvaluator.evaluate(playerCards));
            players.add(player);

            System.out.println("Player #" + i + " cards are: " + asString(playerCards.subList(0, Constants.CARDS_PER_PLAYER)));
            System.out.println("\tHand rank: " + player.bestHand.getRank() + ", " +
                    "cards: " + player.bestHand.getCards() + ", " +
                    "rating: " + player.bestHand.getRating());
        }

        System.out.println("Analyzed player(s): " + players.size());
    }

    public static void main(String[] args) {
        final DealerService dealerService = new DefaultDealerService();

        for (int round = 0; round < NUM_GAMES; ++round) {
            System.out.println("Game #" + round);
            gameRound(dealerService);
            System.out.println("===");
        }
    }
}
