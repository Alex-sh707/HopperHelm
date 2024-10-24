package com.example.hopperhelm;

import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Block {

    public static Set<Integer> enemyPos = generateRandomEnemyPositions();

    public static final int BLOCK = 20;
    private Player player;
    public static int[] randoms = Block.randomHeight(); // To avoid changing cells after every update

    public static int[] randomHeight() { // To make different height for each cell
        int[] randoms = new int[BLOCK];

        Random random = new Random();
        for (int i = 0; i < BLOCK; i++) {
            randoms[i] = random.nextInt(50, 100);
        }
        return randoms;
    }

    public static void updateRandom() {
        int[] randomsArray = new int[BLOCK];

        Random random = new Random();
        for (int i = 0; i < BLOCK; i++) {
            randomsArray[i] = random.nextInt(50, 100);
        }
        randoms = randomsArray;

    }

    public static Set<Integer> generateRandomEnemyPositions() {
        Set<Integer> positions = new HashSet<>();
        Random random = new Random();

        while (positions.size() < 4) {
            positions.add(random.nextInt(2, BLOCK));
        }

        return positions;
    }

    public static void resetEnemyPositions() {
        enemyPos = generateRandomEnemyPositions();
    }

    public HBox createBlock(int pos) { // Making the main border
        HBox blocks = new HBox(); // For all cells
        player = new Player();
        VBox cell;
        Rectangle block;

        for (int i = 0; i < BLOCK; i++) {
            cell = new VBox();
            Circle ply = player.addPlayer();
            if (i != pos)
                ply.setVisible(false);
            cell.getChildren().add(ply);

            if (enemyPos.contains(i)) {
                Arc enemy = addEnemy();
                cell.getChildren().add(enemy);
            }

            block = new Rectangle(60, randoms[i]);

            if (i != BLOCK - 1) {
                block.setFill(Color.BLUE);
                block.setStroke(Color.BLACK);
                block.setStrokeWidth(2);
            } else {
                block.setFill(Color.LIGHTBLUE);
                block.setStroke(Color.BLACK);
                block.setStrokeWidth(2);
            }
            cell.getChildren().add(block);

            cell.setAlignment(Pos.BOTTOM_CENTER);
            cell.setSpacing(3);

            blocks.getChildren().add(cell);
        }

        blocks.setAlignment(Pos.BOTTOM_LEFT);

        return blocks;
    }

    public Arc addEnemy() {
        Arc enemy = new Arc();
        enemy.setRadiusX(30);
        enemy.setRadiusY(30);
        enemy.setStartAngle(45);
        enemy.setLength(270);
        enemy.setType(ArcType.ROUND);
        enemy.setFill(Color.BROWN);
        enemy.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        return enemy;
    }
}
