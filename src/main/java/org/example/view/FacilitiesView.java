package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.facilities.Facilities;
import org.example.domain.facilities.FacilitiesController;
import org.example.domain.pension.Pension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FacilitiesView {
    private final Pension pension;
    private final Stage stage;
    private final FacilitiesController facilitiesController;
    private FlowPane facilitiesGridContainer;
    private List<Facilities> currentFacilitiesList;

    public FacilitiesView(Pension pension, Stage stage) {
        this.pension = pension;
        this.stage = stage;
        this.facilitiesController = FacilitiesController.getInstance();
    }    public void show() {
        stage.setTitle("StayOn - 부대시설");

        // 상단 바 (뒤로가기 버튼)
        Button backButton = new Button("← 펜션 정보로");
        backButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        backButton.setOnAction(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, stage);
            detailView.show();
        });

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.getChildren().add(backButton);

        // 타이틀 섹션
        VBox titleSection = new VBox(8);
        titleSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("부대시설");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        
        titleSection.getChildren().addAll(titleLabel, pensionNameLabel);

        // 필터 버튼 컨테이너
        HBox filterContainer = new HBox(10);
        filterContainer.setAlignment(Pos.CENTER);
        filterContainer.setPadding(new Insets(15));
        filterContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label filterLabel = new Label("필터:");
        filterLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        Button showAllButton = new Button("전체");
        styleFilterButton(showAllButton, "#3498db", "#2980b9");
        showAllButton.setOnAction(e -> updateFacilitiesList());

        Button filterPoolButton = new Button("🏊 수영장");
        styleFilterButton(filterPoolButton, "#16a085", "#138d75");
        filterPoolButton.setOnAction(e -> filterByName("수영장"));

        Button filterGolfButton = new Button("⛳ 골프장");
        styleFilterButton(filterGolfButton, "#27ae60", "#229954");
        filterGolfButton.setOnAction(e -> filterByName("골프장"));

        Button filterGymButton = new Button("💪 헬스장");
        styleFilterButton(filterGymButton, "#e67e22", "#d68910");
        filterGymButton.setOnAction(e -> filterByName("헬스장"));

        Button filterTennisButton = new Button("🎾 테니스장");
        styleFilterButton(filterTennisButton, "#8e44ad", "#7d3c98");
        filterTennisButton.setOnAction(e -> filterByName("테니스장"));

        Button filterSaunaButton = new Button("♨️ 사우나");
        styleFilterButton(filterSaunaButton, "#e74c3c", "#cb4335");
        filterSaunaButton.setOnAction(e -> filterByName("사우나"));

        filterContainer.getChildren().addAll(
            filterLabel,
            showAllButton,
            filterPoolButton,
            filterGolfButton,
            filterGymButton,
            filterTennisButton,
            filterSaunaButton
        );

        // 부대시설 그리드 컨테이너
        facilitiesGridContainer = new FlowPane();
        facilitiesGridContainer.setHgap(25);
        facilitiesGridContainer.setVgap(25);
        facilitiesGridContainer.setPadding(new Insets(20));
        facilitiesGridContainer.setAlignment(Pos.CENTER);
        facilitiesGridContainer.setStyle("-fx-background-color: transparent;");
        updateFacilitiesList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(facilitiesGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefViewportHeight(450);

        // 메인 레이아웃
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(20, 40, 40, 40));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom,rgb(236, 241, 240),rgb(187, 240, 216));");
        
        mainLayout.getChildren().addAll(
            topBar,
            titleSection,
            filterContainer,
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 1000, 700);
        
        // Pretendard 폰트 적용
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS 파일을 불러올 수 없습니다: " + e.getMessage());
        }
          stage.setScene(scene);
        stage.show();
    }

    private void styleFilterButton(Button button, String normalColor, String hoverColor) {
        String normalStyle = String.format("-fx-font-size: 13px; -fx-padding: 8 16; -fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;", normalColor);
        String hoverStyle = String.format("-fx-font-size: 13px; -fx-padding: 8 16; -fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;", hoverColor);
        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    private void updateFacilitiesList() {
        currentFacilitiesList = new ArrayList<>(facilitiesController.findByPensionId(pension.getId()));
        displayFacilitiesList();
    }

    private void filterByName(String name) {
        currentFacilitiesList = new ArrayList<>();
        for (Facilities facility : facilitiesController.findByPensionId(pension.getId())) {
            if (facility.getName().contains(name)) {
                currentFacilitiesList.add(facility);
            }
        }
        displayFacilitiesList();
    }    private void displayFacilitiesList() {
        facilitiesGridContainer.getChildren().clear();
        if (currentFacilitiesList.isEmpty()) {
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            emptyBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
            
            Label emptyIcon = new Label("🏢");
            emptyIcon.setStyle("-fx-font-size: 48px;");
            
            Label noFacilityLabel = new Label("해당하는 부대시설이 없습니다.");
            noFacilityLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #7f8c8d;");
            
            emptyBox.getChildren().addAll(emptyIcon, noFacilityLabel);
            facilitiesGridContainer.getChildren().add(emptyBox);
        } else {
            for (Facilities facility : currentFacilitiesList) {
                facilitiesGridContainer.getChildren().add(createFacilityCard(facility));
            }
        }
    }

    private VBox createFacilityCard(Facilities facility) {
        // 이미지뷰 생성
        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 2);");

        // 이미지 로드
        try {
            File imageFile = new File(facility.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                
                // 이미지의 실제 크기
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                
                // 정사각형으로 자르기 위한 계산
                double size = Math.min(imageWidth, imageHeight);
                double offsetX = (imageWidth - size) / 2;
                double offsetY = (imageHeight - size) / 2;
                
                // 뷰포트 설정
                Rectangle2D viewport = new Rectangle2D(offsetX, offsetY, size, size);
                imageView.setViewport(viewport);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // 빈 이미지
        }

        // 부대시설 정보
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(facility.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");
        nameLabel.setMaxWidth(180);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);
        
        HBox timeBox = new HBox(5);
        timeBox.setAlignment(Pos.CENTER);
        Label timeIcon = new Label("🕐");
        Label timeLabel = new Label(String.format("%02d:00 - %02d:00",
            facility.getOpeningTime().getHour(),
            facility.getClosingTime().getHour()
        ));
        timeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");
        timeBox.getChildren().addAll(timeIcon, timeLabel);
        
        Label reservationLabel = new Label(facility.isRequireReservation() ? "예약 필요" : "예약 불필요");
        if (facility.isRequireReservation()) {
            reservationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #3498db; -fx-padding: 4 12; -fx-background-radius: 12;");
        } else {
            reservationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #27ae60; -fx-padding: 4 12; -fx-background-radius: 12;");
        }
        
        infoBox.getChildren().addAll(nameLabel, timeBox, reservationLabel);

        // 카드 레이아웃
        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(210);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
            "-fx-cursor: hand;"
        );
        card.getChildren().addAll(imageView, infoBox);
        
        // 마우스 호버 효과
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(52, 152, 219, 0.4), 15, 0, 0, 5); " +
                "-fx-cursor: hand; " +
                "-fx-scale-x: 1.03; " +
                "-fx-scale-y: 1.03;"
            );
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                "-fx-cursor: hand;"
            );
        });
        
        return card;
    }
}
