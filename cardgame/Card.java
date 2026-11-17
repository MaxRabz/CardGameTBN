package cardgame;
import java.util.Arrays;
import java.util.Map;

import cardgame.Stable.Condition;
import cardgame.Stable.Effect;
import cardgame.Stable.Type;

public class Card {
    private String name;
    private int maxHp;
    private int currentHP;
    private Type typeOffensive;
    private Map<String, Object[]> moves;
    private int retreatCost;
    private Type typeResist;
    private Type typeWeak;
    private Condition status;
    private int statusTurns;
    private int energy;
    private int maxEnergy;

    public Card(String name, int hp, Map<String, Object[]> moves, int retreatCost, Type typeOffensive, Type typeResist, Type typeWeak) {
        this.name = name;
        this.maxHp = hp;
        this.currentHP = hp;
        this.moves = moves;
        this.retreatCost = retreatCost;
        this.typeOffensive = typeOffensive;
        this.typeResist = typeResist;
        this.typeWeak = typeWeak;
        this.status = Condition.None;
        this.statusTurns = 0;
        this.energy = 0;
        this.maxEnergy = maxHp/20 > 4 ? 4 : maxHp/20;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHp;
    }

    public Type getTypeOffensive() {
        return typeOffensive;
    }

    public Type getTypeResist() {
        return typeResist;
    }

    public Type getTypeWeak() {
        return typeWeak;
    }

    public Condition getStatus() {
        return status;
    }

    public void removeStatus() {
        this.status = Condition.None;
    }

    public int getStatusTurns() {
        return this.statusTurns;
    }

    public void addStatusTurn() {
        this.statusTurns++;
    }

    public void removeStatusTurns() {
        this.statusTurns = 0;
    }

    public int getRetreatCost() {
        return retreatCost;
    }

    public Map<String, Object[]> getMoves() {
        return moves;
    }
    
    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int amount) {
        if (this.energy + amount > this.maxEnergy || this.energy + amount < 0) throw new IllegalArgumentException();
        this.energy += amount;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int dealDamage(int damage, Type attackType, Condition newStatus) {
        if (attackType != null) {
            if (attackType.equals(this.typeResist)) damage /=2;
            if (attackType.equals(this.typeWeak)) damage *= 2;
        }
        this.currentHP -= damage;
        this.status = this.status == Condition.None ? newStatus : this.status;
        return this.currentHP;
    }

    public Object[] useMove(String moveName) {
        Object[] moveDetails = moves.get(moveName);
        if (moveDetails[2].equals(Effect.Recoil)) this.currentHP -= (int) moveDetails[3];
        if (moveDetails[2].equals(Effect.Heal)) this.currentHP += (int) moveDetails[3];
        if (currentHP > maxHp) currentHP = maxHp;
        if (this.energy < (int) moveDetails[0]) throw new IllegalArgumentException();
        this.energy -= (int) moveDetails[0];
        Object[] returnDetails = Arrays.copyOf(moveDetails, moveDetails.length + 1);
        returnDetails[moveDetails.length] = this.typeOffensive;
        return returnDetails;
    }

    public String toString() {
        String cardDetails = "______________________\n";
        cardDetails += "| Energy: " + energy + "/" + maxEnergy + " |\n";
        cardDetails += "|"+name+ " " + typeOffensive + " HP:" + currentHP + "/" + maxHp + "\n";
        cardDetails += "|                    |\n";
        cardDetails += "|____________________|\n";
        for (Map.Entry<String, Object[]> move : moves.entrySet()) {
            cardDetails += "| " + move.getKey() + " " + Arrays.toString(move.getValue()) + " |\n";
        }
        cardDetails += "|____________________|\n";
        cardDetails += "| Retreat Cost: " + retreatCost + " |\n";
        cardDetails += "| " + typeResist + " " + typeWeak + " |\n";
        cardDetails += "|____________________|\n";
        return cardDetails;
    }
}