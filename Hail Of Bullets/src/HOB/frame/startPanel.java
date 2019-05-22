package HOB.frame;
import HOB.Const.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.JPanel;


public class startPanel extends JPanel implements KeyListener {
    private static final int offsetX = setDefine.width/40;//所有x坐标的偏移量
    private static final int startX = setDefine.width/4 + 5*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 + 3*offsetY;//所有y坐标的偏移起点

    private static final long serialVersionUID = 1L;
    private Image backgroud;// 背景图片
    private Image selectBox;//选择框图片
    private final int []y = {startY,
            startY + 2*offsetY,
            startY + 4*offsetY,
            startY + 6*offsetY}; //便于遍历
    private final int y1 = startY;
    private final int y2 = startY + 2*offsetY;
    private final int y3 = startY + 4*offsetY;
    private final int y4 = startY + 6*offsetY;
    private int selectBoxY = y[0];
    private mainFrame frame;
    Clip clickSound;//指针切换声
    Clip doneSound;//确定声
    //todo: 播放音频
    /**
     * 登陆面板构造方法
     *
     * @param frame
     *              主窗体
     */
    public startPanel(mainFrame frame) {
        this.frame=frame;
        addListener();// 添加组件监听
        try {
            backgroud = ImageIO.read(new File(imageConst.LOGIN_BACKGROUD_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(imageConst.SELECT_BOX_IMAGE_URL));// 读取选择框图标
      } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 重写绘图方法
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        for(int i=0; i<stringConst.startPanel.length; i++) {
            g.drawString(stringConst.startPanel[i], startX, y[i]);// 绘制第i行文字
        }
        g.drawImage(selectBox, startX - offsetX, selectBoxY - offsetY, this);

    }
    /**
     * 跳转游戏面板
     */
    private void gotoGamePanel() {
        removeListener();// 主窗体删除键盘监听
        frame.setPanel(new gamePanel(frame));// 主窗体跳转至选项面板
    }

    /**
     * 跳转选项面板
     */
    private void gotoSelectionPanel() {
        removeListener();// 主窗体删除键盘监听
        frame.setPanel(new selectPanel(frame, this));// 主窗体跳转至选项面板
    }
    private void gotoManualPanel() {
        removeListener();// 主窗体删除键盘监听
        frame.setPanel(new manualPanel(frame, this));// 主窗体跳转至说明面板
    }
    /**
     * 添加组件监听
     */
    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }
    private void removeListener() {
        frame.removeKeyListener(this);//主窗体删除键盘监听
    }
    /*private void play(Clip clip) {//播放音效
       clip.start();
    }*/

    /**
     * 当按键按下时
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //todo:鼠标选择
        int code = e.getKeyCode();// 获取按下的按键值
        switch (code) {// 判断按键值
            case KeyEvent.VK_UP:// 如果按下的是“↑”
                switch (selectBoxY) {
                    case y1 :
                        selectBoxY = y4;
                        break;
                    case y2:
                        selectBoxY = y1;
                        break;
                    case y3:
                        selectBoxY = y2;
                        break;
                    case y4:
                        selectBoxY = y3;
                        break;
                    default:
                        selectBoxY = y1;
                        break;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_DOWN:// 如果按下的是“↓”
                switch (selectBoxY) {
                    case y1:
                        selectBoxY = y2;
                        break;
                    case y2:
                        selectBoxY = y3;
                        break;
                    case y3:
                        selectBoxY = y4;
                        break;
                    case y4:
                        selectBoxY = y1;
                        break;
                    default:
                        selectBoxY = y1;
                        break;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_ENTER:// 如果按下的是“Enter”
                switch (selectBoxY) {
                    case y1:
                        gotoGamePanel();
                        break;
                    case y2:
                        gotoManualPanel();
                        break;
                    case y3:
                        gotoSelectionPanel();
                        break;
                    case y4:
                        System.exit(0);
                        break;
                    default:
                        selectBoxY = y2;
                        break;
                }
                repaint();// 按键按下之后，需要重新绘图
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
