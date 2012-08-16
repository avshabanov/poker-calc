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
