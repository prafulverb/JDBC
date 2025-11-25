package com.gardening.platform.model;

public class Gardener extends User {

    public Gardener(int id, String name, String email, String password) {
        super(id, name, email, password, "GARDENER");
    }

    public Gardener(String name, String email, String password) {
        super(name, email, password, "GARDENER");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Gardener Dashboard: Tips, Discussions, Projects.");
    }
}
