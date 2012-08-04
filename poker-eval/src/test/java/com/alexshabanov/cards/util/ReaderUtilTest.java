package com.alexshabanov.cards.util;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.model.Rank;
import com.alexshabanov.cards.model.Suit;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public final class ReaderUtilTest {

    @Test
    public void testRead() {
        assertEquals(Arrays.asList(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.DIAMONDS, Rank.TEN)),
                ReaderUtil.cardsFromLatin1("Ac Td"));

        assertEquals(Arrays.asList(
                new Card(Suit.HEARTS, Rank.TWO),
                new Card(Suit.CLUBS, Rank.THREE),
                new Card(Suit.SPADES, Rank.QUEEN),
                new Card(Suit.DIAMONDS, Rank.JACK),
                new Card(Suit.DIAMONDS, Rank.ACE)),
                ReaderUtil.cardsFromLatin1("2h 3c Qs Jd Ad"));
    }
}
