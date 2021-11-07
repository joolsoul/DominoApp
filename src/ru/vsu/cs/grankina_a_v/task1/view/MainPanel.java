package ru.vsu.cs.grankina_a_v.task1.view;

import ru.vsu.cs.grankina_a_v.task1.model.Domino;
import ru.vsu.cs.grankina_a_v.task1.model.Game;
import ru.vsu.cs.grankina_a_v.task1.model.GameField;
import ru.vsu.cs.grankina_a_v.task1.model.Player;
import ru.vsu.cs.grankina_a_v.task1.service.DominoService;
import ru.vsu.cs.grankina_a_v.task1.service.GameService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainPanel extends JPanel implements MouseListener {

    private final Game game;
    private final GameField gameField;
    private final GameService gameService = new GameService();
    private final DominoService dominoService = new DominoService();
    private final List<Player> playersList = new LinkedList<>();

    private final int DOMINO_HEIGHT = 42;
    private final int DOMINO_WIDTH = 84;
    private final int INDENT = 3;

    public MainPanel() {
        Queue<Player> playerQueue = new LinkedList<>();
        playerQueue.add(new Player("Ivan"));
        playerQueue.add(new Player("Yaroslav"));
        this.game = gameService.createGame(playerQueue);
        this.gameField = game.getGameField();
        this.playersList.addAll(game.getPlayerQueue());
        addMouseListener(this);

        this.setBackground(new Color(0xE47142));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gr = (Graphics2D) g;

        paintBackground(gr);

        paintPlayersDominoes(gr);
        paintPlayersInformation(gr);
        paintGameFieldDominoes(gr);

    }

    private void paintBackground(Graphics2D gr)
    {
        Color oldColor = gr.getColor();
        int height = this.getHeight() / 10;

        gr.setColor(new Color(0x5C8D92));
        gr.fillRect(0, 0, this.getWidth(), height);
        gr.fillRect(0, height * 9, this.getWidth(), height);

        gr.setColor(new Color(0xD2D1CA));
        gr.fillRect(0, height, this.getWidth(), height * 8);

        gr.setColor(oldColor);
    }

    private void paintPlayersDominoes(Graphics2D gr)
    {
        int y = 0;

        for(Map.Entry<Player, List<Domino>> entry : game.getPlayersDomino().entrySet())
        {
           int startX = (this.getWidth() - (DOMINO_HEIGHT + INDENT) * entry.getValue().size()) / 2;
           for(int i = 0; i < entry.getValue().size(); i++, startX += (DOMINO_HEIGHT + INDENT))
           {
               Domino currentDomino = entry.getValue().get(i);
               Image image = rotate(currentDomino.getImage(), 90);
               gr.drawImage(image, startX, y, DOMINO_HEIGHT, DOMINO_WIDTH, this);
           }

           y += this.getHeight() - DOMINO_WIDTH;
        }
    }

    private void paintPlayersInformation(Graphics2D g)
    {
        int y = 117;

        for (Player currentPlayer : playersList) {
            int finalY = y;
            drawWithFont(g, new Font("Serif", Font.BOLD, (int) (0.015 * this.getWidth())), () -> {
                int x = 550;
                g.drawString("Player:", x, finalY);
                x += 80;
                g.drawString(currentPlayer.getName(), x, finalY);
                x += 200;
                g.drawString("Score:", x, finalY);
                x += 80;
                g.drawString(String.valueOf(game.getPlayersScore().get(currentPlayer)), x, finalY);
            });
            y += 750;
        }
    }

    private Image rotate(Image currentImage, int angularToRotate)
    {
        BufferedImage image = (BufferedImage) currentImage;
         double rads = Math.toRadians(angularToRotate);
         double sin = Math.abs(Math.sin(rads));
         double cos = Math.abs(Math.cos(rads));
         int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
         int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
         BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
         AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image,rotatedImage);

        currentImage = rotatedImage;
        return currentImage;
    }

    private void paintGameFieldDominoes(Graphics2D g) {
            if(gameField.getFirstDomino() != null)
            {
                if (calcSizeOfDominoes() < this.getWidth() - DOMINO_WIDTH + INDENT) {
                    int firstX = this.getWidth() / 2;
                    int firstY = this.getHeight() / 2 - DOMINO_HEIGHT / 2;

                    if(dominoService.isDouble(gameField.getFirstDomino()))
                    {
                        firstX -= DOMINO_HEIGHT / 2;
                    }
                    else
                    {
                        firstX -= DOMINO_WIDTH / 2;
                    }
                int startRightX;
                int startLeftX;

                if(!dominoService.isDouble(gameField.getFirstDomino())) {
                    Image currentImage = gameField.getFirstDomino().getImage();
                    g.drawImage(currentImage, firstX, firstY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                    g.setColor(new Color(0xEC7225));
                    g.drawRect(firstX - 1, firstY - 1, DOMINO_WIDTH + 1, DOMINO_HEIGHT + 1);

                    startRightX = firstX + DOMINO_WIDTH + INDENT;
                    startLeftX = firstX;
                }
                else
                {
                    Image currentImage = gameField.getFirstDomino().getImage();
                    currentImage = rotate(currentImage, 90);
                    g.drawImage(currentImage, firstX, firstY - DOMINO_HEIGHT / 2, DOMINO_HEIGHT, DOMINO_WIDTH, this);
                    g.setColor(new Color(0xEC7225));
                    g.drawRect(firstX - 1, (firstY - DOMINO_HEIGHT / 2) - 1, DOMINO_HEIGHT + 1, DOMINO_WIDTH + 1);

                    startRightX = firstX + DOMINO_HEIGHT + INDENT;
                    startLeftX = firstX;
                }

                paintRightOfTheFirstDominoes(g, startRightX, firstY, gameField.getRightOfTheFirstDominoes());
                paintLeftOfTheFirstDominoes(g, startLeftX, firstY, gameField.getLeftOfTheFirstDominoes());
            }

        }
    }

    private void paintRightOfTheFirstDominoes(Graphics2D g, int startRightX, int startRightY,  List<Domino> dominoList) {
        for (int i = 0; i < dominoList.size(); i++) {
            Domino currentDomino = dominoList.get(i);
            Domino prevDomino;

            if (i == 0) {
                prevDomino = gameField.getFirstDomino();
            }
            else {
                prevDomino = dominoList.get(i - 1);
            }

            if (dominoService.isDouble(currentDomino)) {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 90);
                g.drawImage(currentImage, startRightX , startRightY - DOMINO_HEIGHT / 2, DOMINO_HEIGHT, DOMINO_WIDTH, this);
                startRightX += DOMINO_HEIGHT + INDENT;
                continue;
            }
            if (currentDomino.getFirstValue().equals(prevDomino.getSecondValue()) && !prevDomino.isRotated() || currentDomino.getFirstValue().equals(prevDomino.getFirstValue()) && prevDomino.isRotated()) {
                Image currentImage = currentDomino.getImage();
                g.drawImage(currentImage, startRightX , startRightY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startRightX += DOMINO_WIDTH + INDENT;
            } else {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 180);
                currentDomino.setRotated();
                g.drawImage(currentImage, startRightX , startRightY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startRightX += DOMINO_WIDTH + INDENT;
            }
        }
    }

    private void paintLeftOfTheFirstDominoes(Graphics2D g, int startLeftX, int startLeftY, List<Domino> dominoList) {
        for (int i = 0; i < dominoList.size(); i++) {
            Domino currentDomino = dominoList.get(i);
            Domino prevDomino;

            if (i == 0) {
                prevDomino = gameField.getFirstDomino();
            }
            else {
                prevDomino = dominoList.get(i - 1);
            }

            if (dominoService.isDouble(currentDomino)) {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 90);
                g.drawImage(currentImage, startLeftX - (DOMINO_HEIGHT + INDENT), startLeftY - DOMINO_HEIGHT / 2, DOMINO_HEIGHT, DOMINO_WIDTH, this);
                startLeftX = startLeftX - DOMINO_HEIGHT - INDENT;
                continue;
            }
            if (currentDomino.getSecondValue().equals(prevDomino.getFirstValue()) && !prevDomino.isRotated() || currentDomino.getSecondValue().equals(prevDomino.getSecondValue()) && prevDomino.isRotated()) {
                Image currentImage = currentDomino.getImage();
                g.drawImage(currentImage, startLeftX - (DOMINO_WIDTH + INDENT), startLeftY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startLeftX = startLeftX - DOMINO_WIDTH - INDENT;
            }
                else {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 180);
                currentDomino.setRotated();
                g.drawImage(currentImage, startLeftX - (DOMINO_WIDTH + INDENT), startLeftY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startLeftX = startLeftX - DOMINO_WIDTH - INDENT;
            }
        }
    }

    private void drawGameOver(Graphics2D gr) {
        drawWithFont(gr, new Font("Serif", Font.BOLD, 50), new Color(0x0A3A16), () ->
        {
            gr.drawString("Winner", this.getWidth() / 3, this.getHeight() / 2);
        });
        drawWithFont(gr, new Font("Serif", Font.BOLD, 30), new Color(0x0A3A16), () ->
        {
            gr.drawString(game.getWinner().getName(), (int) (this.getWidth() / 2.4), (int) (this.getHeight() / 1.8));
        });
    }

    private void drawWithFont(Graphics2D g, Font font, Color color, Runnable drawAction) {
        Font oldF = g.getFont();
        Color oldC = g.getColor();
        g.setFont(font);
        g.setColor(color);
        drawAction.run();
        g.setFont(oldF);
        g.setColor(oldC);
    }

    private int calcSizeOfDominoes()
    {
        int sizeOfDominoes = 0;

        if(gameField.getFirstDomino() != null) {
            for (Domino domino : gameField.getDominoesOnGameField()) {
                if (domino.getFirstValue().equals(domino.getSecondValue())) {
                    sizeOfDominoes += 45;
                } else
                    sizeOfDominoes += 87;
            }
        }

        return sizeOfDominoes;
    }


    private void drawWithFont(Graphics2D g, Font font, Runnable drawAction) {
        Font oldF = g.getFont();
        g.setFont(font);
        drawAction.run();
        g.setFont(oldF);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(gameService.playGame(game))
            repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
