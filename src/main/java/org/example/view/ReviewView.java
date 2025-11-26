package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.review.Review;
import org.example.domain.review.ReviewController;
import org.example.domain.room.Room;
import org.example.domain.user.customer.Customer;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewView {
    private final Stage stage;
    private final Pension pension;
    private final ReviewController reviewController;
    private final Customer customer;
    private FlowPane reviewGridContainer;
    private List<Review> currentReviewList;

    public ReviewView(Pension pension, Customer customer, Stage stage) {
        this.pension = pension;
        this.customer = customer;
        this.stage = stage;
        this.reviewController = ReviewController.getInstance();
    }

    public void show() {
        stage.setTitle("StayOn - 후기");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 통계 섹션
        HBox statsSection = createStatsSection();

        // 후기 목록 컨테이너
        reviewGridContainer = new FlowPane();
        reviewGridContainer.setHgap(25);
        reviewGridContainer.setVgap(25);
        reviewGridContainer.setPadding(new Insets(30, 50, 50, 50));
        reviewGridContainer.setAlignment(Pos.TOP_CENTER);
        reviewGridContainer.setStyle("-fx-background-color: transparent;");

        updateReviewList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(reviewGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-background-color: #f8fafc; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);        mainContainer.getChildren().addAll(header, heroSection, statsSection, scrollPane);

        Scene scene = new Scene(mainContainer, 1200, 800);

        // 폰트 로드 및 적용
        FontUtil.loadFont();
        mainContainer.setStyle("-fx-font-family: '" + FontUtil.getFontFamily() + "';");

        stage.setScene(scene);
        stage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 40, 15, 40));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        Button backButton = new Button("← 펜션 정보로");
        backButton.setStyle(getBackButtonStyle());
        backButton.setOnMouseEntered(e -> backButton.setStyle(getBackButtonHoverStyle()));
        backButton.setOnMouseExited(e -> backButton.setStyle(getBackButtonStyle()));        backButton.setOnAction(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, customer, stage);
            detailView.show();
        });

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoView.setFitHeight(32);
            logoView.setPreserveRatio(true);
            header.getChildren().addAll(backButton, spacer1, logoView);
        } catch (Exception e) {
            Label logoText = new Label("StayOn");
            logoText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
            header.getChildren().addAll(backButton, spacer1, logoText);
        }

        Region spacer2 = new Region();
        spacer2.setMinWidth(100);
        header.getChildren().add(spacer2);

        return header;
    }

    private VBox createHeroSection() {
        VBox hero = new VBox(15);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(40, 40, 30, 40));
        hero.setStyle("-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e);");

        Label titleLabel = new Label("⭐ 후기");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label(pension.getName() + "의 생생한 후기를 확인해 보세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private HBox createStatsSection() {
        HBox statsBox = new HBox(30);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(25, 50, 25, 50));
        statsBox.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        // 평균 별점 계산
        double avgRating = 0;
        int totalReviews = 0;
        List<Review> reviews = reviewController.findAll().stream()
            .filter(review -> {
                Room room = review.getRoom();
                return room != null && room.getPensionId() == pension.getId();
            })
            .toList();
        
        if (!reviews.isEmpty()) {
            avgRating = reviews.stream().mapToInt(Review::getRate).average().orElse(0);
            totalReviews = reviews.size();
        }

        // 평균 별점 카드
        VBox ratingCard = new VBox(5);
        ratingCard.setAlignment(Pos.CENTER);
        ratingCard.setPadding(new Insets(15, 40, 15, 40));

        Label ratingValue = new Label(String.format("%.1f", avgRating));
        ratingValue.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #f59e0b;");

        HBox starsBox = new HBox(3);
        starsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            Label star = new Label(i < Math.round(avgRating) ? "★" : "☆");
            star.setStyle("-fx-font-size: 18px; -fx-text-fill: " + (i < Math.round(avgRating) ? "#f59e0b" : "#e2e8f0") + ";");
            starsBox.getChildren().add(star);
        }

        Label ratingLabel = new Label("평균 별점");
        ratingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        ratingCard.getChildren().addAll(ratingValue, starsBox, ratingLabel);

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #e2e8f0;");
        divider.setMinWidth(1);
        divider.setMaxWidth(1);
        divider.setMinHeight(80);

        // 총 후기 수 카드
        VBox countCard = new VBox(5);
        countCard.setAlignment(Pos.CENTER);
        countCard.setPadding(new Insets(15, 40, 15, 40));

        Label countValue = new Label(String.valueOf(totalReviews));
        countValue.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

        Label countLabel = new Label("개의 후기");
        countLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        countCard.getChildren().addAll(countValue, countLabel);

        statsBox.getChildren().addAll(ratingCard, divider, countCard);

        return statsBox;
    }

    private String getBackButtonStyle() {
        return "-fx-background-color: transparent; " +
               "-fx-text-fill: #64748b; " +
               "-fx-font-size: 14px; " +
               "-fx-cursor: hand; " +
               "-fx-padding: 8 20; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 20; " +
               "-fx-background-radius: 20;";
    }

    private String getBackButtonHoverStyle() {
        return "-fx-background-color: #f1f5f9; " +
               "-fx-text-fill: #2563eb; " +
               "-fx-font-size: 14px; " +
               "-fx-cursor: hand; " +
               "-fx-padding: 8 20; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 20; " +
               "-fx-background-radius: 20;";
    }

    private void updateReviewList() {
        currentReviewList = reviewController.findAll();
        currentReviewList = currentReviewList.stream()
            .filter(review -> {
                Room room = review.getRoom();
                return room != null && room.getPensionId() == pension.getId();
            })
            .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
            .toList();

        displayReviewList();
    }

    private void displayReviewList() {
        reviewGridContainer.getChildren().clear();

        if (currentReviewList.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(60));
            emptyBox.setMinWidth(800);
            emptyBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
            );

            Label emptyIcon = new Label("💬");
            emptyIcon.setStyle("-fx-font-size: 48px;");

            Label noReviewLabel = new Label("아직 등록된 후기가 없습니다.");
            noReviewLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #94a3b8;");

            emptyBox.getChildren().addAll(emptyIcon, noReviewLabel);
            reviewGridContainer.getChildren().add(emptyBox);
        } else {
            for (Review review : currentReviewList) {
                reviewGridContainer.getChildren().add(createReviewCard(review));
            }
        }
    }

    private VBox createReviewCard(Review review) {
        VBox card = new VBox(15);
        card.setMinWidth(800);
        card.setMaxWidth(800);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        // 상단: 프로필 + 정보 + 별점 + 버튼
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // 프로필 아이콘
        StackPane profileIcon = new StackPane();
        profileIcon.setMinSize(50, 50);
        profileIcon.setMaxSize(50, 50);
        profileIcon.setStyle(
            "-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e); " +
            "-fx-background-radius: 25;"
        );
        Label profileInitial = new Label(review.getCustomer().getName().substring(0, 1));
        profileInitial.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        profileIcon.getChildren().add(profileInitial);

        // 사용자 정보
        VBox userInfoBox = new VBox(3);
        HBox.setHgrow(userInfoBox, Priority.ALWAYS);

        Room room = review.getRoom();
        String roomName = room != null ? room.getRoomName() : "알 수 없는 객실";

        Label nameLabel = new Label(review.getCustomer().getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label roomLabel = new Label("🛏️ " + roomName);
        roomLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        Label dateLabel = new Label("📅 " + review.getDate().format(formatter));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        userInfoBox.getChildren().addAll(nameLabel, roomLabel, dateLabel);

        // 별점
        VBox ratingBox = new VBox(5);
        ratingBox.setAlignment(Pos.CENTER);

        HBox starsBox = new HBox(2);
        starsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            Label star = new Label(i < review.getRate() ? "★" : "☆");
            star.setStyle("-fx-font-size: 16px; -fx-text-fill: " + (i < review.getRate() ? "#f59e0b" : "#e2e8f0") + ";");
            starsBox.getChildren().add(star);
        }

        Label ratingLabel = new Label(review.getRate() + " / 5");
        ratingLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        ratingBox.getChildren().addAll(starsBox, ratingLabel);

        // 버튼 영역
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button editButton = new Button("수정");
        editButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #64748b; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 6 16; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        );
        editButton.setOnMouseEntered(e -> editButton.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-text-fill: #2563eb; " +
            "-fx-border-color: #2563eb; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 6 16; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        ));
        editButton.setOnMouseExited(e -> editButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #64748b; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 6 16; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        ));
        editButton.setOnAction(e -> editReview(review));

        Button deleteButton = new Button("삭제");
        deleteButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e); " +
            "-fx-text-fill: white; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 6 16; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        );
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #db2777, #e11d48); " +
            "-fx-text-fill: white; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 6 16; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        ));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #ec4899, #f43f5e); " +
            "-fx-text-fill: white; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 6 16; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        ));
        deleteButton.setOnAction(e -> deleteReview(review));

        buttonBox.getChildren().addAll(editButton, deleteButton);

        headerBox.getChildren().addAll(profileIcon, userInfoBox, ratingBox, buttonBox);

        // 구분선
        Region divider = new Region();
        divider.setStyle("-fx-background-color: #f1f5f9;");
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        // 리뷰 내용
        Label contentLabel = new Label(review.getContent());
        contentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569; -fx-line-spacing: 5;");
        contentLabel.setWrapText(true);

        card.getChildren().addAll(headerBox, divider, contentLabel);

        // 호버 효과
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(236, 72, 153, 0.2), 25, 0, 0, 8);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        ));

        return card;
    }

    private void editReview(Review review) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("후기 수정");
        dialog.setHeaderText("후기를 수정하세요");

        ButtonType confirmButtonType = new ButtonType("확인", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label rateLabel = new Label("평점 (1-5):");
        rateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Spinner<Integer> rateSpinner = new Spinner<>(1, 5, review.getRate());
        rateSpinner.setEditable(true);

        Label contentLabel = new Label("내용:");
        contentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextArea contentArea = new TextArea(review.getContent());
        contentArea.setPrefRowCount(5);
        contentArea.setWrapText(true);

        content.getChildren().addAll(rateLabel, rateSpinner, contentLabel, contentArea);
        dialog.getDialogPane().setContent(content);

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
