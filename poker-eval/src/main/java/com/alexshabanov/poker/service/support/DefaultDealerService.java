package com.alexshabanov.poker.service.support;

import com.alexshabanov.cards.util.EncodeUtil;
import com.alexshabanov.poker.service.DealerService;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    public List<Integer> deal() {
        final Integer[] cards = new Integer[EncodeUtil.MAX_CARD_CODE];

        // initial disposition
        for (int i = 0; i < cards.length; ++i) {
            cards[i] = i;
        }

        // shuffle
        for (int shuffle = 0; shuffle < maxShuffles; ++shuffle) {
            for (int i = 0; i < cards.length; ++i) {
                final int newPos = random.nextInt(cards.length);

                // swap cards
                final int tmpCode = cards[i];
                cards[i] = cards[newPos];
                cards[newPos] = tmpCode;
            }
        }

        return Collections.unmodifiableList(Arrays.asList(cards));
    }
}
