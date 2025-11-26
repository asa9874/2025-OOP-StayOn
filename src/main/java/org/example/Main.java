package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.view.LoginView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);
        loginView.show();
    }

    public static void main(String[] args) {
        // 의존성 초기화
        Init.initializeDependencies();
        launch(args);
    }
}