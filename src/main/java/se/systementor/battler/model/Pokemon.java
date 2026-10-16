package se.systementor.battler.model;

import java.util.ArrayList;

public class Pokemon {
    private String name;
    private Type type;
    private int maxHp;
    private int currentHp;
    private final ArrayList<Attack> attacks = new ArrayList<>();

    public Pokemon(String name, Type type, int maxHp) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Namnet får inte vara tomt!");
        if (type == null)
            throw new IllegalArgumentException("Type får inte vara tom!");
        if (maxHp < 1 || maxHp > 1000)
            throw new IllegalArgumentException("Max HP ska vara mellan 1-1000!");
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
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Namnet får inte vara tomt!");
        this.name = name;
    }
    public void setType(Type type) {
        if (type == null)
            throw new IllegalArgumentException("Type får inte vara tom!");
        this.type = type;
    }
    public void setMaxHp(int maxHp) {
        if (maxHp < 1 || maxHp > 1000)
            throw new IllegalArgumentException("Max HP ska vara mellan 1-1000!");
        this.maxHp = maxHp;
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }

    public void setCurrentHp( int value) {
        if (value < 0) value = 0;
        if (value > maxHp) value = maxHp;
        this.currentHp = value;
    }

    public void takeDamage(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Skada får inte vara negativ!");
        currentHp = Math.max(0, currentHp - amount);
    }

    public void heal(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Läkningen kan inte vara negativ!");
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public boolean isFainted() {
        return currentHp == 0;
    }

}
