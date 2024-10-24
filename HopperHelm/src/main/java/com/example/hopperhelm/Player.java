package com.example.hopperhelm;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {

    public static int lives = 3;

    public Circle addPlayer() {
        return new Circle(20, Color.RED);
    }
}
