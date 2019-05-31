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

public class pausePanel extends Panel {//todo:恢复计时器
    private static final long serialVersionUID = 1L;
    private static final int offsetX = setDefine.width/40;//所有x坐标的偏移量
    private static final int startX = setDefine.width/4 + 5*offsetX;//所有x坐标的偏移起点

    private static final int offsetY = setDefine.height/20;//所有y坐标的偏移量
    private static final int startY = setDefine.height/4 + 3*offsetY;//所有y坐标的偏移起点

    private Image backGround;// 背景图片
    private Image selectBox;//选择框图片
    private audioPlayer sound;

    private final int y0 = startY - 3*offsetY,
            y1 = startY,
            y2 = startY + 2*offsetY,
            y3 = startY + 4*offsetY; //选择框可以选择的两个位置
    private int selectBoxY = y3;
    private mainFrame frame;// 主窗体
    private gamePanel backPanel;//调用它的界面，用于避免内存爆掉

    /**
     * 绘图方法
     * @param g 是本对象的御用画师
     */
    public void paint(Graphics g) {
        backPanel.paint(g);
        g.drawImage(backGround, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
        Font font = new Font(stringConst.Font, Font.BOLD, setDefine.size);// 创建体字
        g.setFont(font);// 使用字体
        g.setColor(Color.BLACK);// 使用黑色
        g.drawString(stringConst.pausePanel[0], startX, y0);// 绘制第一行文字
        g.drawString(frame.selection.Music(), startX, y1);// 绘制第二行文字
        g.drawString(stringConst.pausePanel[2], startX, y3);// 绘制第四行文字
        font = new Font(stringConst.Font, Font.BOLD, setDefine.size - 7);// 创建体字
        g.setFont(font);// 使用字体
        g.drawString(stringConst.pausePanel[1], startX, y2);// 绘制第三行文字
        g.drawImage(selectBox, startX - offsetX, selectBoxY - offsetY, this);

    }

    private void addListener() {
        frame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
    }

    /**
     * 构造函数
     * @param frame 主界面
     * @param back 上一个界面，用于返回
     */
    public pausePanel(mainFrame frame, gamePanel back) {
        this.frame = frame;
        this.backPanel = back;
        addListener();// 添加组件监听
        try {
            backGround = ImageIO.read(new File(urls.PAUSE_BACKGROUND_IMAGE_URL));// 读取背景图片
            selectBox = ImageIO.read(new File(urls.SELECT_BOX_IMAGE_URL));// 读取选择框图标
        } catch (IOException e) {
            e.printStackTrace();
        }
        sound = new audioPlayer(frame.selection);
    }
    private void saveOptions() throws IOException {
        frame.data.writeInt(stringConst.optionKey[0], frame.selection.getMusic());
        frame.data.writeInt(stringConst.optionKey[1], frame.selection.getLevel(0));
    }

    /**
     * 返回上一层
     */
    private void gotoBackPanel() throws IOException {
        frame.setPanel(backPanel);//返回上一层
        frame.removeKeyListener(this);//删除本对象的键盘监听
        frame.addKeyListener(backPanel);//重新添加上一层的键盘监听
        backPanel.repaint();//重新绘制上一层
        saveOptions();
        backPanel.pauseEvent();
    }

    private void backToStart() throws IOException {
        frame.removeKeyListener(this);//删除本对象的键盘监听
        saveOptions();
        backPanel.Finish();
    }

    /**
     * 键盘事件监听
     */
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// 获取按下的按键值
        switch (code) {// 判断按键值
            case KeyEvent.VK_W://如果按下w
            case KeyEvent.VK_UP:// 如果按下的是“↑”
                sound.play(urls.CLICK_SOUND_UTIL);
                switch (selectBoxY) {
                    case y1 : selectBoxY = y3;break;
                    case y2 : selectBoxY = y1;break;
                    case y3 : selectBoxY = y2;break;
                    default : selectBoxY = y3;break;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_DOWN:// 如果按下的是“↓”
            case KeyEvent.VK_S://或者S
                sound.play(urls.CLICK_SOUND_UTIL);
                switch (selectBoxY) {
                    case y1 : selectBoxY = y2;break;
                    case y2 : selectBoxY = y3;break;
                    case y3 : selectBoxY = y1;break;
                    default : selectBoxY = y3;break;
                }
                repaint();// 按键按下之后，需要重新绘图
                break;
            case KeyEvent.VK_ENTER://如果按下回车
            case KeyEvent.VK_SPACE://或者空格
                sound.play(urls.DONE_SOUND_UTIL);
                switch (selectBoxY) {
                    case y1 : frame.selection.switchMusic();break;//切换音乐开关
                    case y2 :
                        try {
                            backToStart();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    case y3 :
                        try {
                            gotoBackPanel();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;//返回上层
                    default : selectBoxY = y3;
                }
                repaint();
                break;
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_P:
                sound.play(urls.DONE_SOUND_UTIL);
                try {
                    gotoBackPanel();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
