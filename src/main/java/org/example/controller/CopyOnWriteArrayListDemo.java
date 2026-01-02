package org.example.controller;

import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) throws InterruptedException {

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

        Runnable writer = () -> {
            for(int i = 0; i < 5; i++) {
                list.add(i);
            }
        };

        Runnable reader = () -> {
            for(Integer i : list)
                System.out.println("Read: " + i);
        };

        new Thread(writer).start();
        new Thread(reader).start();

        System.out.println("===================================");

        CopyOnWriteArrayList<String> list1 =
                new CopyOnWriteArrayList<>();

        list1.add("A");
        list1.add("B");
        list1.add("C");

        // Safe iteration
        for (String s : list1) {
            System.out.println(s);
            list1.add("D");  // NO Exception
        }

        System.out.println("Final List: " + list1);

        System.out.println("===================================");

    }
}
