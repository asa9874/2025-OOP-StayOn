package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    }

    public void show() {
        stage.setTitle("StayOn - 로그인");

        // 사용자 타입 선택
        Label userTypeLabel = new Label("사용자 타입:");
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("고객", "펜션 매니저");
        userTypeComboBox.setValue("고객");
        userTypeComboBox.setPrefWidth(200);

        // 입력 필드
        Label idLabel = new Label("아이디:");
        TextField idField = new TextField();
        idField.setPromptText("아이디를 입력하세요");
        idField.setPrefWidth(200);

        Label passwordLabel = new Label("비밀번호:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호를 입력하세요");
        passwordField.setPrefWidth(200);

        // 로그인 버튼
        Button loginButton = new Button("로그인");
        loginButton.setPrefWidth(200);
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
        });

        // 회원가입 버튼
        Button registerButton = new Button("회원가입");
        registerButton.setPrefWidth(200);
        registerButton.setOnAction(e -> {
            RegisterView registerView = new RegisterView(stage);
            registerView.show();
        });

        // GridPane 레이아웃
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(30));

        gridPane.add(userTypeLabel, 0, 0);
        gridPane.add(userTypeComboBox, 1, 0);
        gridPane.add(idLabel, 0, 1);
        gridPane.add(idField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);

        // 버튼 박스
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, registerButton);

        // 메인 레이아웃
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Label titleLabel = new Label("StayOn 펜션 예약 시스템");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        vbox.getChildren().addAll(titleLabel, gridPane, buttonBox);

        Scene scene = new Scene(vbox, 800, 600);
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
