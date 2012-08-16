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

package com.alexshabanov.poker.service.support;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.DefaultCard;
import com.alexshabanov.poker.service.DealerService;

import java.security.SecureRandom;
import java.util.*;

/**
 * Default implementation of {@link DealerService}.
 */
public final class DefaultDealerService implements DealerService {

    public static final int DEFAULT_MAX_SHUFFLES = 5;

    private final int maxShuffles;
    private final Random random;


    public DefaultDealerService(int maxShuffles, Random random) {
        this.maxShuffles = maxShuffles;
        this.random = random;
    }

    public DefaultDealerService() {
        this(DEFAULT_MAX_SHUFFLES, new SecureRandom());
    }

    @Override
    public List<Card> deal() {
        // create new copy of unsorted deck
        final List<Card> deck = new ArrayList<Card>(DefaultCard.deck());

        // shuffle
        for (int shuffle = 0; shuffle < maxShuffles; ++shuffle) {
            for (int i = 0; i < deck.size(); ++i) {
                final int newPos = random.nextInt(deck.size());

                Collections.swap(deck, i, newPos);
            }
        }

        return Collections.unmodifiableList(deck);
    }
}
