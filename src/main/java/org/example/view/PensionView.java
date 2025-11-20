package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private VBox pensionListContainer;
    private List<Pension> currentPensionList;

    public PensionView() {
        this.controller = PensionController.getInstance();
    }public void start(Stage stage) {
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
        });        // 전체 목록 조회 버튼
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

        // 펜션 목록 컨테이너
        pensionListContainer = new VBox(10);
        pensionListContainer.setPadding(new Insets(10));
        updatePensionList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(pensionListContainer);
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
            searchByNameButton,            new Separator(),
            showAllButton,
            sortButtonBox,
            new Label("펜션 목록:"),
            scrollPane
        );

        Scene scene = new Scene(mainLayout, 800, 700);
        stage.setScene(scene);
        stage.show();
    }    private void updatePensionList() {
        currentPensionList = new ArrayList<>(controller.findAll());
        displayPensionList();
    }

    private void displayPensionList() {
        pensionListContainer.getChildren().clear();
        for (Pension pension : currentPensionList) {
            pensionListContainer.getChildren().add(createPensionCard(pension));
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
    }    private void searchByName(String name) {
        currentPensionList = new ArrayList<>();
        for (Pension pension : controller.findAll()) {
            if (pension.getName().contains(name)) {
                currentPensionList.add(pension);
            }
        }
        displayPensionList();
    }private HBox createPensionCard(Pension pension) {
        // 이미지뷰 생성
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(false);  // 비율 유지 안함
        
        // 정사각형으로 중앙 크롭을 위한 뷰포트 설정
        Rectangle2D viewport = null;

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
                viewport = new Rectangle2D(offsetX, offsetY, size, size);
                imageView.setViewport(viewport);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // 빈 이미지
        }

        // 펜션 정보 텍스트
        VBox infoBox = new VBox(5);
        Label nameLabel = new Label("이름: " + pension.getName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label addressLabel = new Label("주소: " + pension.getAddress());
        Label phoneLabel = new Label("전화번호: " + pension.getPhoneNumber());
        Label descLabel = new Label("설명: " + pension.getDescription());
        
        infoBox.getChildren().addAll(nameLabel, addressLabel, phoneLabel, descLabel);

        // 카드 레이아웃
        HBox card = new HBox(15);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
        card.getChildren().addAll(imageView, infoBox);
        
        return card;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
