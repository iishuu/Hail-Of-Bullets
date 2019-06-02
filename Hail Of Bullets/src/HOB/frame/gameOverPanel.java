package HOB.frame;

import HOB.Const.urls;
import HOB.Const.setDefine;
import HOB.Const.stringConst;
import HOB.global.audioPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class gameOverPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private static final int offsetX = setDefine.width/40;//所有x坐标的偏移量
    private static final int startX = setDefine.width/4 + 5*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 + 3*offsetY;//所有y坐标的偏移起点

    private Image backGround;// 背景图片
    private Image selectBox;//选择框图片
    private audioPlayer sound;//音效
    private audioPlayer music;//音乐

    private long lastScore;//最后得分
    private long highestScore;//最高分

    private final int y1 = startY - 5*offsetY,
            y2 = startY + 4*offsetY,
            y3 = startY + 6*offsetY,
            y4 = startY + 8*offsetY; //选择框可以选择的四个位置
    private int selectBoxY = y4;
    private mainFrame frame;// 主窗体
    private Panel backFrame;//调用它的界面，用于调用绘图
    private Panel startPanel;//主界面

    public void paint(Graphics g) {
        backFrame.repaint();
        g.drawImage(backGround, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString(stringConst.gameOverPanel[0], startX, y1);// 绘制第一行文字，标题
        g.drawString(stringConst.gameOverPanel[1] + highestScore, startX - 3*offsetX, y2);// 绘制第二行文字，最高分
        g.drawString(stringConst.gameOverPanel[2] + lastScore, startX - 3*offsetX, y3);// 绘制第三行文字，上次得分
        g.drawString(stringConst.gameOverPanel[3], startX, y4);// 绘制第四行文字
        g.drawImage(selectBox, startX - 3*offsetX, selectBoxY - (4*offsetY)/3, this);
        music = frame.musicPlayer;
        music.play(urls.GAME_OVER_MUSIC);
    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    /**
     * 构造函数
     * @param frame 主界面
     * @param back 上一个界面
     */
    public gameOverPanel(mainFrame frame, Panel back, Panel start) {
        this.frame = frame;
        this.backFrame = back;
        this.startPanel = start;
        addListener();// 添加组件监听
        frame.musicPlayer.stop();
        try {
            backGround = ImageIO.read(new File(urls.PAUSE_BACKGROUND_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(urls.SELECT_BOX_IMAGE_URL));// 读取选择框图标
        } catch (IOException e) {
            e.printStackTrace();
        }
        sound = new audioPlayer(frame.selection);
        this.highestScore = frame.data.searchLong(stringConst.rankKey[0]);
        this.lastScore = frame.data.searchLong(stringConst.rankKey[1]);
        if(highestScore == 201914153) {//just for fun
            stringConst.gameOverPanel[0] = "flag{H41L_0F_bUiieT5}";
        }

    }

    /**
     * 返回上一层
     */
    private void gotoStartPanel() {
        frame.setPanel(startPanel);//返回上一层
        frame.removeKeyListener(this);//删除本对象的键盘监听
        frame.addKeyListener(startPanel);//重新添加上一层的键盘监听
        startPanel.repaint();//重新绘制上一层
        frame.musicPlayer.setFilePath(urls.MAIN_MUSIC_MUSIC);//设定路径
        frame.musicPlayer.play();//开始播放，一遍就够了
    }

    /**
     * 键盘事件监听
     */
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// 获取按下的按键值
        switch (code) {// 判断按键值
            case KeyEvent.VK_ENTER://如果按下回车
            case KeyEvent.VK_SPACE://或者空格
                sound.play(urls.DONE_SOUND_MUSIC);
                gotoStartPanel();//返回上层
                break;
        }
    }
    /**
     * 按键抬起时
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // 不实现此方法，但不可删除
    }

    /**
     * 键入某按键事件
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // 不实现此方法，但不可删除
    }

}
