package org.example.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class CancelReservationView {
    private final Stage stage;
    private final Pension pension;
    private final Room room;
    private final Customer customer;
    private final int selectedCount;

    public CancelReservationView(Pension pension, Room room, Customer customer, int selectedCount, Stage stage) {
        this.pension = pension;
        this.room = room;
        this.customer = customer;
        this.selectedCount = selectedCount;
        this.stage = stage;
    }

    public void show() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysLater = now.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH시");
        String formattedDateTime = now.format(formatter);
        String formattedDateTimeLater = threeDaysLater.format(formatter);

        stage.setTitle("확인 및 결제");

        // 뒤로가기 버튼
        Button backButton = new Button("← 객실 정보로");
        backButton.setOnAction(e -> {
            RoomSelectView roomSelectView = new RoomSelectView(pension, stage);
            roomSelectView.show();
        });

        // 예약정보 label
        Label reservationLabel = new Label("예약 정보");
        reservationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
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

        // 팬션 이름 label
        Label pensionNameLabel = new Label(pension.getName());
        pensionNameLabel.setStyle("-fx-font-size: 14px;");

        // 위치 상세주소 label
        Label pensionLocationLabel = new Label(pension.getAddress());
        pensionLocationLabel.setStyle("-fx-font-size: 14px;");

        // 투숙인원 label
        Label peopleNumberLabel = new Label("투숙인원  " + room.getMaxPeople() * selectedCount);
        peopleNumberLabel.setStyle("-fx-font-size: 14px;");

        // 투숙날짜 label
        Label stayPeriodLabel = new Label("투숙날짜  " + formattedDateTime + " ~ " + formattedDateTimeLater);
        stayPeriodLabel.setStyle("-fx-font-size: 14px;");

        // 레이아웃
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(
            imageView,
            new Separator(),
            pensionNameLabel,
            pensionLocationLabel,
            peopleNumberLabel,
            stayPeriodLabel
        );

        // ===================================
        // 1. 취소 사유 선택 영역
        // ===================================
        Label cancelReasonLabel = new Label("취소 사유");
        cancelReasonLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 토글 그룹 생성: 이 그룹에 속한 버튼 중 오직 하나만 선택 가능
        ToggleGroup cancelReasonToggleGroup = new ToggleGroup();

        // 취소 사유 버튼들 생성 및 그룹화
        ToggleButton reasonButton1 = new ToggleButton("일정 변경");
        reasonButton1.setToggleGroup(cancelReasonToggleGroup);
        reasonButton1.setPrefSize(200, 100);
        reasonButton1.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        ToggleButton reasonButton2 = new ToggleButton("예약 실수");
        reasonButton2.setToggleGroup(cancelReasonToggleGroup);
        reasonButton2.setPrefSize(200, 100);
        reasonButton2.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        ToggleButton reasonButton3 = new ToggleButton("다른 숙소 예약");
        reasonButton3.setToggleGroup(cancelReasonToggleGroup);
        reasonButton3.setPrefSize(200, 100);
        reasonButton3.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        ToggleButton reasonButton4 = new ToggleButton("개인 사정");
        reasonButton4.setToggleGroup(cancelReasonToggleGroup);
        reasonButton4.setPrefSize(200, 100);
        reasonButton4.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        ToggleButton reasonButton5 = new ToggleButton("가격 문제");
        reasonButton5.setToggleGroup(cancelReasonToggleGroup);
        reasonButton5.setPrefSize(200, 100);
        reasonButton5.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        ToggleButton reasonButton6 = new ToggleButton("기타");
        reasonButton6.setToggleGroup(cancelReasonToggleGroup);
        reasonButton6.setPrefSize(200, 100);
        reasonButton6.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // GridPane을 사용하여 2열 3행의 버튼 레이아웃 구성
        GridPane cancelReasonGrid = new GridPane();
        cancelReasonGrid.setHgap(15); // 가로 간격
        cancelReasonGrid.setVgap(10); // 세로 간격
        cancelReasonGrid.setPadding(new Insets(10, 0, 20, 0));

        // 버튼들을 그리드에 추가
        cancelReasonGrid.add(reasonButton1, 0, 0);
        cancelReasonGrid.add(reasonButton2, 1, 0);
        cancelReasonGrid.add(reasonButton3, 2, 0);
        cancelReasonGrid.add(reasonButton4, 0, 1);
        cancelReasonGrid.add(reasonButton5, 1, 1);
        cancelReasonGrid.add(reasonButton6, 2, 1);

        // 예약요금 행
        HBox reservationPriceBox = new HBox(300);
        reservationPriceBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label reservationPriceTitleLabel = new Label("예약요금");
        reservationPriceTitleLabel.setStyle("-fx-font-size: 14px;");
        reservationPriceTitleLabel.setMinWidth(100);
        Label reservationPriceValueLabel = new Label(String.format("%,d원", room.getPrice() * selectedCount));
        reservationPriceValueLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        reservationPriceBox.getChildren().addAll(reservationPriceTitleLabel, reservationPriceValueLabel);

        // 최종 결제액 행
        HBox finalPriceBox = new HBox(300);
        finalPriceBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label finalPriceTitleLabel = new Label("최종 결제액");
        finalPriceTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        finalPriceTitleLabel.setMinWidth(100);
        Label finalPriceValueLabel = new Label(String.format("0원"));
        finalPriceValueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        finalPriceBox.getChildren().addAll(finalPriceTitleLabel, finalPriceValueLabel);

        // 취소 금액
        HBox discountPriceBox = new HBox(300);
        discountPriceBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label discountPriceLabel = new Label("취소 금액");
        discountPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        discountPriceLabel.setMinWidth(100);
        Label discountPriceValueLabel = new Label(String.format("-%,d원", room.getPrice() * selectedCount));
        discountPriceValueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        discountPriceBox.getChildren().addAll(discountPriceLabel, discountPriceValueLabel);

        VBox cancelSection = new VBox(30);
        cancelSection.getChildren().addAll(
            cancelReasonLabel,
            cancelReasonGrid,
            new Separator(),
            reservationPriceBox,
            discountPriceBox,
            finalPriceBox
        );

        Button cancelButton = new Button("결제 확인");
        cancelButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0066cc; -fx-text-fill: white; -fx-padding: 10 30;");
        cancelButton.setOnAction(e -> {
            ConfirmReservationView confirmReservationView = new ConfirmReservationView(pension, room, customer, selectedCount, stage);
            confirmReservationView.show();
        });

        // 버튼을 오른쪽 정렬하기 위한 HBox
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(cancelButton);

        HBox filterBox = new HBox(20);
        filterBox.setPadding(new Insets(10));
        filterBox.getChildren().addAll(
            mainLayout,
            cancelSection
        );

        VBox finalBox = new VBox(15);
        finalBox.setPadding(new Insets(10));
        finalBox.getChildren().addAll(
            backButton,
            reservationLabel,
            new Separator(),
            filterBox,
            new Separator(),
            buttonBox
        );

        Scene scene = new Scene(finalBox, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
