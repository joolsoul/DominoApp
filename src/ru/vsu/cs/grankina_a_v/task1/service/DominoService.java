package ru.vsu.cs.grankina_a_v.task1.service;

import ru.vsu.cs.grankina_a_v.task1.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class DominoService {

    public boolean isDouble(Domino domino) {
        return domino.getFirstValue().equals(domino.getSecondValue());
    }

    public void addPlayerDomino(List<Domino> playerDomino, Domino domino) {
        if (playerDomino.isEmpty()) {
            playerDomino.add(domino);
            return;
        }

        for (int i = 0; i < playerDomino.size(); i++) {
            if (getSumValue(domino) > getSumValue(playerDomino.get(i))) {
                playerDomino.add(i, domino);
                return;
            }
        }
        playerDomino.add(domino);
    }

    public boolean isContainsDominoWithEndValue(List<Domino> playerDomino, GameField gameField)
    {
        if(gameField.getLeftEndOfTheGame() == null && gameField.getRightEndOfTheGame() == null)
        {
            return true;
        }
        for (Domino currentDomino : playerDomino) {
            if (currentDomino.getFirstValue().equals(gameField.getLeftEndOfTheGame()) || currentDomino.getSecondValue().equals(gameField.getLeftEndOfTheGame())) {
                return true;
            }
            if (currentDomino.getFirstValue().equals(gameField.getRightEndOfTheGame()) || currentDomino.getSecondValue().equals(gameField.getRightEndOfTheGame())) {
                return true;
            }
        }
        return false;
    }

    public Domino findDominoWithEndValue(List<Domino> playerDomino, GameField gameField)
    {
        if(gameField.getLeftEndOfTheGame() != null && gameField.getRightEndOfTheGame() != null)
        {
            for (int i = 0; i < playerDomino.size(); i++)
            {
                Domino currentDomino = playerDomino.get(i);
                    if (currentDomino.getFirstValue().equals(gameField.getLeftEndOfTheGame()) || currentDomino.getSecondValue().equals(gameField.getLeftEndOfTheGame()))
                    {
                        return playerDomino.get(i);
                    }
                    if (currentDomino.getFirstValue().equals(gameField.getRightEndOfTheGame()) || currentDomino.getSecondValue().equals(gameField.getRightEndOfTheGame()))
                    {
                        return playerDomino.get(i);
                    }
            }
        }
        return playerDomino.get(0);
    }

    public int getSumValue(Domino domino)
    {
        return domino.getFirstValue().ordinal() + domino.getSecondValue().ordinal();
    }

    public void initialStartDominoes(Game game)
    {
        try {
            createDominoesBank(game.getDominoBank());
        } catch (IOException e) {
            e.printStackTrace();
        }
        distributePlayersDominoes(game.getPlayerQueue(), game.getPlayersDomino(), game.getDominoBank());
    }

    private void createDominoesBank(List<Domino> dominoBank) throws IOException {


        for(DominoValue firstValue : DominoValue.values())
        {
            Image firstImage = ImageIO.read(new File("resources/dominoes/" + firstValue.ordinal() + ".png"));

            for(DominoValue secondValue : DominoValue.values())
            {
                BufferedImage dominoImage = ImageIO.read(new File("resources/dominoes/dominoImage.png"));
                Image secondImage = ImageIO.read(new File("resources/dominoes/" + secondValue.ordinal() + ".png"));
                dominoImage.getGraphics().drawImage(firstImage, 1, 1, 40, 40, null);
                dominoImage.getGraphics().drawImage(secondImage, 43, 1, 40, 40, null);

                List<DominoValue> currentDominoValues = new LinkedList<>();
                currentDominoValues.add(firstValue);
                currentDominoValues.add(secondValue);
                dominoBank.add(new Domino(currentDominoValues, dominoImage));
            }
        }

        Collections.shuffle(dominoBank);
    }

    private void distributePlayersDominoes(Queue<Player> playerQueue, Map<Player, List<Domino>> playersDomino, List<Domino> dominoBank)
    {
        for(int i = 0; i < playerQueue.size(); i++)
        {
            Player currentPlayer = playerQueue.poll();
            List<Domino> currentDomino = new LinkedList<>();
            for (int j = 0; j < 7; j++)
            {
                currentDomino.add(dominoBank.get(0));
                dominoBank.remove(0);
            }
            playersDomino.put(currentPlayer, currentDomino);
            playerQueue.add(currentPlayer);
        }
    }

}
