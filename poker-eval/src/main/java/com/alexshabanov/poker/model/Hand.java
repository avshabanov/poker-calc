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

package com.alexshabanov.poker.model;

import com.alexshabanov.cards.model.Card;

import java.util.List;

/**
 * Represents player's hand that forms with the player's card and the river opening combination.
 */
public interface Hand {

    /**
     * @return Cards that forms the winning combination.
     */
    List<Card> getCards();

    /**
     * @return Combination rank, e.g. straight flush.
     */
    HandRank getRank();

    /**
     * Returns local in-hand rank rating (i.e. relative rating for the concrete combination).
     * E.g. for combination "2h 3h 4h 5h 6h" the rating will be lower than for "Th Jh Qh Kh Ah".
     * Each hand rating comparable only for the same hand ranks (e.g. equal hand rating for straight flush and
     * four of a kind does not mean the straight flush combination should be considered as the same as four of a kind).
     *
     * @return Non-negative number.
     */
    int getRating();
}
