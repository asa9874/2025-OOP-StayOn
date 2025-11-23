package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    }

    public void show() {
        stage.setTitle("회원가입");

        // 사용자 타입 선택
        Label userTypeLabel = new Label("가입 유형:");
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("고객", "펜션 매니저");
        userTypeComboBox.setValue("고객");
        userTypeComboBox.setPrefWidth(200);

        // 입력 필드
        TextField idField = new TextField();
        idField.setPromptText("사용할 아이디");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("비밀번호 확인");

        TextField nameField = new TextField();
        nameField.setPromptText("이름");

        TextField phoneField = new TextField();
        phoneField.setPromptText("전화번호");

        TextField emailField = new TextField();
        emailField.setPromptText("이메일");

        // 회원가입 버튼
        Button registerButton = new Button("가입하기");
        registerButton.setPrefWidth(200);
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
        Button cancelButton = new Button("취소");
        cancelButton.setPrefWidth(200);
        cancelButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        // GridPane 레이아웃
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        gridPane.add(userTypeLabel, 0, 0);
        gridPane.add(userTypeComboBox, 1, 0);
        gridPane.add(new Label("아이디:"), 0, 1);
        gridPane.add(idField, 1, 1);
        gridPane.add(new Label("비밀번호:"), 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(new Label("비밀번호 확인:"), 0, 3);
        gridPane.add(confirmPasswordField, 1, 3);
        gridPane.add(new Label("이름:"), 0, 4);
        gridPane.add(nameField, 1, 4);
        gridPane.add(new Label("전화번호:"), 0, 5);
        gridPane.add(phoneField, 1, 5);
        gridPane.add(new Label("이메일:"), 0, 6);
        gridPane.add(emailField, 1, 6);

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(registerButton, cancelButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(gridPane, buttonBox);

        Scene scene = new Scene(vbox, 800, 600);
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
