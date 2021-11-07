package ru.vsu.cs.grankina_a_v.task1.service;


import ru.vsu.cs.grankina_a_v.task1.model.Domino;
import ru.vsu.cs.grankina_a_v.task1.model.DominoValue;
import ru.vsu.cs.grankina_a_v.task1.model.GameField;

import java.util.List;

public class GameFieldService {


    public void addDominoOnField(Domino domino, GameField gameField)
    {
        if(gameField.getLeftEndOfTheGame() == null && gameField.getRightEndOfTheGame() == null)
        {
            gameField.setFirstDomino(domino);
            setEndDominoes(domino, gameField);
            return;
        }

        if(domino.getFirstValue().equals(gameField.getLeftEndOfTheGame()) || domino.getSecondValue().equals(gameField.getLeftEndOfTheGame())) {
            gameField.getLeftOfTheFirstDominoes().add(domino);
            setEndDominoes(domino, gameField.getLeftEndOfTheGame(), gameField);
            return;
        }
        if(domino.getFirstValue().equals(gameField.getRightEndOfTheGame()) || domino.getSecondValue().equals(gameField.getRightEndOfTheGame())){
            gameField.getRightOfTheFirstDominoes().add(domino);
            setEndDominoes(domino, gameField.getRightEndOfTheGame(), gameField);
        }
    }

    private void setEndDominoes(Domino domino, DominoValue necessaryEnd, GameField gameField)
    {
        if(necessaryEnd.equals(gameField.getLeftEndOfTheGame()))
        {
            if(gameField.getLeftEndOfTheGame().equals(domino.getFirstValue()))
            {
                gameField.setLeftEndOfTheGame(domino.getSecondValue());
            }
            else if(gameField.getLeftEndOfTheGame().equals(domino.getSecondValue()))
            {
                gameField.setLeftEndOfTheGame(domino.getFirstValue());
            }
            return;
        }
        if(necessaryEnd.equals(gameField.getRightEndOfTheGame()))
        {
            if(gameField.getRightEndOfTheGame().equals(domino.getFirstValue()))
            {
                gameField.setRightEndOfTheGame(domino.getSecondValue());
            }
            else if(gameField.getRightEndOfTheGame().equals(domino.getSecondValue()))
            {
                gameField.setRightEndOfTheGame(domino.getFirstValue());
            }
        }
    }

    private void setEndDominoes(Domino domino, GameField gameField)
    {
        gameField.setLeftEndOfTheGame(domino.getFirstValue());
        gameField.setRightEndOfTheGame(domino.getSecondValue());
    }
}
