package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.facilities.Facilities;
import org.example.domain.facilities.FacilitiesController;
import org.example.domain.pension.Pension;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FacilitiesView {
    private final Pension pension;
    private final Stage stage;
    private final FacilitiesController facilitiesController;
    private final Customer customer;
    private FlowPane facilitiesGridContainer;
    private List<Facilities> currentFacilitiesList;
    private List<Button> filterButtons;
    private Button activeFilterButton;

    public FacilitiesView(Pension pension, Customer customer, Stage stage) {
        this.pension = pension;
        this.customer = customer;
        this.stage = stage;
        this.facilitiesController = FacilitiesController.getInstance();
        this.filterButtons = new ArrayList<>();
    }

    public void show() {
        stage.setTitle("StayOn - 부대시설");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 필터 섹션
        HBox filterSection = createFilterSection();

        // 부대시설 그리드
        facilitiesGridContainer = new FlowPane();
        facilitiesGridContainer.setHgap(25);
        facilitiesGridContainer.setVgap(25);
        facilitiesGridContainer.setPadding(new Insets(30, 50, 50, 50));
        facilitiesGridContainer.setAlignment(Pos.CENTER);
        facilitiesGridContainer.setStyle("-fx-background-color: transparent;");

        updateFacilitiesList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(facilitiesGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-background-color: #f8fafc; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);        mainContainer.getChildren().addAll(header, heroSection, filterSection, scrollPane);

        Scene scene = new Scene(mainContainer, 1200, 800);

        // 폰트 로드 및 적용
        FontUtil.loadFont();
        mainContainer.setStyle("-fx-font-family: '" + FontUtil.getFontFamily() + "';");

        stage.setScene(scene);
        stage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 40, 15, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        Button backButton = new Button("← 펜션 정보로");
        backButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-text-fill: #2563eb; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-color: #2563eb; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        ));        backButton.setOnAction(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, customer, stage);
            detailView.show();
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
        hero.setStyle("-fx-background-color: linear-gradient(to right, #7c3aed, #a855f7);");

        Label titleLabel = new Label("🏊 부대시설");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label(pension.getName() + "의 다양한 부대시설을 만나보세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private HBox createFilterSection() {
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.setPadding(new Insets(20, 50, 10, 50));
        filterBox.setStyle("-fx-background-color: #f8fafc;");

        Button showAllBtn = createFilterButton("전체", true);
        showAllBtn.setOnAction(e -> {
            setActiveFilter(showAllBtn);
            updateFacilitiesList();
        });

        Button poolBtn = createFilterButton("🏊 수영장", false);
        poolBtn.setOnAction(e -> {
            setActiveFilter(poolBtn);
            filterByName("수영장");
        });

        Button golfBtn = createFilterButton("⛳ 골프장", false);
        golfBtn.setOnAction(e -> {
            setActiveFilter(golfBtn);
            filterByName("골프장");
        });

        Button gymBtn = createFilterButton("💪 헬스장", false);
        gymBtn.setOnAction(e -> {
            setActiveFilter(gymBtn);
            filterByName("헬스장");
        });

        Button tennisBtn = createFilterButton("🎾 테니스장", false);
        tennisBtn.setOnAction(e -> {
            setActiveFilter(tennisBtn);
            filterByName("테니스장");
        });

        Button saunaBtn = createFilterButton("♨️ 사우나", false);
        saunaBtn.setOnAction(e -> {
            setActiveFilter(saunaBtn);
            filterByName("사우나");
        });

        filterButtons.addAll(List.of(showAllBtn, poolBtn, golfBtn, gymBtn, tennisBtn, saunaBtn));
        setActiveFilter(showAllBtn);

        filterBox.getChildren().addAll(showAllBtn, poolBtn, golfBtn, gymBtn, tennisBtn, saunaBtn);

        return filterBox;
    }

    private Button createFilterButton(String text, boolean primary) {
        Button btn = new Button(text);
        btn.setStyle(getInactiveFilterStyle());
        btn.setOnMouseEntered(e -> {
            if (btn != activeFilterButton) {
                btn.setStyle(getHoverFilterStyle());
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeFilterButton) {
                btn.setStyle(getInactiveFilterStyle());
            }
        });
        return btn;
    }

    private void setActiveFilter(Button btn) {
        if (activeFilterButton != null) {
            activeFilterButton.setStyle(getInactiveFilterStyle());
        }
        activeFilterButton = btn;
        btn.setStyle(getActiveFilterStyle());
    }

    private String getActiveFilterStyle() {
        return "-fx-background-color: #7c3aed; " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private String getInactiveFilterStyle() {
        return "-fx-background-color: white; " +
               "-fx-text-fill: #64748b; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private String getHoverFilterStyle() {
        return "-fx-background-color: #f5f3ff; " +
               "-fx-text-fill: #7c3aed; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #7c3aed; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
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
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(60));
            emptyBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
            );

            Label emptyIcon = new Label("🏢");
            emptyIcon.setStyle("-fx-font-size: 48px;");

            Label noFacilityLabel = new Label("해당하는 부대시설이 없습니다.");
            noFacilityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #94a3b8;");

            emptyBox.getChildren().addAll(emptyIcon, noFacilityLabel);
            facilitiesGridContainer.getChildren().add(emptyBox);
        } else {
            for (Facilities facility : currentFacilitiesList) {
                facilitiesGridContainer.getChildren().add(createFacilityCard(facility));
            }
        }
    }

    private VBox createFacilityCard(Facilities facility) {
        VBox card = new VBox(0);
        card.setMaxWidth(260);
        card.setMinWidth(260);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        // 이미지 컨테이너
        StackPane imageContainer = new StackPane();
        imageContainer.setMinHeight(180);
        imageContainer.setMaxHeight(180);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(260);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);

        try {
            File imageFile = new File(facility.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                double size = Math.min(imageWidth, imageHeight);
                double offsetX = (imageWidth - size) / 2;
                double offsetY = (imageHeight - size) / 2;
                Rectangle2D viewport = new Rectangle2D(offsetX, offsetY, size, size);
                imageView.setViewport(viewport);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            imageContainer.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 16 16 0 0;");
        }

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(260, 180);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        imageView.setClip(clip);

        imageContainer.getChildren().add(imageView);

        // 정보 영역
        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(18));        Label nameLabel = new Label(facility.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox statusBox = new HBox(8);
        statusBox.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("✅ 이용 가능");
        statusLabel.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-text-fill: #10b981; " +
            "-fx-padding: 4 12; " +
            "-fx-background-color: #d1fae5; " +
            "-fx-background-radius: 20;"
        );

        statusBox.getChildren().add(statusLabel);

        infoBox.getChildren().addAll(nameLabel, statusBox);
        card.getChildren().addAll(imageContainer, infoBox);

        // 호버 효과
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(124, 58, 237, 0.25), 25, 0, 0, 8); " +
            "-fx-scale-x: 1.02; " +
            "-fx-scale-y: 1.02;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        ));

        return card;
    }
}
