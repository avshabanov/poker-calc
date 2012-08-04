package com.alexshabanov.poker;

import com.alexshabanov.cards.util.EncodeUtil;
import com.alexshabanov.cards.util.PrintUtil;
import com.alexshabanov.poker.service.DealerService;
import com.alexshabanov.poker.service.support.DefaultDealerService;

import java.util.List;

/**
 * Entry point.
 */
public final class App {
    private static void printCards(List<Integer> cards) {
        System.out.print("Cards:");
        for (final Integer c : cards) {
            System.out.print(" " + PrintUtil.asUtfString(EncodeUtil.fromCode(c)));
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
