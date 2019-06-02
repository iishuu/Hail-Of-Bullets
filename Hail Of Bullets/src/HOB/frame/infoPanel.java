package HOB.frame;

import HOB.Const.setDefine;
import HOB.Const.stringConst;
import HOB.Const.urls;
import HOB.global.audioPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class infoPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private static final int offsetX = setDefine.size;//所有x坐标的偏移量
    private static final int startX = setDefine.width/2-3*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 - 3*offsetY;//所有y坐标的偏移起点

    private Image backGround;// 背景图片
    private Image selectBox;//选择框图片
    private audioPlayer sound;

    private long lastScore;//最后得分
    private long highestScore;//最高分

    private int []offset = {0,//文字的偏移量
            -offsetX/2,
            offsetX,
            offsetX,
            0,
            1*offsetX,
            2*offsetX,
            0};
    private int []y = {startY,
            startY + 2*offsetY,
            startY + 4*offsetY,
            startY + 6*offsetY,
            startY + 8*offsetY,
            startY + 9*offsetY,
            startY + 11*offsetY,
            startY + 13*offsetY}; //文字纵坐标
    private int selectBoxY = y[7];//框框初始位置
    private mainFrame frame;// 主窗体
    private startPanel backFrame;//调用它的界面，用于避免内存爆掉

    public void paint(Graphics g) {
        g.drawImage(backGround, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        for(int i=0; i<stringConst.infoPanel.length; i++) {
            g.drawString(stringConst.infoPanel[i], startX - offset[i], y[i]);// 绘制第i行文字
        }
        g.drawImage(selectBox, startX - 2*offsetX, selectBoxY - (4*offsetY)/3, this);
    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    /**
     * 构造函数
     * @param frame 主界面
     * @param back 上一个界面
     */
    public infoPanel(mainFrame frame, startPanel back) {
        this.frame = frame;
        this.backFrame = back;
        addListener();// 添加组件监听
        try {
            backGround = ImageIO.read(new File(urls.MENU_BACKGROUND_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(urls.SELECT_BOX_IMAGE_URL));// 读取选择框图标
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                sound.play(urls.DONE_SOUND_MUSIC);
                gotoBackPanel();//返回上层
                break;
            case KeyEvent.VK_ESCAPE:
                sound.play(urls.DONE_SOUND_MUSIC);
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
