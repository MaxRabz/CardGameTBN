package cardgame;

import cardgame.Stable.Condition;
import cardgame.Stable.Type;
import cardgame.Stable.Effect;
import java.util.Random;

public class Turn {
    public int checkActions(Card card) {
        Random ran = new Random();

        card.addStatusTurn();
        if (card.getStatusTurns() == 4) {
            card.removeStatusTurns();
            card.removeStatus();
        }

        Condition status = card.getStatus();
        if (status.equals(Condition.Burn)) card.dealDamage(10, null, Condition.None);
        if (status.equals(Condition.Poison)) card.dealDamage(30, null, Condition.None);
        if (status.equals(Condition.Sleep)) {
            int returnNum = ran.nextBoolean() ? 3 : 2;
            if (returnNum == 3) {
                card.removeStatus();
            }
            return returnNum;
        }
        if (card.getStatus().equals(Condition.Skip)) {
            card.removeStatusTurns();
            card.removeStatus();
            return 2;
        }

        return 3;
    }

    public void addEnergy(Card card) {
        card.addEnergy(1);
    }

    public boolean useMove(String moveName, Card card, Card target) {
        Random ran = new Random();
        Object[] moveDetails = card.useMove(moveName);
        if (ran.nextBoolean() && card.getStatus().equals(Condition.Confusion)) {
            card.dealDamage((int) moveDetails[1], null, Condition.None);
            return false;
        }
        Condition status = Condition.None;
        if (moveDetails[2].equals(Effect.Burn)) status = Condition.Burn;
        if (moveDetails[2].equals(Effect.Paralyze)) status = Condition.Paralyze;
        if (moveDetails[2].equals(Effect.Sleep)) status = Condition.Sleep;
        if (moveDetails[2].equals(Effect.Confusion)) status = Condition.Confusion;
        if (moveDetails[2].equals(Effect.Skip)) status = Condition.Skip;
        if (moveDetails[2].equals(Effect.Poison)) status = Condition.Poison;
        int halvedDamage = card.getStatus().equals(Condition.Burn) ? 2 : 1;
        target.dealDamage(((int) moveDetails[1]) / halvedDamage, (Type) moveDetails[moveDetails.length - 1], status);
        return true;
    }

    public void retreat(Card card, Player player, int benchNumber) {
        int energyNeeded = card.getStatus().equals(Condition.Paralyze) ? card.getRetreatCost() * 2 : card.getRetreatCost();
        try {
            card.addEnergy(-energyNeeded);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not enough Energy to retreat");
        }
        player.swapActiveWithBench(benchNumber);
    }

    public Card determineOrder(Card card1, Card card2) {
        int card1RetreatCost = card1.getStatus().equals(Condition.Paralyze) ? card1.getRetreatCost() * 2 : card1.getRetreatCost();
        int card2RetreatCost = card2.getStatus().equals(Condition.Paralyze) ? card2.getRetreatCost() * 2 : card2.getRetreatCost();
        if (card1RetreatCost < card2RetreatCost) return card1;
        if (card1RetreatCost > card2RetreatCost) return card2;
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
