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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
import org.example.domain.review.Review;
import org.example.domain.review.ReviewController;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PensionDetailView {
    private final Pension pension;
    private final Stage stage;
    private final RoomController roomController;
    private final ReviewController reviewController;
    private final Customer customer;

    public PensionDetailView(Pension pension, Customer customer, Stage stage) {
        this.pension = pension;
        this.customer = customer;
        this.stage = stage;
        this.roomController = RoomController.getInstance();
        this.reviewController = ReviewController.getInstance();
    }

    public void show() {
        stage.setTitle("StayOn - " + pension.getName());

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 펜션 히어로 섹션
        VBox heroSection = createHeroSection();

        // 콘텐츠 영역
        VBox contentSection = new VBox(30);
        contentSection.setPadding(new Insets(40, 60, 60, 60));
        contentSection.setStyle("-fx-background-color: #f8fafc;");

        // 펜션 정보 카드
        HBox pensionInfoCard = createPensionInfoCard();

        // 액션 버튼들
        HBox actionButtons = createActionButtons();

        // 객실 섹션
        VBox roomSection = createRoomSection();

        contentSection.getChildren().addAll(pensionInfoCard, actionButtons, roomSection);

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(contentSection);
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

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 40, 15, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        // 뒤로가기 버튼
        Button backButton = new Button("← 목록으로");
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
            PensionView pensionView = new PensionView(customer);
            pensionView.start(stage);
        });

        // 로고
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
        VBox hero = new VBox(0);
        hero.setMinHeight(280);
        hero.setMaxHeight(280);

        StackPane imageContainer = new StackPane();
        imageContainer.setMinHeight(280);

        // 배경 이미지
        ImageView bgImage = new ImageView();
        bgImage.setFitWidth(1100);
        bgImage.setFitHeight(280);
        bgImage.setPreserveRatio(false);

        try {
            File imageFile = new File(pension.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                bgImage.setImage(image);
            }
        } catch (Exception e) {
            imageContainer.setStyle("-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed);");
        }

        // 오버레이
        VBox overlay = new VBox(15);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlay.setPadding(new Insets(40));

        Label nameLabel = new Label(pension.getName());
        nameLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label addressLabel = new Label("📍 " + pension.getAddress());
        addressLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.9);");

        // 평점 정보
        int[] reviewInfo = getPensionReviewInfo();
        int reviewCount = reviewInfo[0];
        double avgRate = reviewInfo[1] / 10.0;

        HBox ratingBox = new HBox(15);
        ratingBox.setAlignment(Pos.CENTER);

        Label starLabel = new Label("⭐ " + (reviewCount > 0 ? String.format("%.1f", avgRate) : "-"));
        starLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label reviewLabel = new Label("(" + reviewCount + "개 리뷰)");
        reviewLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.8);");

        ratingBox.getChildren().addAll(starLabel, reviewLabel);

        overlay.getChildren().addAll(nameLabel, addressLabel, ratingBox);

        imageContainer.getChildren().addAll(bgImage, overlay);
        hero.getChildren().add(imageContainer);

        return hero;
    }

    private HBox createPensionInfoCard() {
        HBox card = new HBox(40);
        card.setPadding(new Insets(30));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        // 정보 그리드
        VBox infoCol1 = new VBox(20);
        VBox infoCol2 = new VBox(20);

        infoCol1.getChildren().addAll(
            createInfoItem("📞", "연락처", pension.getPhoneNumber()),
            createInfoItem("🏠", "주소", pension.getAddress())
        );

        infoCol2.getChildren().addAll(
            createInfoItem("📝", "소개", pension.getDescription() != null ? pension.getDescription() : "정보 없음"),
            createInfoItem("💰", "최저가", getMinPriceText())
        );

        HBox.setHgrow(infoCol1, Priority.ALWAYS);
        HBox.setHgrow(infoCol2, Priority.ALWAYS);

        card.getChildren().addAll(infoCol1, infoCol2);

        return card;
    }

    private VBox createInfoItem(String icon, String label, String value) {
        VBox item = new VBox(5);

        HBox labelBox = new HBox(8);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");

        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b; -fx-font-weight: bold;");

        labelBox.getChildren().addAll(iconLabel, titleLabel);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #1e293b;");
        valueLabel.setWrapText(true);
        valueLabel.setMaxWidth(400);

        item.getChildren().addAll(labelBox, valueLabel);

        return item;
    }

    private HBox createActionButtons() {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button reserveButton = new Button("🏠 객실 예약하기");
        reserveButton.setPrefWidth(220);
        reserveButton.setPrefHeight(55);
        reserveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #2563eb, #3b82f6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        reserveButton.setOnMouseEntered(e -> reserveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #1d4ed8, #2563eb); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        reserveButton.setOnMouseExited(e -> reserveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #2563eb, #3b82f6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));        reserveButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView(pension, customer, stage);
            roomSelectView.show();
        });

        Button facilitiesButton = new Button("🏊 부대시설 조회");
        facilitiesButton.setPrefWidth(220);
        facilitiesButton.setPrefHeight(55);
        facilitiesButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #7c3aed, #8b5cf6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        facilitiesButton.setOnMouseEntered(e -> facilitiesButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #6d28d9, #7c3aed); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        facilitiesButton.setOnMouseExited(e -> facilitiesButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #7c3aed, #8b5cf6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));        facilitiesButton.setOnAction(e -> {
            FacilitiesView facilitiesView = new FacilitiesView(pension, customer, stage);
            facilitiesView.show();
        });

        Button reviewButton = new Button("⭐ 후기 조회");
        reviewButton.setPrefWidth(220);
        reviewButton.setPrefHeight(55);
        reviewButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        reviewButton.setOnMouseEntered(e -> reviewButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #db2777, #e11d48); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        reviewButton.setOnMouseExited(e -> reviewButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        reviewButton.setOnAction(e -> {
            ReviewView reviewView = new ReviewView(pension, customer, stage);
            reviewView.show();
        });

        buttonBox.getChildren().addAll(reserveButton, facilitiesButton, reviewButton);

        return buttonBox;
    }

    private VBox createRoomSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(30));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label titleLabel = new Label("객실 목록");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox roomList = new VBox(15);

        List<Room> rooms = roomController.findByPensionId(pension.getId());
        if (rooms.isEmpty()) {
            Label noRoomLabel = new Label("등록된 객실이 없습니다.");
            noRoomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8;");
            roomList.getChildren().add(noRoomLabel);
        } else {
            for (Room room : rooms) {
                roomList.getChildren().add(createRoomCard(room));
            }
        }

        section.getChildren().addAll(titleLabel, roomList);

        return section;
    }

    private HBox createRoomCard(Room room) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 12;"
        );

        // 객실 이미지
        ImageView roomImageView = new ImageView();
        roomImageView.setFitWidth(100);
        roomImageView.setFitHeight(100);
        roomImageView.setPreserveRatio(false);

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(100, 100);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        roomImageView.setClip(clip);

        try {
            File imageFile = new File(room.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                double size = Math.min(imageWidth, imageHeight);
                double offsetX = (imageWidth - size) / 2;
                double offsetY = (imageHeight - size) / 2;
                Rectangle2D viewport = new Rectangle2D(offsetX, offsetY, size, size);
                roomImageView.setViewport(viewport);
                roomImageView.setImage(image);
            }
        } catch (Exception e) {
            // 빈 이미지
        }

        // 객실 정보
        VBox infoBox = new VBox(8);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label nameLabel = new Label(room.getRoomName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox tagsBox = new HBox(10);

        Label typeLabel = new Label(getRoomTypeText(room.getRoomType()));
        typeLabel.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-text-fill: #2563eb; " +
            "-fx-padding: 4 12; " +
            "-fx-background-color: #eff6ff; " +
            "-fx-background-radius: 20;"
        );

        Label statusLabel = new Label(getRoomStatusText(room.getRoomStatus()));
        String statusColor = room.getRoomStatus() == org.example.domain.room.RoomStatus.CLEANING ? "#10b981" : "#f59e0b";
        String statusBg = room.getRoomStatus() == org.example.domain.room.RoomStatus.CLEANING ? "#d1fae5" : "#fef3c7";
        statusLabel.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-text-fill: " + statusColor + "; " +
            "-fx-padding: 4 12; " +
            "-fx-background-color: " + statusBg + "; " +
            "-fx-background-radius: 20;"
        );

        tagsBox.getChildren().addAll(typeLabel, statusLabel);

        Label detailLabel = new Label("최대 " + room.getMaxPeople() + "인 · " + room.getFloor() + "층 · " + room.getBuilding());
        detailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        infoBox.getChildren().addAll(nameLabel, tagsBox, detailLabel);

        // 가격
        VBox priceBox = new VBox(5);
        priceBox.setAlignment(Pos.CENTER_RIGHT);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        Label priceLabel = new Label("₩" + numberFormat.format(room.getPrice()));
        priceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

        Label perNightLabel = new Label("/ 1박");
        perNightLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        priceBox.getChildren().addAll(priceLabel, perNightLabel);

        card.getChildren().addAll(roomImageView, infoBox, priceBox);

        // 호버 효과
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #2563eb; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 12;"
        ));

        return card;
    }

    private int[] getPensionReviewInfo() {
        List<Room> rooms = roomController.findByPensionId(pension.getId());
        int totalReviews = 0;
        int totalRate = 0;

        for (Room room : rooms) {
            List<Review> reviews = reviewController.findByRoomId(room.getId());
            totalReviews += reviews.size();
            for (Review review : reviews) {
                totalRate += review.getRate();
            }
        }

        double avgRate = totalReviews > 0 ? (double) totalRate / totalReviews : 0;
        return new int[]{totalReviews, (int) Math.round(avgRate * 10)};
    }

    private String getMinPriceText() {
        List<Room> rooms = roomController.findByPensionId(pension.getId());
        int minPrice = Integer.MAX_VALUE;

        for (Room room : rooms) {
            if (room.getPrice() < minPrice) {
                minPrice = room.getPrice();
            }
        }

        if (minPrice == Integer.MAX_VALUE) return "가격 정보 없음";
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return "₩" + numberFormat.format(minPrice) + "~ / 1박";
    }

    private String getRoomStatusText(org.example.domain.room.RoomStatus status) {
        switch (status) {
            case RESERVATION: return "예약 중";
            case RESERVATIONED: return "예약완료";
            case USING: return "청소 중";
            case CHECKING: return "점검 중";
            case CLEANING: return "판매 중";
            case NOTSALES: return "판매 중지";
            default: return status.toString();
        }
    }

    private String getRoomTypeText(org.example.domain.room.RoomType type) {
        switch (type) {
            case DUPLEX: return "복층형";
            case SINGLE: return "독채형";
            case HOTEL: return "호텔형";
            default: return type.toString();
        }
    }
}
