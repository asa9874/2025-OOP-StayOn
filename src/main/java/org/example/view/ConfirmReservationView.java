package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionController;
import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.ReservationController;
import org.example.domain.reservation.ReservationStatus;
import org.example.domain.reservation.dto.ReservationRequestDTO;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
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
    private final PensionController pensionController;
    private final RoomController roomController;
    private final ReservationController reservationController;

    public ConfirmReservationView(Pension pension, Room room, Customer customer, int selectedCount, Stage stage) {
        this.pension = pension;
        this.room = room;
        this.customer = customer;
        this.selectedCount = selectedCount;
        this.stage = stage;
        this.pensionController = PensionController.getInstance();
        this.roomController = RoomController.getInstance();
        this.reservationController = ReservationController.getInstance();
    }

    public void show() {
        stage.setTitle("예약 확인");

        // 뒤로가기 버튼
        Button backButton = new Button("← 객실 선택으로");
        backButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView(pension, stage);
            roomSelectView.show();
        });

        // 제목
        Label titleLabel = new Label("예약 확인");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // 날짜 정보
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOut = now.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");

        // 펜션 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
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

        // 예약 정보 섹션
        VBox reservationInfoBox = new VBox(15);
        reservationInfoBox.setPadding(new Insets(20));
        reservationInfoBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label infoTitleLabel = new Label("예약 정보");
        infoTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        int row = 0;

        // 펜션 이름
        addInfoRow(infoGrid, row++, "펜션 이름:", pension.getName());

        // 펜션 주소
        addInfoRow(infoGrid, row++, "펜션 주소:", pension.getAddress());

        // 펜션 전화번호
        addInfoRow(infoGrid, row++, "연락처:", pension.getPhoneNumber());

        // 객실 이름
        addInfoRow(infoGrid, row++, "객실 이름:", room.getRoomName());

        // 객실 타입
        addInfoRow(infoGrid, row++, "객실 타입:", getRoomTypeText(room.getRoomType()));

        // 투숙 인원
        addInfoRow(infoGrid, row++, "투숙 인원:", (room.getMaxPeople() * selectedCount) + "명");

        // 객실 수
        addInfoRow(infoGrid, row++, "객실 수:", selectedCount + "개");

        // 체크인
        addInfoRow(infoGrid, row++, "체크인:", now.format(formatter));

        // 체크아웃
        addInfoRow(infoGrid, row++, "체크아웃:", checkOut.format(formatter));

        reservationInfoBox.getChildren().addAll(infoTitleLabel, new Separator(), infoGrid);

        // 결제 정보 섹션
        VBox paymentInfoBox = new VBox(15);
        paymentInfoBox.setPadding(new Insets(20));
        paymentInfoBox.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #0066cc; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label paymentTitleLabel = new Label("결제 정보");
        paymentTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // 가격 정보
        int roomPrice = room.getPrice();
        int totalPrice = roomPrice * selectedCount;

        HBox pricePerRoomBox = new HBox(100);
        Label pricePerRoomLabel = new Label("객실 요금 (1개)");
        pricePerRoomLabel.setStyle("-fx-font-size: 14px;");
        pricePerRoomLabel.setMinWidth(150);
        Label pricePerRoomValue = new Label(String.format("%,d원", roomPrice));
        pricePerRoomValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        pricePerRoomBox.getChildren().addAll(pricePerRoomLabel, pricePerRoomValue);

        HBox roomCountBox = new HBox(100);
        Label roomCountLabel = new Label("객실 수");
        roomCountLabel.setStyle("-fx-font-size: 14px;");
        roomCountLabel.setMinWidth(150);
        Label roomCountValue = new Label(selectedCount + "개");
        roomCountValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        roomCountBox.getChildren().addAll(roomCountLabel, roomCountValue);

        HBox totalPriceBox = new HBox(100);
        Label totalPriceLabel = new Label("총 결제 금액");
        totalPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        totalPriceLabel.setMinWidth(150);
        Label totalPriceValue = new Label(String.format("%,d원", totalPrice));
        totalPriceValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        totalPriceBox.getChildren().addAll(totalPriceLabel, totalPriceValue);

        paymentInfoBox.getChildren().addAll(
            paymentTitleLabel,
            new Separator(),
            pricePerRoomBox,
            roomCountBox,
            new Separator(),
            totalPriceBox
        );

        // 예약자 정보 섹션
        VBox customerInfoBox = new VBox(15);
        customerInfoBox.setPadding(new Insets(20));
        customerInfoBox.setStyle("-fx-background-color: #fff9f0; -fx-border-color: #FFA500; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label customerTitleLabel = new Label("예약자 정보");
        customerTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane customerGrid = new GridPane();
        customerGrid.setHgap(20);
        customerGrid.setVgap(10);
        customerGrid.setPadding(new Insets(10, 0, 0, 0));

        int custRow = 0;
        if (customer != null) {
            addInfoRow(customerGrid, custRow++, "이름:", customer.getName());
            addInfoRow(customerGrid, custRow++, "전화번호:", customer.getPhone());
            addInfoRow(customerGrid, custRow++, "이메일:", customer.getEmail());
        } else {
            addInfoRow(customerGrid, custRow++, "이름:", "게스트");
            addInfoRow(customerGrid, custRow++, "전화번호:", "-");
            addInfoRow(customerGrid, custRow++, "이메일:", "-");
        }

        customerInfoBox.getChildren().addAll(customerTitleLabel, new Separator(), customerGrid);

        // 버튼 영역
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button cancelButton = new Button("취소");
        cancelButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 30; -fx-background-color: #dc3545; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            CancelReservationView cancelReservationView = new CancelReservationView(pension, room, customer, selectedCount, stage);
            cancelReservationView.show();
        });

        Button confirmButton = new Button("예약 확정");
        confirmButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 30; -fx-background-color: #28a745; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> confirmReservation());

        buttonBox.getChildren().addAll(cancelButton, confirmButton);

        // 메인 레이아웃
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
            backButton,
            titleLabel,
            new Separator(),
            imageView,
            new Separator(),
            reservationInfoBox,
            paymentInfoBox,
            customerInfoBox,
            buttonBox
        );

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 14px;");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private void confirmReservation() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("예약 확정");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("예약을 확정하시겠습니까?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // 예약 생성
                    ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                        room,
                        customer,
                        ReservationStatus.PENDING // 초기 상태는 PENDING
                    );

                    Reservation reservation = reservationController.save(requestDTO);

                    // 성공 메시지
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("예약 완료");
                    successAlert.setHeaderText("예약이 완료되었습니다!");
                    successAlert.setContentText(
                        "예약 번호: " + reservation.getId() + "\n\n" +
                        "예약 내역은 '예약 내역 조회'에서 확인하실 수 있습니다."
                    );
                    successAlert.showAndWait();

                    // 메인 화면으로 이동
                    MainView mainView = new MainView(stage);
                    mainView.show();

                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("예약 실패");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("예약 처리 중 오류가 발생했습니다: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
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
