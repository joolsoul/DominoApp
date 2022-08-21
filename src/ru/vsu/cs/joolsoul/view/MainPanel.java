package ru.vsu.cs.joolsoul.view;

import ru.vsu.cs.joolsoul.model.Domino;
import ru.vsu.cs.joolsoul.model.Game;
import ru.vsu.cs.joolsoul.model.GameField;
import ru.vsu.cs.joolsoul.model.Player;
import ru.vsu.cs.joolsoul.service.DominoService;
import ru.vsu.cs.joolsoul.service.GameService;

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
        playerQueue.add(new Player("Polina"));
        this.game = gameService.createGame(playerQueue);
        this.gameField = game.getGameField();
        this.playersList.addAll(game.getPlayerQueue());
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gr = (Graphics2D) g;

        paintBackground(gr);

        paintPlayersDominoes(gr);
        paintPlayersNames(gr);
        paintGameFieldDominoes(gr);

        if (!game.isInGame()) {
            if (game.isFish()) {
                drawFish(gr);
            } else {
                drawWinner(gr);
            }
        }
    }

    private void paintBackground(Graphics2D gr) {
        Color oldColor = gr.getColor();
        int height = this.getHeight() / 10;

        gr.setColor(new Color(0x5C8D92));
        gr.fillRect(0, 0, this.getWidth(), height);
        gr.fillRect(0, height * 9, this.getWidth(), height);

        gr.setColor(new Color(0xD2D1CA));
        gr.fillRect(0, height, this.getWidth(), height * 8);

        gr.setColor(oldColor);
    }

    private void paintPlayersDominoes(Graphics2D gr) {
        int y = 0;

        for (Map.Entry<Player, List<Domino>> entry : game.getPlayersDomino().entrySet()) {
            int startX = (this.getWidth() - (DOMINO_HEIGHT + INDENT) * entry.getValue().size()) / 2;
            for (int i = 0; i < entry.getValue().size(); i++, startX += (DOMINO_HEIGHT + INDENT)) {
                Domino currentDomino = entry.getValue().get(i);
                Image image = rotate(currentDomino.getImage(), 90);
                gr.drawImage(image, startX, y, DOMINO_HEIGHT, DOMINO_WIDTH, this);
            }
            y += this.getHeight() - DOMINO_WIDTH;
        }
    }

    private void paintPlayersNames(Graphics2D g) {
        int y = 117;

        for (Player currentPlayer : playersList) {
            int finalY = y;
            drawWithFont(g, new Font("Serif", Font.BOLD, (int) (0.015 * this.getWidth())), () -> {
                int x = 650;
                g.drawString("Player:", x, finalY);
                x += 80;
                g.drawString(currentPlayer.getName(), x, finalY);
            });
            y += 750;
        }
    }

    private Image rotate(Image currentImage, int angularToRotate) {
        BufferedImage image = (BufferedImage) currentImage;
        double rads = Math.toRadians(angularToRotate);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads, 0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image, rotatedImage);

        currentImage = rotatedImage;
        return currentImage;
    }

    private void paintGameFieldDominoes(Graphics2D g) {
        if (gameField.getFirstDomino() != null) {
            int firstX = this.getWidth() / 2;
            int firstY = this.getHeight() / 2 - DOMINO_HEIGHT / 2;
            int startRightX; // start X for dominoes to the right of the first
            int startLeftX; // start X for dominoes to the left of the first

            if (dominoService.isDouble(gameField.getFirstDomino())) {
                firstX -= DOMINO_HEIGHT / 2;
                startRightX = firstX + DOMINO_HEIGHT + INDENT;
            } else {
                firstX -= DOMINO_WIDTH / 2;
                startRightX = firstX + DOMINO_WIDTH + INDENT;
            }
            startLeftX = firstX;

            paintFirstDomino(g, firstX, firstY);

            if (calcSizeOfDominoes(gameField.getLeftOfTheFirstDominoes()) < firstX - (DOMINO_HEIGHT + INDENT)) {
                paintLeftOfTheFirstDominoes(g, startLeftX, firstY, gameField.getLeftOfTheFirstDominoes(), gameField.getFirstDomino());
            }
            if (calcSizeOfDominoes(gameField.getRightOfTheFirstDominoes()) < this.getWidth() - startRightX - (DOMINO_HEIGHT + INDENT)) {
                paintRightOfTheFirstDominoes(g, startRightX, firstY, gameField.getRightOfTheFirstDominoes(), gameField.getFirstDomino());
            }

            if (calcSizeOfDominoes(gameField.getLeftOfTheFirstDominoes()) > firstX - (DOMINO_HEIGHT + INDENT)) {
                paintLeftDownDominoes(g, firstX, startLeftX, firstY);
            }

            if (calcSizeOfDominoes(gameField.getRightOfTheFirstDominoes()) > this.getWidth() - startRightX - (DOMINO_HEIGHT + INDENT)) {
                paintRightUpDominoes(g, this.getWidth() - startRightX, startRightX, firstY);

            }
        }
    }

    private void paintFirstDomino(Graphics2D gr, int firstX, int firstY) {
        Image currentImage = gameField.getFirstDomino().getImage();
        gr.setColor(new Color(0xEC7225));
        if (!dominoService.isDouble(gameField.getFirstDomino())) {
            gr.drawImage(currentImage, firstX, firstY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
            gr.drawRect(firstX - 1, firstY - 1, DOMINO_WIDTH + 1, DOMINO_HEIGHT + 1);
        } else {
            currentImage = rotate(currentImage, 90);
            gr.drawImage(currentImage, firstX, firstY - DOMINO_HEIGHT / 2, DOMINO_HEIGHT, DOMINO_WIDTH, this);
            gr.drawRect(firstX - 1, (firstY - DOMINO_HEIGHT / 2) - 1, DOMINO_HEIGHT + 1, DOMINO_WIDTH + 1);
        }
    }

    private void paintRightUpDominoes(Graphics2D gr, int maxLength, int startRightX, int startRightY) {

        int i = calcIndexToPaint(maxLength, gameField.getRightOfTheFirstDominoes());
        List<Domino> allowableDominoes = createAllowableDominoesList(gameField.getRightOfTheFirstDominoes(), 0, i);
        paintRightOfTheFirstDominoes(gr, startRightX, startRightY, allowableDominoes, gameField.getFirstDomino());

        int startUpX = startRightX + calcSizeOfDominoes(allowableDominoes) - (DOMINO_HEIGHT + INDENT);
        int startUpY = startRightY - (DOMINO_WIDTH + INDENT);
        if (dominoService.isDouble(gameField.getRightOfTheFirstDominoes().get(i))) {
            startUpY -= DOMINO_HEIGHT / 2;
        }

        paintVerticalDominoes(gr, i, startUpX, startUpY, gameField.getRightOfTheFirstDominoes(), DOMINO_WIDTH + INDENT, DOMINO_HEIGHT);
        int startLeftUpX = startUpX - (DOMINO_WIDTH + INDENT);
        int startLeftUpY = startUpY - (DOMINO_WIDTH + INDENT);

        paintFirstHorizontalDomino(gr, i, startLeftUpX, startLeftUpY, DOMINO_WIDTH / 4, DOMINO_WIDTH / 2, gameField.getRightOfTheFirstDominoes());

        paintSecondLineDominoes(gr, i, startLeftUpX, startLeftUpY, DOMINO_WIDTH /4, DOMINO_HEIGHT, gameField.getRightOfTheFirstDominoes());
    }

    private void paintLeftDownDominoes(Graphics2D gr, int maxLength, int startLeftX, int startLeftY) {

        int i = calcIndexToPaint(maxLength, gameField.getLeftOfTheFirstDominoes());
        List<Domino> allowableDominoes = createAllowableDominoesList(gameField.getLeftOfTheFirstDominoes(), 0, i);
        paintLeftOfTheFirstDominoes(gr, startLeftX, startLeftY, allowableDominoes, gameField.getFirstDomino());

        int startDownX = startLeftX - calcSizeOfDominoes(allowableDominoes);
        int startDownY = startLeftY + (DOMINO_HEIGHT + INDENT);
        if (dominoService.isDouble(gameField.getLeftOfTheFirstDominoes().get(i))) {
            startDownY += DOMINO_HEIGHT / 2;
        }

        paintVerticalDominoes(gr, i, startDownX, startDownY, gameField.getLeftOfTheFirstDominoes(), -(DOMINO_WIDTH + INDENT), 0);

        int startRightDownX = startDownX + (DOMINO_HEIGHT + INDENT);
        int startRightDownY = startDownY + (DOMINO_HEIGHT + DOMINO_WIDTH + INDENT);

        paintFirstHorizontalDomino(gr, i, startRightDownX, startRightDownY, -DOMINO_WIDTH / 4, -DOMINO_WIDTH / 2, gameField.getLeftOfTheFirstDominoes());

        paintSecondLineDominoes(gr, i, startRightDownX + (DOMINO_WIDTH + INDENT), startRightDownY, - DOMINO_WIDTH / 4, -DOMINO_HEIGHT, gameField.getLeftOfTheFirstDominoes());
    }

    private void paintFirstHorizontalDomino(Graphics2D gr, int i, int startX, int startY, int xDelta, int yDelta, List<Domino> dominoList){
        if (i + 3 < dominoList.size()) {

            if (dominoService.isDouble(dominoList.get(i + 2))) {
                startX -= xDelta;
                startY += yDelta;
            }
            Domino currentDomino = dominoList.get(i + 3);
            Image currentImage = currentDomino.getImage();
            if(dominoList.equals(gameField.getRightOfTheFirstDominoes())) {
                if (checkRightEquals(currentDomino, gameField.getRightOfTheFirstDominoes().get(i + 2))) {
                    currentImage = rotate(currentImage, 180);
                    currentDomino.setRotated();
                }
            }
            if(dominoList.equals(gameField.getLeftOfTheFirstDominoes())) {
                if (checkLeftEquals(currentDomino, gameField.getLeftOfTheFirstDominoes().get(i + 2))) {
                    currentImage = rotate(currentImage, 180);
                    currentDomino.setRotated();
                }
            }
            gr.drawImage(currentImage, startX, startY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
        }
    }

    private void paintSecondLineDominoes(Graphics2D gr, int i, int startX, int startY, int xDelta, int yDelta, List<Domino> dominoList) {
        if (i + 4 < dominoList.size()) {
            List<Domino> allowableLeftUpDominoes = createAllowableDominoesList(dominoList, i + 4, dominoList.size() - 1);
            if (dominoService.isDouble(dominoList.get(i + 2))) {
                startX -= xDelta;
                startY += yDelta;
            }
            if(dominoList.equals(gameField.getLeftOfTheFirstDominoes())) {
                paintRightOfTheFirstDominoes(gr, startX, startY, allowableLeftUpDominoes, dominoList.get(i + 3));
            }
            if(dominoList.equals(gameField.getRightOfTheFirstDominoes())) {
                paintLeftOfTheFirstDominoes(gr, startX, startY, allowableLeftUpDominoes, dominoList.get(i + 3));
            }
        }
    }

    private void paintVerticalDominoes(Graphics2D g, int i, int startX, int startY, List<Domino> dominoList, int xDelta, int yDelta) {
        int currentX = startX;
        int currentY = startY;
        if (i + 1 < dominoList.size()) {
            Domino currentDomino = dominoList.get(i + 1);
            Image currentImage = currentDomino.getImage();
            if (dominoList.equals(gameField.getRightOfTheFirstDominoes())) {
                if (checkRightEquals(currentDomino, dominoList.get(i))) {
                    currentImage = rotate(currentImage, 270);
                } else {
                    currentImage = rotate(currentImage, 90);
                    currentDomino.setRotated();
                }
            }
            if (dominoList.equals(gameField.getLeftOfTheFirstDominoes())) {
                if (checkLeftEquals(currentDomino, dominoList.get(i))) {
                    currentImage = rotate(currentImage, 270);
                } else {
                    currentImage = rotate(currentImage, 90);
                    currentDomino.setRotated();
                }
            }
            g.drawImage(currentImage, currentX, currentY, DOMINO_HEIGHT, DOMINO_WIDTH, this);
            currentY -= xDelta;
        }

        if (i + 2 < dominoList.size()) {
            Domino currentDomino = dominoList.get(i + 2);
            Image currentImage = currentDomino.getImage();

            if (dominoService.isDouble(currentDomino)) {
                currentY += yDelta;
                currentX -= DOMINO_HEIGHT / 2;
                g.drawImage(currentImage, currentX, currentY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
            } else {
                if (dominoList.equals(gameField.getRightOfTheFirstDominoes())) {
                    if (checkRightEquals(currentDomino, dominoList.get(i + 1))) {
                        currentImage = rotate(currentImage, 270);
                    } else {
                        currentImage = rotate(currentImage, 90);
                        currentDomino.setRotated();
                    }
                }
                if (dominoList.equals(gameField.getLeftOfTheFirstDominoes())) {
                    if (checkLeftEquals(currentDomino, dominoList.get(i + 1))) {
                        currentImage = rotate(currentImage, 270);
                    } else {
                        currentImage = rotate(currentImage, 90);
                        currentDomino.setRotated();
                    }
                }
                g.drawImage(currentImage, currentX, currentY, DOMINO_HEIGHT, DOMINO_WIDTH, this);
            }

        }
    }

    private List<Domino> createAllowableDominoesList(List<Domino> sourceList, int n1, int n2) {
        List<Domino> allowableDominoes = new LinkedList<>();

        for (int j = n1; j <= n2; j++) {
            allowableDominoes.add(sourceList.get(j));
        }

        return allowableDominoes;
    }

    private int calcIndexToPaint(int maxLength, List<Domino> dominoList) {
        int length = 0;
        int n = -1;
        int currentIndex = 0;

        while (length < maxLength - (DOMINO_WIDTH + INDENT) && currentIndex < dominoList.size()) {
            Domino currentDomino = dominoList.get(currentIndex);

            if (currentDomino.getFirstValue().equals(currentDomino.getSecondValue())) {
                length += 45;
            } else {
                length += 87;
            }
            n++;
            currentIndex++;
        }
        return n;
    }

    private int calcSizeOfDominoes(List<Domino> dominoList) {
        int sizeOfDominoes = 0;

        if (gameField.getFirstDomino() != null) {
            for (Domino domino : dominoList) {
                if (domino.getFirstValue().equals(domino.getSecondValue())) {
                    sizeOfDominoes += 45;
                } else
                    sizeOfDominoes += 87;
            }
        }

        return sizeOfDominoes;
    }

    private void paintRightOfTheFirstDominoes(Graphics2D g, int startRightX, int startRightY, List<Domino> dominoList, Domino firstDomino) {
        for (int i = 0; i < dominoList.size(); i++) {
            Domino currentDomino = dominoList.get(i);
            Domino prevDomino;

            if (i == 0) {
                prevDomino = firstDomino;
            } else {
                prevDomino = dominoList.get(i - 1);
            }

            if (dominoService.isDouble(currentDomino)) {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 90);
                g.drawImage(currentImage, startRightX, startRightY - DOMINO_HEIGHT / 2, DOMINO_HEIGHT, DOMINO_WIDTH, this);
                startRightX += DOMINO_HEIGHT + INDENT;
                continue;
            }
            if (checkRightEquals(currentDomino, prevDomino)) {
                Image currentImage = currentDomino.getImage();
                g.drawImage(currentImage, startRightX, startRightY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startRightX += DOMINO_WIDTH + INDENT;
            } else {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 180);
                currentDomino.setRotated();
                g.drawImage(currentImage, startRightX, startRightY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startRightX += DOMINO_WIDTH + INDENT;
            }
        }
    }

    private void paintLeftOfTheFirstDominoes(Graphics2D g, int startLeftX, int startLeftY, List<Domino> dominoList, Domino firstDomino) {
        for (int i = 0; i < dominoList.size(); i++) {
            Domino currentDomino = dominoList.get(i);
            Domino prevDomino;

            if (i == 0) {
                prevDomino = firstDomino;
            } else {
                prevDomino = dominoList.get(i - 1);
            }

            if (dominoService.isDouble(currentDomino)) {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 90);
                g.drawImage(currentImage, startLeftX - (DOMINO_HEIGHT + INDENT), startLeftY - DOMINO_HEIGHT / 2, DOMINO_HEIGHT, DOMINO_WIDTH, this);
                startLeftX += - DOMINO_HEIGHT - INDENT;
                continue;
            }
            if (checkLeftEquals(currentDomino, prevDomino)) {
                Image currentImage = currentDomino.getImage();
                g.drawImage(currentImage, startLeftX - (DOMINO_WIDTH + INDENT), startLeftY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startLeftX += - DOMINO_WIDTH - INDENT;
            } else {
                Image currentImage = currentDomino.getImage();
                currentImage = rotate(currentImage, 180);
                currentDomino.setRotated();
                g.drawImage(currentImage, startLeftX - (DOMINO_WIDTH + INDENT), startLeftY, DOMINO_WIDTH, DOMINO_HEIGHT, this);
                startLeftX += - DOMINO_WIDTH - INDENT;
            }
        }
    }

    private boolean checkLeftEquals(Domino currentDomino, Domino prevDomino) {
        return currentDomino.getSecondValue().equals(prevDomino.getFirstValue()) && !prevDomino.isRotated() ||
                currentDomino.getSecondValue().equals(prevDomino.getSecondValue()) && prevDomino.isRotated();
    }

    private boolean checkRightEquals(Domino currentDomino, Domino prevDomino) {
        return currentDomino.getFirstValue().equals(prevDomino.getSecondValue()) && !prevDomino.isRotated() ||
                currentDomino.getFirstValue().equals(prevDomino.getFirstValue()) && prevDomino.isRotated();
    }

    private void drawWinner(Graphics2D gr) {
        drawWithFont(gr, new Font("Serif", Font.BOLD, 70), new Color(0x00526E), () ->
        {
            gr.drawString("Winner", (int) (this.getWidth() / 3), (int) (this.getHeight() / 3));
        });

        drawWithFont(gr, new Font("Serif", Font.BOLD, 50), new Color(0x0093C5), () ->
        {
            int x = (int) (this.getWidth() / 2);
            for (Player player : game.getWinner()) {
                gr.drawString(player.getName(), x, (int) (this.getHeight() / 3));
                x += 150;
            }
        });
    }
    private void drawFish(Graphics2D gr) {
        drawWithFont(gr, new Font("Serif", Font.BOLD, 90), new Color(0x00526E), () ->
        {
            gr.drawString("Fish", (int) (this.getWidth() / 2.3), this.getHeight() / 2);
        });
        drawWithFont(gr, new Font("Serif", Font.BOLD, 50), new Color(0x00526E), () ->
        {
            int x = (int) (this.getWidth() / 2.3);
            for (Player player : game.getWinner()) {
                gr.drawString(player.getName(), x, (int) (this.getHeight() / 1.8));
                x += 150;
            }
        });
    }

    private void drawWithFont(Graphics2D g, Font font, Runnable drawAction) {
        Font oldF = g.getFont();
        g.setFont(font);
        drawAction.run();
        g.setFont(oldF);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.isInGame()) {
            gameService.playGame(game);
            repaint();
        }
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
