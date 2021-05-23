package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {


    private Pane root = new Pane();
    private double t = 0;
    private GameObject player = new GameObject(300, 750, 40, 40, "player", Color.GREEN);
    private ArrayList<GameObject> enemys = new ArrayList<GameObject>();


    private boolean CheckAllDead(ArrayList<GameObject> enemyList) {

        for (int i = 0; i < enemyList.size(); i++) {
            if (!(enemyList.get(i).dead)) return false;
        }
        return true;
    }


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
        player.dead = false;
        enemys.clear();
        for (int i = 0; i < 5; i++) {
            GameObject obj = new GameObject(90 + i * 100, 150, 25, 25, "enemy", Color.RED);
            enemys.add(obj);
            root.getChildren().add(obj);
        }
    }


    private List<GameObject> gameObjects() {
        return root.getChildren().stream().map(n -> (GameObject) n).collect(Collectors.toList());
    }

    private void GameStatus() {

        if (player.dead) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("GAME ALERT");
            alert.setContentText("GAME OVER!");
            alert.show();
            final Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            nextLevel();
        }
        if (CheckAllDead(enemys)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("GAME ALERT");
            alert.setContentText("YOU WON ! ");
            alert.show();
            final Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            nextLevel();
        }
    }

    private void update() {
        t += 0.015;
        gameObjects().forEach(s -> {
            switch (s.type) {

                case "enemybullet":
                    s.moveDown(3);
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.dead = true;
                        s.dead = true;
                    }
                    break;
                case "playerbullet":
                    s.moveUp(5);
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

                        gameObjects().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                            if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                                if (Math.random() < 0.2) s.moveLeft(5);
                                if (Math.random() < 0.1) s.moveRight(5);

                            }
                        });
                    }
                    break;
            }
        });


        root.getChildren().removeIf(n -> {
            GameObject S = (GameObject) n;
            return S.dead;
        });
        GameStatus();
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
                    player.moveLeft(15);
                    break;
                case W:
                    player.moveUp(15);
                    break;
                case S:
                    player.moveDown(15);
                    break;
                case D:
                    player.moveRight(15);
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
