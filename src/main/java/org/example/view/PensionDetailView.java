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
        
        Label nameLabel = new Label("펜션 이름: " + pension.getName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label addressLabel = new Label("주소: " + pension.getAddress());
        addressLabel.setStyle("-fx-font-size: 14px;");
        
        Label phoneLabel = new Label("전화번호: " + pension.getPhoneNumber());
        phoneLabel.setStyle("-fx-font-size: 14px;");
        
        Label descLabel = new Label("설명: " + pension.getDescription());
        descLabel.setStyle("-fx-font-size: 14px;");
        descLabel.setWrapText(true);
        
        infoBox.getChildren().addAll(nameLabel, addressLabel, phoneLabel, descLabel);

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
            roomBox
        );

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 700);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createRoomCard(Room room) {
        HBox card = new HBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        
        VBox infoBox = new VBox(5);
        Label nameLabel = new Label("객실명: " + room.getRoomName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label typeLabel = new Label("객실 타입: " + room.getRoomType());
        Label statusLabel = new Label("상태: " + room.getRoomStatus());
        Label priceLabel = new Label("가격: " + room.getPrice() + "원");
        
        infoBox.getChildren().addAll(nameLabel, typeLabel, statusLabel, priceLabel);
        card.getChildren().add(infoBox);
        
        return card;
    }
}
