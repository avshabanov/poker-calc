package com.alexshabanov.poker.service;

import com.alexshabanov.cards.model.Card;

import java.util.List;

/**
 * Deals cards.
 */
public interface DealerService {

    /**
     * @return Unmodifiable, non-null dealt cards.
     */
    List<Card> deal();
}
