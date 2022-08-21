package ru.vsu.cs.joolsoul.model;

import java.awt.*;
import java.util.List;

public class Domino {

    private final List<DominoValue> dominoValues;
    private final Image image;
    private boolean isRotated = false;

    public Domino(List<DominoValue> dominoValues, Image image) {
        this.dominoValues = dominoValues;
        this.image = image;
    }

    public DominoValue getFirstValue() {
        return dominoValues.get(0);
    }

    public DominoValue getSecondValue() {
        return dominoValues.get(1);
    }

    public List<DominoValue> getDominoValues() {
        return dominoValues;
    }

    public Image getImage() {
        return image;
    }

    public boolean isRotated() {
        return isRotated;
    }

    public void setRotated() {
        isRotated = true;
    }

    @Override
    public String toString() {
        return "[" + getFirstValue() + " | " + getSecondValue() + "]";
    }
}
