package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameObject extends Rectangle {
    boolean dead = false;
    final String type;

    GameObject(int x, int y, int w, int h, String type, Color color) {
        super(w, h, color);
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    void moveLeft() {
        setTranslateX(getTranslateX() - 5);
    }

    void moveRight() {
        setTranslateX(getTranslateX() + 5);
    }

    void moveUp() {
        setTranslateY(getTranslateY() - 5);
    }

    void moveDown() {
        setTranslateY(getTranslateY() + 5);
    }

}
