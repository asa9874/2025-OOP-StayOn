package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
import java.io.File;
import java.util.List;

public class PensionDetailView {
    private final Pension pension;
    private final Stage stage;
    private final RoomController roomController;

    public PensionDetailView(Pension pension, Stage stage) {
        this.pension = pension;
        this.stage = stage;
        this.roomController = RoomController.getInstance();
    }    public void show() {
        stage.setTitle("StayOn - 펜션 상세");

        // 상단 바 (뒤로가기 버튼)
        Button backButton = new Button("← 목록으로");
        backButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        backButton.setOnAction(e -> {
            PensionView pensionView = new PensionView();
            pensionView.start(stage);
        });

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.getChildren().add(backButton);

        // 펜션 정보 카드
        HBox pensionInfoCard = new HBox(25);
        pensionInfoCard.setPadding(new Insets(25));
        pensionInfoCard.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        pensionInfoCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        // 펜션 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(225);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 2);");

        // 이미지 로드
        try {
            File imageFile = new File(pension.getImage());
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

        // 펜션 정보
        VBox infoBox = new VBox(12);
        
        Label nameLabel = new Label(pension.getName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label descLabel = new Label(pension.getDescription());
        descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(400);
        
        HBox addressBox = new HBox(8);
        addressBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label addressIcon = new Label("📍");
        Label addressLabel = new Label(pension.getAddress());
        addressLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #34495e;");
        addressBox.getChildren().addAll(addressIcon, addressLabel);
        
        HBox phoneBox = new HBox(8);
        phoneBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label phoneIcon = new Label("📞");
        Label phoneLabel = new Label(pension.getPhoneNumber());
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #34495e;");
        phoneBox.getChildren().addAll(phoneIcon, phoneLabel);

        infoBox.getChildren().addAll(nameLabel, descLabel, addressBox, phoneBox);
        pensionInfoCard.getChildren().addAll(imageView, infoBox);

        // 버튼 박스
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 20, 0));
        
        // 객실 선택 버튼
        Button selectRoomButton = new Button("🏠 예약하기");
        selectRoomButton.setPrefWidth(200);
        selectRoomButton.setPrefHeight(45);
        selectRoomButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        selectRoomButton.setOnMouseEntered(e -> selectRoomButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        selectRoomButton.setOnMouseExited(e -> selectRoomButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        selectRoomButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView(pension, stage);
            roomSelectView.show();
        });
        
        // 부대시설 조회 버튼
        Button facilitiesButton = new Button("🏊 부대시설 조회");
        facilitiesButton.setPrefWidth(200);
        facilitiesButton.setPrefHeight(45);
        facilitiesButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        facilitiesButton.setOnMouseEntered(e -> facilitiesButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        facilitiesButton.setOnMouseExited(e -> facilitiesButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        facilitiesButton.setOnAction(e -> {
            FacilitiesView facilitiesView = new FacilitiesView(pension, stage);
            facilitiesView.show();
        });
        
        buttonBox.getChildren().addAll(selectRoomButton, facilitiesButton);

        Button reviewButton = new Button("후기 조회");
        reviewButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0066cc; -fx-text-fill: white; -fx-padding: 10 20;");
        reviewButton.setOnAction(e -> {
            ReviewView reviewView = new ReviewView(pension, stage);
            reviewView.show();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(
            selectRoomButton,
            reviewButton
        );

        // 객실 정보
        VBox roomBox = new VBox(10);
        roomBox.setPadding(new Insets(10));
        
        Label roomTitleLabel = new Label("객실 목록");
        roomTitleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        VBox roomList = new VBox(12);
        
        List<Room> rooms = roomController.findByPensionId(pension.getId());
        if (rooms.isEmpty()) {
            Label noRoomLabel = new Label("등록된 객실이 없습니다.");
            noRoomLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");
            roomList.getChildren().add(noRoomLabel);
        } else {
            for (Room room : rooms) {
                HBox roomCard = createRoomCard(room);
                roomList.getChildren().add(roomCard);
            }
        }
        
        roomSection.getChildren().addAll(roomTitleLabel, roomList);

        // 메인 레이아웃
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20, 40, 40, 40));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom,rgb(236, 241, 240),rgb(187, 240, 216));");
        mainLayout.getChildren().addAll(
            topBar,
            pensionInfoCard,
            buttonBox,
            roomSection
        );

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Scene scene = new Scene(scrollPane, 900, 700);
        
        // Pretendard 폰트 적용
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS 파일을 불러올 수 없습니다: " + e.getMessage());
        }
        
        stage.setScene(scene);
        stage.show();
    }    private HBox createRoomCard(Room room) {
        // 객실 이미지뷰 생성
        ImageView roomImageView = new ImageView();
        roomImageView.setFitWidth(80);
        roomImageView.setFitHeight(80);
        roomImageView.setPreserveRatio(false);
        roomImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");

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
                
                // 뷰포트 설정 (중앙 정사각형 부분만)
                Rectangle2D viewport = new Rectangle2D(offsetX, offsetY, size, size);
                roomImageView.setViewport(viewport);
                roomImageView.setImage(image);
            }
        } catch (Exception e) {
            // 빈 이미지
        }

        HBox card = new HBox(15);
        card.setPadding(new Insets(12));
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 8;");
        
        VBox infoBox = new VBox(6);
        
        Label nameLabel = new Label(room.getRoomName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        
        HBox detailsBox = new HBox(15);
        
        Label typeLabel = new Label(getRoomTypeText(room.getRoomType()));
        typeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-padding: 3 8; -fx-background-color: #ecf0f1; -fx-background-radius: 3;");
        
        Label statusLabel = new Label(getRoomStatusText(room.getRoomStatus()));
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #27ae60; -fx-padding: 3 8; -fx-background-color: #d5f4e6; -fx-background-radius: 3;");
        
        detailsBox.getChildren().addAll(typeLabel, statusLabel);
        
        Label priceLabel = new Label(String.format("%,d원", room.getPrice()));
        priceLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3498db;");
        
        infoBox.getChildren().addAll(nameLabel, detailsBox, priceLabel);
        card.getChildren().addAll(roomImageView, infoBox);
        
        // 호버 효과
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-border-color: #3498db; -fx-border-width: 2; -fx-border-radius: 8; -fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 8;");
        });
        
        return card;
    }

    private String getRoomStatusText(org.example.domain.room.RoomStatus status) {
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
