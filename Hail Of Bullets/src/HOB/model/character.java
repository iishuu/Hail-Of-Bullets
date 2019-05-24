package HOB.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

public class character {
    //todo:implements characterInterface
    //角色移动速度
    public static int xSPEED = 8;
    public static int ySPEED = 8;

    //角色尺寸
    public final int WIDTH = 30;
    public final int HEIGHT = 30;

    //是否活着
    private boolean live = true;

    //生命值
    private int life = 100;

    //绘制角色左上角坐标
    private int characterX, characterY;
    //用于记录角色原来的坐标,碰到墙、坦克时方便退一步
    private int characterOldX, characterOldY;

    //枚举类型定义了角色的八个方向和静止时的方向
    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    //角色方向
    private Direction dir = Direction.STOP;

    //判断是否按下方向键
    private boolean bL = false, bU = false, bR = false, bD = false;

    //血条
    private BloodBar bar = new BloodBar();

    void move() {
        this.characterOldX = characterX;
        this.characterOldY = characterY;
        switch (dir) {
            case L:
                characterX -= xSPEED;
                break;
            case LU:
                characterX -= xSPEED;
                characterY -= ySPEED;
                break;
            case U:
                characterY -= ySPEED;
                break;
            case RU:
                characterX += xSPEED;
                characterY -= ySPEED;
                break;
            case R:
                characterX += xSPEED;
                break;
            case RD:
                characterX += xSPEED;
                characterY += ySPEED;
                break;
            case D:
                characterY += ySPEED;
                break;
            case LD:
                characterX -= xSPEED;
                characterY += ySPEED;
                break;
            case STOP:
                break;
        }
    }

    /**
     * 画出玩家角色
     */
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.fillOval(characterX, characterY, WIDTH, HEIGHT);
        g.setColor(c);
        bar.draw(g);//血条
    }

    /**
     * 玩家角色的血条
     */
    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(characterX, characterY - 10, WIDTH, 8);
            //里面的
            g.setColor(Color.RED);
            int w = WIDTH * life / 100;
            g.fillRect(characterX, characterY - 10, w, 8);
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
    public void keyPressed(KeyEvent e) {
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


    /**
     * 松开键时的监听
     * todo:在这设置移动的音效触发
     * 其实没啥必要
     */
    public void keyReleased(KeyEvent e) {
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


    public boolean isAlive() {
        return live;
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
}
