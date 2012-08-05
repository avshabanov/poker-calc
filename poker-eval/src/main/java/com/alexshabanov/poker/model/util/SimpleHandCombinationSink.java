package com.alexshabanov.poker.model.util;

import com.alexshabanov.poker.model.Hand;


/**
 * Base implementation of {@link HandCombinationSink}.
 */
public abstract class SimpleHandCombinationSink implements HandCombinationSink {
    @Override
    public void setBestHand(Hand hand) {
    }
}
