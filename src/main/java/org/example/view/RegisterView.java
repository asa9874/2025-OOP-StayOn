package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.user.customer.CustomerController;
import org.example.domain.user.customer.dto.CustomerRequestDTO;
import org.example.domain.user.pensionManager.PensionManagerController;
import org.example.domain.user.pensionManager.dto.PensionManagerRequestDTO;

public class RegisterView {
    private final Stage stage;
    private final CustomerController customerController = CustomerController.getInstance();
    private final PensionManagerController pensionManagerController = PensionManagerController.getInstance();

    public RegisterView(Stage stage) {
        this.stage = stage;
    }    public void show() {
        stage.setTitle("StayOn - 회원가입");

        // 로고 이미지
        ImageView logoView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoView.setImage(logoImage);
            logoView.setFitWidth(250);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("로고 이미지를 불러올 수 없습니다: " + e.getMessage());
        }

        // 사용자 타입 선택
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("고객", "펜션 매니저");
        userTypeComboBox.setValue("고객");
        userTypeComboBox.setPromptText("가입 유형 선택");
        userTypeComboBox.setPrefWidth(300);
        userTypeComboBox.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // 입력 필드
        TextField idField = new TextField();
        idField.setPromptText("아이디");
        idField.setPrefWidth(300);
        idField.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호");
        passwordField.setPrefWidth(300);
        passwordField.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("비밀번호 확인");
        confirmPasswordField.setPrefWidth(300);
        confirmPasswordField.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        
        TextField nameField = new TextField();
        nameField.setPromptText("이름");
        nameField.setPrefWidth(300);
        nameField.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("전화번호");
        phoneField.setPrefWidth(300);
        phoneField.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        
        TextField emailField = new TextField();
        emailField.setPromptText("이메일");
        emailField.setPrefWidth(300);
        emailField.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");// 회원가입 버튼
        Button registerButton = new Button("가입하기");
        registerButton.setPrefWidth(300);
        registerButton.setPrefHeight(45);
        registerButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        registerButton.setOnAction(e -> {
            if (!validateInput(idField, passwordField, confirmPasswordField, nameField, phoneField)) {
                return;
            }
            // 회원가입
            if (userTypeComboBox.getValue().equals("고객")) {
                // 고객 회원가입 로직
                customerController.save(new CustomerRequestDTO(
                    nameField.getText(),
                    idField.getText(),
                    passwordField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    0
                ));
            } else {
                // 펜션 매니저 회원가입 요청
                pensionManagerController.save(new PensionManagerRequestDTO(
                    idField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText()
                ));
            }

            
            showAlert(Alert.AlertType.INFORMATION, "회원가입 완료", "회원가입이 완료되었습니다.");

            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        // 취소 버튼
        Button cancelButton = new Button("로그인으로 돌아가기");
        cancelButton.setPrefWidth(300);
        cancelButton.setPrefHeight(45);
        cancelButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        cancelButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });        // 폼 컨테이너
        VBox formContainer = new VBox(12);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setMaxWidth(350);
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        formContainer.getChildren().addAll(
            userTypeComboBox,
            idField,
            passwordField,
            confirmPasswordField,
            nameField,
            phoneField,
            emailField
        );

        // 버튼 박스
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().addAll(registerButton, cancelButton);        // 메인 레이아웃
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #ecf0f1, #bdc3c7);");

        mainLayout.getChildren().addAll(logoView, formContainer, buttonBox);

        Scene scene = new Scene(mainLayout, 800, 650);
        stage.setScene(scene);
        stage.show();
    }

    private boolean validateInput(TextField idField, PasswordField passwordField,
                                  PasswordField confirmPasswordField, TextField nameField,
                                  TextField phoneField) {
        if (idField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                nameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "입력 오류", "모든 필드를 입력해주세요.");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.WARNING, "비밀번호 오류", "비밀번호가 일치하지 않습니다.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
