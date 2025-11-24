package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {
    private final Stage stage;

    public MainView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("StayOn 관리 시스템");

        // 청소 스태프 관리 버튼
        Button cleaningStaffButton = new Button("청소 스태프 관리");
        cleaningStaffButton.setOnAction(e -> {
            CleaningStaffView cleaningStaffView = new CleaningStaffView();
            cleaningStaffView.start(stage);
        });

        // 펜션 관리 버튼
        Button pensionButton = new Button("펜션 목록 검색");
        pensionButton.setOnAction(e -> {
            PensionView pensionView = new PensionView();
            pensionView.start(stage);
        });

        // 로그인 버튼
        Button loginButton = new Button("로그인");
        loginButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        // 레이아웃 설정
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(30));        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(cleaningStaffButton, pensionButton, loginButton);

        // 씬 설정
        Scene scene = new Scene(vbox, 800, 600);
        
        // Pretendard 폰트 적용
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS 파일을 불러올 수 없습니다: " + e.getMessage());
        }
        
        stage.setScene(scene);
        stage.show();
    }
}
