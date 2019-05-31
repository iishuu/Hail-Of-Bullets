package HOB.model;

import java.awt.*;

public interface characterInterface {
    //todo：extends DisplayableImages
    /**
     * gamePanel 负责监听键盘事件，命令character移动
     */
    void moveLeft();
    void moveUp();
    void moveDown();
    void moveRight();

    void stop();

    int getCharacterX();

    int getCharacterY();

    boolean isAlive();

    void setCharacterX(int x);

    void setCharacterY(int x);
    void setAlive();

    /**
     * 获取角色及血条的图像
     * 在displayableimage里有类似的方法
     */
    Graphics getGraphic();
    Graphics getBloodBar();

    /**
     * @return 角色及血条图像的宽和高
     * 在displayableimage里有类似的方法
     */
    int graphicHigh();
    int graphicWidth();
    int bloodBarHigh();

    int bloodBarWidth();

}
