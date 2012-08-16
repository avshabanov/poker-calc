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

package com.alexshabanov.cards.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;

/**
 * Represents printing utility.
 */
public final class PrintUtil {
    private PrintUtil() {}

    public static char asUtfChar(Suit suit) {
        switch (suit) {
            case SPADES: return '\u2660';
            case HEARTS: return '\u2661';
            case DIAMONDS: return '\u2662';
            case CLUBS: return '\u2663';
            default:
                throw new IllegalArgumentException("Unknown or null suit " + suit);
        }
    }

    public static char asChar(Rank rank) {
        switch (rank) {
            case ACE: return 'A';
            case TWO: return '2';
            case THREE: return '3';
            case FOUR: return '4';
            case FIVE: return '5';
            case SIX: return '6';
            case SEVEN: return '7';
            case EIGHT: return '8';
            case NINE: return '9';
            case TEN: return 'T';
            case JACK: return 'J';
            case QUEEN: return 'Q';
            case KING: return 'K';
            default:
                throw new IllegalArgumentException("Illegal rank: " + rank);
        }
    }

    public static char asLatin1Char(Suit suit) {
        switch (suit) {
            case SPADES: return 's';
            case HEARTS: return 'h';
            case DIAMONDS: return 'd';
            case CLUBS: return 'c';
            default:
                throw new IllegalArgumentException("Unknown or null suit " + suit);
        }
    }

    public static String asUtfString(Card card) {
        return "" + asChar(card.getRank()) + asUtfChar(card.getSuit());
    }

//    public static Suit suitFromLatin1(char suitLatin1Char) {
//        for (final Suit suit : Suit.values()) {
//            if (asLatin1Char(suit) == suitLatin1Char) {
//                return suit;
//            }
//        }
//        throw new IllegalArgumentException("Unknown suit latin-1 char " + suitLatin1Char);
//    }
}
