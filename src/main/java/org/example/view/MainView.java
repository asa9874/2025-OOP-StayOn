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

        // 예약 관리 버튼
        Button reservationButton = new Button("방 등록");
        reservationButton.setOnAction(e -> {
            RoomEnrollView roomEnrollView = new RoomEnrollView();
            roomEnrollView.start(stage);
        });
        
        // 레이아웃 설정
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                cleaningStaffButton,
                reservationButton
        );

        // 씬 설정
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}