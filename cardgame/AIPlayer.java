package cardgame;

public class AIPlayer extends Player {
    public AIPlayer(Card[] deck) {
        super(deck);
    }

    public void establishStartingCards() {
        this.setActiveCard(this.getHand()[0], 0);
        this.setBenchCard(this.getHand()[1], 1, 1);
        this.setBenchCard(this.getHand()[2], 2, 2);
        this.drawCard();
    }

    public void playTurn(Turn turn) {
        // Implement AI logic for playing a turn
        for (int i = 0; i < 3; i++) {
            try {
                turn.addEnergy(this.getActiveCard());
            } catch (Exception e) {
                try {
                    turn.addEnergy(this.getBenchCard(1));
                } catch (Exception e1) {
                    try {
                        turn.addEnergy(this.getBenchCard(2));
                    } catch (Exception e2) {
                        this.swapActiveWithBench(1);
                    }
                }
            }
        }
    }

    public void replaceDeadCard() {
        this.removeActiveCard();
        if (this.getBenchCard(1) != null) {
            this.promoteBenchCardToActive(1);
            this.setBenchCard(this.hand[1], 1, 1);
        } else if (this.getBenchCard(2) != null) {
            this.promoteBenchCardToActive(2);
            this.setBenchCard(this.hand[1], 2, 1);
        }
        this.drawCard();
    }
}
