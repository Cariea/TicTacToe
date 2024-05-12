package com.cariea.tictactoe;

public class Player {
    private String id;
    private String name;


    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}