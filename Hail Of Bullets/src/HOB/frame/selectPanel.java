package HOB.frame;
import HOB.Const.*;
import HOB.Selections;
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

public class selectPanel extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private static final int offsetX = setDefine.width/40;//所有x坐标的偏移量
    private static final int startX = setDefine.width/4 + 5*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 + 3*offsetY;//所有y坐标的偏移起点

    private Image backgroud;// 背景图片
    private Image selectBox;//选择框图片
    private final int y1 = startY,
            y2 = startY + 2*offsetY,
            y3 = startY + 4*offsetY; //选择框可以选择的两个位置
    private int selectBoxY = y1;
    private mainFrame frame;// 主窗体
    private startPanel backFrame;//调用它的界面，用于避免内存爆掉

    public void paint(Graphics g) {
        g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString(stringConst.selectPanel[0] + frame.selection.getLevel(), startX, y1);// 绘制第一行文字
        g.drawString(frame.selection.Music(), startX, y2);// 绘制第二行文字
        g.drawString(stringConst.selectPanel[3], startX, y3);// 绘制第三行文字
        g.drawImage(selectBox, startX - offsetX, selectBoxY - offsetY, this);

    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    public selectPanel(mainFrame frame, startPanel back) {
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
            case KeyEvent.VK_UP:// 如果按下的是“↑”
                switch (selectBoxY) {
                    case y1 : selectBoxY = y3;break;
                    case y2 : selectBoxY = y1;break;
                    case y3 : selectBoxY = y2;break;
                    default : selectBoxY = y1;break;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_DOWN:// 如果按下的是“↓”
                switch (selectBoxY) {
                    case y1 : selectBoxY = y2;break;
                    case y2 : selectBoxY = y3;break;
                    case y3 : selectBoxY = y1;break;
                    default : selectBoxY = y1;break;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_ENTER://如果按下回车
                switch (selectBoxY) {
                    case y1 : frame.selection.addLevel();break;//难度增加（循环）
                    case y2 : frame.selection.switchMusic();break;//切换音乐开关
                    case y3 : gotoBackPanel();break;//返回上层
                    default : selectBoxY = y1;
                }
                repaint();
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
