package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.cleaningStaff.CleaningStaff;
import org.example.domain.cleaningStaff.CleaningStaffController;
import org.example.domain.cleaningStaff.dto.CleaningStaffRequestDTO;

public class CleaningStaffView {
    private final CleaningStaffController controller;
    private FlowPane staffGridContainer;

    public CleaningStaffView() {
        this.controller = CleaningStaffController.getInstance();
    }

    public void start(Stage stage) {
        stage.setTitle("StayOn - 청소 스태프 관리");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader(stage);

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 콘텐츠 영역
        HBox contentBox = new HBox(30);
        contentBox.setPadding(new Insets(30, 50, 50, 50));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // 왼쪽: 스태프 추가 카드
        VBox addStaffCard = createAddStaffCard();

        // 오른쪽: 스태프 목록 카드
        VBox staffListCard = createStaffListCard();

        contentBox.getChildren().addAll(addStaffCard, staffListCard);

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-background-color: #f8fafc; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);        mainContainer.getChildren().addAll(header, heroSection, scrollPane);

        Scene scene = new Scene(mainContainer, 1200, 800);

        // 폰트 로드 및 적용
        FontUtil.loadFont();
        mainContainer.setStyle("-fx-font-family: '" + FontUtil.getFontFamily() + "';");

        stage.setScene(scene);
        stage.show();
    }

    private HBox createHeader(Stage stage) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 40, 15, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");        Button backButton = new Button("← 로그인으로");
        backButton.setStyle(getBackButtonStyle());
        backButton.setOnMouseEntered(e -> backButton.setStyle(getBackButtonHoverStyle()));
        backButton.setOnMouseExited(e -> backButton.setStyle(getBackButtonStyle()));
        backButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoView.setFitHeight(32);
            logoView.setPreserveRatio(true);
            header.getChildren().addAll(backButton, spacer1, logoView);
        } catch (Exception e) {
            Label logoText = new Label("StayOn");
            logoText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
            header.getChildren().addAll(backButton, spacer1, logoText);
        }

        Region spacer2 = new Region();
        spacer2.setMinWidth(100);
        header.getChildren().add(spacer2);

        return header;
    }

    private VBox createHeroSection() {
        VBox hero = new VBox(15);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(40, 40, 30, 40));
        hero.setStyle("-fx-background-color: linear-gradient(to right, #06b6d4, #0891b2);");

        Label titleLabel = new Label("🧹 청소 스태프 관리");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("청소 스태프를 등록하고 관리하세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private VBox createAddStaffCard() {
        VBox card = new VBox(20);
        card.setMinWidth(350);
        card.setMaxWidth(350);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("➕ 새 스태프 등록");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        // 이름 입력
        VBox nameBox = new VBox(8);
        Label nameLabel = new Label("👤 이름");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        TextField nameField = new TextField();
        nameField.setPromptText("스태프 이름을 입력하세요");
        nameField.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 12; " +
            "-fx-font-size: 14px;"
        );
        nameBox.getChildren().addAll(nameLabel, nameField);

        // 전화번호 입력
        VBox phoneBox = new VBox(8);
        Label phoneLabel = new Label("📞 전화번호");
        phoneLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        TextField phoneField = new TextField();
        phoneField.setPromptText("전화번호를 입력하세요");
        phoneField.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 12; " +
            "-fx-font-size: 14px;"
        );
        phoneBox.getChildren().addAll(phoneLabel, phoneField);

        // 저장 버튼
        Button saveButton = new Button("✅ 스태프 등록");
        saveButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #06b6d4, #0891b2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #0891b2, #0e7490); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        saveButton.setOnMouseExited(e -> saveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #06b6d4, #0891b2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            if (!name.isEmpty() && !phone.isEmpty()) {
                controller.save(new CleaningStaffRequestDTO(name, phone));
                updateStaffList();
                nameField.clear();
                phoneField.clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("등록 완료");
                alert.setHeaderText(null);
                alert.setContentText("스태프가 등록되었습니다.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("입력 오류");
                alert.setHeaderText(null);
                alert.setContentText("이름과 전화번호를 모두 입력해주세요.");
                alert.showAndWait();
            }
        });

        card.getChildren().addAll(sectionTitle, divider, nameBox, phoneBox, saveButton);

        return card;
    }

    private VBox createStaffListCard() {
        VBox card = new VBox(20);
        card.setMinWidth(500);
        card.setMaxWidth(500);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("👥 스태프 목록");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        // 스태프 목록 컨테이너
        staffGridContainer = new FlowPane();
        staffGridContainer.setVgap(15);
        staffGridContainer.setHgap(15);
        staffGridContainer.setAlignment(Pos.TOP_LEFT);

        updateStaffList();

        // 스크롤
        ScrollPane scrollPane = new ScrollPane(staffGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white; -fx-border-color: transparent;");

        card.getChildren().addAll(sectionTitle, divider, scrollPane);

        return card;
    }

    private void updateStaffList() {
        staffGridContainer.getChildren().clear();

        var staffList = controller.findAll();

        if (staffList.isEmpty()) {
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            emptyBox.setMinWidth(450);

            Label emptyIcon = new Label("👥");
            emptyIcon.setStyle("-fx-font-size: 40px;");

            Label emptyLabel = new Label("등록된 스태프가 없습니다.");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8;");

            emptyBox.getChildren().addAll(emptyIcon, emptyLabel);
            staffGridContainer.getChildren().add(emptyBox);
        } else {
            for (CleaningStaff staff : staffList) {
                staffGridContainer.getChildren().add(createStaffCard(staff));
            }
        }
    }

    private HBox createStaffCard(CleaningStaff staff) {
        HBox card = new HBox(15);
        card.setMinWidth(450);
        card.setMaxWidth(450);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12;"
        );

        // 프로필 아이콘
        StackPane profileIcon = new StackPane();
        profileIcon.setMinSize(45, 45);
        profileIcon.setMaxSize(45, 45);
        profileIcon.setStyle(
            "-fx-background-color: linear-gradient(to right, #06b6d4, #0891b2); " +
            "-fx-background-radius: 23;"
        );
        Label profileInitial = new Label(staff.getName().substring(0, 1));
        profileInitial.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        profileIcon.getChildren().add(profileInitial);

        // 정보
        VBox infoBox = new VBox(3);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label idLabel = new Label("#" + staff.getId());
        idLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #94a3b8;");

        Label nameLabel = new Label(staff.getName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label phoneLabel = new Label("📞 " + staff.getPhoneNumber());
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        infoBox.getChildren().addAll(idLabel, nameLabel, phoneLabel);

        // 삭제 버튼
        Button deleteButton = new Button("🗑️");
        deleteButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #ef4444; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8 12; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #fee2e2; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        );
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
            "-fx-background-color: #fef2f2; " +
            "-fx-text-fill: #dc2626; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8 12; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #ef4444; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #ef4444; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8 12; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #fee2e2; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        deleteButton.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("삭제 확인");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("'" + staff.getName() + "' 스태프를 삭제하시겠습니까?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.deleteById(staff.getId());
                    updateStaffList();
                }
            });
        });

        card.getChildren().addAll(profileIcon, infoBox, deleteButton);

        // 호버 효과
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #06b6d4; " +
            "-fx-border-radius: 12; " +
            "-fx-effect: dropshadow(gaussian, rgba(6, 182, 212, 0.2), 10, 0, 0, 4);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12;"
        ));

        return card;
    }

    private String getBackButtonStyle() {
        return "-fx-background-color: transparent; " +
               "-fx-text-fill: #64748b; " +
               "-fx-font-size: 14px; " +
               "-fx-cursor: hand; " +
               "-fx-padding: 8 20; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 20; " +
               "-fx-background-radius: 20;";
    }

    private String getBackButtonHoverStyle() {
        return "-fx-background-color: #f1f5f9; " +
               "-fx-text-fill: #2563eb; " +
               "-fx-font-size: 14px; " +
               "-fx-cursor: hand; " +
               "-fx-padding: 8 20; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 20; " +
               "-fx-background-radius: 20;";
    }
}