package com.alexshabanov.poker.model.util;

import com.alexshabanov.poker.model.Hand;

/**
 * Callback, that is invoked when the search method finds the appropriate combination.
 * {@see #maybeStraightFlush}
 */
public interface HandCombinationSink {
    void setBestHand(Hand hand);
}
