package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.pension.Pension;
import org.example.domain.room.Room;
import org.example.domain.room.RoomController;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
import org.example.domain.user.customer.Customer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RoomSelectView {    
    private final Pension pension;
    private final Stage stage;
    private final RoomController roomController;
    private final Customer customer;
    private FlowPane roomGridContainer;
    private List<Room> currentRoomList;
    private List<Button> filterButtons;
    private Button activeFilterButton;

    public RoomSelectView(Pension pension, Customer customer, Stage stage) {
        this.pension = pension;
        this.customer = customer;
        this.stage = stage;
        this.roomController = RoomController.getInstance();
        this.filterButtons = new ArrayList<>();
    }

    public void show() {
        stage.setTitle("StayOn - 객실 선택");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");

        // 헤더
        HBox header = createHeader();

        // 히어로 섹션
        VBox heroSection = createHeroSection();

        // 필터 섹션
        HBox filterSection = createFilterSection();

        // 객실 그리드
        roomGridContainer = new FlowPane();
        roomGridContainer.setHgap(25);
        roomGridContainer.setVgap(25);
        roomGridContainer.setPadding(new Insets(30, 50, 50, 50));
        roomGridContainer.setAlignment(Pos.CENTER);
        roomGridContainer.setStyle("-fx-background-color: transparent;");

        updateRoomList();

        // 스크롤 패널
        ScrollPane scrollPane = new ScrollPane(roomGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-background-color: #f8fafc; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);        mainContainer.getChildren().addAll(header, heroSection, filterSection, scrollPane);

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
        hero.setStyle("-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed);");

        Label titleLabel = new Label("🛏️ 객실 선택");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label(pension.getName() + "의 객실을 선택해 주세요");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.85);");

        hero.getChildren().addAll(titleLabel, subtitleLabel);

        return hero;
    }

    private HBox createFilterSection() {
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.setPadding(new Insets(20, 50, 10, 50));
        filterBox.setStyle("-fx-background-color: #f8fafc;");

        Button showAllBtn = createFilterButton("전체");
        showAllBtn.setOnAction(e -> {
            setActiveFilter(showAllBtn);
            updateRoomList();
        });

        Button filterSingleBtn = createFilterButton("🏠 독채형");
        filterSingleBtn.setOnAction(e -> {
            setActiveFilter(filterSingleBtn);
            filterByType(RoomType.SINGLE);
        });

        Button filterDuplexBtn = createFilterButton("🏢 복층형");
        filterDuplexBtn.setOnAction(e -> {
            setActiveFilter(filterDuplexBtn);
            filterByType(RoomType.DUPLEX);
        });

        Button filterHotelBtn = createFilterButton("🏨 호텔형");
        filterHotelBtn.setOnAction(e -> {
            setActiveFilter(filterHotelBtn);
            filterByType(RoomType.HOTEL);
        });

        Button filterAvailableBtn = createFilterButton("✅ 예약가능");
        filterAvailableBtn.setOnAction(e -> {
            setActiveFilter(filterAvailableBtn);
            filterAvailableRooms();
        });

        filterButtons.addAll(List.of(showAllBtn, filterSingleBtn, filterDuplexBtn, filterHotelBtn, filterAvailableBtn));
        setActiveFilter(showAllBtn);

        filterBox.getChildren().addAll(showAllBtn, filterSingleBtn, filterDuplexBtn, filterHotelBtn, filterAvailableBtn);

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
        if (activeFilterButton != null) {
            activeFilterButton.setStyle(getInactiveFilterStyle());
        }
        activeFilterButton = btn;
        btn.setStyle(getActiveFilterStyle());
    }

    private String getActiveFilterStyle() {
        return "-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed); " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private String getInactiveFilterStyle() {
        return "-fx-background-color: white; " +
               "-fx-text-fill: #64748b; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #e2e8f0; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
    }

    private String getHoverFilterStyle() {
        return "-fx-background-color: #f1f5f9; " +
               "-fx-text-fill: #2563eb; " +
               "-fx-font-size: 13px; " +
               "-fx-padding: 10 20; " +
               "-fx-background-radius: 20; " +
               "-fx-border-color: #2563eb; " +
               "-fx-border-radius: 20; " +
               "-fx-cursor: hand;";
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

    private void updateRoomList() {
        currentRoomList = new ArrayList<>(roomController.findByPensionId(pension.getId()));
        displayRoomList();
    }

    private void filterByType(RoomType type) {
        currentRoomList = new ArrayList<>();
        for (Room room : roomController.findByPensionId(pension.getId())) {
            if (room.getRoomType() == type) {
                currentRoomList.add(room);
            }
        }
        displayRoomList();
    }

    private void filterAvailableRooms() {
        currentRoomList = new ArrayList<>();
        for (Room room : roomController.findByPensionId(pension.getId())) {
            if (room.getRoomStatus() == RoomStatus.CLEANING) {
                currentRoomList.add(room);
            }
        }
        displayRoomList();
    }

    private void displayRoomList() {
        roomGridContainer.getChildren().clear();
        if (currentRoomList.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(60));
            emptyBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
            );

            Label emptyIcon = new Label("🛏️");
            emptyIcon.setStyle("-fx-font-size: 48px;");

            Label noRoomLabel = new Label("해당하는 객실이 없습니다.");
            noRoomLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #94a3b8;");

            emptyBox.getChildren().addAll(emptyIcon, noRoomLabel);
            roomGridContainer.getChildren().add(emptyBox);
        } else {
            for (Room room : currentRoomList) {
                roomGridContainer.getChildren().add(createRoomCard(room));
            }
        }
    }

    private VBox createRoomCard(Room room) {
        VBox card = new VBox(0);
        card.setMaxWidth(320);
        card.setMinWidth(320);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        // 이미지 컨테이너
        StackPane imageContainer = new StackPane();
        imageContainer.setMinHeight(200);
        imageContainer.setMaxHeight(200);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);

        try {
            File imageFile = new File(room.getImage());
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
            imageContainer.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 16 16 0 0;");
        }

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(320, 200);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        imageView.setClip(clip);

        // 상태 태그 (이미지 위)
        HBox tagBox = new HBox(8);
        tagBox.setAlignment(Pos.TOP_LEFT);
        tagBox.setPadding(new Insets(12));

        Label typeTag = new Label(getRoomTypeText(room.getRoomType()));
        typeTag.setStyle(
            "-fx-background-color: rgba(37, 99, 235, 0.9); " +
            "-fx-text-fill: white; " +
            "-fx-padding: 4 12; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 11px;"
        );

        Label statusTag = new Label(getRoomStatusText(room.getRoomStatus()));
        String statusColor = getStatusTagColor(room.getRoomStatus());
        statusTag.setStyle(
            "-fx-background-color: " + statusColor + "; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 4 12; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 11px;"
        );

        tagBox.getChildren().addAll(typeTag, statusTag);
        StackPane.setAlignment(tagBox, Pos.TOP_LEFT);

        imageContainer.getChildren().addAll(imageView, tagBox);

        // 정보 영역
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(18));

        Label nameLabel = new Label(room.getRoomName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox detailsBox = new HBox(15);
        detailsBox.setAlignment(Pos.CENTER_LEFT);

        Label peopleLabel = new Label("👥 최대 " + room.getMaxPeople() + "명");
        peopleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label floorLabel = new Label("🏢 " + room.getFloor() + "층 / " + room.getBuilding() + "동");
        floorLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        detailsBox.getChildren().addAll(peopleLabel, floorLabel);

        Label priceLabel = new Label(String.format("%,d원", room.getPrice()) + " / 1박");
        priceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

        // 예약 버튼 영역
        HBox reserveBox = new HBox(15);
        reserveBox.setAlignment(Pos.CENTER_LEFT);
        reserveBox.setPadding(new Insets(10, 0, 0, 0));

        Label selectLabel = new Label("객실 수:");
        selectLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Spinner<Integer> roomCountSpinner = new Spinner<>(0, 10, 1);
        roomCountSpinner.setPrefWidth(70);
        roomCountSpinner.setEditable(true);
        roomCountSpinner.setStyle("-fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button reserveButton = new Button("예약하기");
        reserveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 20; " +
            "-fx-cursor: hand;"
        );
        reserveButton.setOnMouseEntered(e -> reserveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #1d4ed8, #6d28d9); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 20; " +
            "-fx-cursor: hand;"
        ));
        reserveButton.setOnMouseExited(e -> reserveButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 20; " +
            "-fx-cursor: hand;"
        ));        reserveButton.setOnAction(e -> {
            int selectedCount = roomCountSpinner.getValue();
            if (selectedCount > 0) {
                PaymentView paymentView = new PaymentView(pension.getId(), room.getId(), selectedCount, customer);
                try {
                    paymentView.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("경고");
                alert.setHeaderText(null);
                alert.setContentText("객실 수를 1개 이상 선택해주세요.");
                alert.showAndWait();
            }
        });

        reserveBox.getChildren().addAll(selectLabel, roomCountSpinner, spacer, reserveButton);

        infoBox.getChildren().addAll(nameLabel, detailsBox, priceLabel, reserveBox);
        card.getChildren().addAll(imageContainer, infoBox);

        // 호버 효과
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(37, 99, 235, 0.25), 25, 0, 0, 8); " +
            "-fx-scale-x: 1.02; " +
            "-fx-scale-y: 1.02;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        ));

        return card;
    }

    private String getStatusTagColor(RoomStatus status) {
        switch (status) {
            case CLEANING:
                return "rgba(16, 185, 129, 0.9)";
            case NOTSALES:
                return "rgba(239, 68, 68, 0.9)";
            case RESERVATION:
            case RESERVATIONED:
                return "rgba(245, 158, 11, 0.9)";
            default:
                return "rgba(100, 116, 139, 0.9)";
        }
    }

    private String getRoomStatusText(RoomStatus status) {
        switch (status) {
            case RESERVATION:
                return "예약 중";
            case RESERVATIONED:
                return "예약완료";
            case USING:
                return "청소 중";
            case CHECKING:
                return "점검 중";
            case CLEANING:
                return "판매 중";
            case NOTSALES:
                return "판매 중지";
            default:
                return status.toString();
        }
    }

    private String getRoomTypeText(RoomType type) {
        switch (type) {
            case DUPLEX:
                return "복층형";
            case SINGLE:
                return "독채형";
            case HOTEL:
                return "호텔형";
            default:
                return type.toString();
        }
    }
}