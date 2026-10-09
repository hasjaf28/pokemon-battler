package se.systementor.battler.model;

import java.util.ArrayList;

public class Pokemon {
    private String name;
    private Type type;
    private int maxHp;
    private int currentHp;
    private ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon(String name, Type type, int maxHp) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
    }
    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }
    public int getMaxHp() {
        return maxHp;
    }
    public int getCurrentHp() {
        return currentHp;
    }
    public ArrayList<Attack> getAttacks() {
        return attacks;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }
}
