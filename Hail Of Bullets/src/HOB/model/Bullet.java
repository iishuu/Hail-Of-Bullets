package HOB.model;

import HOB.frame.gamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Bullet extends DisplayableImage
{
    Direction direction;
    gamePanel gamePanel;
    static final int length= 8;// 子弹的（正方形）边长
    private int speed;// 移动速度
    private boolean alive = true;// 子弹是否在边界内
    Color color = Color.ORANGE;// 子弹颜色.黄色
    private character man;
    /**
     *
     * 子弹构造方法
     * @param x          子弹的初始横坐标
     * @param y          子弹的初始纵坐标
     * @param speed      子弹的初始速度
     * @param direction  子弹发射方向
     */
    public Bullet (int x,int y,int speed,Direction direction,gamePanel gamePanel)
    {
        super(x,y,length,length);//调用父类构造方法
        this.direction=direction;
        this.gamePanel=gamePanel;
        this.speed=speed;
        init();// 初始化组件
    }
    /**
     * 初始化组件
     */
    private void init() {
        Graphics g = image.getGraphics();// 获取图片的绘图方法
        g.setColor(Color.WHITE);// 使用黑色绘图
        g.fillRect(0, 0, length, length);// 绘制一个铺满整个图片的黑色实心矩形
        g.setColor(color);// 使用子弹颜色
        g.fillOval(0, 0, length, length);// 绘制一个铺满整个图片的实心圆形
        g.drawOval(0, 0, length - 1, length - 1);// 给圆形绘制一个边框，防止出界，宽高减小1像素
    }
    /**
     * 子弹移动
     */
    public void move() {
        switch (direction) {// 判断移动方向
            case up:// 如果向上
                upward();// 向上移动
                break;
            case down:// 如果向下
                downward();// 向下移动
                break;
            case left:// 如果向左
                leftward();// 向左移动
                break;
            case right:// 如果向右
                rightward();// 向右移动
                break;
        }
    }
    /**
     * 向左移动
     */
    private void leftward() {
        x -= speed;// 横坐标减少
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 向右移动
     */
    private void rightward() {
        x += speed;// 横坐标增加
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 向上移动
     */
    private void upward() {
        y -= speed;// 总坐标减少
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 向下移动
     */
    private void downward() {
        y += speed;// 纵坐标增加
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 击中主角啦
     */
    public void hitman()
    {
        man=gamePanel.getCharacter();//获取主角对象
        if(man.isAlive() && this.hit(man))
        {
            this.dispose();//销毁子弹
            man.setAlive(false);//主角凉凉
        }
    }
    private void moveToBorder() {
        if (x < 0 || x > width - getWidth() || y < 0 || y > height - getHeight()) {// 如果子弹坐标离开游戏面板
            this.dispose();// 销毁子弹
        }
    }
    /**
     * 销毁子弹
     */
    private synchronized void dispose() {
        this.alive = false;// 存活（有效）状态变为false
    }
    /**
     * 获取子弹存活状态
     */
    public boolean isAlive() {
        return true;
    }
}
