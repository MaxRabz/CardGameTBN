package cardgame;

import java.util.Arrays;
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
            System.out.println("Turn #" + turnsPassed + ". You have lost " + player.getDeadCards() + " cards. Your opponent has lost " + opponent.getDeadCards() + " cards.");
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
        if (player.getDeadCards() >= 10) {
            System.out.println("You have lost the game. Your opponent has won.");
        } else {
            System.out.println("Congratulations! You have won the game.");
        }
    }

    private void replaceDeadCard() {
        player.removeActiveCard();
        printCards("Your bench:", new Card[] {player.getBenchCard(1), player.getBenchCard(2)},
                new String[] {"1. " + cardName(player.getBenchCard(1)), "2. " + cardName(player.getBenchCard(2))});
        printOptions("Your active card has been defeated. Select a new active card:",
                "1. " + cardName(player.getBenchCard(1)), "2. " + cardName(player.getBenchCard(2)));
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
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<String> choices = new ArrayList<>();
        for (int i = 0; i < player.getHand().length; i++) {
            Card card = player.getHand()[i];
            if (card != null) {
                cards.add(card);
                choices.add((i + 1) + ". " + card.getName());
            }
        }
        printCards("Your hand:", cards.toArray(new Card[0]), choices.toArray(new String[0]));
    }

    private void pickNewBenchCard(int num) {
        System.out.println("Your bench card has been defeated.");
        printHand();
        System.out.println("Select a new bench card from your hand:");
        int handIndex = getLegitNumber(1, player.getHand().length) - 1;
        Card newBenchCard = player.getHand()[handIndex];
        player.setBenchCard(newBenchCard, num, handIndex);
    }

    private void getAction() {
        System.out.println("Your active card is: \n" + player.getActiveCard().toString());
        printCards("Your bench cards:", new Card[] {player.getBenchCard(1), player.getBenchCard(2)},
                new String[] {"1.", "2."});

        System.out.println("Your opponent's active card is: \n" + opponent.getActiveCard().toString());
        printCards("Your opponent's bench cards:",
                new Card[] {opponent.getBenchCard(1), opponent.getBenchCard(2)}, new String[] {"1.", "2."});

        int actions = turn.checkActions(player.getActiveCard());
        for (int i = 0; i < actions; i++) {
            System.out.println("You have " + (actions - i) + " actions remaining this turn.");
            if (actions == 2) {
                System.out.println("You may only add energy this turn due to your active card's status condition.");
                addEnergyToCard();
                continue;
            }
            printOptions("Please select an action:", "1. Use Move", "2. Retreat", "3. Add Energy", "4. View Hand", "5. View Rules");
            int action = getLegitNumber(1, 5);
            try {
                playAction(action);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                i--;
                continue;
            }
        }
    }

    private void playAction(int action) {
        if (action == 1) {
            Map<String, Object[]> moves = player.getActiveCard().getMoves();
            ArrayList<String> moveOptions = new ArrayList<>();
            int moveIndex = 1;
            for (String moveName : moves.keySet()) {
                moveOptions.add(moveIndex + ". " + moveName + ": " + Arrays.toString(moves.get(moveName)));
                moveIndex++;
            }
            printOptions("Please select a move:", moveOptions.toArray(new String[0]));
            if (opponent.getActiveCard().getCurrentHP() <= 0) throw new IllegalArgumentException("Can't attack when no opponent");
            int selectedMove = getLegitNumber(1, moves.size()) - 1;
            String moveName = new ArrayList<>(moves.keySet()).get(selectedMove);
            int damageDealt = turn.useMove(moveName, player.getActiveCard(), opponent.getActiveCard());
            if (damageDealt == ((int) moves.get(moveName)[1]) / 2) {
                System.out.println("Your move failed due to confusion! you dealt " + damageDealt + " damage to yourself.");
            } else {
                System.out.println("You used " + moveName + " on " + opponent.getActiveCard().getName() + ". You dealt " + damageDealt + " damage.");
                if (moves.get(moveName)[2] == Effect.Backrow) {
                    printOptions("Please select a bench card to target:",
                            "1. " + cardName(opponent.getBenchCard(1)),
                            "2. " + cardName(opponent.getBenchCard(2)));
                    int benchNumber = getLegitNumber(1, 2);
                    Card targetCard = opponent.getBenchCard(benchNumber);
                    targetCard.dealDamage((int) moves.get(moveName)[3], (Type) moves.get(moveName)[moves.get(moveName).length - 1], Condition.None);
                    System.out.println("You dealt " + (int) moves.get(moveName)[3] + " damage to " + targetCard.getName() + ".");
                }
                if (moves.get(moveName)[2] == Effect.Swap) {
                    printOptions("Please select a bench card to swap with:",
                            "1. " + cardName(player.getBenchCard(1)),
                            "2. " + cardName(player.getBenchCard(2)));
                    int benchNumber = getLegitNumber(1, 2);
                    player.swapActiveWithBench(benchNumber);
                }
                if (moves.get(moveName)[2] == Effect.Recoil) {
                    System.out.println("You took " + (int) moves.get(moveName)[3] + " recoil damage.");
                }
                if (opponent.getActiveCard().getCurrentHP() <= 0) {
                    System.out.println("Your opponent's active card has been defeated!");
                }
            }
        } else if (action == 2) {
            printOptions("Please select a bench card to swap with:",
                    "1. " + cardName(player.getBenchCard(1)),
                    "2. " + cardName(player.getBenchCard(2)));
            int benchNumber = getLegitNumber(1, 2);
            turn.retreat(player.getActiveCard(), player, benchNumber);
        } else if (action == 3) {
            addEnergyToCard();
        } else if (action == 4) {
            printHand();
        } else if (action == 5) {
            System.out.println("Rules: Each player has a deck of cards. Each card has a type, HP, energy, and moves. Players take turns using moves to deal damage to the opponent's active card. When a card's HP reaches 0, it is defeated and removed from play. Players can also retreat their active card to swap with a bench card. The game ends when one player has lost 10 cards.");
        }
    }

    private void addEnergyToCard() {
        printOptions("Select a card to add energy to:",
                "1. " + cardName(player.getActiveCard()),
                "2. " + cardName(player.getBenchCard(1)),
                "3. " + cardName(player.getBenchCard(2)));
        int cardNumber = getLegitNumber(1, 3);
        if (cardNumber == 1) {
            turn.addEnergy(player.getActiveCard());
        } else if (cardNumber == 2) {
            turn.addEnergy(player.getBenchCard(1));
        } else if (cardNumber == 3) {
            turn.addEnergy(player.getBenchCard(2));
        }
    }

    private void EstablishInitialActiveCards() {
        System.out.println("Your Hand is:");
        printHand();
        System.out.println("Please select your active card by entering the corresponding number:");
        int cardIndex = getLegitNumber(1, player.getHand().length) - 1;
        Card playedCard = player.getHand()[cardIndex];
        player.setActiveCard(playedCard, cardIndex);
        System.out.println("Your active card is:\n" + playedCard);
        System.out.println("Please select your first bench card by entering the corresponding number:");
        cardIndex = -1;
        while (cardIndex == -1) {
            cardIndex = getLegitNumber(1, player.getHand().length) - 1;
            if (player.getHand()[cardIndex] == null) {
                System.out.println("You cannot select a card that has already been played. Please select a different card.");
                cardIndex = -1;
            }
        }
        playedCard = player.getHand()[cardIndex];
        player.setBenchCard(playedCard, 1, cardIndex);
        System.out.println("Your first bench card is:\n" + playedCard);
        System.out.println("Please select your second bench card by entering the corresponding number:");
        cardIndex = -1;
        while (cardIndex == -1) {
            cardIndex = getLegitNumber(1, player.getHand().length) - 1;
            if (player.getHand()[cardIndex] == null) {
                System.out.println("You cannot select a card that has already been played. Please select a different card.");
                cardIndex = -1;
            }
        }
        playedCard = player.getHand()[cardIndex];
        player.setBenchCard(playedCard, 2, cardIndex);
        System.out.println("Your second bench card is:\n" + playedCard);
        player.drawCard();
        System.out.println("Your new Hand is:");
        printHand();
    }

    private void printOptions(String prompt, String... options) {
        StringBuilder line = new StringBuilder(prompt);
        for (String option : options) {
            line.append(line.length() == prompt.length() ? " " : " | ").append(option);
        }
        System.out.println(line);
    }

    private String cardName(Card card) {
        return card == null ? "(empty)" : card.getName();
    }

    private void printCards(String label, Card[] cards, String[] choices) {
        StringBuilder heading = new StringBuilder(label);
        for (String choice : choices) {
            heading.append(heading.length() == label.length() ? " " : " | ").append(choice);
        }
        System.out.println(heading);

        ArrayList<String[]> renderedCards = new ArrayList<>();
        ArrayList<Integer> cardWidths = new ArrayList<>();
        int rowCount = 0;
        for (Card card : cards) {
            if (card == null) {
                renderedCards.add(new String[0]);
                cardWidths.add(0);
                continue;
            }
            String[] lines = card.toString().split("\n");
            int cardWidth = 0;
            for (String line : lines) {
                cardWidth = Math.max(cardWidth, line.length());
            }
            renderedCards.add(lines);
            cardWidths.add(cardWidth);
            rowCount = Math.max(rowCount, lines.length);
        }

        int firstRowCardCount = renderedCards.size() > 3 ? renderedCards.size() / 2 : renderedCards.size();
        printCardRow(renderedCards, cardWidths, 0, firstRowCardCount, rowCount);
        if (firstRowCardCount < renderedCards.size()) {
            System.out.println();
            printCardRow(renderedCards, cardWidths, firstRowCardCount, renderedCards.size(), rowCount);
        }
    }

    private void printCardRow(ArrayList<String[]> renderedCards, ArrayList<Integer> cardWidths,
            int startIndex, int endIndex, int rowCount) {
        for (int row = 0; row < rowCount; row++) {
            StringBuilder line = new StringBuilder();
            for (int cardIndex = startIndex; cardIndex < endIndex; cardIndex++) {
                if (cardIndex > startIndex) {
                    line.append("   ");
                }
                String[] cardLines = renderedCards.get(cardIndex);
                String cardLine = row < cardLines.length ? cardLines[row] : "";
                line.append(cardLine);
                for (int padding = cardLine.length(); padding < cardWidths.get(cardIndex); padding++) {
                    line.append(' ');
                }
            }
            System.out.println(line);
        }
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
