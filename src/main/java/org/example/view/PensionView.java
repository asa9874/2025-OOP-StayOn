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
    }

    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("펜션 목록 검색");

        // 검색 필드
        TextField searchNameField = new TextField();
        searchNameField.setPromptText("펜션 이름으로 검색");

        // 이름으로 검색 버튼
        Button searchByNameButton = new Button("이름으로 검색");
        searchByNameButton.setOnAction(e -> {
            String nameText = searchNameField.getText();
            if (!nameText.isEmpty()) {
                searchByName(nameText);
            }
        });

        // 전체 목록 조회 버튼
        Button showAllButton = new Button("전체 목록 조회");
        showAllButton.setOnAction(e -> updatePensionList());

        // 정렬 버튼들
        Button sortByIdAscButton = new Button("ID ↑");
        sortByIdAscButton.setOnAction(e -> sortById(true));
        
        Button sortByIdDescButton = new Button("ID ↓");
        sortByIdDescButton.setOnAction(e -> sortById(false));
        
        Button sortByNameAscButton = new Button("이름 ↑");
        sortByNameAscButton.setOnAction(e -> sortByName(true));
        
        Button sortByNameDescButton = new Button("이름 ↓");
        sortByNameDescButton.setOnAction(e -> sortByName(false));

        HBox sortButtonBox = new HBox(10);
        sortButtonBox.getChildren().addAll(
            new Label("정렬:"),
            sortByIdAscButton,
            sortByIdDescButton,
            sortByNameAscButton,
            sortByNameDescButton
        );

        // 펜션 그리드 컨테이너 (FlowPane 사용으로 자동 줄바꿈)
        pensionGridContainer = new FlowPane();
        pensionGridContainer.setHgap(20);
        pensionGridContainer.setVgap(20);
        pensionGridContainer.setPadding(new Insets(10));
        updatePensionList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(pensionGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        // 뒤로가기 버튼
        Button backButton = new Button("뒤로가기");
        backButton.setOnAction(e -> {
            MainView mainView = new MainView(stage);
            mainView.show();
        });

        // 레이아웃
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(
            backButton,
            new Label("펜션 검색"),
            new Separator(),
            new Label("펜션 이름:"),
            searchNameField,
            searchByNameButton,
            new Separator(),
            showAllButton,
            sortButtonBox,
            new Label("펜션 목록:"),
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 900, 700);
        stage.setScene(scene);
        stage.show();
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
    }

    private VBox createPensionCard(Pension pension) {
        // 이미지뷰 생성
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
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
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setMaxWidth(150);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);

        // 카드 레이아웃
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9; -fx-cursor: hand;");
        card.getChildren().addAll(imageView, nameLabel);
        
        // 클릭 이벤트 - 상세 정보 화면으로 이동
        card.setOnMouseClicked(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, stage);
            detailView.show();
        });
        
        // 마우스 호버 효과
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-border-color: #0066cc; -fx-border-width: 2; -fx-background-color: #e6f2ff; -fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9; -fx-cursor: hand;");
        });
        
        return card;
    }
}
