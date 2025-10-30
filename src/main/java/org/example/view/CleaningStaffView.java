package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.cleaningStaff.CleaningStaff;
import org.example.domain.cleaningStaff.CleaningStaffController;
import org.example.domain.cleaningStaff.dto.CleaningStaffRequestDTO;

public class CleaningStaffView {
    private final CleaningStaffController controller;
    private ListView<String> staffListView;

    public CleaningStaffView() {
        this.controller = CleaningStaffController.getInstance();
    }

    public void start(Stage stage) {
        stage.setTitle("청소 스태프 관리");

        // 입력 필드
        TextField nameField = new TextField();
        nameField.setPromptText("이름");
        TextField phoneField = new TextField();
        phoneField.setPromptText("전화번호");

        // 저장 버튼
        Button saveButton = new Button("저장");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !phone.isEmpty()) {
                controller.save(new CleaningStaffRequestDTO(name, phone));
                updateStaffList();
                nameField.clear();
                phoneField.clear();
            }
        });

        // 리스트뷰
        staffListView = new ListView<>();
        updateStaffList();

        // 삭제 버튼
        Button deleteButton = new Button("선택 항목 삭제");
        deleteButton.setOnAction(e -> {
            String selected = staffListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int id = Integer.parseInt(selected.split(":")[0]);
                controller.deleteById(id);
                updateStaffList();
            }
        });

        // 레이아웃
        // 뒤로가기 버튼
        Button backButton = new Button("뒤로가기");
        backButton.setOnAction(e -> {
            MainView mainView = new MainView(stage);
            mainView.show();
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
            backButton,
            new Label("이름:"),
            nameField,
            new Label("전화번호:"),
            phoneField,
            saveButton,
            staffListView,
            deleteButton
        );

        Scene scene = new Scene(vbox, 300, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void updateStaffList() {
        staffListView.getItems().clear();
        for (CleaningStaff staff : controller.findAll()) {
            staffListView.getItems().add(
                String.format("%d: %s (%s)", 
                    staff.getId(), 
                    staff.getName(), 
                    staff.getPhoneNumber())
            );
        }
    }
}