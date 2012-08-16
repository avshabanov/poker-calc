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

package com.alexshabanov.sampleapp;

import com.alexshabanov.cards.model.Card;
import com.alexshabanov.cards.util.PrintUtil;
import com.alexshabanov.poker.service.DealerService;
import com.alexshabanov.poker.service.support.DefaultDealerService;
import org.junit.Ignore;

import java.util.List;

@Ignore
public final class DealerSampleApp {
    private static void printCards(List<Card> cards) {
        System.out.print("Cards:");
        for (final Card c : cards) {
            System.out.print(" " + PrintUtil.asUtfString(c));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final DealerService dealerService = new DefaultDealerService();
        for (int i = 0; i < 3; ++i) {
            printCards(dealerService.deal());
        }
    }
}
