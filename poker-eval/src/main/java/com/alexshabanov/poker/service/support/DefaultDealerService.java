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
