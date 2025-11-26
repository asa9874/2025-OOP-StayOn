package org.example.view;

import javafx.scene.text.Font;

public class FontUtil {
    private static Font pretendardFont;
    private static boolean fontLoaded = false;

    public static void loadFont() {
        if (!fontLoaded) {
            try {
                pretendardFont = Font.loadFont(
                    FontUtil.class.getResourceAsStream("/fonts/Pretendard-Regular.ttf"),
                    14
                );
                fontLoaded = true;
                if (pretendardFont != null) {
                    System.out.println("Pretendard 폰트 로드 성공: " + pretendardFont.getName());
                }
            } catch (Exception e) {
                System.out.println("Pretendard 폰트 로드 실패: " + e.getMessage());
            }
        }
    }

    public static Font getFont(double size) {
        loadFont();
        if (pretendardFont != null) {
            return Font.font(pretendardFont.getFamily(), size);
        }
        return Font.font("System", size);
    }

    public static String getFontFamily() {
        loadFont();
        if (pretendardFont != null) {
            return pretendardFont.getFamily();
        }
        return "System";
    }

    public static String getFontStyle(double size) {
        return "-fx-font-family: '" + getFontFamily() + "'; -fx-font-size: " + size + "px;";
    }
}
