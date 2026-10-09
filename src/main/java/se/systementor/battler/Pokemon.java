package se.systementor.battler;

import java.util.ArrayList;

public class Pokemon {
    public String name;
    public Type type;
    public int maxHp;
    public int currentHp;
    public ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon(String name, Type type, int maxHp) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
    }
}
