package com.alexshabanov.poker.model.util;

import com.alexshabanov.cards.model.Card;

import java.util.List;

/**
 * User defined callback used in iterate method.
 */
public interface CardCombinationCallback {

    /**
     * Processes combination of cards.
     * The processing function may change and/or safely save the given card codes since the caller party
     * SHALL guarantee that it will no longer access the provided card code list.
     *
     * @param cards Combination of cards.
     * @return True, if the execution shall stop, false otherwise.
     */
    boolean process(List<Card> cards);
}
