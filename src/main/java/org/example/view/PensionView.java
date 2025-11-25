package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionController;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PensionView {
    private final PensionController controller;
    private FlowPane pensionGridContainer;
    private List<Pension> currentPensionList;
    private Stage stage;

    public PensionView() {
        this.controller = PensionController.getInstance();
    }    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("StayOn - 펜션 목록");

        // 상단 타이틀
        Label titleLabel = new Label("펜션 예약");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // 검색 및 필터 컨테이너
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER);
        searchContainer.setPadding(new Insets(15));
        searchContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        TextField searchNameField = new TextField();
        searchNameField.setPromptText("펜션 이름 검색");
        searchNameField.setPrefWidth(250);
        searchNameField.setStyle("-fx-font-size: 13px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        
        Button searchButton = new Button("🔍 검색");
        searchButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        searchButton.setOnAction(e -> {
            String nameText = searchNameField.getText();
            if (!nameText.isEmpty()) {
                searchByName(nameText);
            }
        });

        Button showAllButton = new Button("전체보기");
        showAllButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        showAllButton.setOnMouseEntered(e -> showAllButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        showAllButton.setOnMouseExited(e -> showAllButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        showAllButton.setOnAction(e -> updatePensionList());

        searchContainer.getChildren().addAll(searchNameField, searchButton, showAllButton);

        // 정렬 버튼 컨테이너
        HBox sortContainer = new HBox(10);
        sortContainer.setAlignment(Pos.CENTER);
        sortContainer.setPadding(new Insets(10));
        
        Label sortLabel = new Label("정렬:");
        sortLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        Button sortByIdAscButton = new Button("ID ↑");
        styleSmallButton(sortByIdAscButton);
        sortByIdAscButton.setOnAction(e -> sortById(true));
        
        Button sortByIdDescButton = new Button("ID ↓");
        styleSmallButton(sortByIdDescButton);
        sortByIdDescButton.setOnAction(e -> sortById(false));
        
        Button sortByNameAscButton = new Button("이름 ↑");
        styleSmallButton(sortByNameAscButton);
        sortByNameAscButton.setOnAction(e -> sortByName(true));
        
        Button sortByNameDescButton = new Button("이름 ↓");
        styleSmallButton(sortByNameDescButton);
        sortByNameDescButton.setOnAction(e -> sortByName(false));

        sortContainer.getChildren().addAll(sortLabel, sortByIdAscButton, sortByIdDescButton, sortByNameAscButton, sortByNameDescButton);

        // 펜션 그리드 컨테이너
        pensionGridContainer = new FlowPane();
        pensionGridContainer.setHgap(25);
        pensionGridContainer.setVgap(25);
        pensionGridContainer.setPadding(new Insets(20));
        pensionGridContainer.setAlignment(Pos.CENTER);
        pensionGridContainer.setStyle("-fx-background-color: transparent;");
        updatePensionList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(pensionGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefViewportHeight(450);

        // 로그아웃 버튼
        Button logoutButton = new Button("로그아웃");
        logoutButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;"));
        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.getChildren().add(logoutButton);

        // 메인 레이아웃
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(20, 40, 20, 40));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom,rgb(236, 241, 240),rgb(187, 240, 216));");
        
        mainLayout.getChildren().addAll(
            topBar,
            titleLabel,            searchContainer,
            sortContainer,
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 1000, 700);
        
        // Pretendard 폰트 적용
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS 파일을 불러올 수 없습니다: " + e.getMessage());
        }
        
        stage.setScene(scene);
        stage.show();
    }

    private void styleSmallButton(Button button) {
        button.setStyle("-fx-font-size: 12px; -fx-padding: 5 15; -fx-background-color: #ecf0f1; -fx-text-fill: #34495e; -fx-background-radius: 5; -fx-cursor: hand; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 12px; -fx-padding: 5 15; -fx-background-color: #bdc3c7; -fx-text-fill: #2c3e50; -fx-background-radius: 5; -fx-cursor: hand; -fx-border-color: #95a5a6; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 12px; -fx-padding: 5 15; -fx-background-color: #ecf0f1; -fx-text-fill: #34495e; -fx-background-radius: 5; -fx-cursor: hand; -fx-border-color: #bdc3c7; -fx-border-radius: 5;"));
    }

    private void updatePensionList() {
        currentPensionList = new ArrayList<>(controller.findAll());
        displayPensionList();
    }

    private void displayPensionList() {
        pensionGridContainer.getChildren().clear();
        for (Pension pension : currentPensionList) {
            pensionGridContainer.getChildren().add(createPensionCard(pension));
        }
    }

    private void sortById(boolean ascending) {
        if (currentPensionList != null) {
            currentPensionList.sort((p1, p2) -> {
                if (ascending) {
                    return Integer.compare(p1.getId(), p2.getId());
                } else {
                    return Integer.compare(p2.getId(), p1.getId());
                }
            });
            displayPensionList();
        }
    }

    private void sortByName(boolean ascending) {
        if (currentPensionList != null) {
            currentPensionList.sort((p1, p2) -> {
                if (ascending) {
                    return p1.getName().compareTo(p2.getName());
                } else {
                    return p2.getName().compareTo(p1.getName());
                }
            });
            displayPensionList();
        }
    }

    private void searchByName(String name) {
        currentPensionList = new ArrayList<>();
        for (Pension pension : controller.findAll()) {
            if (pension.getName().contains(name)) {
                currentPensionList.add(pension);
            }
        }
        displayPensionList();
    }    private VBox createPensionCard(Pension pension) {
        // 이미지뷰 생성
        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
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
                
                // 뷰포트 설정 (중앙 정사각형 부분만)
                Rectangle2D viewport = new Rectangle2D(offsetX, offsetY, size, size);
                imageView.setViewport(viewport);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // 빈 이미지
        }

        // 펜션 이름 레이블
        Label nameLabel = new Label(pension.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");
        nameLabel.setMaxWidth(180);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);

        // 카드 레이아웃
        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(210);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
            "-fx-cursor: hand;"
        );
        card.getChildren().addAll(imageView, nameLabel);
        
        // 클릭 이벤트 - 상세 정보 화면으로 이동
        card.setOnMouseClicked(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, stage);
            detailView.show();
        });
        
        // 마우스 호버 효과
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(52, 152, 219, 0.4), 15, 0, 0, 5); " +
                "-fx-cursor: hand; " +
                "-fx-scale-x: 1.03; " +
                "-fx-scale-y: 1.03;"
            );
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                "-fx-cursor: hand;"
            );
        });
        
        return card;
    }
}
