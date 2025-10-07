    package com.uet.arkanoid;

    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Rectangle;

    public class Brick {

        public Texture brickTexture;
        public Rectangle brickRectangle;
        int type;
        private boolean deleted = false;
        int hit;



        public Brick(int t, int x, int y, int h, int w,Texture Brick, int hit) {
            this.type = t;
            this.brickTexture = Brick;
            this.brickRectangle = new Rectangle(x, y, w, h);
            this.hit = hit;
            this.deleted = false;
        }

        public void render(SpriteBatch sb) {
            if (!deleted) {
                sb.draw(brickTexture, brickRectangle.x, brickRectangle.y, brickRectangle.width, brickRectangle.height);
            }
        }

        public void destroy() {
            hit--;
            if (hit <= 0) {
                deleted = true;
            }
        }

        public boolean isDeleted() {
            return deleted;
        }


        public void setType(int i) {
            type = i;
        }

        public void setHit(int i) {
            hit = i;
        }

        public int getHit() {
            return hit;
        }

        public Rectangle getBrickRectangle() {
            return brickRectangle;
        }

        public int getType() {
            return type;
        }

    }
