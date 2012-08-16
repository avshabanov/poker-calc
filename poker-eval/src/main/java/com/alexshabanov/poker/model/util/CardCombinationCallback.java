/*
 * Copyright 2012 Alexander Shabanov - http://alexshabanov.com.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
