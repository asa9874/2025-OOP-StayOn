package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionController;
import org.example.domain.review.Review;
import org.example.domain.review.ReviewController;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewView {
    private final Stage stage;
    private final Pension pension;
    private final ReviewController reviewController;
    private final RoomController roomController;
    private final PensionController pensionController;
    private VBox reviewListContainer;
    private List<Review> currentReviewList;

    public ReviewView(Pension pension, Stage stage) {
        this.pension = pension;
        this.stage = stage;
        this.reviewController = ReviewController.getInstance();
        this.roomController = RoomController.getInstance();
        this.pensionController = PensionController.getInstance();
    }

    public void show() {
        stage.setTitle("후기 조회");

        // 뒤로가기 버튼
        Button backButton = new Button("← 뒤로");
        backButton.setOnAction(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, stage);
            detailView.show();
        });

        // 제목
        Label titleLabel = new Label("후기");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 후기 목록 컨테이너
        reviewListContainer = new VBox(15);
        reviewListContainer.setPadding(new Insets(10));

        // 후기 목록 업데이트
        updateReviewList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(reviewListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);

        // 레이아웃
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(
            backButton,
            titleLabel,
            new Separator(),
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 600, 700);
        stage.setScene(scene);
        stage.show();
    }

    private void updateReviewList() {
        // 펜션의 모든 객실에 대한 리뷰 가져오기
        currentReviewList = reviewController.findAll();
        // 펜션에 해당하는 리뷰만 필터링
        currentReviewList = currentReviewList.stream()
            .filter(review -> {
                Room room = review.getRoom();
                return room != null && room.getPensionId() == pension.getId();
            })
            .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate())) // 최신순 정렬
            .toList();

        displayReviewList();
    }

    private void displayReviewList() {
        reviewListContainer.getChildren().clear();

        if (currentReviewList.isEmpty()) {
            Label noReviewLabel = new Label("아직 등록된 후기가 없습니다.");
            noReviewLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            reviewListContainer.getChildren().add(noReviewLabel);
        } else {
            for (Review review : currentReviewList) {
                reviewListContainer.getChildren().add(createReviewCard(review));
            }
        }
    }

    private VBox createReviewCard(Review review) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-background-color: white; -fx-background-radius: 5; -fx-border-radius: 5;");

        // 상단: 이름, 펜션/객실 정보
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // 프로필 이미지 (원형) - 기본 아이콘
        Label profileIcon = new Label("👤");
        profileIcon.setStyle("-fx-font-size: 40px;");
        profileIcon.setMinWidth(50);
        profileIcon.setMinHeight(50);
        profileIcon.setAlignment(Pos.CENTER);

        // 사용자 정보 및 날짜
        VBox userInfoBox = new VBox(5);

        Room room = review.getRoom();
        String roomName = room != null ? room.getRoomName() : "알 수 없는 객실";

        Label nameAndRoomLabel = new Label(review.getCustomer().getName() + " / " + pension.getName() + " / " + roomName);
        nameAndRoomLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Label dateLabel = new Label(review.getDate().format(formatter));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        userInfoBox.getChildren().addAll(nameAndRoomLabel, dateLabel);

        // 버튼 영역 (오른쪽)
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button editButton = new Button("수정");
        editButton.setStyle("-fx-background-color: white; -fx-text-fill: #666; -fx-border-color: #666; -fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 5 15;");
        editButton.setOnAction(e -> editReview(review));

        Button deleteButton = new Button("삭제");
        deleteButton.setStyle("-fx-background-color: #8B7EC8; -fx-text-fill: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 5 15;");
        deleteButton.setOnAction(e -> deleteReview(review));

        buttonBox.getChildren().addAll(editButton, deleteButton);

        HBox.setHgrow(userInfoBox, javafx.scene.layout.Priority.ALWAYS);
        headerBox.getChildren().addAll(profileIcon, userInfoBox, buttonBox);

        // 별점 표시
        HBox starBox = new HBox(3);
        starBox.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < 5; i++) {
            Label star = new Label(i < review.getRate() ? "★" : "☆");
            star.setStyle("-fx-font-size: 16px; -fx-text-fill: " + (i < review.getRate() ? "#FFD700" : "#cccccc") + ";");
            starBox.getChildren().add(star);
        }

        // 리뷰 내용
        Label contentLabel = new Label(review.getContent());
        contentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        contentLabel.setWrapText(true);

        card.getChildren().addAll(headerBox, starBox, contentLabel);

        return card;
    }

    private void editReview(Review review) {
        // 수정 다이얼로그 생성
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("후기 수정");
        dialog.setHeaderText("후기를 수정하세요");

        // 버튼 추가
        ButtonType confirmButtonType = new ButtonType("확인", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // 입력 필드
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label rateLabel = new Label("평점 (1-5):");
        Spinner<Integer> rateSpinner = new Spinner<>(1, 5, review.getRate());
        rateSpinner.setEditable(true);

        Label contentLabel = new Label("내용:");
        TextArea contentArea = new TextArea(review.getContent());
        contentArea.setPrefRowCount(5);
        contentArea.setWrapText(true);

        content.getChildren().addAll(rateLabel, rateSpinner, contentLabel, contentArea);
        dialog.getDialogPane().setContent(content);

        // 결과 처리
        dialog.showAndWait().ifPresent(response -> {
            if (response == confirmButtonType) {
                try {
                    int newRate = rateSpinner.getValue();
                    String newContent = contentArea.getText();

                    if (newContent.trim().isEmpty()) {
                        showAlert("오류", "내용을 입력해주세요.");
                        return;
                    }

                    reviewController.updateReview(review.getId(), newRate, newContent);
                    updateReviewList();
                    showAlert("성공", "후기가 수정되었습니다.");
                } catch (Exception e) {
                    showAlert("오류", "후기 수정에 실패했습니다: " + e.getMessage());
                }
            }
        });
    }

    private void deleteReview(Review review) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("후기 삭제");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("정말로 이 후기를 삭제하시겠습니까?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reviewController.deleteById(review.getId());
                    updateReviewList();
                    showAlert("성공", "후기가 삭제되었습니다.");
                } catch (Exception e) {
                    showAlert("오류", "후기 삭제에 실패했습니다: " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
