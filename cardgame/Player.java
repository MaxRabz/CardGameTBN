package cardgame;

import java.util.Stack;

public class Player {
    Stack<Card> deck;
    Card[] hand;
    Card activeCard;
    Card benchCard1;
    Card benchCard2;
    int deadCards;

    public Player(Card[] deck) {

        this.deck = new Stack<>();
        for (Card card : deck) {
            this.deck.push(card);
        }
        this.hand = new Card[5];
        for (int i = 0; i < 5; i++) {
            this.hand[i] = this.deck.pop();
        }
        this.activeCard = null;
        this.benchCard1 = null;
        this.benchCard2 = null;
        this.deadCards = 0;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public Card[] getHand() {
        return hand;
    }

    public int getDeadCards() {
        return deadCards;
    }

    public void swapActiveWithBench(int benchNumber) {
        if (benchNumber == 1 && benchCard1 != null) {
            Card temp = activeCard;
            activeCard = benchCard1;
            benchCard1 = temp;
        } else if (benchNumber == 2 && benchCard2 != null) {
            Card temp = activeCard;
            activeCard = benchCard2;
            benchCard2 = temp;
        }
    }

    public void setActiveCard(Card activeCard, int handIndex) {
        this.activeCard = activeCard;
        this.hand[handIndex] = null;
    }

    public void removeActiveCard() {
        this.activeCard = null;
        this.deadCards++;
    }

    public void promoteBenchCardToActive(int benchNumber) {
        if (benchNumber == 1) {
            this.activeCard = this.benchCard1;
            this.benchCard1 = null;
        } else if (benchNumber == 2) {
            this.activeCard = this.benchCard2;
            this.benchCard2 = null;
        }
    }

    public void setBenchCard(Card benchCard, int benchNumber, int handIndex) {
        this.hand[handIndex] = null;
        if (benchNumber == 1) {
            this.benchCard1 = benchCard;
        } else if (benchNumber == 2) {
            this.benchCard2 = benchCard;
        }
    }

    public Card getActiveCard() {
        return activeCard;
    }

    public Card getBenchCard(int benchNumber) {
        if (benchNumber == 1) {
            return benchCard1;
        } else if (benchNumber == 2) {
            return benchCard2;
        }
        return null;
    }

    public Card drawCard() {
        if (this.deck.isEmpty()) return null;
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i] == null) {
                this.hand[i] = this.deck.pop();
            }
        }
        return null;
    }
}
