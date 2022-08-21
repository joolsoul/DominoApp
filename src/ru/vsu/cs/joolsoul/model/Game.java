package ru.vsu.cs.joolsoul.model;

import java.util.*;

public class Game {

    private List<Player> winner = new LinkedList<>();
    private boolean isInGame = true;
    private boolean isFish = false;
    private Queue<Player> playerQueue;

    private List<Domino> dominoBank = new LinkedList<>();
    private GameField gameField = new GameField();

    private Map<Player, List<Domino>> playersDomino = new LinkedHashMap<>();

    public Game(Queue<Player> playerQueue) {
        this.playerQueue = playerQueue;
    }


    public Map<Player, List<Domino>> getPlayersDomino() {
        return playersDomino;
    }

    public void setPlayersDomino(Map<Player, List<Domino>> playersDomino) {
        this.playersDomino = playersDomino;
    }

    public List<Player> getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner.add(winner);
    }

    public void setWinner(List<Player> winner) {
        this.winner = winner;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public void setNotInGame() {
        this.isInGame = false;
    }

    public List<Domino> getDominoBank() {
        return dominoBank;
    }

    public void setDominoBank(List<Domino> dominoBank) {
        this.dominoBank = dominoBank;
    }

    public Queue<Player> getPlayerQueue() {
        return playerQueue;
    }

    public void setPlayerQueue(Queue<Player> playerQueue) {
        this.playerQueue = playerQueue;
    }

    public GameField getGameField() {
        return gameField;
    }

    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }

    public boolean isFish() {
        return isFish;
    }

    public void setFish() {
        isFish = true;
    }
}