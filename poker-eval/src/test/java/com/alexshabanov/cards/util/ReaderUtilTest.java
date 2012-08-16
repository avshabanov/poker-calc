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

import com.alexshabanov.cards.model.DefaultCard;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public final class ReaderUtilTest {

    @Test
    public void testRead() {
        assertEquals(Arrays.asList(DefaultCard.valueOf(Suit.CLUBS, Rank.ACE), DefaultCard.valueOf(Suit.DIAMONDS, Rank.TEN)),
                ReaderUtil.cardsFromLatin1("Ac Td"));

        assertEquals(Arrays.asList(
                DefaultCard.valueOf(Suit.HEARTS, Rank.TWO),
                DefaultCard.valueOf(Suit.CLUBS, Rank.THREE),
                DefaultCard.valueOf(Suit.SPADES, Rank.QUEEN),
                DefaultCard.valueOf(Suit.DIAMONDS, Rank.JACK),
                DefaultCard.valueOf(Suit.DIAMONDS, Rank.ACE)),
                ReaderUtil.cardsFromLatin1("2h 3c Qs Jd Ad"));
    }
}
