package HOB.frame;

import HOB.Const.urls;
import HOB.Const.setDefine;
import HOB.Const.stringConst;
import HOB.global.audioPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class rankPanel extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private static final int offsetX = setDefine.width/40;//所有x坐标的偏移量
    private static final int startX = setDefine.width/4 + 5*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 + 3*offsetY;//所有y坐标的偏移起点

    private Image backgroud;// 背景图片
    private Image selectBox;//选择框图片
    private audioPlayer sound;

    private long lastScore;//最后得分
    private long highestScore;//最高分

    private final int y1 = startY,
            y2 = startY + 2*offsetY,
            y3 = startY + 4*offsetY,
            y4 = startY + 6*offsetY; //选择框可以选择的四个位置
    private int selectBoxY = y4;
    private mainFrame frame;// 主窗体
    private startPanel backFrame;//调用它的界面，用于避免内存爆掉

    public void paint(Graphics g) {
        g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString(stringConst.rankPanel[0], startX, y1);// 绘制第一行文字，标题
        g.drawString(stringConst.rankPanel[1] + highestScore, startX - 4*offsetX, y2);// 绘制第二行文字，最高分
        g.drawString(stringConst.rankPanel[2] + lastScore, startX - 4*offsetX, y3);// 绘制第三行文字，上次得分
        g.drawString(stringConst.rankPanel[3], startX, y4);// 绘制第四行文字
        g.drawImage(selectBox, startX - offsetX, selectBoxY - offsetY, this);

    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    /**
     * 构造函数
     * @param frame 主界面
     * @param back 上一个界面
     */
    public rankPanel(mainFrame frame, startPanel back) {
        this.frame = frame;
        this.backFrame = back;
        addListener();// 添加组件监听
        try {
            backgroud = ImageIO.read(new File(urls.LOGIN_BACKGROUD_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(urls.SELECT_BOX_IMAGE_URL));// 读取选择框图标
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.highestScore = frame.data.searchLong(stringConst.rankKey[0]);
        this.lastScore = frame.data.searchLong(stringConst.rankKey[1]);
        sound = new audioPlayer(frame.selection);
    }

    /**
     * 返回上一层
     */
    private void gotoBackPanel() {
        frame.setPanel(backFrame);//返回上一层
        frame.removeKeyListener(this);//删除本对象的键盘监听
        frame.addKeyListener(backFrame);//重新添加上一层的键盘监听
        backFrame.repaint();//重新绘制上一层
    }

    /**
     * 键盘事件监听
     */
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// 获取按下的按键值
        switch (code) {// 判断按键值
            case KeyEvent.VK_ENTER://如果按下回车
            case KeyEvent.VK_SPACE://或者空格
                sound.play(urls.DONE_SOUND_UTIL);
                gotoBackPanel();//返回上层
                break;
            case KeyEvent.VK_ESCAPE:
                sound.play(urls.DONE_SOUND_UTIL);
                gotoBackPanel();
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
