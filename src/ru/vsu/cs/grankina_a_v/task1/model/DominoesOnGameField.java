package ru.vsu.cs.grankina_a_v.task1.model;

import java.util.LinkedList;
import java.util.List;

public class DominoesOnGameField {

    private Domino firstDomino;
    private List<Domino> leftOfTheFirstDominoes = new LinkedList<>();
    private List<Domino> rightOfTheFirstDominoes = new LinkedList<>();

    public DominoesOnGameField() {
    }

    public Domino getFirstDomino() {
        return firstDomino;
    }

    public void setFirstDomino(Domino firstDomino) {
        this.firstDomino = firstDomino;
    }

    public List<Domino> getDominoesOnGameField()
    {
        List<Domino> getDominoesOnGameField = new LinkedList<>(leftOfTheFirstDominoes);
        getDominoesOnGameField.add(firstDomino);
        getDominoesOnGameField.addAll(rightOfTheFirstDominoes);

        return getDominoesOnGameField;
    }

    public List<Domino> getLeftOfTheFirstDominoes() {
        return leftOfTheFirstDominoes;
    }

    public void setLeftOfTheFirstDominoes(List<Domino> leftOfTheFirstDominoes) {
        this.leftOfTheFirstDominoes = leftOfTheFirstDominoes;
    }

    public List<Domino> getRightOfTheFirstDominoes() {
        return rightOfTheFirstDominoes;
    }

    public void setRightOfTheFirstDominoes(List<Domino> rightOfTheFirstDominoes) {
        this.rightOfTheFirstDominoes = rightOfTheFirstDominoes;
    }
}
