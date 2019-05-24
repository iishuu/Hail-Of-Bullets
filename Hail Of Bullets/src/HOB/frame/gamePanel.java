package HOB.frame;
import HOB.Const.setDefine;
import HOB.global.Selections;
import HOB.model.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class gamePanel extends JPanel implements KeyListener{
    /**
     * 未完成
     */
    character character;
    Graphics g;
    private mainFrame frame;
    int level;
    Selections selections;

    public gamePanel(mainFrame frame) {
        this.frame = frame;
        this.selections = frame.selection;
        this.level = selections.getLevel(1);
        frame.setSize(setDefine.width, setDefine.height);
        setBackground(Color.BLACK);// 面板使用黑色背景

    }
    public character getCharacter() {
        return this.character;
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
