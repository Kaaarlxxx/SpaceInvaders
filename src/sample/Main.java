package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {


    private Pane root = new Pane();
    private double t = 0;
    private GameObject player = new GameObject(300, 750, 40, 40, "player", Color.GREEN);



    private Parent createContent() {
        root.setPrefSize(600, 800);
        root.getChildren().add(player);
        player.setId("player");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        timer.start();

        nextLevel();

        return root;
    }

    private void nextLevel() {

        for (int i = 0; i < 5; i++) {
            GameObject obj = new GameObject(90 + i * 100, 150, 25, 25, "enemy", Color.RED);
            root.getChildren().add(obj);
        }


    }

    private List<GameObject> gameObjects() {
        return root.getChildren().stream().map(n -> (GameObject) n).collect(Collectors.toList());
    }

    private void update() {
        t += 0.015;
        gameObjects().forEach(s -> {
            switch (s.type) {

                case "enemybullet":
                    s.moveDown();
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.dead = true;
                        s.dead = true;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("GAME OVER");
                        alert.show();
                    }
                    break;
                case "playerbullet":
                    s.moveUp();
                    gameObjects().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.dead = true;
                            s.dead = true;
                        }
                    });
                    break;
                case "enemy":

                    if (t > 2) {
                        if (Math.random() < 0.3) shoot(s);
                    }
                    break;
            }
        });

        root.getChildren().removeIf(n -> {
            GameObject S = (GameObject) n;
            return S.dead;
        });

        if (t > 2) t = 0;
    }

    private void shoot(GameObject who) {
        GameObject bullet = new GameObject((int) who.getTranslateX() + 10, (int) who.getTranslateY(), 5, 20, who.type + "bullet", Color.CORAL);
        root.getChildren().add(bullet);
    }


    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        root.setId("pane");

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    player.moveLeft();
                    break;
                case W:
                    player.moveUp();
                    break;
                case S:
                    player.moveDown();
                    break;
                case D:
                    player.moveRight();
                    break;
                case SPACE:
                    shoot(player);
                    break;
            }
        });
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
