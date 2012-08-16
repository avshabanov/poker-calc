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

package com.alexshabanov.cards.model;

import com.alexshabanov.cards.util.ReaderUtil;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DefaultCardTest {

    @Test
    public void testUniquenessTraitsOfValueOf() {
        final Set<Card> cardSet = new HashSet<Card>();
        for (int i = 0; i < DefaultCard.MAX_CARD_CODE; ++i) {
            assertTrue(cardSet.add(DefaultCard.valueOf(i)));
        }

        assertEquals(cardSet, new HashSet<Card>(DefaultCard.deck()));
    }

    @Test
    public void testValueOf() {
        for (int i = 0; i < DefaultCard.MAX_CARD_CODE; ++i) {
            assertEquals(DefaultCard.valueOf(i),
                    DefaultCard.valueOf(DefaultCard.suitFromCode(i), DefaultCard.rankFromCode(i)));
        }
    }

    @Test
    public void testStraightComparator() {
        {
            final List<Card> sorted = ReaderUtil.cardsFromLatin1("Ac Ad Ah As");
            Collections.sort(sorted);
            assertEquals(ReaderUtil.cardsFromLatin1("As Ah Ad Ac"), sorted);
        }

        {
            final List<Card> sorted = ReaderUtil.cardsFromLatin1("2d Kh Jh 8h");
            Collections.sort(sorted);
            assertEquals(ReaderUtil.cardsFromLatin1("8h Jh Kh 2d"), sorted);
        }
    }
}
