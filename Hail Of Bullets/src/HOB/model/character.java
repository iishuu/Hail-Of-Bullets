package HOB.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

public class character extends DisplayableImage implements characterInterface {
    //todo:和子弹碰撞
    //角色移动速度
    private static int xSPEED = 8;
    private int ySPEED = 8;

    //角色尺寸
    private final int characterWIDTH = 30;
    private final int characterHEIGHT = 30;

    //是否活着
    private boolean live = true;

    //生命值
    private int life = 100;

    //绘制角色左上角坐标
    private int characterX, characterY;
    //用于记录角色原来的坐标,碰到墙、坦克时方便退一步
    private int characterOldX, characterOldY;

    //枚举类型定义了角色的八个方向和静止时的方向
    public enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    //角色方向
    private Direction dir;

    //判断是否按下方向键
    private boolean bL = false, bU = false, bR = false, bD = false;

    //血条
    private BloodBar bar = new BloodBar();

    public character(int characterX, int characterY, Direction dir) {
        this.dir = dir;
        this.characterX = characterX;
        this.characterY = characterY;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    private void move() {
        this.characterOldX = characterX;
        this.characterOldY = characterY;
        switch (dir) {
            case L:
                moveLeft();
                break;
            case LU:
                moveLeft();
                moveUp();
                break;
            case U:
                moveUp();
                break;
            case RU:
                moveRight();
                moveUp();
                break;
            case R:
                moveRight();
                break;
            case RD:
                moveRight();
                moveDown();
                break;
            case D:
                moveDown();
                break;
            case LD:
                moveLeft();
                moveDown();
                break;
            case STOP:
                break;
        }
    }

    @Override
    public void moveLeft() {
        characterX -= xSPEED;
    }

    @Override
    public void moveRight() {
        characterX += xSPEED;
    }

    @Override
    public void moveDown() {
        characterY += ySPEED;
    }

    @Override
    public void moveUp() {
        characterY -= ySPEED;
    }

    @Override
    public void stop() {
        setDir(Direction.STOP);
    }
    /**
     * 画出玩家角色
     */
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillOval(characterX, characterY, characterWIDTH, characterHEIGHT);
        g.setColor(c);
        bar.draw(g);//血条

        move();
    }

    /**
     * 玩家角色的血条
     */
    private class BloodBar {
        void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(characterX, characterY + 35, characterWIDTH, 5);
            //里面的
            g.setColor(Color.RED);
            int w = characterWIDTH * life / 100;
            g.fillRect(characterX, characterY + 35, w, 5);
            g.setColor(c);
        }
    }

    /**
     * 角色碰到边框弹回
     */
    private void stay() {
        characterX = characterOldX;
        characterY = characterOldY;
    }


    //按下键时监听
   /* public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }
    */

    /**
     * 松开键时的监听
     * todo:在这设置移动的音效触发
     * 其实没啥必要
     */
    /*public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = false;
                //new Audio(1);
                break;
            case KeyEvent.VK_UP:
                bU = false;
                //new Audio(1);
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                //new Audio(1);
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                //new Audio(1);
                break;
        }
        locateDirection();
    }
    */

    /**
     * 定位角色的方向
     */
    void locateDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.RU;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (bL && !bU && !bR && bD) dir = Direction.LD;
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;

    }

    /**
     * 检查是否撞到边界，是则不可移动
     * todo:右和下边界应该还需要调一调
     */
    public void checkBorder(int width, int height) {
        if (characterX < 0 || characterX > (width - 2 * characterWIDTH) || characterY < 0 || characterY > (height - 2 * characterHEIGHT)) {
            stay();
        }
    }

    @Override
    public boolean isAlive() {
        return live;
    }

    @Override
    public void setCharacterX(int x) {
        this.characterX = x;
    }

    @Override
    public void setCharacterY(int y) {
        this.characterY = y;
    }

    @Override
    public void setAlive() {
        live = true;
    }

    @Override
    public Graphics getGraphic() {
        return null;
    }

    @Override
    public Graphics getBloodBar() {
        return null;
    }

    @Override
    public int graphicHigh() {
        return this.characterHEIGHT;
    }

    @Override
    public int graphicWidth() {
        return this.characterWIDTH;
    }

    @Override
    public int bloodBarHigh() {
        return 5;
    }

    @Override
    public int bloodBarWidth() {
        return 20;
    }

    public void setAlive(boolean live) {
        this.live = live;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public int getCharacterX() {
        return this.characterX;
    }

    @Override
    public int getCharacterY() {
        return this.characterY;
    }


}
