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
