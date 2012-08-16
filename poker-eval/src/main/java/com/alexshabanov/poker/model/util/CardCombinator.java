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

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the prospective clients with the iteration capabilities over the possible card combination space.
 */
public final class CardCombinator {

    private final int handCardsLength;
    private final List<Card> cards;
    private final CardCombinationCallback callback;
    private final int[] positions;

    private CardCombinator(CardCombinationCallback callback, List<Card> cards, int handCardsLength) {
        this.positions = new int[handCardsLength];
        this.callback = callback;
        this.cards = cards;
        this.handCardsLength = handCardsLength;
    }

    private boolean iterateInternal(int pos, int step) {
        if (step == this.handCardsLength) {
            final List<Card> result = new ArrayList<Card>(this.positions.length);
            for (final int position : this.positions) {
                result.add(this.cards.get(position));
            }

            return this.callback.process(result);
        }

        final int newStep = step + 1;
        for (int i = pos; i < this.cards.size() - this.handCardsLength + newStep; ++i) {
            this.positions[step] = i;
            if (iterateInternal(i + 1, newStep)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Iterates over the possible combinations of hand cards length
     * in the cards collection.
     *
     * @param cards             Cards collection (e.g. player's cards + river cards).
     * @param callback          Callback that accepts each combination {@link CardCombinationCallback}.
     * @param handCardsLength   Size of the checked combination.
     * @return True, if the iteration was interferred from the user's callback, true otherwise.
     */
    public static boolean iterate(List<Card> cards, CardCombinationCallback callback, int handCardsLength) {
        return new CardCombinator(callback, cards, handCardsLength).iterateInternal(0, 0);
    }
}
