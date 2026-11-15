package cardgame;

import cardgame.Stable.Condition;
import cardgame.Stable.Type;
import cardgame.Stable.Effect;

public class Turn {
    public void addEnergy(int amount, Card card) {
        card.addEnergy(amount);
    }
    public void useMove(String moveName, Card card, Card target) {
        Object[] moveDetails = card.useMove(moveName);
        Condition status = Condition.None;
        if (moveDetails[2].equals(Effect.Burn)) status = Condition.Burn;
        if (moveDetails[2].equals(Effect.Paralyze)) status = Condition.Paralyze;
        if (moveDetails[2].equals(Effect.Sleep)) status = Condition.Sleep;
        if (moveDetails[2].equals(Effect.Confusion)) status = Condition.Confusion;
        if (moveDetails[2].equals(Effect.Skip)) status = Condition.Skip;
        if (moveDetails[2].equals(Effect.Poison)) status = Condition.Poison;
        target.dealDamage((int) moveDetails[1], (Type) moveDetails[moveDetails.length - 1], status);

    }
    public void retreat(Card card) {
        card.addEnergy(-card.getRetreatCost());
    }
    public Card determineOrder(Card card1, Card card2) {
        if (card1.getRetreatCost() < card2.getRetreatCost()) return card1;
        if (card1.getRetreatCost() > card2.getRetreatCost()) return card2;
        if (card1.getMaxHP() < card2.getMaxHP()) return card1;
        if (card1.getMaxHP() > card2.getMaxHP()) return card2;
        int card1MoveCost = 0;
        for (String moveName : card1.getMoves().keySet()) {
            card1MoveCost += (int) card1.getMoves().get(moveName)[0];
        }
        int card2MoveCost = 0;
        for (String moveName : card2.getMoves().keySet()) {
            card2MoveCost += (int) card2.getMoves().get(moveName)[0];
        }
        if (card1MoveCost < card2MoveCost) return card1;
        if (card1MoveCost > card2MoveCost) return card2;
        return card1;
    }
}
