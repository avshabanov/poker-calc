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
