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
