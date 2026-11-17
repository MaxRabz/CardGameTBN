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
        this.maxEnergy = maxHp/20 > 4 ? maxHp/20 : 4;
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
        if (this.energy + amount > this.maxEnergy || this.energy + amount < 0) throw new IllegalArgumentException("Added too much energy to this card");
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
        if (this.energy < (int) moveDetails[0]) throw new IllegalArgumentException("Not enough energy to use this move");
        this.energy -= (int) moveDetails[0];
        Object[] returnDetails = Arrays.copyOf(moveDetails, moveDetails.length + 1);
        returnDetails[moveDetails.length] = this.typeOffensive;
        return returnDetails;
    }

    public String toString() {
        String energyLine = "Energy: " + energy + "/" + maxEnergy;
        String hpLine = "HP: " + currentHP + "/" + maxHp;
        String nameLine = name;
        String offensiveTypeLine = typeOffensive.toString();
        String retreatLine = "Retreat Cost: " + retreatCost;
        String resistLine = "Resist " + typeResist;
        String weakLine = "Weak " + typeWeak;
        int cardWidth = 30;
        int contentWidth = cardWidth - 4;
        int moveCount = moves.size();
        int spacerRows = Math.max(0, 8 - moveCount * 2);
        int firstGapRows = spacerRows / 2;
        int secondGapRows = spacerRows - firstGapRows;
        String cardBorder = repeat('_', cardWidth);
        String bottomBorder = "|" + repeat('_', cardWidth - 2) + "|";
        StringBuilder cardDetails = new StringBuilder();
        cardDetails.append(cardBorder).append('\n');
        appendPairedCardLine(cardDetails, energyLine, hpLine, contentWidth);
        appendPairedCardLine(cardDetails, nameLine, offensiveTypeLine, contentWidth);
        appendBlankCardRows(cardDetails, firstGapRows, contentWidth);
        for (Map.Entry<String, Object[]> move : moves.entrySet()) {
            appendCardLine(cardDetails, move.getKey() + ":", contentWidth);
            appendCardLine(cardDetails, "    " + Arrays.toString(move.getValue()), contentWidth);
        }
        appendBlankCardRows(cardDetails, secondGapRows, contentWidth);
        appendCardLine(cardDetails, retreatLine, contentWidth);
        appendPairedCardLine(cardDetails, resistLine, weakLine, contentWidth);
        cardDetails.append(bottomBorder);
        return cardDetails.toString();
    }

    private void appendPairedCardLine(StringBuilder cardDetails, String leftText, String rightText, int contentWidth) {
        int spacing = contentWidth - leftText.length() - rightText.length();
        cardDetails.append("| ")
                .append(leftText)
                .append(repeat(' ', spacing))
                .append(rightText)
                .append(" |\n");
    }

    private void appendCardLine(StringBuilder cardDetails, String text, int contentWidth) {
        cardDetails.append("| ")
                .append(text)
                .append(repeat(' ', contentWidth - text.length()))
                .append(" |\n");
    }

    private void appendBlankCardRows(StringBuilder cardDetails, int count, int contentWidth) {
        String blankRow = "|" + repeat(' ', contentWidth + 2) + "|\n";
        for (int i = 0; i < count; i++) {
            cardDetails.append(blankRow);
        }
    }

    private String repeat(char character, int count) {
        StringBuilder repeated = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            repeated.append(character);
        }
        return repeated.toString();
    }
}