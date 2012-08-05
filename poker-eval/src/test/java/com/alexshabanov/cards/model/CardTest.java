package com.alexshabanov.cards.model;

import com.alexshabanov.cards.util.ReaderUtil;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class CardTest {

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
