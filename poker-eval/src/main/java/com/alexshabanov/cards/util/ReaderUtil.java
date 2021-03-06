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
import com.alexshabanov.cards.model.DefaultCard;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;

import java.util.ArrayList;
import java.util.List;

/**
 * Card reader utility (read card value by using the string representation provided).
 */
public final class ReaderUtil {
    private ReaderUtil() {}

    public static Rank rankFromLatin1Char(char ch) {
        for (final Rank rank : Rank.values()) {
            if (ch == PrintUtil.asChar(rank)) {
                return rank;
            }
        }

        throw new IllegalArgumentException("Unknown rank char: " + ch);
    }

    public static Suit suitFromLatin1Char(char ch) {
        for (final Suit suit : Suit.values()) {
            if (ch == PrintUtil.asLatin1Char(suit)) {
                return suit;
            }
        }

        throw new IllegalArgumentException("Unknown suit char: " + ch);
    }

    public static Card cardFromLatin1(String latin1CardCode) {
        if (latin1CardCode.length() != 2) {
            throw new IllegalArgumentException("Card code should contain two symbols");
        }

        return DefaultCard.valueOf(suitFromLatin1Char(
                latin1CardCode.charAt(1)),
                rankFromLatin1Char(latin1CardCode.charAt(0)));
    }

    public static List<Card> cardsFromLatin1(String cards) {
        final List<Card> result = new ArrayList<Card>();
        for (final String cardCode : cards.split("\\s")) {
            result.add(cardFromLatin1(cardCode));
        }
        return result;
    }
}
