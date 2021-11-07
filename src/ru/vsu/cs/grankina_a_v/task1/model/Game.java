package ru.vsu.cs.grankina_a_v.task1.model;

import java.util.*;

public class Game {

    private Player winner;
    private boolean isInGame = true;
    private Queue<Player> playerQueue;

    private List<Domino> dominoBank = new LinkedList<>();
    private GameField gameField = new GameField();

    private Map<Player, List<Domino>> playersDomino = new LinkedHashMap<>();
    private Map<Player, Integer> playersScore = new LinkedHashMap<>();

    public Game(Queue<Player> playerQueue) {
        this.playerQueue = playerQueue;
    }


    public Map<Player, List<Domino>> getPlayersDomino()
    {
        return playersDomino;
    }

    public void setPlayersDomino(Map<Player, List<Domino>> playersDomino)
    {
        this.playersDomino = playersDomino;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isInGame() {
        return isInGame;
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

    public Queue<Player> getPlayerQueue()
    {
        return playerQueue;
    }

    public void setPlayerQueue(Queue<Player> playerQueue)
    {
        this.playerQueue = playerQueue;
    }

    public GameField getGameField()
    {
        return gameField;
    }

    public void setGameField(GameField gameField)
    {
        this.gameField = gameField;
    }

    public Map<Player, Integer> getPlayersScore() {
        return playersScore;
    }

    public void setPlayersScore(Map<Player, Integer> playersScore) {
        this.playersScore = playersScore;
    }
}