package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.user.customer.Customer;
import org.example.domain.user.customer.CustomerController;
import org.example.domain.user.pensionManager.PensionManager;
import org.example.domain.user.pensionManager.PensionManagerController;

public class LoginView {
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }    public void show() {
        stage.setTitle("StayOn - 로그인");

        // 로고 이미지
        ImageView logoView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoView.setImage(logoImage);
            logoView.setFitWidth(300);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("로고 이미지를 불러올 수 없습니다: " + e.getMessage());
        }

        // 사용자 타입 선택
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("고객", "펜션 매니저");
        userTypeComboBox.setValue("고객");
        userTypeComboBox.setPromptText("사용자 타입 선택");
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

        // 로그인 버튼
        Button loginButton = new Button("로그인");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(45);
        loginButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        loginButton.setOnAction(e -> {
            String userType = userTypeComboBox.getValue();
            String id = idField.getText();
            String password = passwordField.getText();

            if (id.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "입력 오류", "아이디와 비밀번호를 모두 입력해주세요.");
                return;
            }

            try {
                if (userType.equals("고객")) {
                    Customer customer = CustomerController.getInstance().login(id, password);
                    if (customer != null) {
                        showAlert(Alert.AlertType.INFORMATION, "로그인 성공", customer.getName() + "님 환영합니다!");
                        MainView mainView = new MainView(stage);
                        mainView.show();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "로그인 실패", "아이디 또는 비밀번호가 올바르지 않습니다.");
                    }
                } else {
                    PensionManager manager = PensionManagerController.getInstance().login(id, password);
                    if (manager != null) {
                        showAlert(Alert.AlertType.INFORMATION, "로그인 성공", manager.getName() + "님 환영합니다!");
                        MainView mainView = new MainView(stage);
                        mainView.show();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "로그인 실패", "아이디 또는 비밀번호가 올바르지 않습니다.");
                    }
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "오류", "로그인 중 오류가 발생했습니다: " + ex.getMessage());
            }
        });        // 회원가입 버튼
        Button registerButton = new Button("회원가입");
        registerButton.setPrefWidth(300);
        registerButton.setPrefHeight(45);
        registerButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        registerButton.setOnAction(e -> {
            RegisterView registerView = new RegisterView(stage);
            registerView.show();
        });        // 폼 컨테이너
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setMaxWidth(350);
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        formContainer.getChildren().addAll(
            userTypeComboBox,
            idField,
            passwordField
        );

        // 버튼 박스
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().addAll(loginButton, registerButton);        // 메인 레이아웃
        VBox mainLayout = new VBox(25);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #ecf0f1, #bdc3c7);");

        mainLayout.getChildren().addAll(logoView, formContainer, buttonBox);

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
