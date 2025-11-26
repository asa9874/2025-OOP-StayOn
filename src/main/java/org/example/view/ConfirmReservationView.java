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
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfirmReservationView {
    private final Stage stage;
    private final Pension pension;    
    private final Room room;
    private final Customer customer;
    private final int selectedCount;

    public ConfirmReservationView(Pension pension, Room room, Customer customer, int selectedCount, Stage stage) {
        this.pension = pension;
        this.room = room;        
        this.customer = customer;
        this.selectedCount = selectedCount;
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("StayOn - 예약 확인");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 콘텐츠 영역
        VBox contentBox = new VBox(25);
        contentBox.setPadding(new Insets(30, 50, 50, 50));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // 예약 요약 카드
        HBox summaryCards = createSummaryCards();

        // 상세 정보 카드들
        HBox detailCards = new HBox(25);
        detailCards.setAlignment(Pos.TOP_CENTER);

        VBox reservationInfoCard = createReservationInfoCard();
        VBox customerInfoCard = createCustomerInfoCard();

        detailCards.getChildren().addAll(reservationInfoCard, customerInfoCard);

        // 버튼 영역
        HBox buttonBox = createButtonBox();

        contentBox.getChildren().addAll(summaryCards, detailCards, buttonBox);

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

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 40, 15, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        Button backButton = new Button("← 객실 선택으로");
        backButton.setStyle(getBackButtonStyle());
        backButton.setOnMouseEntered(e -> backButton.setStyle(getBackButtonHoverStyle()));
        backButton.setOnMouseExited(e -> backButton.setStyle(getBackButtonStyle()));        backButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView(pension, customer, stage);
            roomSelectView.show();
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
        hero.setStyle("-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed);");        Label titleLabel = new Label("✅ 예약 완료");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("예약이 완료되었습니다. 예약 정보를 확인해 주세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private HBox createSummaryCards() {
        HBox cards = new HBox(25);
        cards.setAlignment(Pos.CENTER);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOut = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");
        int totalPrice = room.getPrice() * selectedCount;

        // 펜션 이미지 카드
        VBox imageCard = new VBox(0);
        imageCard.setMinWidth(300);
        imageCard.setMaxWidth(300);
        imageCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);

        try {
            File imageFile = new File(pension.getImage());
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
            // 빈 이미지
        }

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(300, 180);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        imageView.setClip(clip);

        VBox imgInfoBox = new VBox(5);
        imgInfoBox.setPadding(new Insets(15));
        Label pensionName = new Label(pension.getName());
        pensionName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label roomName = new Label(room.getRoomName());
        roomName.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        imgInfoBox.getChildren().addAll(pensionName, roomName);

        imageCard.getChildren().addAll(imageView, imgInfoBox);

        // 체크인 카드
        VBox checkInCard = createSummaryCard("📅 체크인", now.format(formatter), "#10b981");

        // 체크아웃 카드
        VBox checkOutCard = createSummaryCard("📅 체크아웃", checkOut.format(formatter), "#f59e0b");

        // 결제 금액 카드
        VBox priceCard = createSummaryCard("💰 총 금액", String.format("%,d원", totalPrice), "#2563eb");

        cards.getChildren().addAll(imageCard, checkInCard, checkOutCard, priceCard);

        return cards;
    }

    private VBox createSummaryCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setMinWidth(150);
        card.setMinHeight(150);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);

        return card;
    }

    private VBox createReservationInfoCard() {
        VBox card = new VBox(15);
        card.setMinWidth(450);
        card.setMaxWidth(450);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("📋 예약 상세 정보");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOut = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");

        VBox infoGrid = new VBox(12);
        infoGrid.getChildren().addAll(
            createInfoRow("🏠 펜션 이름", pension.getName()),
            createInfoRow("📍 펜션 주소", pension.getAddress()),
            createInfoRow("📞 연락처", pension.getPhoneNumber()),
            createInfoRow("🛏️ 객실 이름", room.getRoomName()),
            createInfoRow("🏷️ 객실 타입", getRoomTypeText(room.getRoomType())),
            createInfoRow("👥 투숙 인원", (room.getMaxPeople() * selectedCount) + "명"),
            createInfoRow("🔢 객실 수", selectedCount + "개"),
            createInfoRow("📅 체크인", now.format(formatter)),
            createInfoRow("📅 체크아웃", checkOut.format(formatter))
        );

        card.getChildren().addAll(sectionTitle, divider, infoGrid);

        return card;
    }

    private VBox createCustomerInfoCard() {
        VBox card = new VBox(15);
        card.setMinWidth(450);
        card.setMaxWidth(450);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("👤 예약자 정보");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        VBox infoGrid = new VBox(12);
        if (customer != null) {
            infoGrid.getChildren().addAll(
                createInfoRow("👤 이름", customer.getName()),
                createInfoRow("📞 전화번호", customer.getPhone()),
                createInfoRow("✉️ 이메일", customer.getEmail())
            );
        } else {
            infoGrid.getChildren().addAll(
                createInfoRow("👤 이름", "게스트"),
                createInfoRow("📞 전화번호", "-"),
                createInfoRow("✉️ 이메일", "-")
            );
        }

        // 결제 정보
        Region divider2 = new Region();
        divider2.setStyle("-fx-background-color: #e2e8f0;");
        divider2.setMinHeight(1);
        divider2.setMaxHeight(1);

        Label paymentTitle = new Label("💳 결제 정보");
        paymentTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        int roomPrice = room.getPrice();
        int totalPrice = roomPrice * selectedCount;

        VBox priceBox = new VBox(10);
        priceBox.setPadding(new Insets(15));
        priceBox.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 12;");

        HBox priceRow = createPriceRow("객실 요금 (1개)", String.format("%,d원", roomPrice));
        HBox countRow = createPriceRow("객실 수", selectedCount + "개");

        Region priceDivider = new Region();
        priceDivider.setStyle("-fx-background-color: #e2e8f0;");
        priceDivider.setMinHeight(1);
        priceDivider.setMaxHeight(1);

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        Label totalLabel = new Label("총 결제 금액");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalValue = new Label(String.format("%,d원", totalPrice));
        totalValue.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
        totalRow.getChildren().addAll(totalLabel, spacer, totalValue);

        priceBox.getChildren().addAll(priceRow, countRow, priceDivider, totalRow);

        card.getChildren().addAll(sectionTitle, divider, infoGrid, divider2, paymentTitle, priceBox);

        return card;
    }

    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        labelNode.setMinWidth(120);

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }

    private HBox createPriceRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 14px; -fx-text-fill: #1e293b;");

        row.getChildren().addAll(labelNode, spacer, valueNode);
        return row;
    }    private HBox createButtonBox() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button reservationListButton = new Button("📋 예약 내역 보기");
        reservationListButton.setPrefWidth(200);
        reservationListButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #2563eb; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #2563eb; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        );
        reservationListButton.setOnMouseEntered(e -> reservationListButton.setStyle(
            "-fx-background-color: #eff6ff; " +
            "-fx-text-fill: #1d4ed8; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #1d4ed8; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        reservationListButton.setOnMouseExited(e -> reservationListButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #2563eb; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #2563eb; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        reservationListButton.setOnAction(e -> {
            ReservationListView reservationListView = new ReservationListView(customer, stage);
            reservationListView.show();
        });

        Button homeButton = new Button("🏠 홈으로");
        homeButton.setPrefWidth(200);
        homeButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #10b981, #059669); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        homeButton.setOnMouseEntered(e -> homeButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #059669, #047857); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        homeButton.setOnMouseExited(e -> homeButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #10b981, #059669); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        homeButton.setOnAction(e -> {
            PensionView pensionView = new PensionView(customer);
            pensionView.start(stage);
        });

        buttonBox.getChildren().addAll(reservationListButton, homeButton);

        return buttonBox;
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
    }    private String getBackButtonHoverStyle() {
        return "-fx-background-color: #f1f5f9; " +
               "-fx-text-fill: #2563eb; " +
               "-fx-font-size: 14px; " +
               "-fx-cursor: hand; " +
               "-fx-padding: 8 20; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 20; " +
               "-fx-background-radius: 20;";
    }

    private String getRoomTypeText(org.example.domain.room.RoomType type) {
        switch (type) {
            case DUPLEX:
                return "복층형";
            case SINGLE:
                return "독채형";
            case HOTEL:
                return "호텔형";
            default:
                return type.toString();
        }
    }
}
