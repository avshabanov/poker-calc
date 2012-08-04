package com.alexshabanov.cards.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class EncodeUtilTest {

    @Test
    public void testStraightforwardEncoding() {
        final List<Integer> cardCodes = new ArrayList<Integer>(EncodeUtil.MAX_CARD_CODE);
        for (int i = 0; i < EncodeUtil.MAX_CARD_CODE; ++i) {
            cardCodes.add(i);
        }

        assertEquals(cardCodes, EncodeUtil.toCodes(EncodeUtil.fromCodes(cardCodes)));
    }
}
