package com.example.hopperhelm;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Game {

    private Button defence = new Button("⚔");
    private Button back = new Button("←");
    private Button forward = new Button("→");
    private Button attack = new Button("\uD83E\uDD3A");

    private boolean inDefenseMode = false;
    private Label scoreLBL;
    private Timeline enemyAttackTimer;
    private Random random;
    public static HBox cells;
    private int position = 0;
    private int score = 0;
    private HBox heartsHBox;

    private Block bk;

    private Stage primaryStage;

    public HBox getController() {

        defence.setFont(new Font(15));
        back.setFont(new Font(15));
        forward.setFont(new Font(15));
        attack.setFont(new Font(15));
        HBox controls = new HBox(defence, back, forward, attack);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(50);
        controls.setPadding(new Insets(15));
        return controls;
    }

    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        Game game = new Game();

        bk = new Block();

        VBox bGround = new VBox();

        scoreLBL = new Label("Score : " + score);
        scoreLBL.setFont(new Font(25));

        bGround.setPrefHeight(200);

        cells = bk.createBlock(position);


        heartsHBox = createHearts();


        game.getForward().setOnAction(event -> {
            if (Block.enemyPos.contains(position + 1)) {
                heartsHBox.getChildren().remove(Player.lives - 1);
                Player.lives -= 1;
                if (Player.lives == 0) {
                    exit();
                }
                updateBlocks(bk);
            }
            if (!Block.enemyPos.contains(position + 1) && position < 19) {
                position += 1;
                updateBlocks(bk);
            }

            if (position == 19) {
                nextLevel(primaryStage);
            }

            checkFighting();

        });

        game.getBack().setOnAction(event -> {

            inDefenseMode = false;
            if (Block.enemyPos.contains(position - 1)) {
                heartsHBox.getChildren().remove(Player.lives - 1);
                Player.lives -= 1;
                if (Player.lives == 0) {
                    exit();
                }
                updateBlocks(bk);
            }
            if (!Block.enemyPos.contains(position - 1) && position > 0) {
                position -= 1;
                updateBlocks(bk);
            }

            checkFighting();

        });

        game.getAttack().setOnAction(event -> {

            inDefenseMode = false;

            random = new Random();

            if (Block.enemyPos.contains(position + 1)) {
                Block.enemyPos.remove(position + 1);
                score += 10;
                scoreLBL.setText("Score : " + score);

                int new_pos = random.nextInt(0, 20);

                while (new_pos == position || new_pos == position + 1 || Block.enemyPos.contains(new_pos)) {
                    new_pos = random.nextInt(0, 20);
                }

                Block.enemyPos.add(new_pos);
                updateBlocks(bk);


            }
            if (Block.enemyPos.contains(position - 1)) {
                Block.enemyPos.remove(position - 1);
                score += 10;
                scoreLBL.setText("Score : " + score);


                int new_pos = random.nextInt(0, 20);

                while (new_pos == position || new_pos == position - 1 || Block.enemyPos.contains(new_pos)) {
                    new_pos = random.nextInt(0, 20);
                }

                Block.enemyPos.add(new_pos);


                updateBlocks(bk);

            }

            updateBlocks(bk);
        });

        game.getDefence().setOnAction(event -> {
            inDefenseMode = true;
        });


        bGround.getChildren().addAll(cells, game.getController(), heartsHBox, scoreLBL);
        bGround.setSpacing(10);
        bGround.setPadding(new Insets(10));
        bGround.setAlignment(Pos.BOTTOM_CENTER);
        primaryStage.setScene(new Scene(bGround));
        primaryStage.show();

    }


    private void nextLevel(Stage primaryStage) {
        primaryStage.hide();

        position = 0;

        score += 50;

        Block.resetEnemyPositions();

        cells = bk.createBlock(position);

        Block.updateRandom();

        start(new Stage());

    }

    private void startEnemyAttackTimer() {
        if (enemyAttackTimer != null) {
            enemyAttackTimer.stop();
        }
        enemyAttackTimer = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (Block.enemyPos.contains(position + 1) && !inDefenseMode) {  // Confirm enemy is still there before attacking
                Player.lives--;
                updateHearts();
                if (Player.lives <= 0) {
                    System.out.println("Game Over");
                    exit();
                    // Handle game over logic here
                } else {
                    System.out.println("You were attacked! Lives remaining: " + Player.lives);
                }
            }
            startEnemyAttackTimer();  // Restart the timer for continuous attacks
        }));
        enemyAttackTimer.setCycleCount(1);
        enemyAttackTimer.play();
    }

    public void exit() {
        primaryStage.hide();

        VBox newGame = new VBox();

        Label lose = new Label("YOU LOSE");
        lose.setAlignment(Pos.CENTER);
        lose.setFont(new Font(50));

        Button playAgain = new Button("Play again");
        playAgain.setFont(new Font(25));

        newGame.getChildren().addAll(lose, playAgain);
        newGame.setAlignment(Pos.CENTER);

        playAgain.setOnAction(event -> {

            primaryStage.hide();

            position = 0;

            Player.lives = 3;

            score = 0;

            Block.updateRandom();

            Block.resetEnemyPositions();

            start(new Stage());

        });

        primaryStage.setScene(new Scene(newGame));
        primaryStage.setHeight(200);
        primaryStage.setWidth(350);
        primaryStage.show();


    }

    private void stopEnemyAttackTimer() {
        if (enemyAttackTimer != null) {
            enemyAttackTimer.stop();
        }
    }

    private void checkFighting() {
        if (Block.enemyPos.contains(position + 1)) {
            startEnemyAttackTimer();
        } else {
            stopEnemyAttackTimer();
        }
    }

    private void updateHearts() {
        heartsHBox.getChildren().clear();
        for (int i = 0; i < Player.lives; i++) {
            Circle heart = new Circle(10, Color.RED);
            heartsHBox.getChildren().add(heart);
        }
    }


    private HBox createHearts() {
        HBox hearts = new HBox();
        hearts.setAlignment(Pos.CENTER);
        hearts.setSpacing(10);

        for (int i = 0; i < Player.lives; i++) {
            Circle heart = new Circle(10, Color.RED);
            hearts.getChildren().add(heart);
        }

        return hearts;
    }

    private void updateBlocks(Block bk) {
        cells.getChildren().clear();
        cells.getChildren().addAll(bk.createBlock(position).getChildren());
    }


    public Button getDefence() {
        return defence;
    }

    public Button getBack() {
        return back;
    }

    public Button getForward() {
        return forward;
    }

    public Button getAttack() {
        return attack;
    }

}

