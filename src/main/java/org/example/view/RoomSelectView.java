package org.example.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.reservation.ReservationController;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;

import java.io.File;
import java.util.List;

// 객실 domain에서 객실 이름, 기준 인원, max 인원, 침대 갯수, 가격, 객실수 선택 버튼, 예약하기 버튼 구현 + 사진ㄴ
public class RoomSelectView extends Application {
    private final RoomController roomcontroller;
    private final ReservationController reservationController;
    private List<Room> roomList;

    public RoomSelectView() {
        this.roomcontroller = RoomController.getInstance();
        this.reservationController = ReservationController.getInstance();
        this.roomList = roomcontroller.findAll();
    }


    @Override
    public void start(Stage primaryStage) {
        // UI 구성 코드 작성
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // 객실 목록 표시
        for (Room room : roomList) {
            GridPane roomPane = createRoomPane(room);
            root.getChildren().add(roomPane);
        }

        ScrollPane scrollPane = new ScrollPane(root);
        Scene scene = new Scene(scrollPane, 800, 600);

        primaryStage.setTitle("객실 선택");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createRoomPane(Room room) {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(30));

        ImageView roomImage = createRoomImage(room);

        // 이미지 추가
        pane.add(roomImage, 0, 0, 1, 6);

        // 객실 정보 표시
        pane.add(new Label("객실명:"), 1, 0);
        pane.add(new Label(room.getRoomName()), 2, 0);

        pane.add(new Label("최대 인원:"), 1, 2);
        pane.add(new Label(String.valueOf(room.getMaxPeople())), 2, 2);

        pane.add(new Label("룸 타입:"), 1, 3);
        pane.add(new Label(String.valueOf(room.getRoomType())), 2, 3);

        pane.add(new Label("층수:"), 1, 4);
        pane.add(new Label(String.valueOf(room.getFloor())), 2, 4);

        pane.add(new Label("가격:"), 1, 5);
        pane.add(new Label(String.format("%,d원", room.getPrice())), 2, 5);

        // 객실 수 선택
        Spinner<Integer> roomCountSpinner = new Spinner<>(0, 10, 0);
        pane.add(new Label("객실 수:"), 4, 0);
        pane.add(roomCountSpinner, 5, 0);

        // 예약 버튼
        Button reserveButton = new Button("예약하기");
        reserveButton.setOnAction(e -> {
            int selectedCount = roomCountSpinner.getValue();
            if (selectedCount > 0) {
                // 예약 처리 로직 -> 여기서 다음 화면으로 넘어감 / reservationController가 customer reservation저장
                // 그러면 reservation storage에 reservation이 저장됨(날짜, 인원수, 팬션)
                System.out.println(room.getRoomName() + " " + selectedCount + "개 예약");
            }
        });
        pane.add(reserveButton, 5, 4);

        return pane;
    }

    private ImageView createRoomImage(Room room) {
        ImageView imageView = new ImageView();

        try {
            // 이미지 파일 경로 설정 (resources 폴더 또는 외부 경로)
            String imagePath = room.getImage();
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
            } else {
                // 기본 이미지 또는 플레이스홀더
            }
        } catch (Exception e) {
            // 에러 발생 시 플레이스홀더 이미지 사용
        }

        // 이미지 크기 설정
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // 테두리 추가
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);");

        return imageView;
    }
}