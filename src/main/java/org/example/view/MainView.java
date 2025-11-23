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

        // 객실 선택 버튼
        Button roomSelectButton = new Button("객실 선택");
        roomSelectButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView();
            roomSelectView.start(stage);
        });

        // 결제 버튼
        Button paymentButton = new Button("결제");
        paymentButton.setOnAction(e -> {
            PaymentView paymentView = new PaymentView();
            paymentView.start(stage);
        });
        
        // 레이아웃 설정
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(cleaningStaffButton, roomSelectButton, paymentButton);

        // 씬 설정
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}