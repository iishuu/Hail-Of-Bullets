package HOB.frame;

import HOB.Const.*;
import HOB.model.*;
import HOB.global.audioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import HOB.model.Bullet;
import HOB.model.character;

public class gamePanel extends Panel{
    private int freshTime;//刷新率
    private Image backGround;//显示主图片
    private mainFrame frame;//主窗体
    private Panel backFrame;
    private character player;//玩家
    private boolean up_key, down_key, left_key, right_key;// 按键是否按下标志，左侧单词是按键名
    private List<Bullet> bullets;// 所有子弹集合
    private volatile boolean finish = false;// 游戏是否结束,是否暂停
    private int width, height;//窗口尺寸
    private int level;
    private FreshThead thread;//刷新游戏帧的家伙
    private long score = 0;//分数
    private int allTime = 0;//控制分数增长，用于稀释时间
    private int maxSpeed=3;
    private Bullet warning;//预警子弹
    private int tempx,tempy;//方形子弹阵的坐标
    private audioPlayer music;

    public gamePanel(mainFrame frame, Panel back) throws IOException {//构造方法
        this.frame = frame;//设定主界面
        this.backFrame = back;//设定返回界面
        this.level = frame.selection.getLevel(1);//获取难度
        this.freshTime = setDefine.freshTime;
        this.width = setDefine.width;
        this.height = setDefine.height;
        music = frame.musicPlayer;
        init();//initialize,初始化
        thread = new FreshThead();//创建游戏帧刷新线程
        thread.start();//开始线程
        addListener();//开启键盘监听
    }

    private void init() throws IOException {
        bullets = new ArrayList<Bullet>();// 实例化子弹集合
        switch (level) {
            case 1:
                music.setFilePath(urls.GAME_BGM_1);
                backGround = ImageIO.read(new File(urls.GAME_BACKGROUND_IMAGE_1));// 读取背景图片
                break;
            case 2:
                music.setFilePath(urls.GAME_BGM_2);
                backGround = ImageIO.read(new File(urls.GAME_BACKGROUND_IMAGE_2));// 读取背景图片
                break;
            default:
            case 3:
                music.setFilePath(urls.GAME_BGM_3);
                backGround = ImageIO.read(new File(urls.GAME_BACKGROUND_IMAGE_3));// 读取背景图片
                break;
        }
        music.play();
        if (frame.selection.getFun() == 1) {
            player = new character(width / 2, height / 2, true);// 实例化玩家
        }
        else {
            player = new character(width / 2, height / 2, false);// 实例化玩家
        }
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
            return pause;
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
            case KeyEvent.VK_P:
                if (!finish) pauseEvent();
                break;
        }
    }

    public void pauseEvent() {
        if(!thread.isPause()) {
            thread.pauseThread();
            gotoPausePanel();
        }
        else {
            thread.resumeThread();
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
        if(!thread.isPause()) {
            paintBullet(g);
            paintPlayer(g);
        }
    }

    private void paintMain(Graphics g) {    //绘制其他对象
        g.drawImage(backGround, 0, 0, width, height, this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size/2);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString("当前分数：" + score + "", width/80, height/30);// 绘制得分
    }

    private void addBullet(Bullet b) // 子弹的添加
    {
        bullets.add(b);
    }

    private void warning()//设置预警的子弹
    {
        do {
            tempx = (int) (100 + (Math.random() * (width - 200)));//在X为100-700生成方形子弹阵的X坐标
            tempy = (int) (100 + (Math.random() * (height - 200)));//在Y为100-500生成方形子弹阵的Y坐标
        }while(tempx>player.getX() && tempx<player.getX()+setDefine.characterWidth && tempy>player.getY() && tempy<player.getY()+setDefine.characterHeight);//防止刷到脸上
        warning=new Bullet(tempx,tempy,0,0,Direction.sector,this,2);
        addBullet(warning);
    }
    private void buildBullet()
    {
        for(int j=1;j<=4;j++)//上下左右四个方向生成子弹
        {
            Bullet a = null;
                switch (j)
                {
                    case 1 :a = new Bullet(0,(int) (Math.random() * height),(int)(Math.random()*maxSpeed)+1,0,Direction.right,this,1);
                        break;
                    case 2 :a = new Bullet(width,(int)(Math.random() * height),(int)(Math.random()*maxSpeed)+1,0,Direction.left,this,1);
                        break;
                    case 3 :a = new Bullet((int)(Math.random() * width),0,(int)(Math.random()*maxSpeed)+1,0,Direction.down,this,1);
                        break;
                    case 4 :a = new Bullet((int)(Math.random() * width),height,(int)(Math.random()*maxSpeed)+1,0,Direction.up,this,1);
                }
            addBullet(a);
        }
    }
    private void buildsBullet(int tempx,int tempy)
    {
        bullets.remove(warning);
        for(int i=-2;i<=2;i++)//生成方形子弹的X,Y速度
        {
            for(int j=-2;j<=2;j++)
            {
                if(i!=0 && j!=0)
                {
                    Bullet a=new Bullet(tempx,tempy,i,j,Direction.sector,this,2);
                    addBullet(a);
                }
                else if(i==0 && j!=0)
                {
                    Bullet a=new Bullet(tempx,tempy,0,j,Direction.sector,this,2);
                    addBullet(a);
                }
                else if(i!=0 && j==0)
                {
                    Bullet a=new Bullet(tempx,tempy,i,0,Direction.sector,this,2);
                    addBullet(a);
                }
            }
        }
    }
    private void paintBullet(Graphics g) //绘制子弹
    {
        switch (level)
        {
            case 1://难度1
            {
                if(allTime<=3000 && allTime%100==0) buildBullet();//30s以内隔1s生成新一轮子弹
                else if(allTime>3000 && allTime<=6000 && allTime%50==0)//30s~1min
                {
                    buildBullet();//隔0.5s生成新一轮子弹
                    if(allTime%700==500) warning();//前两秒制造预警子弹
                    if(allTime%700==0) buildsBullet(tempx,tempy);//隔7s制造方形子弹阵
                }
                else if(allTime>6000 && allTime<=12000 && allTime%25==0)//1~2min
                {
                    buildBullet();//隔0.25s生成新一轮子弹
                    if(allTime%600==400) warning();//前两秒制造预警子弹
                    if(allTime%600==0) buildsBullet(tempx,tempy);//隔6s制造方形子弹阵
            }
                else if(allTime>12000 && allTime%20==0) //2min后
                {
                    buildBullet();//隔0.2s生成新一轮子弹
                    if(allTime%500==300) warning();//前两秒制造预警子弹
                    if(allTime%500==0) buildsBullet(tempx,tempy);//隔5s制造方形子弹阵
                }
            }break;
            case 2://难度2
            {
                if(allTime<=3000 && allTime%50==0)//30s以内
                {
                    buildBullet();//隔0.5s生成新一轮子弹
                    if(allTime%700==500) warning();//前两秒制造预警子弹
                    if(allTime%700==0) buildsBullet(tempx,tempy);//隔7s制造方形子弹阵
                }
                else if(allTime>3000 && allTime<=6000 && allTime%25==0)//30s~1min
                {
                    buildBullet();//隔0.25s生成新一轮子弹
                    if(allTime%600==400) warning();//前两秒制造预警子弹
                    if(allTime%600==0) buildsBullet(tempx,tempy);//隔6s制造方形子弹阵
                }
                else if(allTime>6000 && allTime%20==0)//1min后
                {
                    buildBullet();//隔0.2s生成新一轮子弹
                    if(allTime%500==300) warning();//前两秒制造预警子弹
                    if(allTime%500==0) buildsBullet(tempx,tempy);//隔5s制造方形子弹阵
                }
            }break;
            case 3://难度3
            {
                if(allTime<=3000 && allTime%40==0)//30s内
                {
                    buildBullet();//0.4s生成新一轮子弹
                    if(allTime%600==400) warning();//前两秒制造预警子弹
                    if(allTime%600==0) buildsBullet(tempx,tempy);//隔6s制造方形子弹阵
                }
                else if(allTime>3000 && allTime<=6000 && allTime%25==0)//30s~1min
                {
                    buildBullet();//隔0.25s生成新一轮子弹
                    if(allTime%500==300) warning();//前两秒制造预警子弹
                    if(allTime%500==0) buildsBullet(tempx,tempy);//隔5s制造方形子弹阵
                }
                else if(allTime>6000 && allTime%21==0)//1min后
                {
                    buildBullet();// 隔0.2s生成新一轮子弹
                    if(allTime%500==300) warning();//前两秒制造预警子弹
                    if(allTime%500==0) buildsBullet(tempx,tempy);//隔5s制造方形子弹阵
                }
            }break;
        }
        for (int i = 0; i < bullets.size(); i++) {// 循环遍历子弹集合
            Bullet b = bullets.get(i);// 获取子弹对象
            if (b.isAlive())
            {// 如果子弹在界内
                b.move();// 子弹执行移动操作
                b.hitCharacter();//判断子弹是否击中主角
                g.drawImage(b.getImage(), b.x, b.y, this);// 绘制子弹
            }
            else
            {// 如果子弹无效
                bullets.remove(i);// 在集合中刪除此子弹
                i--;// 循环变量-1，保证下次循环i的值不会变成i+1，以便有效遍历集合，且防止下标越界
            }
        }
    }

    private void paintPlayer(Graphics g) {    //绘制角色
        movePlayer();
        g.drawImage(player.getImage(left_key), player.getX(), player.getY(),this);
    }

    private void movePlayer() {
        if(right_key) player.moveRight();
        if(left_key) player.moveLeft();
        if(up_key) player.moveUp();
        if(down_key) player.moveDown();
    }

    private void checkCollision() { //碰撞检测
        if(!player.isAlive()) finish = true;
    }

    private void scoreAdd() {//分数增长
        if(allTime%setDefine.scoreDilution == 0) {//如果循环到了地方
            switch(level) {//根据难度，得分系数不一样
                case 1: score += 1;break;
                case 2: score += 2;break;
                case 3: score += 3
                ;break;
                default: score += 1;break;
            }
        }
        allTime++;
    }

    public void Finish() {
        finish = true;
    }

    public void gameOver() throws IOException {
        player.setAlive(false);
        frame.data.writeLong(stringConst.rankKey[1], score);//更新最后得分
        if(score > frame.data.searchLong(stringConst.rankKey[0])) {
            frame.data.writeLong(stringConst.rankKey[0], score);
        }//更新最高分
        showScore();
    }

    private void showScore() {
        frame.removeKeyListener(this);
        frame.setPanel(new gameOverPanel(frame, this, backFrame));// 主窗体跳转至说明面板
        System.gc();
    }

    private void gotoPausePanel() {
        frame.removeKeyListener(this);
        frame.setPanel(new pausePanel(frame, this));// 主窗体跳转至说明面板
        System.gc();
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
