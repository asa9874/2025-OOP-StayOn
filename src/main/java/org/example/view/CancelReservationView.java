package org.example.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;
import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.ReservationController;

public class CancelReservationView {
    private final Stage stage;
    private final Pension pension;    private final Room room;
    private final Customer customer;
    private final int selectedCount;
    private final Reservation reservation;
    private final ReservationController reservationController;

    public CancelReservationView(Pension pension, Room room, Customer customer, int selectedCount, Stage stage) {
        this(pension, room, customer, selectedCount, stage, null);
    }

    public CancelReservationView(Pension pension, Room room, Customer customer, int selectedCount, Stage stage, Reservation reservation) {
        this.pension = pension;
        this.room = room;
        this.customer = customer;
        this.selectedCount = selectedCount;
        this.stage = stage;
        this.reservation = reservation;
        this.reservationController = ReservationController.getInstance();
    }

    public void show() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDaysLater = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");
        String formattedDateTime = now.format(formatter);
        String formattedDateTimeLater = oneDaysLater.format(formatter);

        stage.setTitle("StayOn - 예약 취소");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 콘텐츠 영역
        HBox contentBox = new HBox(30);
        contentBox.setPadding(new Insets(30, 50, 50, 50));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // 왼쪽: 예약 정보 카드
        VBox reservationCard = createReservationCard(formattedDateTime, formattedDateTimeLater);

        // 오른쪽: 취소 사유 카드
        VBox cancelCard = createCancelCard();

        contentBox.getChildren().addAll(reservationCard, cancelCard);

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
        hero.setStyle("-fx-background-color: linear-gradient(to right, #ef4444, #dc2626);");

        Label titleLabel = new Label("❌ 예약 취소");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("예약 취소 사유를 선택해 주세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private VBox createReservationCard(String checkIn, String checkOut) {
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

        // 예약 정보
        VBox infoGrid = new VBox(12);

        HBox roomRow = createInfoRow("🛏️ 객실", room.getRoomName());
        HBox peopleRow = createInfoRow("👥 투숙 인원", (room.getMaxPeople() * selectedCount) + "명");
        HBox checkInRow = createInfoRow("📅 체크인", checkIn);
        HBox checkOutRow = createInfoRow("📅 체크아웃", checkOut);

        infoGrid.getChildren().addAll(roomRow, peopleRow, checkInRow, checkOutRow);

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

    private VBox createCancelCard() {
        VBox card = new VBox(20);
        card.setMinWidth(450);
        card.setMaxWidth(450);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Label sectionTitle = new Label("📝 취소 사유");
        sectionTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // 취소 사유 그리드
        ToggleGroup cancelToggleGroup = new ToggleGroup();

        GridPane reasonGrid = new GridPane();
        reasonGrid.setHgap(12);
        reasonGrid.setVgap(12);

        String[] reasons = {"📅 일정 변경", "❌ 예약 실수", "🏨 다른 숙소 예약", "👤 개인 사정", "💰 가격 문제", "📋 기타"};

        for (int i = 0; i < reasons.length; i++) {
            ToggleButton btn = createReasonButton(reasons[i], cancelToggleGroup);
            reasonGrid.add(btn, i % 2, i / 2);
        }

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        // 환불 정보
        Label refundTitle = new Label("💰 환불 정보");
        refundTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        int totalPrice = room.getPrice() * selectedCount;

        VBox priceBox = new VBox(12);
        priceBox.setPadding(new Insets(15));
        priceBox.setStyle("-fx-background-color: #fef2f2; -fx-background-radius: 12;");

        HBox priceRow = createPriceRow("예약 금액", String.format("%,d원", totalPrice), "#64748b");
        HBox cancelRow = createPriceRow("취소 금액", String.format("-%,d원", totalPrice), "#ef4444");

        Region priceDivider = new Region();
        priceDivider.setStyle("-fx-background-color: #fecaca;");
        priceDivider.setMinHeight(1);
        priceDivider.setMaxHeight(1);

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        Label totalLabel = new Label("환불 예정액");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalValue = new Label(String.format("%,d원", totalPrice));
        totalValue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ef4444;");
        totalRow.getChildren().addAll(totalLabel, spacer, totalValue);

        priceBox.getChildren().addAll(priceRow, cancelRow, priceDivider, totalRow);

        // 취소 버튼
        Button cancelButton = new Button("예약 취소하기");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #ef4444, #dc2626); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #dc2626, #b91c1c); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #ef4444, #dc2626); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15 30; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        cancelButton.setOnAction(e -> {
            if (cancelToggleGroup.getSelectedToggle() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("경고");
                alert.setHeaderText(null);
                alert.setContentText("취소 사유를 선택해주세요.");
                alert.showAndWait();
                return;
            }

//            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
//            confirmAlert.setTitle("예약 취소 확인");
//            confirmAlert.setHeaderText(null);
//            confirmAlert.setContentText("정말로 예약을 취소하시겠습니까?");
//            confirmAlert.showAndWait().ifPresent(response -> {
//                if (response == ButtonType.OK) {
                    // 예약이 있으면 취소 처리
                    if (reservation != null) {
                        try {
                            reservationController.cancel(reservation.getId());
                        } catch (Exception ex) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("오류");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("예약 취소 중 오류가 발생했습니다: " + ex.getMessage());
                            errorAlert.showAndWait();
                            return;
                        }
                    }
                    
//                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
//                    successAlert.setTitle("예약 취소 완료");
//                    successAlert.setHeaderText("예약이 취소되었습니다.");
//                    successAlert.setContentText("환불 금액: " + String.format("%,d원", totalPrice));
//                    successAlert.showAndWait();

                    // 예약 내역 페이지로 이동
                    ReservationListView reservationListView = new ReservationListView(customer, stage);
                    reservationListView.show();
//                }
//            });
        });

        // 돌아가기 버튼
        Button backButton = new Button("← 돌아가기");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 12 30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-text-fill: #1e293b; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 12 30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #cbd5e1; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 12 30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12; " +
            "-fx-cursor: hand;"
        ));
        backButton.setOnAction(e -> {
            ConfirmReservationView confirmView = new ConfirmReservationView(pension, room, customer, selectedCount, stage);
            confirmView.show();
        });

        card.getChildren().addAll(sectionTitle, reasonGrid, divider, refundTitle, priceBox, cancelButton, backButton);

        return card;
    }

    private ToggleButton createReasonButton(String text, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setPrefWidth(195);
        btn.setPrefHeight(50);
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
                    "-fx-background-color: #fef2f2; " +
                    "-fx-text-fill: #ef4444; " +
                    "-fx-font-size: 13px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: #ef4444; " +
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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 14px; -fx-text-fill: " + color + ";");

        row.getChildren().addAll(labelNode, spacer, valueNode);
        return row;
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
