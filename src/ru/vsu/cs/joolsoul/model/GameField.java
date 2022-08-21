package ru.vsu.cs.joolsoul.model;

import java.util.LinkedList;
import java.util.List;

public class GameField {

    private final DominoesOnGameField dominoesOnGameField = new DominoesOnGameField();
    private List<DominoValue> endDominoesOfTheGame = new LinkedList<>();

    public GameField() {
    }

    public List<Domino> getDominoesOnGameField() {
        return dominoesOnGameField.getDominoesOnGameField();
    }

    public Domino getFirstDomino() {
        return dominoesOnGameField.getFirstDomino();
    }

    public void setFirstDomino(Domino firstDomino) {
        this.dominoesOnGameField.setFirstDomino(firstDomino);
    }

    public List<Domino> getLeftOfTheFirstDominoes() {
        return dominoesOnGameField.getLeftOfTheFirstDominoes();
    }

    public void setLeftOfTheFirstDominoes(List<Domino> leftOfTheFirstDominoes) {
        this.dominoesOnGameField.setLeftOfTheFirstDominoes(leftOfTheFirstDominoes);
    }

    public List<Domino> getRightOfTheFirstDominoes() {
        return dominoesOnGameField.getRightOfTheFirstDominoes();
    }

    public void setRightOfTheFirstDominoes(List<Domino> rightOfTheFirstDominoes) {
        this.dominoesOnGameField.setRightOfTheFirstDominoes(rightOfTheFirstDominoes);
    }

    public DominoValue getLeftEndOfTheGame() {
        if (endDominoesOfTheGame.isEmpty())
            return null;
        return endDominoesOfTheGame.get(0);
    }

    public void setLeftEndOfTheGame(DominoValue leftEndOfTheGame) {
        if (!endDominoesOfTheGame.isEmpty()) {
            this.endDominoesOfTheGame.remove(0);
        }
        this.endDominoesOfTheGame.add(0, leftEndOfTheGame);
    }

    public DominoValue getRightEndOfTheGame() {
        if (endDominoesOfTheGame.isEmpty())
            return null;
        return endDominoesOfTheGame.get(1);
    }

    public void setRightEndOfTheGame(DominoValue rightEndOfTheGame) {
        if (endDominoesOfTheGame.size() > 1) {
            this.endDominoesOfTheGame.remove(1);
        }
        this.endDominoesOfTheGame.add(1, rightEndOfTheGame);
    }

    public List<DominoValue> getEndDominoesOfTheGame() {
        return endDominoesOfTheGame;
    }

    public void setEndDominoesOfTheGame(List<DominoValue> endDominoesOfTheGame) {
        this.endDominoesOfTheGame = endDominoesOfTheGame;
    }


}
