package ru.vsu.cs.joolsoul.service;

import ru.vsu.cs.joolsoul.model.Domino;
import ru.vsu.cs.joolsoul.model.Game;
import ru.vsu.cs.joolsoul.model.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GameService {

    private final DominoService dominoService = new DominoService();
    private final GameFieldService gameFieldService = new GameFieldService();

    public GameService() {

    }

    public Game createGame(Queue<Player> playerQueue) {
        Game game = new Game(playerQueue);
        dominoService.initialStartDominoes(game);
        return game;
    }

    public boolean playGame(Game game) {
        return doStep(game);
    }

    private boolean doStep(Game game) {
        Player currentPlayer = game.getPlayerQueue().poll();
        game.getPlayerQueue().add(currentPlayer);
        List<Domino> playerDomino = game.getPlayersDomino().get(currentPlayer);
        if (gameFieldService.isFish(game.getGameField())) {
            game.setFish();
            game.setWinner(getWinnerAtFish(game.getPlayersDomino(), game.getPlayerQueue()));
            game.setNotInGame();
            return false;
        }
        if (dominoService.isContainsDominoWithEndValue(playerDomino, game.getGameField())) {
            Domino domino = dominoService.findDominoWithEndValue(playerDomino, game.getGameField());
            gameFieldService.addDominoOnField(domino, game.getGameField());
            playerDomino.remove(domino);
            if (playerDomino.size() == 0) {
                game.setWinner(currentPlayer);
                game.setNotInGame();
                return false;
            }
            return true;
        }

        return takeDominoFromBank(currentPlayer, game.getPlayersDomino(), game.getDominoBank());
    }

    private List<Player> getWinnerAtFish(Map<Player, List<Domino>> playersDomino, Queue<Player> playerQueue) {

        List<Player> winnersList = new LinkedList<>();
        Player firstPlayer = playerQueue.poll();
        playerQueue.add(firstPlayer);
        Player secondPlayer = playerQueue.poll();
        playerQueue.add(secondPlayer);

        int firstPlayerScore = 0;
        int secondPlayerScore = 0;

        for (Domino domino : playersDomino.get(firstPlayer)) {
            firstPlayerScore += dominoService.getSumValue(domino);
        }
        for (Domino domino : playersDomino.get(secondPlayer)) {
            secondPlayerScore += dominoService.getSumValue(domino);
        }
        if (firstPlayerScore > secondPlayerScore) {
            winnersList.add(secondPlayer);
        }
        if (firstPlayerScore < secondPlayerScore) {
            winnersList.add(firstPlayer);
        }
        if (firstPlayerScore == secondPlayerScore) {
            winnersList.add(firstPlayer);
            winnersList.add(secondPlayer);
        }
        return winnersList;
    }


    private boolean takeDominoFromBank(Player player, Map<Player, List<Domino>> playersDomino, List<Domino> dominoBank) {
        if (!dominoBank.isEmpty()) {
            List<Domino> playerDomino = playersDomino.get(player);
            int index = getRandomIndexOfDomino(dominoBank);
            Domino domino = dominoBank.get(index);
            dominoService.addPlayerDomino(playerDomino, domino);
            dominoBank.remove(index);
            return true;
        }
        return false;
    }

    private int getRandomIndexOfDomino(List<Domino> dominoBank) {
        return getRandomInt(0, dominoBank.size() - 1);
    }

    private int getRandomInt(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
