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
 * Represents card encoding utility.
 */
public final class EncodeUtil {

    private EncodeUtil() {}

    public static List<Card> fromCodes(List<Integer> codes) {
        final List<Card> result = new ArrayList<Card>(codes.size());
        for (final int code : codes) {
            result.add(DefaultCard.valueOf(code));
        }

        return result;
    }

    public static List<Integer> toCodes(List<Card> cards) {
        final List<Integer> result = new ArrayList<Integer>(cards.size());
        for (final Card card : cards) {
            result.add(card.getCode());
        }
        return result;
    }
}
