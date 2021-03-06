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

package com.alexshabanov.cards.model;

/**
 * Represents a single card.
 */
public interface Card extends Comparable<Card> {

    /**
     * @return Card's rank, not null.
     */
    Rank getRank();

    /**
     * @return Card's suit, not null.
     */
    Suit getSuit();

    /**
     * @return Unique integer value, that represents rank and suit fold in the code value.
     */
    int getCode();
}
