package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.user.customer.CustomerController;
import org.example.domain.user.customer.dto.CustomerRequestDTO;

public class RegisterView {
    private final Stage stage;
    private final CustomerController customerController = CustomerController.getInstance();

    public RegisterView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("StayOn - 회원가입");

        // 메인 컨테이너 (좌우 분할)
        HBox mainContainer = new HBox(0);

        // 왼쪽 영역 - 그라데이션 배경 + 환영 메시지
        VBox leftPanel = createLeftPanel();
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        // 오른쪽 영역 - 회원가입 폼
        VBox rightPanel = createRightPanel();
        rightPanel.setMinWidth(480);
        rightPanel.setMaxWidth(480);        mainContainer.getChildren().addAll(leftPanel, rightPanel);

        Scene scene = new Scene(mainContainer, 1200, 800);

        // 폰트 로드 및 적용
        FontUtil.loadFont();
        mainContainer.setStyle("-fx-font-family: '" + FontUtil.getFontFamily() + "';");

        stage.setScene(scene);
        stage.show();
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(60));
        leftPanel.setStyle("-fx-background-color: linear-gradient(to bottom right, #7c3aed, #2563eb);");

        // 로고
        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoView.setFitWidth(160);
            logoView.setPreserveRatio(true);
            logoView.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.3), 20, 0, 0, 0);");
            leftPanel.getChildren().add(logoView);
        } catch (Exception e) {
            Label logoText = new Label("StayOn");
            logoText.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: white;");
            leftPanel.getChildren().add(logoText);
        }

        // 환영 메시지
        VBox messageBox = new VBox(15);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(40, 0, 0, 0));

        Label welcomeTitle = new Label("회원이 되어주세요!");
        welcomeTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label welcomeSubtitle = new Label("가입하고 다양한 혜택을 누리세요");
        welcomeSubtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: rgba(255,255,255,0.85);");

        // 혜택 안내
        VBox benefits = new VBox(15);
        benefits.setAlignment(Pos.CENTER_LEFT);
        benefits.setPadding(new Insets(40, 0, 0, 0));
        benefits.setMaxWidth(280);

        benefits.getChildren().addAll(
            createBenefitItem("✨", "신규 가입 특별 혜택"),
            createBenefitItem("🎁", "첫 예약 10% 할인"),
            createBenefitItem("📱", "실시간 예약 알림"),
            createBenefitItem("💝", "멤버십 포인트 적립")
        );

        messageBox.getChildren().addAll(welcomeTitle, welcomeSubtitle, benefits);
        leftPanel.getChildren().add(messageBox);

        return leftPanel;
    }

    private HBox createBenefitItem(String icon, String text) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9);");

        item.getChildren().addAll(iconLabel, textLabel);
        return item;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(0);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(40, 50, 40, 50));
        rightPanel.setStyle("-fx-background-color: #f8fafc;");

        // 회원가입 타이틀
        Label titleLabel = new Label("회원가입");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label subtitleLabel = new Label("아래 정보를 입력하여 가입하세요");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        subtitleLabel.setPadding(new Insets(8, 0, 25, 0));        // 폼 컨테이너
        VBox formBox = new VBox(16);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(380);

        // 아이디 필드
        VBox idBox = createStyledTextField("아이디", "사용할 아이디를 입력하세요");
        TextField idField = (TextField) idBox.getChildren().get(1);
        idField.setText("solid");

        // 비밀번호 필드
        VBox pwBox = createStyledPasswordField("비밀번호", "비밀번호를 입력하세요");
        PasswordField passwordField = (PasswordField) pwBox.getChildren().get(1);
        passwordField.setText("1234");

        // 비밀번호 확인 필드
        VBox confirmPwBox = createStyledPasswordField("비밀번호 확인", "비밀번호를 다시 입력하세요");
        PasswordField confirmPasswordField = (PasswordField) confirmPwBox.getChildren().get(1);
        confirmPasswordField.setText("1234");

        // 이름 필드
        VBox nameBox = createStyledTextField("이름", "이름을 입력하세요");
        TextField nameField = (TextField) nameBox.getChildren().get(1);
        nameField.setText("솔리드");

        // 전화번호 필드
        VBox phoneBox = createStyledTextField("전화번호", "010-0000-0000");
        TextField phoneField = (TextField) phoneBox.getChildren().get(1);
        phoneField.setText("010-1234-5678");

        // 이메일 필드
        VBox emailBox = createStyledTextField("이메일 (선택)", "example@email.com");
        TextField emailField = (TextField) emailBox.getChildren().get(1);
        emailField.setText("solid@koreatech.ac.kr");

        // 회원가입 버튼
        Button registerButton = new Button("가입하기");
        registerButton.setPrefWidth(380);
        registerButton.setPrefHeight(50);
        registerButton.setStyle(getPrimaryButtonStyle());
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(getPrimaryButtonHoverStyle()));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(getPrimaryButtonStyle()));

        registerButton.setOnAction(e -> {
            if (!validateInput(idField, passwordField, confirmPasswordField, nameField, phoneField)) {
                return;
            }

            customerController.save(new CustomerRequestDTO(
                nameField.getText(),
                idField.getText(),
                passwordField.getText(),
                phoneField.getText(),
                emailField.getText(),
                10000000
            ));

            showAlert(Alert.AlertType.INFORMATION, "회원가입 완료", "회원가입이 완료되었습니다.");
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        // 로그인으로 돌아가기
        HBox loginBox = new HBox(8);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20, 0, 0, 0));

        Label haveAccountLabel = new Label("이미 계정이 있으신가요?");
        haveAccountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label loginLink = new Label("로그인");
        loginLink.setStyle("-fx-font-size: 13px; -fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-cursor: hand;");
        loginLink.setOnMouseEntered(e -> loginLink.setStyle("-fx-font-size: 13px; -fx-text-fill: #1d4ed8; -fx-font-weight: bold; -fx-cursor: hand; -fx-underline: true;"));
        loginLink.setOnMouseExited(e -> loginLink.setStyle("-fx-font-size: 13px; -fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-cursor: hand;"));
        loginLink.setOnMouseClicked(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        loginBox.getChildren().addAll(haveAccountLabel, loginLink);        formBox.getChildren().addAll(
            idBox,
            pwBox,
            confirmPwBox,
            nameBox,
            phoneBox,
            emailBox,
            registerButton,
            loginBox
        );

        rightPanel.getChildren().addAll(titleLabel, subtitleLabel, formBox);

        return rightPanel;
    }

    private VBox createStyledTextField(String label, String placeholder) {
        VBox container = new VBox(6);

        Label fieldLabel = new Label(label);
        fieldLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(44);
        field.setStyle(getInputFieldStyle());
        field.setOnMouseEntered(e -> field.setStyle(getInputFieldFocusStyle()));
        field.setOnMouseExited(e -> {
            if (!field.isFocused()) field.setStyle(getInputFieldStyle());
        });

        container.getChildren().addAll(fieldLabel, field);
        return container;
    }

    private VBox createStyledPasswordField(String label, String placeholder) {
        VBox container = new VBox(6);

        Label fieldLabel = new Label(label);
        fieldLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setPrefHeight(44);
        field.setStyle(getInputFieldStyle());
        field.setOnMouseEntered(e -> field.setStyle(getInputFieldFocusStyle()));
        field.setOnMouseExited(e -> {
            if (!field.isFocused()) field.setStyle(getInputFieldStyle());
        });

        container.getChildren().addAll(fieldLabel, field);
        return container;
    }

    private String getInputFieldStyle() {
        return "-fx-background-color: white; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 10; " +
               "-fx-background-radius: 10; " +
               "-fx-padding: 10 14; " +
               "-fx-font-size: 13px;";
    }

    private String getInputFieldFocusStyle() {
        return "-fx-background-color: white; " +
               "-fx-border-color: #7c3aed; " +
               "-fx-border-radius: 10; " +
               "-fx-background-radius: 10; " +
               "-fx-padding: 10 14; " +
               "-fx-font-size: 13px;";
    }

    private String getPrimaryButtonStyle() {
        return "-fx-background-color: linear-gradient(to right, #7c3aed, #2563eb); " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 15px; " +
               "-fx-font-weight: bold; " +
               "-fx-background-radius: 12; " +
               "-fx-cursor: hand;";
    }

    private String getPrimaryButtonHoverStyle() {
        return "-fx-background-color: linear-gradient(to right, #6d28d9, #1d4ed8); " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 15px; " +
               "-fx-font-weight: bold; " +
               "-fx-background-radius: 12; " +
               "-fx-cursor: hand;";
    }    private boolean validateInput(TextField idField, PasswordField passwordField,
                                  PasswordField confirmPasswordField, TextField nameField,
                                  TextField phoneField) {
        if (idField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                nameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "입력 오류", "필수 항목을 모두 입력해주세요.");
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
