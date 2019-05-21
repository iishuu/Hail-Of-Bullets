package HOB.frame;
import HOB.util.setDefine;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

public class gamePanel extends JPanel implements KeyListener{
    /**
     * 未完成
     */

    Graphics g;
    private mainFrame frame;

    public gamePanel(mainFrame frame) {
        this.frame = frame;
        frame.setSize(setDefine.width, setDefine.height);
        //this.setSize(775, 600);
        setBackground(Color.BLACK);// 面板使用黑色背景

    }
    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }
    /**
     * 键入某按键事件
     */
    public void keyTyped(KeyEvent e) {
        // 不实现此方法，但不可删除
    }

}
