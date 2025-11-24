package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.reservation.ReservationController;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RoomSelectView {
    private final Pension pension;
    private final Stage stage;
    private final RoomController roomController;
    private final ReservationController reservationController;
    private VBox roomListContainer;
    private List<Room> currentRoomList;

    public RoomSelectView(Pension pension, Stage stage) {
        this.pension = pension;
        this.stage = stage;
        this.roomController = RoomController.getInstance();
        this.reservationController = ReservationController.getInstance();
    }

    public void show() {
        stage.setTitle(pension.getName() + " - 객실 선택");

        // 뒤로가기 버튼
        Button backButton = new Button("← 펜션 정보로");
        backButton.setOnAction(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, stage);
            detailView.show();
        });

        // 펜션 정보 요약
        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");        // 객실 목록 컨테이너 (VBox로 변경하여 행 단위 출력)
        roomListContainer = new VBox(15);
        roomListContainer.setPadding(new Insets(10));

        // 필터 버튼들
        Button showAllButton = new Button("전체 객실");
        showAllButton.setOnAction(e -> updateRoomList());

        Button filterSingleButton = new Button("독채형");
        filterSingleButton.setOnAction(e -> filterByType(RoomType.SINGLE));

        Button filterDuplexButton = new Button("복층형");
        filterDuplexButton.setOnAction(e -> filterByType(RoomType.DUPLEX));

        Button filterHotelButton = new Button("호텔형");
        filterHotelButton.setOnAction(e -> filterByType(RoomType.HOTEL));

        Button filterAvailableButton = new Button("예약 가능");
        filterAvailableButton.setOnAction(e -> filterAvailableRooms());

        HBox filterBox = new HBox(10);
        filterBox.getChildren().addAll(
            new Label("필터:"),
            showAllButton,
            filterSingleButton,
            filterDuplexButton,
            filterHotelButton,
            filterAvailableButton
        );        updateRoomList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(roomListContainer);
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
            new Label("객실 목록:"),
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    private void updateRoomList() {
        currentRoomList = new ArrayList<>(roomController.findByPensionId(pension.getId()));
        displayRoomList();
    }

    private void filterByType(RoomType type) {
        currentRoomList = new ArrayList<>();
        for (Room room : roomController.findByPensionId(pension.getId())) {
            if (room.getRoomType() == type) {
                currentRoomList.add(room);
            }
        }
        displayRoomList();
    }

    private void filterAvailableRooms() {
        currentRoomList = new ArrayList<>();
        for (Room room : roomController.findByPensionId(pension.getId())) {
            // 예약 가능한 객실 (판매 중 상태)
            if (room.getRoomStatus() == RoomStatus.CLEANING) {
                currentRoomList.add(room);
            }
        }
        displayRoomList();
    }    private void displayRoomList() {
        roomListContainer.getChildren().clear();
        if (currentRoomList.isEmpty()) {
            Label noRoomLabel = new Label("해당하는 객실이 없습니다.");
            noRoomLabel.setStyle("-fx-font-size: 14px;");
            roomListContainer.getChildren().add(noRoomLabel);
        } else {
            for (Room room : currentRoomList) {
                roomListContainer.getChildren().add(createRoomRow(room));
            }
        }
    }    private HBox createRoomRow(Room room) {
        // 왼쪽: 객실 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);

        // 이미지 로드
        try {
            File imageFile = new File(room.getImage());
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

        // 오른쪽: 객실 정보
        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(10));
        
        Label nameLabel = new Label(room.getRoomName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        
        Label typeLabel = new Label("객실 타입: " + getRoomTypeText(room.getRoomType()));
        typeLabel.setStyle("-fx-font-size: 14px;");
        
        Label statusLabel = new Label("상태: " + getRoomStatusText(room.getRoomStatus()));
        // 상태에 따른 색상 변경
        if (room.getRoomStatus() == RoomStatus.CLEANING) {
            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green; -fx-font-weight: bold;");
        } else if (room.getRoomStatus() == RoomStatus.NOTSALES) {
            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: orange; -fx-font-weight: bold;");
        }
        
        Label priceLabel = new Label("가격: " + String.format("%,d", room.getPrice()) + "원");
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        
        Label maxPeopleLabel = new Label("최대 인원: " + room.getMaxPeople() + "명");
        maxPeopleLabel.setStyle("-fx-font-size: 14px;");
        
        Label floorLabel = new Label("층: " + room.getFloor() + "층 | 건물: " + room.getBuilding());
        floorLabel.setStyle("-fx-font-size: 14px;");
        
        infoBox.getChildren().addAll(nameLabel, typeLabel, statusLabel, priceLabel, maxPeopleLabel, floorLabel);

        // 예약 컨트롤
        VBox reservationBox = new VBox(10);
        reservationBox.setPadding(new Insets(10));
        reservationBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label selectLabel = new Label("예약할 객실 수:");
        selectLabel.setStyle("-fx-font-size: 14px;");
        
        Spinner<Integer> roomCountSpinner = new Spinner<>(0, 10, 1);
        roomCountSpinner.setPrefWidth(80);
        roomCountSpinner.setEditable(true);
        
        Button reserveButton = new Button("예약하기");
        reserveButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0066cc; -fx-text-fill: white; -fx-padding: 10 30;");
        reserveButton.setOnAction(e -> {
            int selectedCount = roomCountSpinner.getValue();
            if (selectedCount > 0) {
                // PaymentView로 이동
                PaymentView paymentView = new PaymentView();
                try {
                    paymentView.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("경고");
                alert.setHeaderText(null);
                alert.setContentText("객실 수를 1개 이상 선택해주세요.");
                alert.showAndWait();
            }
        });
        
        reservationBox.getChildren().addAll(selectLabel, roomCountSpinner, reserveButton);

        // 행 레이아웃
        HBox row = new HBox(20);
        row.setPadding(new Insets(15));
        row.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        row.getChildren().addAll(imageView, infoBox, reservationBox);
        
        HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);
        
        return row;
    }

    private String getRoomStatusText(RoomStatus status) {
        switch (status) {
            case RESERVATION:
                return "예약 중";
            case RESERVATIONED:
                return "예약완료";
            case USING:
                return "청소 중";
            case CHECKING:
                return "점검 중";
            case CLEANING:
                return "판매 중";
            case NOTSALES:
                return "판매 중지";
            default:
                return status.toString();
        }
    }
    private String getRoomTypeText(RoomType type) {
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