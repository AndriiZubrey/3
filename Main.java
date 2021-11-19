package myproject;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        final int MIN_PRICE = 2;
        final int MAX_PRICE = 20;
        final int MIN_WEIGHT = 1;
        final int MAX_WEIGHT = 10;
        final int MAX_BAG_WEIGHT = 200; //місткість рюкзака
        final int ITEMS_AMOUNT = 100;
        final int SOLUTION_LIST_SIZE = 100;
        final int ITERATIONS_AMOUNT = 1000;

        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < ITEMS_AMOUNT; i++) {
            int price = (int)(Math.random()*(MAX_PRICE-MIN_PRICE+1)+MIN_PRICE);
            int weight = (int)(Math.random()*(MAX_WEIGHT-MIN_WEIGHT+1)+MIN_WEIGHT);
            items.add(new Item(price, weight));
        }

        GeneticAlgorithm ga = new GeneticAlgorithm(MAX_BAG_WEIGHT, SOLUTION_LIST_SIZE,items);
        System.out.println(ga.start(ITERATIONS_AMOUNT));

    }
}
