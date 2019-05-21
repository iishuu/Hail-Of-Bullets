package HOB.frame;
import HOB.util.imageUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class loginPanel extends JPanel implements KeyListener {
    private static final int offsetX = 10;//所有x坐标的偏移量
    private static final int startX = 300;//所有x坐标的偏移起点

    private static final int offsetY = 30;//所有y坐标的偏移量
    private static final int startY = 420;//所有y坐标的偏移起点

    private static final long serialVersionUID = 1L;
    private Image backgroud;// 背景图片
    private Image selectBox;//选择框图片
    private int y1 = startY - offsetY, y2 = startY + offsetY; //选择框可以选择的两个位置
    private int selectBoxY = y1;
    private mainFrame frame;

    /**
     * 登陆面板构造方法
     *
     * @param frame
     *              主窗体
     */
    public loginPanel(mainFrame frame) {
        this.frame=frame;
        addListener();// 添加组件监听
        try {
            backgroud = ImageIO.read(new File(imageUtil.LOGIN_BACKGROUD_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(imageUtil.SELECT_BOX_IMAGE_URL));// 读取选择框图标
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
        Font font = new Font("幼圆", Font.BOLD, 30);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString("开始游戏", startX, y1 + 3*offsetY);// 绘制第一行文字
        g.drawString("设置", startX, y2 + 3*offsetY);// 绘制第二行文字

        g.drawImage(selectBox, startX - offsetX, selectBoxY, this);

    }
    /**
     * 跳转游戏面板
     */
    private void gotoGamePanel() {
        frame.removeKeyListener(this);// 主窗体删除键盘监听
        frame.setPanel(new gamePanel(frame));// 主窗体跳转至游戏面板
    }

    /**
     * 跳转选项面板
     */
    private void gotoSelectionPanel() {
        frame.removeKeyListener(this);// 主窗体删除键盘监听
        frame.setPanel(new selectPanel(frame));// 主窗体跳转至选项面板
    }

    /**
     * 添加组件监听
     */
    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    /**
     * 当按键按下时
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// 获取按下的按键值
        switch (code) {// 判断按键值
            case KeyEvent.VK_UP:// 如果按下的是“↑”
                if(selectBoxY == y1) {
                    selectBoxY = y2;
                }
                else if(selectBoxY == y2) {
                    selectBoxY = y1;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_DOWN:// 如果按下的是“↓”
                if (selectBoxY == y1) {
                    selectBoxY = y2;
                }
                else if(selectBoxY ==y2){
                    selectBoxY = y1;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_ENTER:// 如果按下的是“Enter”
                if (selectBoxY == y1) {// 如果坦克图标在第一个位置
                    gotoGamePanel();// 跳转游戏面板
                }
                if(selectBoxY == y2){
                    gotoSelectionPanel();// 跳转设置面板
                }
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
