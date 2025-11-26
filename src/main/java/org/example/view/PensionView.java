package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionController;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
import org.example.domain.review.Review;
import org.example.domain.review.ReviewController;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PensionView {
    private final PensionController controller;
    private final RoomController roomController;
    private final ReviewController reviewController;
    private FlowPane pensionGridContainer;
    private List<Pension> currentPensionList;
    private Stage stage;
    private TextField searchField;
    private Label resultCountLabel;
    private List<Button> filterButtons;
    private Button activeFilterButton;
    private Customer customer;

    public PensionView() {
        this.controller = PensionController.getInstance();
        this.roomController = RoomController.getInstance();
        this.reviewController = ReviewController.getInstance();
        this.filterButtons = new ArrayList<>();
    }

    public PensionView(Customer customer) {
        this();
        this.customer = customer;
    }

    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("StayOn - 펜션 예약");

        // 메인 컨테이너
        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더 생성
        HBox header = createHeader();

        // 히어로 섹션 생성
        VBox heroSection = createHeroSection();

        // 필터 섹션 생성
        HBox filterSection = createFilterSection();

        // 펜션 그리드 컨테이너
        pensionGridContainer = new FlowPane();
        pensionGridContainer.setHgap(30);
        pensionGridContainer.setVgap(30);
        pensionGridContainer.setPadding(new Insets(30, 50, 50, 50));
        pensionGridContainer.setAlignment(Pos.CENTER);
        pensionGridContainer.setStyle("-fx-background-color: transparent;");

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(pensionGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-background-color: #f8fafc; -fx-border-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 데이터 로드
        updatePensionList();        mainContainer.getChildren().addAll(header, heroSection, filterSection, scrollPane);

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

        // 로고
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoView.setFitHeight(36);
            logoView.setPreserveRatio(true);
            logoBox.getChildren().add(logoView);
        } catch (Exception e) {
            Label logoText = new Label("StayOn");
            logoText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
            logoBox.getChildren().add(logoText);
        }

        // 네비게이션 메뉴
        HBox navMenu = new HBox(30);
        navMenu.setAlignment(Pos.CENTER);
        navMenu.setPadding(new Insets(0, 0, 0, 50));

        Label navPension = createNavLabel("펜션 목록", true);
        Label navAbout = createNavLabel("서비스 소개", false);
        Label navHelp = createNavLabel("고객센터", false);

        navMenu.getChildren().addAll(navPension, navAbout, navHelp);

        // 우측 영역 (로그아웃)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("로그아웃");
        logoutBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        );
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(
            "-fx-background-color: #fee2e2; " +
            "-fx-text-fill: #dc2626; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-color: #fecaca; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        ));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        ));
        logoutBtn.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        Button reservationBtn = new Button("예약 조회");
        reservationBtn.setStyle(
            "-fx-background-color: #2563eb; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        );
        reservationBtn.setOnMouseEntered(e -> reservationBtn.setStyle(
            "-fx-background-color: #1d4ed8; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        ));
        reservationBtn.setOnMouseExited(e -> reservationBtn.setStyle(
            "-fx-background-color: #2563eb; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8 20; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20;"
        ));        reservationBtn.setOnAction(e -> {
            ReservationListView reservationListView = new ReservationListView(customer, stage);
            reservationListView.show();
        });

        header.getChildren().addAll(logoBox, navMenu, spacer, reservationBtn, logoutBtn);
        return header;
    }

    private Label createNavLabel(String text, boolean active) {
        Label label = new Label(text);
        if (active) {
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-cursor: hand;");
            label.setOnMouseEntered(e -> label.setStyle("-fx-font-size: 14px; -fx-text-fill: #2563eb; -fx-cursor: hand;"));
            label.setOnMouseExited(e -> label.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-cursor: hand;"));
        }
        return label;
    }

    private VBox createHeroSection() {
        VBox hero = new VBox(20);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(40, 40, 30, 40));
        hero.setStyle(
            "-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed);"
        );

        // 메인 타이틀
        Label title = new Label("완벽한 휴식을 위한 펜션");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("국내 최고의 펜션을 만나보세요");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        // 검색 바
        HBox searchBar = new HBox(0);
        searchBar.setAlignment(Pos.CENTER);
        searchBar.setMaxWidth(600);
        searchBar.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 30; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 5);"
        );

        searchField = new TextField();
        searchField.setPromptText("펜션 이름으로 검색하세요");
        searchField.setPrefWidth(450);
        searchField.setPrefHeight(50);
        searchField.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-font-size: 15px; " +
            "-fx-padding: 0 25; " +
            "-fx-prompt-text-fill: #94a3b8;"
        );
        searchField.setOnAction(e -> performSearch());

        Button searchBtn = new Button("검색");
        searchBtn.setPrefHeight(50);
        searchBtn.setPrefWidth(100);
        searchBtn.setStyle(
            "-fx-background-color: #2563eb; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 0 30 30 0; " +
            "-fx-cursor: hand;"
        );
        searchBtn.setOnMouseEntered(e -> searchBtn.setStyle(
            "-fx-background-color: #1d4ed8; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 0 30 30 0; " +
            "-fx-cursor: hand;"
        ));
        searchBtn.setOnMouseExited(e -> searchBtn.setStyle(
            "-fx-background-color: #2563eb; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 0 30 30 0; " +
            "-fx-cursor: hand;"
        ));
        searchBtn.setOnAction(e -> performSearch());

        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBar.getChildren().addAll(searchField, searchBtn);

        hero.getChildren().addAll(title, subtitle, searchBar);
        return hero;
    }

    private HBox createFilterSection() {
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(20, 50, 10, 50));
        filterBox.setStyle("-fx-background-color: #f8fafc;");

        // 결과 카운트
        resultCountLabel = new Label("전체 펜션");
        resultCountLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 정렬 버튼들
        HBox sortButtons = new HBox(10);
        sortButtons.setAlignment(Pos.CENTER_RIGHT);

        Button showAllBtn = createFilterButton("전체보기");
        showAllBtn.setOnAction(e -> {
            setActiveFilter(showAllBtn);
            searchField.clear();
            updatePensionList();
        });

        Button sortIdAsc = createFilterButton("ID ↑");
        sortIdAsc.setOnAction(e -> {
            setActiveFilter(sortIdAsc);
            sortById(true);
        });

        Button sortIdDesc = createFilterButton("ID ↓");
        sortIdDesc.setOnAction(e -> {
            setActiveFilter(sortIdDesc);
            sortById(false);
        });

        Button sortNameAsc = createFilterButton("이름 ↑");
        sortNameAsc.setOnAction(e -> {
            setActiveFilter(sortNameAsc);
            sortByName(true);
        });

        Button sortNameDesc = createFilterButton("이름 ↓");
        sortNameDesc.setOnAction(e -> {
            setActiveFilter(sortNameDesc);
            sortByName(false);
        });

        filterButtons.add(showAllBtn);
        filterButtons.add(sortIdAsc);
        filterButtons.add(sortIdDesc);
        filterButtons.add(sortNameAsc);
        filterButtons.add(sortNameDesc);

        // 기본 활성화 버튼
        setActiveFilter(showAllBtn);

        sortButtons.getChildren().addAll(showAllBtn, sortIdAsc, sortIdDesc, sortNameAsc, sortNameDesc);

        filterBox.getChildren().addAll(resultCountLabel, spacer, sortButtons);
        return filterBox;
    }

    private Button createFilterButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(getInactiveFilterStyle());
        btn.setOnMouseEntered(e -> {
            if (btn != activeFilterButton) {
                btn.setStyle(getHoverFilterStyle());
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeFilterButton) {
                btn.setStyle(getInactiveFilterStyle());
            }
        });
        return btn;
    }

    private void setActiveFilter(Button btn) {
        // 이전 활성 버튼 비활성화
        if (activeFilterButton != null) {
            activeFilterButton.setStyle(getInactiveFilterStyle());
        }
        // 새 버튼 활성화
        activeFilterButton = btn;
        btn.setStyle(getActiveFilterStyle());
    }

    private String getActiveFilterStyle() {
        return "-fx-background-color: #2563eb; " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 8 18; " +
               "-fx-background-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private String getInactiveFilterStyle() {
        return "-fx-background-color: white; " +
               "-fx-text-fill: #64748b; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 8 18; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private String getHoverFilterStyle() {
        return "-fx-background-color: #f1f5f9; " +
               "-fx-text-fill: #2563eb; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 8 18; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            updatePensionList();
        } else {
            searchByName(keyword);
        }
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
        resultCountLabel.setText("전체 " + currentPensionList.size() + "개의 펜션");
    }

    private void sortById(boolean ascending) {
        if (currentPensionList != null) {
            currentPensionList.sort((p1, p2) -> ascending ?
                Integer.compare(p1.getId(), p2.getId()) :
                Integer.compare(p2.getId(), p1.getId()));
            displayPensionList();
        }
    }

    private void sortByName(boolean ascending) {
        if (currentPensionList != null) {
            currentPensionList.sort((p1, p2) -> ascending ?
                p1.getName().compareTo(p2.getName()) :
                p2.getName().compareTo(p1.getName()));
            displayPensionList();
        }
    }

    private void searchByName(String name) {
        currentPensionList = new ArrayList<>();
        for (Pension pension : controller.findAll()) {
            if (pension.getName().toLowerCase().contains(name.toLowerCase())) {
                currentPensionList.add(pension);
            }
        }
        displayPensionList();
    }

    // 펜션의 리뷰 정보 계산 (Room들을 통해)
    private int[] getPensionReviewInfo(Pension pension) {
        List<Room> rooms = roomController.findByPensionId(pension.getId());
        int totalReviews = 0;
        int totalRate = 0;

        for (Room room : rooms) {
            List<Review> reviews = reviewController.findByRoomId(room.getId());
            totalReviews += reviews.size();
            for (Review review : reviews) {
                totalRate += review.getRate();
            }
        }

        double avgRate = totalReviews > 0 ? (double) totalRate / totalReviews : 0;
        return new int[]{totalReviews, (int) Math.round(avgRate * 10)}; // [리뷰수, 평균*10]
    }

    // 펜션의 최저가 계산
    private int getPensionMinPrice(Pension pension) {
        List<Room> rooms = roomController.findByPensionId(pension.getId());
        int minPrice = Integer.MAX_VALUE;

        for (Room room : rooms) {
            if (room.getPrice() < minPrice) {
                minPrice = room.getPrice();
            }
        }

        return minPrice == Integer.MAX_VALUE ? 0 : minPrice;
    }

    private VBox createPensionCard(Pension pension) {
        VBox card = new VBox(0);
        card.setMaxWidth(280);
        card.setMinWidth(280);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        // 이미지 컨테이너
        StackPane imageContainer = new StackPane();
        imageContainer.setMinHeight(200);
        imageContainer.setMaxHeight(200);
        imageContainer.setStyle("-fx-background-radius: 16 16 0 0;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(280);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);

        // 이미지 로드
        try {
            File imageFile = new File(pension.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                double size = Math.min(imageWidth, imageHeight);
                double offsetX = (imageWidth - size) / 2;
                double offsetY = (imageHeight - size) / 2;
                Rectangle2D viewport = new Rectangle2D(offsetX, offsetY, size, size);
                imageView.setViewport(viewport);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // 기본 이미지 또는 배경색
            imageContainer.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 16 16 0 0;");
        }

        // 이미지 클립 (둥근 모서리)
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(280, 200);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        imageView.setClip(clip);

        // 하트 아이콘 (즐겨찾기)
        Label heartIcon = new Label("♡");
        heartIcon.setStyle(
            "-fx-font-size: 22px; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: rgba(0,0,0,0.3); " +
            "-fx-padding: 5 8; " +
            "-fx-background-radius: 20; " +
            "-fx-cursor: hand;"
        );
        StackPane.setAlignment(heartIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(heartIcon, new Insets(12, 12, 0, 0));

        imageContainer.getChildren().addAll(imageView, heartIcon);

        // 정보 영역
        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(18, 18, 20, 18));

        // 펜션 이름
        Label nameLabel = new Label(pension.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        nameLabel.setWrapText(true);

        // 위치 정보
        Label locationLabel = new Label("📍 " + (pension.getAddress() != null ? pension.getAddress() : "위치 정보 없음"));
        locationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
        locationLabel.setWrapText(true);
        locationLabel.setMaxWidth(244);

        // 리뷰 정보 가져오기
        int[] reviewInfo = getPensionReviewInfo(pension);
        int reviewCount = reviewInfo[0];
        double avgRate = reviewInfo[1] / 10.0;

        // 평점 및 리뷰
        HBox ratingBox = new HBox(8);
        ratingBox.setAlignment(Pos.CENTER_LEFT);

        Label star = new Label("⭐");
        star.setStyle("-fx-font-size: 14px;");

        Label rating = new Label(reviewCount > 0 ? String.format("%.1f", avgRate) : "-");
        rating.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label reviews = new Label("(" + reviewCount + "개 리뷰)");
        reviews.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        ratingBox.getChildren().addAll(star, rating, reviews);

        // 최저가 가져오기
        int minPrice = getPensionMinPrice(pension);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        // 가격 정보
        HBox priceBox = new HBox(5);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        priceBox.setPadding(new Insets(8, 0, 0, 0));

        Label price = new Label(minPrice > 0 ? "₩" + numberFormat.format(minPrice) : "가격 정보 없음");
        price.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

        Label perNight = new Label(minPrice > 0 ? "~ / 1박" : "");
        perNight.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        priceBox.getChildren().addAll(price, perNight);

        infoBox.getChildren().addAll(nameLabel, locationLabel, ratingBox, priceBox);
        card.getChildren().addAll(imageContainer, infoBox);

        // 호버 효과
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(37, 99, 235, 0.25), 25, 0, 0, 8); " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;"
            );
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
            );
        });        // 클릭 이벤트
        card.setOnMouseClicked(e -> {
            PensionDetailView detailView = new PensionDetailView(pension, customer, stage);
            detailView.show();
        });
        card.setCursor(javafx.scene.Cursor.HAND);

        return card;
    }
}
