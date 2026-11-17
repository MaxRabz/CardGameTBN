package cardgame;

import java.util.Scanner;

import cardgame.Stable.Effect;
import cardgame.Stable.Type;
import cardgame.Stable.Condition;

import java.util.ArrayList;
import java.util.Map;
public class Game {
    Scanner scanner = new Scanner(System.in);
    Player player;
    AIPlayer opponent;
    Turn turn;
    public void main(String args[]) {
        CreateDecks deckCreator = new CreateDecks();
        Card[] playerDeck = deckCreator.getDeck1();
        Card[] opponentDeck = deckCreator.getDeck2();
        this.player = new Player(playerDeck);
        this.opponent = new AIPlayer(opponentDeck);
        this.turn = new Turn();
        EstablishInitialActiveCards();
        this.opponent.establishStartingCards();
        int turnsPassed = 1;
        while (player.getDeadCards() < 10 && opponent.getDeadCards() < 10) {
            System.out.println("Turn #" + turnsPassed);
            Card result = turn.determineOrder(player.getActiveCard(), opponent.getActiveCard());
            System.out.println("The order of play is: " + result.getName() + " goes first.");
            if (player.getActiveCard().equals(result)) {
                getAction();
                if (opponent.getActiveCard().getCurrentHP() <= 0) {
                    opponent.replaceDeadCard();
                }
                opponent.playTurn(turn);
            } else {
                opponent.playTurn(turn);
                if (player.getActiveCard().getCurrentHP() <= 0) {
                    replaceDeadCard();
                }
                getAction();
            }
            turnsPassed++;
            if (player.getActiveCard().getCurrentHP() <= 0) {
                replaceDeadCard();
            }
            if (opponent.getActiveCard().getCurrentHP() <= 0) {
                opponent.replaceDeadCard();
            }
        }
    }

    private void replaceDeadCard() {
        player.removeActiveCard();
        System.out.println("Your active card has been defeated. Please select a new active card from your bench:");
        System.out.println("1. " + player.getBenchCard(1).toString());
        System.out.println("2. " + player.getBenchCard(2).toString());
        int benchNumber = getLegitNumber(1, 2);
        if (benchNumber == 1) {
            player.promoteBenchCardToActive(1);
            pickNewBenchCard(1);
        } else if (benchNumber == 2) {
            player.promoteBenchCardToActive(2);
            pickNewBenchCard(2);
        }
        player.drawCard();
        printHand();
    }

    private void printHand() {
        for (int i = 0; i < player.getHand().length; i++) {
            if (player.getHand()[i] != null) {
                System.out.println((i + 1) + ". \n" + player.getHand()[i].toString());
            }
        }
    }

    private void pickNewBenchCard(int num) {
        System.out.println("Your bench card has been defeated. Please select a new bench card from your hand:");
        printHand();
        int handIndex = getLegitNumber(1, player.getHand().length) - 1;
        Card newBenchCard = player.getHand()[handIndex];
        player.setBenchCard(newBenchCard, num, handIndex);
    }

    private void getAction() {
        System.out.println("Your active card is: \n" + player.getActiveCard().toString());
        System.out.println("Your bench cards are: \n" + player.getBenchCard(1).toString() + "\n" + player.getBenchCard(2).toString());
        System.out.println("Your hand is: ");
        for (int i = 0; i < player.getHand().length; i++) {
            if (player.getHand()[i] != null) {
                System.out.println((i + 1) + ". " + player.getHand()[i].toString());
            }
        }

        System.out.println("Your opponent's active card is: \n" + opponent.getActiveCard().toString());
        System.out.println("Your opponent's bench cards are: \n" + opponent.getBenchCard(1).toString() + "\n" + opponent.getBenchCard(2).toString());

        int actions = turn.checkActions(player.getActiveCard());
        for (int i = 0; i < actions; i++) {
            System.out.println("You have " + (actions - i) + " actions remaining this turn.");
            if (actions == 2) {
                System.out.println("You may only add energy this turn due to your active card's status condition.");
                addEnergyToCard();
                continue;
            }
            System.out.println("Please select an action:");
            System.out.println("1. Use Move");
            System.out.println("2. Retreat");
            System.out.println("3. Add Energy");
            int action = getLegitNumber(1, 3);
            if (action == 1) {
                System.out.println("Please select a move:");
                Map<String, Object[]> moves = player.getActiveCard().getMoves();
                int moveIndex = 0;
                for (String moveName : moves.keySet()) {
                    System.out.println((moveIndex + 1) + ". " + moveName);
                    moveIndex++;
                }
                int selectedMove = getLegitNumber(1, moves.size()) - 1;
                String moveName = new ArrayList<>(moves.keySet()).get(selectedMove);
                boolean success = turn.useMove(moveName, player.getActiveCard(), opponent.getActiveCard());
                if (!success) {
                    System.out.println("Your move failed due to confusion!");
                } else {
                    System.out.println("You used " + moveName + " on " + opponent.getActiveCard().getName() + ".");
                    if (moves.get(moveName)[2] == Effect.Backrow) {
                        System.out.println("Please select a bench card to target:");
                        System.out.println("1. " + opponent.getBenchCard(1).getName());
                        System.out.println("2. " + opponent.getBenchCard(2).getName());
                        int benchNumber = getLegitNumber(1, 2);
                        Card targetCard = opponent.getBenchCard(benchNumber);
                        targetCard.dealDamage((int) moves.get(moveName)[1], (Type) moves.get(moveName)[moves.get(moveName).length - 1], Condition.None);

                    }
                    if (moves.get(moveName)[2] == Effect.Swap) {
                        System.out.println("Please select a bench card to swap with:");
                        System.out.println("1. " + player.getBenchCard(1).getName());
                        System.out.println("2. " + player.getBenchCard(2).getName());
                        int benchNumber = getLegitNumber(1, 2);
                        player.swapActiveWithBench(benchNumber);
                    }
                }
            } else if (action == 2) {
                System.out.println("Please select a bench card to swap with:");
                System.out.println("1. " + player.getBenchCard(1).getName());
                System.out.println("2. " + player.getBenchCard(2).getName());
                int benchNumber = getLegitNumber(1, 2);
                turn.retreat(player.getActiveCard(), player, benchNumber);
            } else if (action == 3) {
                addEnergyToCard();
            }
        }
    }

    private void addEnergyToCard() {
        System.out.println("Select a card to add energy to:");
        System.out.println("1. " + player.getActiveCard().getName());
        System.out.println("2. " + player.getBenchCard(1).getName());
        System.out.println("3. " + player.getBenchCard(2).getName());
        int cardNumber = getLegitNumber(1, 3);
        if (cardNumber == 1) {
            turn.addEnergy(player.getActiveCard());
        } else if (cardNumber == 2) {
            turn.addEnergy(player.getBenchCard(1));
        } else if (cardNumber == 3) {
            turn.addEnergy(player.getBenchCard(2));
        }
        turn.addEnergy(player.getActiveCard());
    }

    private void EstablishInitialActiveCards() {
        System.out.println("Your Hand is:");
        printHand();
        System.out.println("Please select your active card by entering the corresponding number:");
        int cardIndex = getLegitNumber(1, player.getHand().length) - 1;
        Card playedCard = player.getHand()[cardIndex];
        player.setActiveCard(playedCard, cardIndex);
        System.out.println("Your active card is: \n" + playedCard.toString());
        System.out.println("Please select your first bench card by entering the corresponding number:");
        cardIndex = getLegitNumber(1, player.getHand().length) - 1;
        playedCard = player.getHand()[cardIndex];
        player.setBenchCard(playedCard, 1, cardIndex);
        System.out.println("Your first bench card is: \n" + playedCard.toString());
        System.out.println("Please select your second bench card by entering the corresponding number:");
        cardIndex = getLegitNumber(1, player.getHand().length) - 1;
        playedCard = player.getHand()[cardIndex];
        player.setBenchCard(playedCard, 2, cardIndex);
        System.out.println("Your second bench card is: \n" + playedCard.toString());
        player.drawCard();
    }

    private int getLegitNumber(int min, int max) {
        int number = 0;
        while (true) {
            try {
                number = Integer.parseInt(scanner.nextLine());
                if (number < min || number > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        return number;
    }
}
