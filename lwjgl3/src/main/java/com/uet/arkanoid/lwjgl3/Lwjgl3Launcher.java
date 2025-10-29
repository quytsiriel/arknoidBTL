package com.uet.arkanoid.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.uet.arkanoid.ArkanoidGame; // Import lớp Game chính
import com.sun.tools.javac.Main;
import com.uet.arkanoid.ArkanoidGame;

/**
 * Lớp khởi chạy ứng dụng trên Desktop (sử dụng LWJGL3).
 * Thiết lập các thông số cấu hình cửa sổ và khởi tạo lớp ArkanoidGame.
 */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        // Đây là phương thức hỗ trợ cần thiết cho macOS và Windows để khởi chạy ổn định hơn
        // Giả định StartupHelper đã được định nghĩa trong dự án của bạn
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        // Khởi tạo ứng dụng bằng lớp ArkanoidGame đã định nghĩa
        return new Lwjgl3Application(new com.uet.arkanoid.ArkanoidGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        // VSync và FPS
        configuration.useVsync(true);
        // Giới hạn FPS dựa trên tần số quét của màn hình
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        // Lấy tiêu đề từ hằng số trong lớp ArkanoidGame
        configuration.setTitle(com.uet.arkanoid.ArkanoidGame.TITLE);

        // Thiết lập kích thước cửa sổ cố định theo yêu cầu
        configuration.setWindowedMode(1240, 810);
        configuration.setResizable(false); // Khóa kích thước cửa sổ

        // Thiết lập icon cửa sổ
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        return configuration;
    }
}
