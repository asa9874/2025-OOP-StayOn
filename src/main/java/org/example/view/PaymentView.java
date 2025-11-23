package org.example.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.domain.pension.PensionController;
import org.example.domain.room.RoomController;

// pension명, pension위치, 예약 인원수, 예약 날짜, 예약 요금, 최종 결제액, 결제수단(현금, 카드)
// 예약하기 -> PENDING상태, 결제완료 -> CONFIRMED상태
// 어떤 controller를 써야하지? 핵심 정보위주 - 예약 정보
public class PaymentView extends Application {
    private final RoomController roomcontroller;
    private final PensionController pensionController;

    public PaymentView() {
        this.roomcontroller = RoomController.getInstance();
        this.pensionController = PensionController.getInstance();
    }

    @Override
    public void start(Stage primaryStage) {

    }
}
