package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.user.customer.Customer;
import org.example.domain.user.customer.CustomerController;

public class LoginView {
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("StayOn - 로그인");

        // 메인 컨테이너 (좌우 분할)
        HBox mainContainer = new HBox(0);

        // 왼쪽 영역 - 그라데이션 배경 + 환영 메시지
        VBox leftPanel = createLeftPanel();
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        // 오른쪽 영역 - 로그인 폼
        VBox rightPanel = createRightPanel();
        rightPanel.setMinWidth(450);
        rightPanel.setMaxWidth(450);

        mainContainer.getChildren().addAll(leftPanel, rightPanel);        Scene scene = new Scene(mainContainer, 1200, 800);

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
        leftPanel.setStyle("-fx-background-color: linear-gradient(to bottom right, #2563eb, #7c3aed);");

        // 로고
        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoView.setFitWidth(180);
            logoView.setPreserveRatio(true);
            // 로고에 밝은 효과
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

        Label welcomeTitle = new Label("환영합니다!");
        welcomeTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label welcomeSubtitle = new Label("완벽한 휴식을 위한 펜션 예약 서비스");
        welcomeSubtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        // 특징 아이콘들
        VBox features = new VBox(15);
        features.setAlignment(Pos.CENTER_LEFT);
        features.setPadding(new Insets(40, 0, 0, 0));
        features.setMaxWidth(300);

        features.getChildren().addAll(
            createFeatureItem("🏡", "전국 최고의 펜션"),
            createFeatureItem("💳", "간편한 예약 시스템"),
            createFeatureItem("⭐", "실시간 리뷰 확인")
        );

        messageBox.getChildren().addAll(welcomeTitle, welcomeSubtitle, features);
        leftPanel.getChildren().add(messageBox);

        return leftPanel;
    }

    private HBox createFeatureItem(String icon, String text) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9);");

        item.getChildren().addAll(iconLabel, textLabel);
        return item;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(0);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(60, 50, 60, 50));
        rightPanel.setStyle("-fx-background-color: #f8fafc;");

        // 로그인 타이틀
        Label titleLabel = new Label("로그인");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label subtitleLabel = new Label("계정에 로그인하여 서비스를 이용하세요");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        subtitleLabel.setPadding(new Insets(8, 0, 30, 0));

        // 폼 컨테이너
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(350);

        // 아이디 필드
        VBox idBox = new VBox(8);
        Label idLabel = new Label("아이디");
        idLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        TextField idField = new TextField();
        idField.setPromptText("아이디를 입력하세요");
        idField.setPrefHeight(48);
        idField.setStyle(getInputFieldStyle());
        idField.setOnMouseEntered(e -> idField.setStyle(getInputFieldFocusStyle()));
        idField.setOnMouseExited(e -> {
            if (!idField.isFocused()) idField.setStyle(getInputFieldStyle());
        });
        idField.setText("solid");
        idBox.getChildren().addAll(idLabel, idField);

        // 비밀번호 필드
        VBox pwBox = new VBox(8);
        Label pwLabel = new Label("비밀번호");
        pwLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호를 입력하세요");
        passwordField.setPrefHeight(48);
        passwordField.setStyle(getInputFieldStyle());
        passwordField.setOnMouseEntered(e -> passwordField.setStyle(getInputFieldFocusStyle()));
        passwordField.setOnMouseExited(e -> {
            if (!passwordField.isFocused()) passwordField.setStyle(getInputFieldStyle());
        });

        pwBox.getChildren().addAll(pwLabel, passwordField);

        // 로그인 버튼
        Button loginButton = new Button("로그인");
        loginButton.setPrefWidth(350);
        loginButton.setPrefHeight(50);
        loginButton.setStyle(getPrimaryButtonStyle());
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(getPrimaryButtonHoverStyle()));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(getPrimaryButtonStyle()));

        loginButton.setOnAction(e -> {
            String id = idField.getText();
            String password = passwordField.getText();

            if (id.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "입력 오류", "아이디와 비밀번호를 모두 입력해주세요.");
                return;
            }            
            try {
                Customer customer = CustomerController.getInstance().login(id, password);
                if (customer != null) {
                    showAlert(Alert.AlertType.INFORMATION, "로그인 성공", customer.getName() + "님 환영합니다!");
                    PensionView pensionView = new PensionView(customer);
                    pensionView.start(stage);
                } else {
                    showAlert(Alert.AlertType.ERROR, "로그인 실패", "아이디 또는 비밀번호가 올바르지 않습니다.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "오류", "로그인 중 오류가 발생했습니다: " + ex.getMessage());
            }
        });

        // 구분선
        HBox divider = new HBox(15);
        divider.setAlignment(Pos.CENTER);
        divider.setPadding(new Insets(15, 0, 15, 0));

        Region line1 = new Region();
        line1.setPrefWidth(100);
        line1.setStyle("-fx-background-color: #e2e8f0; -fx-pref-height: 1;");
        HBox.setHgrow(line1, Priority.ALWAYS);

        Label orLabel = new Label("또는");
        orLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        Region line2 = new Region();
        line2.setPrefWidth(100);
        line2.setStyle("-fx-background-color: #e2e8f0; -fx-pref-height: 1;");
        HBox.setHgrow(line2, Priority.ALWAYS);

        divider.getChildren().addAll(line1, orLabel, line2);

        // 회원가입 버튼
        Button registerButton = new Button("회원가입");
        registerButton.setPrefWidth(350);
        registerButton.setPrefHeight(50);
        registerButton.setStyle(getSecondaryButtonStyle());
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(getSecondaryButtonHoverStyle()));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(getSecondaryButtonStyle()));
        registerButton.setOnAction(e -> {
            RegisterView registerView = new RegisterView(stage);
            registerView.show();
        });

        formBox.getChildren().addAll(idBox, pwBox, loginButton, divider, registerButton);

        rightPanel.getChildren().addAll(titleLabel, subtitleLabel, formBox);

        return rightPanel;
    }

    private String getInputFieldStyle() {
        return "-fx-background-color: white; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 12; " +
               "-fx-background-radius: 12; " +
               "-fx-padding: 12 16; " +
               "-fx-font-size: 14px;";
    }

    private String getInputFieldFocusStyle() {
        return "-fx-background-color: white; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 12; " +
               "-fx-background-radius: 12; " +
               "-fx-padding: 12 16; " +
               "-fx-font-size: 14px;";
    }

    private String getPrimaryButtonStyle() {
        return "-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed); " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 15px; " +
               "-fx-font-weight: bold; " +
               "-fx-background-radius: 12; " +
               "-fx-cursor: hand;";
    }

    private String getPrimaryButtonHoverStyle() {
        return "-fx-background-color: linear-gradient(to right, #1d4ed8, #6d28d9); " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 15px; " +
               "-fx-font-weight: bold; " +
               "-fx-background-radius: 12; " +
               "-fx-cursor: hand;";
    }

    private String getSecondaryButtonStyle() {
        return "-fx-background-color: white; " +
               "-fx-text-fill: #2563eb; " +
               "-fx-font-size: 15px; " +
               "-fx-font-weight: bold; " +
               "-fx-background-radius: 12; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 12; " +
               "-fx-cursor: hand;";
    }

    private String getSecondaryButtonHoverStyle() {
        return "-fx-background-color: #eff6ff; " +
               "-fx-text-fill: #1d4ed8; " +
               "-fx-font-size: 15px; " +
               "-fx-font-weight: bold; " +
               "-fx-background-radius: 12; " +
               "-fx-border-color: #1d4ed8; " +
               "-fx-border-radius: 12; " +
               "-fx-cursor: hand;";
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
