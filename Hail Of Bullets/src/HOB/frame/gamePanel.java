package HOB.frame;

import HOB.Const.*;
import HOB.model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import HOB.model.Bullet;
import HOB.model.character;

public class gamePanel extends JPanel implements KeyListener{
    private int freshTime;//刷新率
    private Image backGround;//显示主图片
    private mainFrame frame;//主窗体
    private startPanel backFrame;
    private character player;//玩家
    private boolean up_key, down_key, left_key, right_key;// 按键是否按下标志，左侧单词是按键名
    private List<Bullet> bullets;// 所有子弹集合
    private volatile boolean finish = false;// 游戏是否结束,是否暂停
    private int width, height;//窗口尺寸
    private int level;
    private timer timer;//计时器
    FreshThead thread;//刷新游戏帧的家伙
    private long score = 0;//分数
    private int scoreLoop = 0;//控制分数增长，用于稀释时间

    public gamePanel(mainFrame frame, startPanel back) throws IOException {//构造方法
        this.frame = frame;//设定主界面
        this.backFrame = back;//设定返回界面
        this.level = frame.selection.getLevel(1);//获取难度
        init();//initialize,初始化
        thread = new FreshThead();//创建游戏帧刷新线程
        thread.start();//开始线程
        addListener();//开启键盘监听
    }

    private void init() throws IOException {
        this.freshTime = setDefine.freshTime;
        this.width = setDefine.width;
        this.height = setDefine.height;
        bullets = new ArrayList<Bullet>();// 实例化子弹集合
        backGround = ImageIO.read(new File(urls.GAME_BACKGROUND_IMAGE_URL));// 读取背景图片
        player = new character();// 实例化玩家集合
        timer = new timer();//实例化计时器
        timer.timerStart();//开始计时器
    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    private class FreshThead extends Thread{//游戏帧刷新线程
        private final Object lock = new Object();
        private boolean pause = false;
        /**
         * 调用该方法实现线程的暂停
         */
        void pauseThread(){
            pause = true;
        }

        /*
        调用该方法实现恢复线程的运行
         */
        void resumeThread(){
            pause =false;
            synchronized (lock){
                lock.notify();
            }
        }

        public boolean isPause() {
            return !pause;
        }

        /**
         * 这个方法只能在run 方法中实现，不然会阻塞主线程，导致页面无响应
         */
        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run() {// 线程主方法
            while (!finish) {// 如果游戏未停止且未暂停
                while (pause){
                    onPause();
                }
                repaint();// 执行本类重绘方法
                System.gc();// 绘制一次会产生大量垃圾对象，回收内存
                checkCollision();//碰撞检测
                scoreAdd();//加分
                if(finish) {//判断游戏结束
                    try {
                        gameOver();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                } try {
                    Thread.sleep(freshTime);// 指定时间后重新绘制界面
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {//判断键盘按下事件
        switch (e.getKeyCode()) {// 判断按下的按键值
            case KeyEvent.VK_W:// 如果按下的是“W”
            case KeyEvent.VK_UP:// 如果按下的是“↑”
                up_key = true;// “W”按下标志为true
                break;
            case KeyEvent.VK_A:// 如果按下的是“A”
            case KeyEvent.VK_LEFT:// 如果按下的是“←”
                left_key = true;// “A”按下标志为true
                break;
            case KeyEvent.VK_S:// 如果按下的是“S”
            case KeyEvent.VK_DOWN:// 如果按下的是“↓”
                down_key = true;// “S”按下标志为true
                break;
            case KeyEvent.VK_D:// 如果按下的是“D”
            case KeyEvent.VK_RIGHT:// 如果按下的是“→”
                right_key = true;// “D”按下标志为true
                break;
            case KeyEvent.VK_ESCAPE:
                if(thread.isPause()) {
                    thread.pauseThread();
                }
                else {
                    thread.resumeThread();
                }
                break;
        }
    }

    public void keyReleased(KeyEvent e) {//判断键盘抬起事件
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:// 如果抬起的是“W”
            case KeyEvent.VK_UP:// 如果抬起的是“↑”
                up_key = false;// “W”按下标志为false
                break;
            case KeyEvent.VK_A:// 如果抬起的是“A”
            case KeyEvent.VK_LEFT:// 如果抬起的是“←”
                left_key = false;// “A”按下标志为false
                break;
            case KeyEvent.VK_S:// 如果抬起的是“S”
            case KeyEvent.VK_DOWN:// 如果抬起的是“↓”
                down_key = false;// “S”按下标志为false
                break;
            case KeyEvent.VK_D:// 如果抬起的是“D”
            case KeyEvent.VK_RIGHT:// 如果抬起的是“→”
                right_key = false;// “D”按下标志为false
                break;
        }
    }

    public void paint(Graphics g) {
        paintMain(g);
        paintBullet(g);
        paintPlayer(g);
    }

    private void paintMain(Graphics g) {    //绘制其他对象
        g.drawImage(backGround, 0, 0, width, height, this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size/2);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString("当前分数：" + score + "", width/80, height/30);// 绘制得分
    }

    private void paintBullet(Graphics g) {    //绘制子弹
        /**
         * bullet
         */
    }

    private void paintPlayer(Graphics g) {    //绘制角色
        /**
         * character
         */
    }

    private void checkCollision() { //碰撞检测
        /** 遍历子弹集合
         * 根据每个子弹的xy坐标，和角色的xy坐标进行运算，
         * 满足某个条件之后，设置finish为true，即游戏结束
         */
        if(right_key&&up_key&&down_key&&left_key) finish = true;
        //由于游戏暂时没有完成，同时前后左右即为暂时的强制退出条件
    }

    private void scoreAdd() {//分数增长
        if(scoreLoop%setDefine.scoreDilution == 0) {//如果循环到了地方
            score++;
        }
        scoreLoop++;
    }

    private void gameOver() throws IOException {
        player.setAlive(false);
        timer.timerStop();
        /**
         * 这一段由iishuu后续完成
         */
        frame.data.writeLong(stringConst.rankKey[1], score);//更新最后得分
        if(score>frame.data.searchLong(stringConst.rankKey[0])) {
            frame.data.writeLong(stringConst.rankKey[0], score);
        }//更新最高分
        gotoBackPanel();
    }

    private void gotoBackPanel() {
        frame.setPanel(backFrame);//返回上一层
        frame.removeKeyListener(this);//删除本对象的键盘监听
        frame.addKeyListener(backFrame);//重新添加上一层的键盘监听
        backFrame.repaint();//重新绘制上一层
    }

    public List<Bullet> getBullets(){
        return bullets;//获取游戏面板所有子弹
    }

    public character getCharacter() {
        return player;//获取玩家
    }

    public void keyTyped(KeyEvent e) {
        //不实现，但不可以删除
    }

}
