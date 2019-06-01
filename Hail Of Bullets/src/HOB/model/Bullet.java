package HOB.model;

import HOB.Const.urls;
import HOB.frame.gamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import HOB.Const.setDefine;

import javax.imageio.ImageIO;

public class Bullet extends DisplayableImage
{
    Direction direction;
    gamePanel gamePanel;
    static final int length = 8;// 子弹的（正方形）边长
    private boolean alive = true;// 子弹是否在边界内
    private character man;
    private int BlletType;//子弹类型
    private int xspeed,yspeed;// 移动速度
    /**
     *
     * 子弹构造方法
     * @param x          子弹的初始横坐标
     * @param y          子弹的初始纵坐标
     * @param xspeed,yspeed      子弹的初始速度
     * @param direction  子弹发射方向
     */
    public Bullet (int x,int y,int xspeed,int yspeed,Direction direction,gamePanel gamePanel,int BulletType)
    {
        super(x,y,length,length);//调用父类构造方法
        this.xspeed=xspeed;
        this.yspeed=yspeed;
        this.direction=direction;
        this.gamePanel=gamePanel;
        this.BlletType=BulletType;
        this.man=gamePanel.getCharacter();//获取主角对象
        init(BulletType);// 初始化组件
    }
    /**
     * 初始化组件
     */
    private void init(int BulletType) {
        if(BulletType==1)
        {
            try
            {
                this.image = ImageIO.read(new File(urls.BULLET_IMAGE_URL1));// 读取背景图片1
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(BulletType==2)
        {
            try
            {
                this.image = ImageIO.read(new File(urls.BULLET_IMAGE_URL2));// 读取背景图片2
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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
            case sector://如果方形
                sectorward();//方形移动
                break;
        }
    }
    /**
     * 向左移动
     */
    private void leftward() {
        x -= xspeed;// 横坐标减少
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 向右移动
     */
    private void rightward() {
        x += xspeed;// 横坐标增加
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 向上移动
     */
    private void upward() {
        y -= xspeed;// 总坐标减少
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 向下移动
     */
    private void downward() {
        y += xspeed;// 纵坐标增加
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    private void sectorward() {
        x += xspeed;  //横坐标增加
        y += yspeed;//纵坐标增加
        moveToBorder();// 移动出面板边界时销毁子弹
    }
    /**
     * 击中主角啦
     */
    public void hitCharacter() {
        if (x + length/2 > man.getX() && x + length/2 < man.getX() + setDefine.characterWidth && y + length/2 > man.getY() && y + length/2 < man.getY() + setDefine.characterHeight)
        {//如果子弹中心位置在角色的矩形中。（适当缩小）
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
