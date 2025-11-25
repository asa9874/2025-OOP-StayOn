package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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
    }

    public void show() {
        stage.setTitle("펜션 상세 정보");

        // 뒤로가기 버튼
        Button backButton = new Button("← 목록으로");
        backButton.setOnAction(e -> {
            PensionView pensionView = new PensionView();
            pensionView.start(stage);
        });

        // 펜션 이미지
        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(false);

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

        // 펜션 기본 정보
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(10));
        
        Label nameLabel = new Label(pension.getName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label descLabel = new Label(pension.getDescription());
        descLabel.setStyle("-fx-font-size: 14px;");
        descLabel.setWrapText(true);
        
        Label addressLabel = new Label(pension.getAddress());
        addressLabel.setStyle("-fx-font-size: 14px;");
        
        Label phoneLabel = new Label(pension.getPhoneNumber());
        phoneLabel.setStyle("-fx-font-size: 14px;");


        infoBox.getChildren().addAll(nameLabel, descLabel, addressLabel, phoneLabel);        // 객실 선택 버튼
        Button selectRoomButton = new Button("객실 선택하기");
        selectRoomButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0066cc; -fx-text-fill: white; -fx-padding: 10 20;");
        selectRoomButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView(pension, stage);
            roomSelectView.show();
        });

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
        roomTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        roomBox.getChildren().add(roomTitleLabel);
        
        List<Room> rooms = roomController.findByPensionId(pension.getId());
        if (rooms.isEmpty()) {
            Label noRoomLabel = new Label("등록된 객실이 없습니다.");
            roomBox.getChildren().add(noRoomLabel);
        } else {
            for (Room room : rooms) {
                HBox roomCard = createRoomCard(room);
                roomBox.getChildren().add(roomCard);
            }
        }

        // 레이아웃
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(
            backButton,
            new Separator(),
            imageView,
            new Separator(),
            infoBox,
            new Separator(),
            buttonBox,
            new Separator(),
            roomBox
        );

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 700);
        stage.setScene(scene);
        stage.show();
    }    private HBox createRoomCard(Room room) {
        // 객실 이미지뷰 생성
        ImageView roomImageView = new ImageView();
        roomImageView.setFitWidth(100);
        roomImageView.setFitHeight(100);
        roomImageView.setPreserveRatio(false);

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
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        
        VBox infoBox = new VBox(5);
        Label nameLabel = new Label(room.getRoomName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label typeLabel = new Label(getRoomTypeText(room.getRoomType()));
        Label statusLabel = new Label(getRoomStatusText(room.getRoomStatus()));
        Label priceLabel = new Label(room.getPrice() + "원");
        
        infoBox.getChildren().addAll(nameLabel, typeLabel, statusLabel, priceLabel);
        card.getChildren().addAll(roomImageView, infoBox);
        
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
