package org.example.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import org.example.domain.pension.PensionController;
import org.example.domain.room.RoomController;
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;
import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.ReservationController;
import org.example.domain.reservation.ReservationStatus;
import org.example.domain.reservation.dto.ReservationRequestDTO;

public class PaymentView extends Application {
    private final PensionController pensionController;
    private final RoomController roomController;
    private final ReservationController reservationController;
    private int roomId;
    private int pensionId;    
    private int selectedCount;
    private Customer customer;
    private Reservation reservation;
    private boolean existingReservation = false;

    public PaymentView(int pensionId, int roomId, int selectedCount) {
        this(pensionId, roomId, selectedCount, null);
    }

    public PaymentView(int pensionId, int roomId, int selectedCount, Customer customer) {
        this.pensionController = PensionController.getInstance();
        this.roomController = RoomController.getInstance();
        this.reservationController = ReservationController.getInstance();
        this.roomId = roomId;
        this.pensionId = pensionId;
        this.selectedCount = selectedCount;
        this.customer = customer;
        this.existingReservation = false;
    }

    // 기존 예약으로 결제하는 경우
    public PaymentView(int pensionId, int roomId, int selectedCount, Customer customer, Reservation reservation) {
        this(pensionId, roomId, selectedCount, customer);
        this.reservation = reservation;
        this.existingReservation = true;
    }

    @Override
    public void start(Stage stage) {
        Pension pension = pensionController.findById(pensionId);
        Room room = roomController.findById(roomId);
        
        // 기존 예약이 없는 경우에만 PENDING 상태로 예약 생성
        if (!existingReservation) {
            ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                room,
                customer,
                ReservationStatus.PENDING
            );
            this.reservation = reservationController.save(requestDTO);
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDaysLater = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");
        String formattedDateTime = now.format(formatter);
        String formattedDateTimeLater = oneDaysLater.format(formatter);

        stage.setTitle("StayOn - 결제하기");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader(pension, stage);

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 콘텐츠 영역
        HBox contentBox = new HBox(30);
        contentBox.setPadding(new Insets(30, 50, 50, 50));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // 왼쪽: 예약 정보 카드
        VBox reservationCard = createReservationCard(pension, room, formattedDateTime, formattedDateTimeLater);

        // 오른쪽: 결제 정보 카드
        VBox paymentCard = createPaymentCard(pension, room, stage);

        contentBox.getChildren().addAll(reservationCard, paymentCard);

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

    private HBox createHeader(Pension pension, Stage stage) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 40, 15, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");        Button backButton = new Button("← 예약 내역으로");
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
        ));
        backButton.setOnAction(e -> {
            ReservationListView reservationListView = new ReservationListView(customer, stage);
            reservationListView.show();
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
        hero.setStyle("-fx-background-color: linear-gradient(to right, #10b981, #059669);");

        Label titleLabel = new Label("💳 결제하기");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("예약 정보를 확인하고 결제를 진행해 주세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private VBox createReservationCard(Pension pension, Room room, String checkIn, String checkOut) {
        VBox card = new VBox(20);
        card.setMinWidth(450);
        card.setMaxWidth(450);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("📋 예약 정보");
        sectionTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(200);
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

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(400, 200);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        // 펜션 정보
        VBox pensionInfo = new VBox(8);
        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label pensionAddressLabel = new Label("📍 " + pension.getAddress());
        pensionAddressLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        pensionInfo.getChildren().addAll(pensionNameLabel, pensionAddressLabel);

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        // 객실 정보
        VBox infoGrid = new VBox(12);

        HBox roomRow = createInfoRow("🛏️ 객실", room.getRoomName());
        HBox countRow = createInfoRow("🔢 객실 수", selectedCount + "개");
        HBox peopleRow = createInfoRow("👥 투숙 인원", (room.getMaxPeople() * selectedCount) + "명");
        HBox checkInRow = createInfoRow("📅 체크인", checkIn);
        HBox checkOutRow = createInfoRow("📅 체크아웃", checkOut);

        infoGrid.getChildren().addAll(roomRow, countRow, peopleRow, checkInRow, checkOutRow);

        card.getChildren().addAll(sectionTitle, imageView, pensionInfo, divider, infoGrid);

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

    private VBox createPaymentCard(Pension pension, Room room, Stage stage) {
        VBox card = new VBox(20);
        card.setMinWidth(450);
        card.setMaxWidth(450);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("💳 결제 수단");
        sectionTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // 결제 수단 그리드
        ToggleGroup paymentToggleGroup = new ToggleGroup();

        GridPane paymentGrid = new GridPane();
        paymentGrid.setHgap(12);
        paymentGrid.setVgap(12);

        String[] paymentMethods = {"국민카드", "신한카드", "현대카드", "삼성카드", "카카오페이", "토스뱅크"};
        String[] icons = {"💳", "💳", "💳", "💳", "🟡", "🔵"};

        for (int i = 0; i < paymentMethods.length; i++) {
            ToggleButton btn = createPaymentButton(paymentMethods[i], icons[i], paymentToggleGroup);
            paymentGrid.add(btn, i % 3, i / 3);
        }

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        // 금액 정보
        Label priceTitle = new Label("💰 결제 금액");
        priceTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        int totalPrice = room.getPrice() * selectedCount;

        VBox priceBox = new VBox(12);
        priceBox.setPadding(new Insets(15));
        priceBox.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 12;");

        HBox priceRow = createPriceRow("객실 요금", String.format("%,d원", room.getPrice()) + " × " + selectedCount, "#64748b");
        HBox discountRow = createPriceRow("할인 금액", "0원", "#10b981");
        
        Region priceDivider = new Region();
        priceDivider.setStyle("-fx-background-color: #e2e8f0;");
        priceDivider.setMinHeight(1);
        priceDivider.setMaxHeight(1);

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        Label totalLabel = new Label("최종 결제액");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        totalLabel.setMinWidth(120);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalValue = new Label(String.format("%,d원", totalPrice));
        totalValue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
        totalRow.getChildren().addAll(totalLabel, spacer, totalValue);

        priceBox.getChildren().addAll(priceRow, discountRow, priceDivider, totalRow);

        // 결제 버튼
        Button paymentButton = new Button("결제하기");
        paymentButton.setMaxWidth(Double.MAX_VALUE);
        paymentButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #10b981, #059669); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        paymentButton.setOnMouseEntered(e -> paymentButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #059669, #047857); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        paymentButton.setOnMouseExited(e -> paymentButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #10b981, #059669); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));        paymentButton.setOnAction(e -> {
            int paymentAmount = room.getPrice() * selectedCount;
            
            // 잔액 확인
            if (customer.getMoney() < paymentAmount) {
                Alert insufficientAlert = new Alert(Alert.AlertType.WARNING);
                insufficientAlert.setTitle("잔액 부족");
                insufficientAlert.setHeaderText("💸 잔액이 부족합니다!");
                insufficientAlert.setContentText(
                    "결제 금액: " + String.format("%,d원", paymentAmount) + "\n" +
                    "현재 잔액: " + String.format("%,d원", customer.getMoney()) + "\n" +
                    "부족 금액: " + String.format("%,d원", paymentAmount - customer.getMoney())
                );
                insufficientAlert.showAndWait();
                return;
            }
            
            // 잔액 차감
            try {
                customer.subtractMoney(paymentAmount);
            } catch (IllegalArgumentException ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("결제 실패");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText(ex.getMessage());
                errorAlert.showAndWait();
                return;
            }
            
            // 결제 완료 - 예약 상태를 CONFIRMED로 변경
            reservationController.pay(reservation.getId());
            
//            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
//            successAlert.setTitle("결제 완료");
//            successAlert.setHeaderText("🎉 결제가 완료되었습니다!");
//            successAlert.setContentText(
//                "예약 번호: " + reservation.getId() + "\n" +
//                "결제 금액: " + String.format("%,d원", paymentAmount) + "\n" +
//                "남은 잔액: " + String.format("%,d원", customer.getMoney())
//            );
//            successAlert.showAndWait();
            
            ConfirmReservationView confirmReservationView = new ConfirmReservationView(pension, room, customer, selectedCount, stage);
            confirmReservationView.show();
        });

        card.getChildren().addAll(sectionTitle, paymentGrid, divider, priceTitle, priceBox, paymentButton);

        return card;
    }

    private ToggleButton createPaymentButton(String text, String icon, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(icon + " " + text);
        btn.setToggleGroup(group);
        btn.setPrefWidth(130);
        btn.setPrefHeight(60);
        btn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #1e293b; " +
            "-fx-font-size: 13px; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        );

        btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                btn.setStyle(
                    "-fx-background-color: #eff6ff; " +
                    "-fx-text-fill: #2563eb; " +
                    "-fx-font-size: 13px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: #2563eb; " +
                    "-fx-border-radius: 12; " +
                    "-fx-border-width: 2; " +
                    "-fx-cursor: hand;"
                );
            } else {
                btn.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-text-fill: #1e293b; " +
                    "-fx-font-size: 13px; " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: #e2e8f0; " +
                    "-fx-border-radius: 12; " +
                    "-fx-cursor: hand;"
                );
            }
        });

        return btn;
    }

    private HBox createPriceRow(String label, String value, String color) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        labelNode.setMinWidth(120);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 14px; -fx-text-fill: " + color + ";");

        row.getChildren().addAll(labelNode, spacer, valueNode);
        return row;
    }
}
