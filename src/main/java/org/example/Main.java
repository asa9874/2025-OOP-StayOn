package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.view.MainView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView(stage);
        mainView.show();
    }

    public static void main(String[] args) {
        // 의존성 초기화
        Init.initializeDependencies();
        launch(args);
    }
}