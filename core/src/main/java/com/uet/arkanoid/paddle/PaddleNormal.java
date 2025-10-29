package com.uet.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.uet.arkanoid.paddle.Paddle;

// Paddle BÂY GIỜ kế thừa từ PaddleNoiChung
public class PaddleNormal extends Paddle {

    // 'texture' và 'bounds' đã được kế thừa từ PaddleNoiChung

    private float speed;

    public PaddleNormal(float x, float y) {
        // 1. Gọi constructor của lớp cha để tải texture "paddle.png"
        super("paddle.png");

        // 2. Định nghĩa các thuộc tính riêng của Paddle
        this.speed = 500f;

        // 3. Thiết lập kích thước mong muốn cho bounds (đã được khởi tạo ở lớp cha)
        float desiredWidth = 130;
        float aspectRatio = (float) texture.getHeight() / texture.getWidth();
        float desiredHeight = desiredWidth * aspectRatio;

        // 4. Thiết lập vị trí và kích thước cho 'bounds'
        this.bounds.set(x, y, desiredWidth, desiredHeight);
    }

    @Override // Định nghĩa lại phương thức abstract từ lớp cha
    public void update(float delta) {
        // Điều khiển paddle (logic giữ nguyên)
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            bounds.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            bounds.x += speed * delta;
        }

        // Giới hạn (logic giữ nguyên)
        if (bounds.x < 0) bounds.x = 0;
        if (bounds.x + bounds.width > Gdx.graphics.getWidth())
            bounds.x = Gdx.graphics.getWidth() - bounds.width;
    }


    // Phương thức này là riêng của Paddle, nên giữ lại
    public void resetPosition() {
        bounds.x = (Gdx.graphics.getWidth() - bounds.width) / 2f;
        bounds.y = 50; // khoảng cách từ đáy lên
    }
}
