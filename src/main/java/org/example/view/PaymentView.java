package org.example.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
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
import org.example.domain.pension.PensionController;
import org.example.domain.room.RoomController;
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.scene.layout.HBox;

// pension명, pension위치, 예약 인원수, 예약 날짜, 예약 요금, 최종 결제액, 결제수단(현금, 카드)
// 예약하기 -> PENDING상태, 결제완료 -> CONFIRMED상태
// 어떤 controller를 써야하지? 핵심 정보위주 - 예약 정보
public class PaymentView extends Application {
    private final PensionController pensionController;
    private final RoomController roomController;
    private int roomId;
    private int pensionId;
    private int selectedCount;
    private Customer customer;

    public PaymentView(int pensionId, int roomId, int selectedCount) {
        this(pensionId, roomId, selectedCount, null);
    }

    public PaymentView(int pensionId, int roomId, int selectedCount, Customer customer) {
        this.pensionController = PensionController.getInstance();
        this.roomController = RoomController.getInstance();
        this.roomId = roomId;
        this.pensionId = pensionId;
        this.selectedCount = selectedCount;
        this.customer = customer;
    }

    @Override
    public void start(Stage stage) {
        Pension pension = pensionController.findById(pensionId);
        Room room = roomController.findById(roomId);
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
        reservationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");        // 객실 목록 컨테이너 (VBox로 변경하여 행 단위 출력)

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
        pensionNameLabel.setStyle("-fx-font-size: 14px;");

        // 투숙인원 label
        Label peopleNumberLabel = new Label("투숙인원  " + room.getMaxPeople() * selectedCount);
        pensionNameLabel.setStyle("-fx-font-size: 14px;");

        // 투숙날짜 label
        Label stayPeriodLabel = new Label("투숙날짜  " + formattedDateTime + " ~ " + formattedDateTimeLater);
        pensionNameLabel.setStyle("-fx-font-size: 14px;");

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


        // 결제 UI
        

        // ... (다른 start 메서드 내용)

        // ===================================
        // 1. 결제 수단 선택 영역
        // ===================================
        Label paymethodLabel = new Label("결제 방법");
        paymethodLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 토글 그룹 생성: 이 그룹에 속한 버튼 중 오직 하나만 선택 가능
        ToggleGroup paymentToggleGroup = new ToggleGroup();

        // 카드 버튼 생성 및 그룹화
        ToggleButton cardButton1 = new ToggleButton("국민카드");
        cardButton1.setToggleGroup(paymentToggleGroup);
        cardButton1.setPrefSize(200, 100); // 버튼 크기 설정
        // CSS 스타일링을 통해 이미지와 모양을 구현합니다.
        cardButton1.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // 현금 버튼 생성 및 그룹화
        ToggleButton cardButton2 = new ToggleButton("신한카드");
        cardButton2.setToggleGroup(paymentToggleGroup);
        cardButton2.setPrefSize(200, 100);
        cardButton2.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // 기타 결제 수단 (더미 버튼)
        ToggleButton cardButton3 = new ToggleButton("현대카드");
        cardButton3.setToggleGroup(paymentToggleGroup);
        cardButton3.setPrefSize(200, 100);
        cardButton3.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // 기타 결제 수단 (더미 버튼)
        ToggleButton cardButton4 = new ToggleButton("삼성카드");
        cardButton4.setToggleGroup(paymentToggleGroup);
        cardButton4.setPrefSize(200, 100);
        cardButton4.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // 기타 결제 수단 (더미 버튼)
        ToggleButton cardButton5 = new ToggleButton("카카오페이");
        cardButton5.setToggleGroup(paymentToggleGroup);
        cardButton5.setPrefSize(200, 100);
        cardButton5.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // 기타 결제 수단 (더미 버튼)
        ToggleButton cardButton6 = new ToggleButton("토스뱅크");
        cardButton6.setToggleGroup(paymentToggleGroup);
        cardButton6.setPrefSize(200, 100);
        cardButton6.setStyle("-fx-background-color: #e0e0f0; -fx-text-fill: #333; -fx-background-radius: 8;");

        // GridPane을 사용하여 2열 3행의 버튼 레이아웃 구성
        GridPane paymentMethodGrid = new GridPane();
        paymentMethodGrid.setHgap(15); // 가로 간격
        paymentMethodGrid.setVgap(10); // 세로 간격
        paymentMethodGrid.setPadding(new Insets(10, 0, 20, 0));

        // 버튼들을 그리드에 추가 (GridPane.add(노드, 열 인덱스, 행 인덱스))
        paymentMethodGrid.add(cardButton1, 0, 0); 
        paymentMethodGrid.add(cardButton2, 1, 0); 
        paymentMethodGrid.add(cardButton3, 2, 0); 
        paymentMethodGrid.add(cardButton4, 0, 1); 
        paymentMethodGrid.add(cardButton5, 1, 1); 
        paymentMethodGrid.add(cardButton6, 2, 1); 

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
        Label finalPriceValueLabel = new Label(String.format("%,d원", room.getPrice() * selectedCount));
        finalPriceValueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        finalPriceBox.getChildren().addAll(finalPriceTitleLabel, finalPriceValueLabel);

        // 할인 금액
        HBox discountPriceBox = new HBox(300);
        discountPriceBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label discountPriceLabel = new Label("할인 금액");
        discountPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        discountPriceLabel.setMinWidth(100);
        Label discountPriceValueLabel = new Label("0원");
        discountPriceValueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        discountPriceBox.getChildren().addAll(discountPriceLabel, discountPriceValueLabel);

        VBox paymentSection = new VBox(30);
        paymentSection.getChildren().addAll(
            paymethodLabel,
            paymentMethodGrid, // Grid를 VBox에 추가
            new Separator(),
            reservationPriceBox,
            discountPriceBox,
            finalPriceBox
        );

        Button paymentButton = new Button("결제 확인");
        paymentButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0066cc; -fx-text-fill: white; -fx-padding: 10 30;");
        paymentButton.setOnAction(e -> {
            ConfirmReservationView confirmReservationView = new ConfirmReservationView(pension, room, customer, selectedCount, stage);
            confirmReservationView.show();
        });

        // 버튼을 오른쪽 정렬하기 위한 HBox
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(paymentButton);

        HBox filterBox = new HBox(20);
        filterBox.setPadding(new Insets(10));
        filterBox.getChildren().addAll(
            mainLayout,
            paymentSection
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
