package se.systementor.battler.repository;

import se.systementor.battler.model.Type;

import java.util.ArrayList;

public class PokemonData {
    public String name;
    public Type type;
    public int maxHp;
    public int currentHp;
    public ArrayList<AttackData> attacks;
}
