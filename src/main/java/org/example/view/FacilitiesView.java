package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    }

    public void show() {
        stage.setTitle(pension.getName() + " - 부대시설");

        // 뒤로가기 버튼
        Button backButton = new Button("← 펜션 정보로");
        backButton.setOnAction(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, stage);
            detailView.show();
        });

        // 펜션 정보 요약
        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 필터 버튼들
        Button showAllButton = new Button("전체 시설");
        showAllButton.setOnAction(e -> updateFacilitiesList());

        Button filterPoolButton = new Button("수영장");
        filterPoolButton.setOnAction(e -> filterByName("수영장"));

        Button filterGolfButton = new Button("골프장");
        filterGolfButton.setOnAction(e -> filterByName("골프장"));

        Button filterGymButton = new Button("헬스장");
        filterGymButton.setOnAction(e -> filterByName("헬스장"));

        Button filterTennisButton = new Button("테니스장");
        filterTennisButton.setOnAction(e -> filterByName("테니스장"));

        Button filterSaunaButton = new Button("사우나");
        filterSaunaButton.setOnAction(e -> filterByName("사우나"));

        HBox filterBox = new HBox(10);
        filterBox.getChildren().addAll(
            new Label("필터:"),
            showAllButton,
            filterPoolButton,
            filterGolfButton,
            filterGymButton,
            filterTennisButton,
            filterSaunaButton
        );

        // 부대시설 그리드 컨테이너
        facilitiesGridContainer = new FlowPane();
        facilitiesGridContainer.setHgap(20);
        facilitiesGridContainer.setVgap(20);
        facilitiesGridContainer.setPadding(new Insets(10));
        updateFacilitiesList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(facilitiesGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        // 레이아웃
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(
            backButton,
            new Separator(),
            pensionNameLabel,
            new Separator(),
            filterBox,
            new Label("부대시설 목록:"),
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 900, 700);
        stage.setScene(scene);
        stage.show();
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
    }

    private void displayFacilitiesList() {
        facilitiesGridContainer.getChildren().clear();
        if (currentFacilitiesList.isEmpty()) {
            Label noFacilityLabel = new Label("해당하는 부대시설이 없습니다.");
            noFacilityLabel.setStyle("-fx-font-size: 14px;");
            facilitiesGridContainer.getChildren().add(noFacilityLabel);
        } else {
            for (Facilities facility : currentFacilitiesList) {
                facilitiesGridContainer.getChildren().add(createFacilityCard(facility));
            }
        }
    }

    private VBox createFacilityCard(Facilities facility) {
        // 이미지뷰 생성
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);

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
        infoBox.setPadding(new Insets(10));
        
        Label nameLabel = new Label(facility.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        
        Label timeLabel = new Label(String.format("운영시간: %02d:00 - %02d:00",
            facility.getOpeningTime().getHour(),
            facility.getClosingTime().getHour()
        ));
        timeLabel.setStyle("-fx-font-size: 14px;");
        
        Label reservationLabel = new Label(facility.isRequireReservation() ? "예약 필요" : "예약 불필요");
        if (facility.isRequireReservation()) {
            reservationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0066cc; -fx-font-weight: bold;");
        } else {
            reservationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green; -fx-font-weight: bold;");
        }
        
        infoBox.getChildren().addAll(nameLabel, timeLabel, reservationLabel);

        // 카드 레이아웃
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        card.getChildren().addAll(imageView, infoBox);
        
        // 마우스 호버 효과
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-border-color: #0066cc; -fx-border-width: 2; -fx-background-color: #e6f2ff;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        });
        
        return card;
    }
}
