package com.alexshabanov.poker.service;

import java.util.List;

/**
 * Deals cards.
 */
public interface DealerService {

    /**
     * @return Unmodifiable, non-null dealt cards.
     */
    List<Integer> deal();
}
