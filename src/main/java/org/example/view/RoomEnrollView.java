package org.example.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.room.RoomController;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
import org.example.domain.room.dto.RoomRequestDTO;

public class RoomEnrollView extends Application {
    private final RoomController roomcontroller;

    public RoomEnrollView() {this.roomcontroller = RoomController.getInstance();}

    @Override
    public void start(Stage primaryStage) {
        // UI 컴포넌트 생성
        Label titleLabel = new Label("새로운 객실 정보 입력");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // 입력 필드들
        TextField roomNameField = new TextField();
        roomNameField.setPromptText("객실 이름");

        TextField floorField = new TextField();
        floorField.setPromptText("층수 (숫자)");

        TextField buildingField = new TextField();
        buildingField.setPromptText("건물 이름");

        TextField maxPeopleField = new TextField();
        maxPeopleField.setPromptText("최대 인원 (숫자)");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("객실 설명");
        descriptionArea.setWrapText(true); // 자동 줄바꿈
        descriptionArea.setPrefRowCount(3); // 초기 3줄 높이

        ComboBox<String> roomStatusComboBox = new ComboBox<>();
        roomStatusComboBox.getItems().addAll(
                "RESERVATION",
                "RESERVATIONED",
                "USING",
                "CHECKING",
                "CLEANING",
                "NOTSALES"
        );
        roomStatusComboBox.setPromptText("객실 상태");

        ComboBox<String> roomTypeComboBox = new ComboBox<>();
        roomTypeComboBox.getItems().addAll(
                "DUPLEX",
                "SINGLE",
                "HOTEL"
        );
        roomTypeComboBox.setPromptText("객실 타입");

        TextField priceField = new TextField();
        priceField.setPromptText("가격 (숫자)");

        TextField pensionIdField = new TextField();
        pensionIdField.setPromptText("펜션 ID (숫자)");

        TextField imageField = new TextField();
        imageField.setPromptText("이미지 URL");

        Button saveButton = new Button("객실 정보 저장");
        saveButton.setMaxWidth(Double.MAX_VALUE); // 부모 너비에 맞춤

        // GridPane을 사용하여 레이블과 입력 필드를 정렬
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // 가로 간격
        gridPane.setVgap(10); // 세로 간격
        gridPane.setPadding(new Insets(20));

        // GridPane에 컴포넌트 추가
        int row = 0;
        gridPane.add(new Label("객실 이름:"), 0, row);
        gridPane.add(roomNameField, 1, row++);

        gridPane.add(new Label("층수:"), 0, row);
        gridPane.add(floorField, 1, row++);

        gridPane.add(new Label("건물:"), 0, row);
        gridPane.add(buildingField, 1, row++);

        gridPane.add(new Label("최대 인원:"), 0, row);
        gridPane.add(maxPeopleField, 1, row++);

        gridPane.add(new Label("설명:"), 0, row);
        gridPane.add(descriptionArea, 1, row++);

        gridPane.add(new Label("객실 상태:"), 0, row);
        gridPane.add(roomStatusComboBox, 1, row++);

        gridPane.add(new Label("객실 타입:"), 0, row);
        gridPane.add(roomTypeComboBox, 1, row++);

        gridPane.add(new Label("가격:"), 0, row);
        gridPane.add(priceField, 1, row++);

        gridPane.add(new Label("펜션 ID:"), 0, row);
        gridPane.add(pensionIdField, 1, row++);

        gridPane.add(new Label("이미지 URL:"), 0, row);
        gridPane.add(imageField, 1, row++);

        // 저장 버튼 액션
        saveButton.setOnAction(event -> {
            try {
                String roomName = roomNameField.getText();
                String floorText = floorField.getText();        // String으로 먼저 받기
                String building = buildingField.getText();
                String maxPeopleText = maxPeopleField.getText(); // String으로 먼저 받기
                String description = descriptionArea.getText();
                RoomStatus roomStatus = RoomStatus.valueOf(roomStatusComboBox.getValue()); // Enum은 ComboBox에서 직접 받으므로 String 변환 필요 없음
                RoomType roomType = RoomType.valueOf(roomTypeComboBox.getValue());
                String priceText = priceField.getText();         // String으로 먼저 받기
                String pensionIdText = pensionIdField.getText();   // String으로 먼저 받기
                String image = imageField.getText();

                // 1. **수정된 필수 필드 검사**: 문자열 필드와 숫자 필드의 빈 값/null 검사를 동시에 수행
                if (roomName.isEmpty() || floorText.isEmpty() || building.isEmpty() ||
                        maxPeopleText.isEmpty() || description.isEmpty() || priceText.isEmpty() ||
                        pensionIdText.isEmpty() || image.isEmpty() || roomStatus == null || roomType == null) {

                    showAlert(Alert.AlertType.ERROR, "입력 오류", "모든 필수 항목을 채우고 선택해주세요.");
                    return;
                }

                // 2. **유효성 통과 후 안전하게 int로 변환** (이 시점에서 NumberFormatException 발생 가능성이 줄어듭니다)
                int floor = Integer.parseInt(floorText);
                int maxPeople = Integer.parseInt(maxPeopleText);
                int price = Integer.parseInt(priceText);
                int pensionId = Integer.parseInt(pensionIdText);

                // 3. DTO 생성 및 로직 실행
                RoomRequestDTO requestDTO = new RoomRequestDTO(
                        roomName, floor, building, maxPeople, description,
                        roomStatus, roomType, price, pensionId, image
                );

                // 4. 콘솔 출력 및 저장 완료
                roomcontroller.save(requestDTO);

                showAlert(Alert.AlertType.INFORMATION, "저장 완료", "객실 정보가 성공적으로 입력되었습니다.");

            } catch (NumberFormatException e) {
                // 이 catch 블록은 이제 "빈 문자열"이 아닌, "숫자가 아닌 문자열"이 입력되었을 때만 실행됩니다.
                showAlert(Alert.AlertType.ERROR, "입력 오류", "층수, 최대 인원, 가격, 펜션 ID는 유효한 숫자 형식이어야 합니다.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "오류", "객실 정보 저장 중 오류 발생: " + e.getMessage());
            }
        });

        // 메인 레이아웃 (VBox)
        VBox root = new VBox(10); // 컴포넌트 간 간격 10px
        root.setPadding(new Insets(20));
        root.getChildren().addAll(titleLabel, gridPane, saveButton);

        Scene scene = new Scene(root, 450, 650); // 씬 크기 조절
        primaryStage.setTitle("관리자 - 객실 정보 등록");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 알림창을 띄우는 헬퍼 메서드
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}