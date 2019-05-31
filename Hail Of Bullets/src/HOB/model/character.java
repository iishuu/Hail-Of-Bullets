package HOB.model;

import java.io.File;
import java.io.IOException;

import HOB.Const.urls;
import HOB.Const.setDefine;

import javax.imageio.ImageIO;

public class character extends DisplayableImage {
    //角色移动速度
    private int speed = setDefine.characterSpeed;

    //是否活着
    private boolean live = true;

    //绘制角色左上角坐标
    private int characterX, characterY;

    private int frameWidth, frameHeight;

    public character(int characterX, int characterY) {
        this.characterX = characterX;
        this.characterY = characterY;
        frameWidth = setDefine.width;
        frameHeight = setDefine.height;
        try {
            this.image = ImageIO.read(new File(urls.CHARACTER_FUN_IMAGE_URL));// 读取背景图片
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX(){
        return characterX;
    }

    public int getY(){
        return characterY;
    }

    public void moveLeft() {
        if(characterX > 0) characterX -= speed;
    }

    public void moveRight() {
        if(characterX < frameWidth - setDefine.edgeWidth)characterX += speed;
    }

    public void moveDown() {
        if(characterY < frameHeight - setDefine.edgeHeight) characterY += speed;
    }

    public void moveUp() {
        if(characterY > 0)characterY -= speed;
    }

    public boolean isAlive() {
        return live;
    }

    public void setAlive(boolean live) {
        this.live = live;
    }

}
