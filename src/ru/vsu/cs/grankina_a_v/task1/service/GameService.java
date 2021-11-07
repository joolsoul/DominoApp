package ru.vsu.cs.grankina_a_v.task1.service;

import ru.vsu.cs.grankina_a_v.task1.model.Domino;
import ru.vsu.cs.grankina_a_v.task1.model.Game;
import ru.vsu.cs.grankina_a_v.task1.model.Player;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GameService
{
    private final DominoService dominoService = new DominoService();
    private final GameFieldService gameFieldService = new GameFieldService();

    public GameService()
    {

    }

    public Game createGame(Queue<Player> playerQueue)
    {
        Game game = new Game(playerQueue);
        dominoService.initialStartDominoes(game);
        for (int i = 0; i < game.getPlayerQueue().size(); i++)
        {
            Player currentPlayer = game.getPlayerQueue().poll();
            game.getPlayersScore().put(currentPlayer, 0);
            game.getPlayerQueue().add(currentPlayer);
        }
        return game;
    }

    public boolean playGame(Game game)
    {
        return doStep(game);
    }

    private boolean doStep(Game game)
    {
        Player currentPlayer = game.getPlayerQueue().poll();
        game.getPlayerQueue().add(currentPlayer);
        List<Domino> playerDomino = game.getPlayersDomino().get(currentPlayer);

        if(dominoService.isContainsDominoWithEndValue(playerDomino, game.getGameField()))
        {
            Domino domino = dominoService.findDominoWithEndValue(playerDomino, game.getGameField());
            gameFieldService.addDominoOnField(domino, game.getGameField());
            playerDomino.remove(domino);
            return true;
        }

        return takeDominoFromBank(currentPlayer, game.getPlayersDomino(), game.getDominoBank());
    }


    private boolean takeDominoFromBank(Player player, Map<Player, List<Domino>> playersDomino, List<Domino> dominoBank)
    {
        if(!dominoBank.isEmpty()) {
            List<Domino> playerDomino = playersDomino.get(player);
            int index = getRandomIndexOfDomino(dominoBank);
            Domino domino = dominoBank.get(index);
            dominoService.addPlayerDomino(playerDomino, domino);
            dominoBank.remove(index);
            return true;
        }
        return false;
    }

    private int getRandomIndexOfDomino(List<Domino> dominoBank)
    {
        return getRandomInt(0, dominoBank.size() - 1);
    }

    private int getRandomInt(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
