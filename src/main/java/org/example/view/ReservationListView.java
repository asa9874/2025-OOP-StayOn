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
import org.example.domain.pension.PensionController;
import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.ReservationController;
import org.example.domain.reservation.ReservationStatus;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservationListView {
    private final Stage stage;    private final Customer customer;
    private final ReservationController reservationController;
    private final PensionController pensionController;
    private FlowPane reservationGridContainer;
    private List<Reservation> currentReservationList;
    private ReservationStatus currentFilter;
    private List<Button> filterButtons;
    private Button activeFilterButton;

    public ReservationListView(Customer customer, Stage stage) {        this.customer = customer;
        this.stage = stage;
        this.reservationController = ReservationController.getInstance();
        this.pensionController = PensionController.getInstance();
        this.currentFilter = null;
        this.filterButtons = new ArrayList<>();
    }

    public void show() {
        stage.setTitle("StayOn - 예약 내역");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 필터 섹션
        HBox filterSection = createFilterSection();

        // 예약 목록 그리드
        reservationGridContainer = new FlowPane();
        reservationGridContainer.setHgap(25);
        reservationGridContainer.setVgap(25);
        reservationGridContainer.setPadding(new Insets(30, 50, 50, 50));
        reservationGridContainer.setAlignment(Pos.TOP_CENTER);
        reservationGridContainer.setStyle("-fx-background-color: transparent;");

        updateReservationList(null);

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(reservationGridContainer);
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
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");        Button backButton = new Button("← 펜션 목록으로");
        backButton.setStyle(getBackButtonStyle());
        backButton.setOnMouseEntered(e -> backButton.setStyle(getBackButtonHoverStyle()));
        backButton.setOnMouseExited(e -> backButton.setStyle(getBackButtonStyle()));
        backButton.setOnAction(e -> {
            PensionView pensionView = new PensionView(customer);
            pensionView.start(stage);
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
        hero.setStyle("-fx-background-color: linear-gradient(to right, #f59e0b, #d97706);");

        Label titleLabel = new Label("📋 예약 내역");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("예약 내역을 확인하고 관리하세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private HBox createFilterSection() {
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.setPadding(new Insets(20, 50, 10, 50));
        filterBox.setStyle("-fx-background-color: #f8fafc;");

        Button showAllBtn = createFilterButton("전체");
        showAllBtn.setOnAction(e -> {
            setActiveFilter(showAllBtn);
            updateReservationList(null);
        });

        Button pendingBtn = createFilterButton("⏳ 예약 대기");
        pendingBtn.setOnAction(e -> {
            setActiveFilter(pendingBtn);
            updateReservationList(ReservationStatus.PENDING);
        });

        Button confirmedBtn = createFilterButton("✅ 예약 확정");
        confirmedBtn.setOnAction(e -> {
            setActiveFilter(confirmedBtn);
            updateReservationList(ReservationStatus.CONFIRMED);
        });

        Button cancelledBtn = createFilterButton("❌ 취소됨");
        cancelledBtn.setOnAction(e -> {
            setActiveFilter(cancelledBtn);
            updateReservationList(ReservationStatus.CANCELLED);
        });

        Button refundedBtn = createFilterButton("💰 환불 완료");
        refundedBtn.setOnAction(e -> {
            setActiveFilter(refundedBtn);
            updateReservationList(ReservationStatus.REFUNDED);
        });

        filterButtons.addAll(List.of(showAllBtn, pendingBtn, confirmedBtn, cancelledBtn, refundedBtn));
        setActiveFilter(showAllBtn);

        filterBox.getChildren().addAll(showAllBtn, pendingBtn, confirmedBtn, cancelledBtn, refundedBtn);

        return filterBox;
    }

    private Button createFilterButton(String text) {
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
        return "-fx-background-color: linear-gradient(to right, #f59e0b, #d97706); " +
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
        return "-fx-background-color: #fffbeb; " +
               "-fx-text-fill: #d97706; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #f59e0b; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
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

    private void updateReservationList(ReservationStatus filterStatus) {
        currentFilter = filterStatus;
        currentReservationList = reservationController.findByCustomer(customer);

        if (filterStatus != null) {
            currentReservationList = currentReservationList.stream()
                .filter(reservation -> reservation.getReservationStatus() == filterStatus)
                .toList();
        }

        displayReservationList();
    }

    private void displayReservationList() {
        reservationGridContainer.getChildren().clear();

        if (currentReservationList.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(60));
            emptyBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
            );

            Label emptyIcon = new Label("📋");
            emptyIcon.setStyle("-fx-font-size: 48px;");

            Label noReservationLabel = new Label("예약 내역이 없습니다.");
            noReservationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #94a3b8;");

            emptyBox.getChildren().addAll(emptyIcon, noReservationLabel);
            reservationGridContainer.getChildren().add(emptyBox);
        } else {
            // 카운트 레이블
            VBox countBox = new VBox();
            countBox.setMinWidth(1000);
            countBox.setPadding(new Insets(0, 0, 10, 0));
            Label countLabel = new Label("총 " + currentReservationList.size() + "건의 예약");
            countLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-font-weight: bold;");
            countBox.getChildren().add(countLabel);
            reservationGridContainer.getChildren().add(countBox);

            for (Reservation reservation : currentReservationList) {
                reservationGridContainer.getChildren().add(createReservationCard(reservation));
            }
        }
    }

    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(0);
        card.setMinWidth(1000);
        card.setMaxWidth(1000);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        Room room = reservation.getRoom();
        Pension pension = pensionController.findById(room.getPensionId());

        // 상단 영역: 이미지 + 정보 + 상태
        HBox topBox = new HBox(20);
        topBox.setPadding(new Insets(20));
        topBox.setAlignment(Pos.CENTER_LEFT);

        // 펜션 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(120);
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

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(180, 120);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        imageView.setClip(clip);

        // 정보 영역
        VBox infoBox = new VBox(8);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label reservationIdLabel = new Label("예약 번호: #" + reservation.getId());
        reservationIdLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #1e293b;");

        Label roomNameLabel = new Label("🛏️ " + room.getRoomName());
        roomNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        Label addressLabel = new Label("📍 " + pension.getAddress());
        addressLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        infoBox.getChildren().addAll(reservationIdLabel, pensionNameLabel, roomNameLabel, addressLabel);

        // 상태 태그 영역
        VBox statusBox = new VBox(10);
        statusBox.setAlignment(Pos.TOP_RIGHT);

        Label statusTag = new Label(getStatusText(reservation.getReservationStatus()));
        String statusColor = getStatusColor(reservation.getReservationStatus());
        String statusBg = getStatusBgColor(reservation.getReservationStatus());
        statusTag.setStyle(
            "-fx-background-color: " + statusBg + "; " +
            "-fx-text-fill: " + statusColor + "; " +
            "-fx-padding: 6 16; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold;"
        );

        Label priceLabel = new Label(String.format("%,d원", room.getPrice()));
        priceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

        statusBox.getChildren().addAll(statusTag, priceLabel);

        topBox.getChildren().addAll(imageView, infoBox, statusBox);

        // 하단 영역: 날짜 정보 + 버튼
        HBox bottomBox = new HBox(30);
        bottomBox.setPadding(new Insets(15, 20, 20, 20));
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 0 0 16 16;");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOut = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        HBox dateInfoBox = new HBox(30);
        dateInfoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(dateInfoBox, Priority.ALWAYS);

        VBox checkInBox = new VBox(3);
        Label checkInTitle = new Label("체크인");
        checkInTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        Label checkInValue = new Label("📅 " + now.format(formatter));
        checkInValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        checkInBox.getChildren().addAll(checkInTitle, checkInValue);

        VBox checkOutBox = new VBox(3);
        Label checkOutTitle = new Label("체크아웃");
        checkOutTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        Label checkOutValue = new Label("📅 " + checkOut.format(formatter));
        checkOutValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        checkOutBox.getChildren().addAll(checkOutTitle, checkOutValue);

        dateInfoBox.getChildren().addAll(checkInBox, checkOutBox);

        // 버튼 영역
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button detailButton = new Button("상세 보기");
        detailButton.setStyle(getOutlineButtonStyle("#2563eb"));
        detailButton.setOnMouseEntered(e -> detailButton.setStyle(getOutlineButtonHoverStyle("#2563eb")));
        detailButton.setOnMouseExited(e -> detailButton.setStyle(getOutlineButtonStyle("#2563eb")));
        detailButton.setOnAction(e -> showReservationDetail(reservation));
        buttonBox.getChildren().add(detailButton);        if (reservation.getReservationStatus() == ReservationStatus.PENDING) {
            // PENDING: 결제하기 + 예약 취소
            Button payButton = new Button("결제하기");
            payButton.setStyle(getFilledButtonStyle("#10b981"));
            payButton.setOnMouseEntered(e -> payButton.setStyle(getFilledButtonHoverStyle("#059669")));
            payButton.setOnMouseExited(e -> payButton.setStyle(getFilledButtonStyle("#10b981")));
            payButton.setOnAction(e -> processPayment(reservation));

            Button cancelButton = new Button("예약 취소");
            cancelButton.setStyle(getOutlineButtonStyle("#ef4444"));
            cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(getOutlineButtonHoverStyle("#ef4444")));
            cancelButton.setOnMouseExited(e -> cancelButton.setStyle(getOutlineButtonStyle("#ef4444")));
            cancelButton.setOnAction(e -> cancelReservation(reservation));

            buttonBox.getChildren().addAll(payButton, cancelButton);
        } else if (reservation.getReservationStatus() == ReservationStatus.CONFIRMED) {
            // CONFIRMED: 예약 취소만 (결제 버튼 없음)
            Button cancelButton = new Button("예약 취소");
            cancelButton.setStyle(getOutlineButtonStyle("#ef4444"));
            cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(getOutlineButtonHoverStyle("#ef4444")));
            cancelButton.setOnMouseExited(e -> cancelButton.setStyle(getOutlineButtonStyle("#ef4444")));
            cancelButton.setOnAction(e -> cancelReservation(reservation));

            buttonBox.getChildren().add(cancelButton);
        }
        // CANCELLED: 환불 요청 버튼 삭제 (아무 버튼도 추가하지 않음)

        bottomBox.getChildren().addAll(dateInfoBox, buttonBox);

        card.getChildren().addAll(topBox, bottomBox);

        // 호버 효과
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(245, 158, 11, 0.25), 25, 0, 0, 8);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        ));

        return card;
    }

    private String getOutlineButtonStyle(String color) {
        return "-fx-background-color: white; " +
               "-fx-text-fill: " + color + "; " +
               "-fx-padding: 8 20; " +
               "-fx-font-size: 12px; " +
               "-fx-background-radius: 8; " +
               "-fx-border-color: " + color + "; " +
               "-fx-border-radius: 8; " +
               "-fx-cursor: hand;";
    }

    private String getOutlineButtonHoverStyle(String color) {
        return "-fx-background-color: " + color + "15; " +
               "-fx-text-fill: " + color + "; " +
               "-fx-padding: 8 20; " +
               "-fx-font-size: 12px; " +
               "-fx-background-radius: 8; " +
               "-fx-border-color: " + color + "; " +
               "-fx-border-radius: 8; " +
               "-fx-cursor: hand;";
    }

    private String getFilledButtonStyle(String color) {
        return "-fx-background-color: " + color + "; " +
               "-fx-text-fill: white; " +
               "-fx-padding: 8 20; " +
               "-fx-font-size: 12px; " +
               "-fx-background-radius: 8; " +
               "-fx-cursor: hand;";
    }

    private String getFilledButtonHoverStyle(String color) {
        return "-fx-background-color: " + color + "; " +
               "-fx-text-fill: white; " +
               "-fx-padding: 8 20; " +
               "-fx-font-size: 12px; " +
               "-fx-background-radius: 8; " +
               "-fx-cursor: hand;";
    }

    private void showReservationDetail(Reservation reservation) {
        Room room = reservation.getRoom();
        Pension pension = pensionController.findById(room.getPensionId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("예약 상세 정보");
        alert.setHeaderText("예약 번호: #" + reservation.getId());

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label[] labels = {
            new Label("━━━━━ 펜션 정보 ━━━━━"),
            new Label("펜션 이름: " + pension.getName()),
            new Label("주소: " + pension.getAddress()),
            new Label("전화번호: " + pension.getPhoneNumber()),
            new Label(""),
            new Label("━━━━━ 객실 정보 ━━━━━"),
            new Label("객실 이름: " + room.getRoomName()),
            new Label("객실 타입: " + getRoomTypeText(room.getRoomType())),
            new Label("최대 인원: " + room.getMaxPeople() + "명"),
            new Label("가격: " + String.format("%,d원", room.getPrice())),
            new Label(""),
            new Label("━━━━━ 예약자 정보 ━━━━━"),
            new Label("이름: " + customer.getName()),
            new Label("전화번호: " + customer.getPhone()),
            new Label("이메일: " + customer.getEmail()),
            new Label(""),
            new Label("━━━━━ 예약 상태 ━━━━━"),
            new Label("상태: " + getStatusText(reservation.getReservationStatus()))
        };

        for (Label label : labels) {
            if (label.getText().startsWith("━")) {
                label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
            } else {
                label.setStyle("-fx-font-size: 12px;");
            }
        }

        content.getChildren().addAll(labels);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }    private void processPayment(Reservation reservation) {
        Room room = reservation.getRoom();
        Pension pension = pensionController.findById(room.getPensionId());
        
        // 기존 예약을 사용하여 PaymentView로 이동
        PaymentView paymentView = new PaymentView(pension.getId(), room.getId(), 1, customer, reservation);
        try {
            paymentView.start(stage);
        } catch (Exception e) {
            showAlert("오류", "결제 화면 이동 중 오류가 발생했습니다: " + e.getMessage());
        }
    }private void cancelReservation(Reservation reservation) {
        Room room = reservation.getRoom();
        Pension pension = pensionController.findById(room.getPensionId());
        
        if (reservation.getReservationStatus() == ReservationStatus.PENDING) {
            // PENDING 상태인 경우 - 예약 삭제
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("예약 취소");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("결제 대기 중인 예약을 취소하시겠습니까?\n예약이 삭제됩니다.");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        reservationController.deleteById(reservation.getId());
                        showAlert("성공", "예약이 취소되었습니다.");
                        updateReservationList(currentFilter);
                    } catch (Exception e) {
                        showAlert("오류", "예약 취소 중 오류가 발생했습니다: " + e.getMessage());
                    }
                }
            });
        } else if (reservation.getReservationStatus() == ReservationStatus.CONFIRMED) {
            // CONFIRMED 상태인 경우 - CancelReservationView로 이동
            CancelReservationView cancelView = new CancelReservationView(pension, room, customer, 1, stage, reservation);
            cancelView.show();
        }
    }

    private String getStatusText(ReservationStatus status) {
        switch (status) {
            case PENDING:
                return "⏳ 예약 대기";
            case CONFIRMED:
                return "✅ 예약 확정";
            case CANCELLED:
                return "❌ 취소됨";
            case REFUNDED:
                return "💰 환불 완료";
            default:
                return status.toString();
        }
    }

    private String getStatusColor(ReservationStatus status) {
        switch (status) {
            case PENDING:
                return "#f59e0b";
            case CONFIRMED:
                return "#10b981";
            case CANCELLED:
                return "#ef4444";
            case REFUNDED:
                return "#6b7280";
            default:
                return "#1e293b";
        }
    }

    private String getStatusBgColor(ReservationStatus status) {
        switch (status) {
            case PENDING:
                return "#fef3c7";
            case CONFIRMED:
                return "#d1fae5";
            case CANCELLED:
                return "#fee2e2";
            case REFUNDED:
                return "#f3f4f6";
            default:
                return "#f8fafc";
        }
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
