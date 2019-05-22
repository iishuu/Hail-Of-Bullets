package HOB.frame;

import HOB.Const.imageConst;
import HOB.Const.setDefine;
import HOB.Const.stringConst;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class manualPanel extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private static final int offsetX = setDefine.width/40;//所有x坐标的偏移量
    private static final int startX = setDefine.width/4 + 5*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 + 3*offsetY;//所有y坐标的偏移起点

    private Image backgroud;// 背景图片
    private Image selectBox;//选择框图片
    private final int y[] = {startY,
            startY + 2*offsetY,
            startY + 4*offsetY}; //选择框可以选择的三个位置
    private int selectBoxY = y[stringConst.manualPanel.length-1];
    private mainFrame frame;// 主窗体
    private startPanel backFrame;//调用它的界面，用于避免内存爆掉

    public void paint(Graphics g) {
        g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        for(int i=1; i<stringConst.manualPanel.length; i++) {
            g.drawString(stringConst.manualPanel[i], startX, y[i]);// 绘制第i行文字
        }
        g.drawImage(selectBox, startX - offsetX, selectBoxY - offsetY, this);

    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    public manualPanel(mainFrame frame, startPanel back) {
        this.frame = frame;
        this.backFrame = back;
        addListener();// 添加组件监听
        try {
            backgroud = ImageIO.read(new File(imageConst.LOGIN_BACKGROUD_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(imageConst.SELECT_BOX_IMAGE_URL));// 读取选择框图标
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void gotoBackPanel() {
        frame.setPanel(backFrame);//返回上一层
        frame.removeKeyListener(this);//删除本对象的键盘监听
        frame.addKeyListener(backFrame);//重新添加上一层的键盘监听
        backFrame.repaint();//重新绘制上一层
    }
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// 获取按下的按键值
        switch (code) {// 判断按键值
            case KeyEvent.VK_ENTER://如果按下回车
                gotoBackPanel();
                break;
            case KeyEvent.VK_ESCAPE:
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
