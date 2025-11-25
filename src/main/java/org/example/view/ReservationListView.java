package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionController;
import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.ReservationController;
import org.example.domain.reservation.ReservationStatus;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationListView {
    private final Stage stage;
    private final Customer customer;
    private final ReservationController reservationController;
    private final RoomController roomController;
    private final PensionController pensionController;
    private VBox reservationListContainer;
    private List<Reservation> currentReservationList;
    private ReservationStatus currentFilter;

    public ReservationListView(Customer customer, Stage stage) {
        this.customer = customer;
        this.stage = stage;
        this.reservationController = ReservationController.getInstance();
        this.roomController = RoomController.getInstance();
        this.pensionController = PensionController.getInstance();
        this.currentFilter = null;
    }

    public void show() {
        stage.setTitle("예약 내역 조회");

        // 뒤로가기 버튼
        Button backButton = new Button("← 메인으로");
        backButton.setOnAction(e -> {
            MainView mainView = new MainView(stage);
            mainView.show();
        });

        // 제목
        Label titleLabel = new Label("예약 내역");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // 필터 버튼들 (ToggleButton 사용)
        ToggleGroup filterGroup = new ToggleGroup();

        ToggleButton showAllButton = new ToggleButton("전체");
        showAllButton.setToggleGroup(filterGroup);
        showAllButton.setStyle("-fx-padding: 8 20; -fx-font-size: 12px;");
        showAllButton.setSelected(true);
        showAllButton.setOnAction(e -> updateReservationList(null));

        ToggleButton filterPendingButton = new ToggleButton("예약 대기");
        filterPendingButton.setToggleGroup(filterGroup);
        filterPendingButton.setStyle("-fx-padding: 8 20; -fx-font-size: 12px;");
        filterPendingButton.setOnAction(e -> updateReservationList(ReservationStatus.PENDING));

        ToggleButton filterConfirmedButton = new ToggleButton("예약 확정");
        filterConfirmedButton.setToggleGroup(filterGroup);
        filterConfirmedButton.setStyle("-fx-padding: 8 20; -fx-font-size: 12px;");
        filterConfirmedButton.setOnAction(e -> updateReservationList(ReservationStatus.CONFIRMED));

        ToggleButton filterCancelledButton = new ToggleButton("취소됨");
        filterCancelledButton.setToggleGroup(filterGroup);
        filterCancelledButton.setStyle("-fx-padding: 8 20; -fx-font-size: 12px;");
        filterCancelledButton.setOnAction(e -> updateReservationList(ReservationStatus.CANCELLED));

        ToggleButton filterRefundedButton = new ToggleButton("환불 완료");
        filterRefundedButton.setToggleGroup(filterGroup);
        filterRefundedButton.setStyle("-fx-padding: 8 20; -fx-font-size: 12px;");
        filterRefundedButton.setOnAction(e -> updateReservationList(ReservationStatus.REFUNDED));

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10, 0, 10, 0));
        filterBox.getChildren().addAll(
            new Label("필터:"),
            showAllButton,
            filterPendingButton,
            filterConfirmedButton,
            filterCancelledButton,
            filterRefundedButton
        );

        // 예약 목록 컨테이너
        reservationListContainer = new VBox(15);
        reservationListContainer.setPadding(new Insets(10));

        // 초기 목록 로드
        updateReservationList(null);

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(reservationListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        // 레이아웃
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
            backButton,
            titleLabel,
            new Separator(),
            filterBox,
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    private void updateReservationList(ReservationStatus filterStatus) {
        currentFilter = filterStatus;
        // 고객의 예약 목록 가져오기
        currentReservationList = reservationController.findByCustomer(customer);

        // 필터 적용
        if (filterStatus != null) {
            currentReservationList = currentReservationList.stream()
                .filter(reservation -> reservation.getReservationStatus() == filterStatus)
                .toList();
        }

        displayReservationList();
    }

    private void displayReservationList() {
        reservationListContainer.getChildren().clear();

        if (currentReservationList.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));

            Label emptyIcon = new Label("📋");
            emptyIcon.setStyle("-fx-font-size: 48px;");

            Label noReservationLabel = new Label("예약 내역이 없습니다.");
            noReservationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

            emptyBox.getChildren().addAll(emptyIcon, noReservationLabel);
            reservationListContainer.getChildren().add(emptyBox);
        } else {
            // 상태별 카운트 표시
            Label countLabel = new Label("총 " + currentReservationList.size() + "건의 예약");
            countLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-font-weight: bold;");
            reservationListContainer.getChildren().add(countLabel);

            for (Reservation reservation : currentReservationList) {
                reservationListContainer.getChildren().add(createReservationCard(reservation));
            }
        }
    }

    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Room room = reservation.getRoom();
        Pension pension = pensionController.findById(room.getPensionId());

        // 상단: 펜션 이미지 + 기본 정보
        HBox topBox = new HBox(20);

        // 펜션 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
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

        // 정보 영역
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(5));

        Label reservationIdLabel = new Label("예약 번호: #" + reservation.getId());
        reservationIdLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        Label roomNameLabel = new Label(room.getRoomName());
        roomNameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

        Label addressLabel = new Label("📍 " + pension.getAddress());
        addressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label phoneLabel = new Label("📞 " + pension.getPhoneNumber());
        phoneLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        infoBox.getChildren().addAll(
            reservationIdLabel,
            pensionNameLabel,
            roomNameLabel,
            addressLabel,
            phoneLabel
        );

        HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);
        topBox.getChildren().addAll(imageView, infoBox);

        // 중간: 예약 상세 정보
        HBox detailBox = new HBox(30);
        detailBox.setPadding(new Insets(15, 0, 15, 0));
        detailBox.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 5; -fx-padding: 15;");

        // 날짜 정보 (예시)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOut = now.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        VBox dateBox = new VBox(5);
        Label checkInTitleLabel = new Label("체크인");
        checkInTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        Label checkInLabel = new Label(now.format(formatter));
        checkInLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        dateBox.getChildren().addAll(checkInTitleLabel, checkInLabel);

        VBox dateBox2 = new VBox(5);
        Label checkOutTitleLabel = new Label("체크아웃");
        checkOutTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        Label checkOutLabel = new Label(checkOut.format(formatter));
        checkOutLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        dateBox2.getChildren().addAll(checkOutTitleLabel, checkOutLabel);

        VBox priceBox = new VBox(5);
        Label priceTitleLabel = new Label("결제 금액");
        priceTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        Label priceLabel = new Label(String.format("%,d원", room.getPrice()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        priceBox.getChildren().addAll(priceTitleLabel, priceLabel);

        VBox statusBox = new VBox(5);
        Label statusTitleLabel = new Label("예약 상태");
        statusTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        Label statusLabel = new Label(getStatusText(reservation.getReservationStatus()));
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + getStatusColor(reservation.getReservationStatus()) + ";");
        statusBox.getChildren().addAll(statusTitleLabel, statusLabel);

        detailBox.getChildren().addAll(dateBox, dateBox2, priceBox, statusBox);

        // 하단: 액션 버튼
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button detailButton = new Button("상세 보기");
        detailButton.setStyle("-fx-background-color: #0066cc; -fx-text-fill: white; -fx-padding: 8 20; -fx-font-size: 12px; -fx-background-radius: 5;");
        detailButton.setOnAction(e -> showReservationDetail(reservation));

        buttonBox.getChildren().add(detailButton);

        // 상태에 따른 추가 버튼
        if (reservation.getReservationStatus() == ReservationStatus.PENDING ||
            reservation.getReservationStatus() == ReservationStatus.CONFIRMED) {

            Button payButton = new Button("결제하기");
            payButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 8 20; -fx-font-size: 12px; -fx-background-radius: 5;");
            payButton.setOnAction(e -> processPayment(reservation));

            Button cancelButton = new Button("예약 취소");
            cancelButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 8 20; -fx-font-size: 12px; -fx-background-radius: 5;");
            cancelButton.setOnAction(e -> cancelReservation(reservation));

            buttonBox.getChildren().addAll(payButton, cancelButton);
        } else if (reservation.getReservationStatus() == ReservationStatus.CANCELLED) {
            Button refundButton = new Button("환불 요청");
            refundButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-padding: 8 20; -fx-font-size: 12px; -fx-background-radius: 5;");
            refundButton.setOnAction(e -> requestRefund(reservation));

            buttonBox.getChildren().add(refundButton);
        }

        card.getChildren().addAll(topBox, new Separator(), detailBox, buttonBox);

        return card;
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
                label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
            } else {
                label.setStyle("-fx-font-size: 12px;");
            }
        }

        content.getChildren().addAll(labels);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    private void processPayment(Reservation reservation) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("결제 확인");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("결제를 진행하시겠습니까?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationController.pay(reservation.getId());
                    showAlert("성공", "결제가 완료되었습니다.");
                    updateReservationList(currentFilter);
                } catch (Exception e) {
                    showAlert("오류", "결제 처리 중 오류가 발생했습니다: " + e.getMessage());
                }
            }
        });
    }

    private void cancelReservation(Reservation reservation) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("예약 취소");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("정말로 예약을 취소하시겠습니까?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationController.cancel(reservation.getId());
                    showAlert("성공", "예약이 취소되었습니다.");
                    updateReservationList(currentFilter);
                } catch (Exception e) {
                    showAlert("오류", "예약 취소 중 오류가 발생했습니다: " + e.getMessage());
                }
            }
        });
    }

    private void requestRefund(Reservation reservation) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("환불 요청");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("환불을 요청하시겠습니까?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationController.refund(reservation.getId());
                    showAlert("성공", "환불이 완료되었습니다.");
                    updateReservationList(currentFilter);
                } catch (Exception e) {
                    showAlert("오류", "환불 처리 중 오류가 발생했습니다: " + e.getMessage());
                }
            }
        });
    }

    private String getStatusText(ReservationStatus status) {
        switch (status) {
            case PENDING:
                return "예약 대기";
            case CONFIRMED:
                return "예약 확정";
            case CANCELLED:
                return "취소됨";
            case REFUNDED:
                return "환불 완료";
            default:
                return status.toString();
        }
    }

    private String getStatusColor(ReservationStatus status) {
        switch (status) {
            case PENDING:
                return "#FFA500";
            case CONFIRMED:
                return "#28a745";
            case CANCELLED:
                return "#dc3545";
            case REFUNDED:
                return "#6c757d";
            default:
                return "#000000";
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
