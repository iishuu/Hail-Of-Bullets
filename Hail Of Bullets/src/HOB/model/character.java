package HOB.model;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import HOB.Const.urls;
import HOB.Const.setDefine;

import javax.imageio.ImageIO;

public class character{
    //角色移动速度
    private int speed = setDefine.characterSpeed;

    //是否活着
    private boolean live = true;

    //绘制角色左上角坐标
    private int characterX, characterY;

    private int characterWidth, characterHeight;

    private int frameWidth, frameHeight;

    private String imageUrl_l;
    private String imageUrl_r;

    private Image image_left;
    private Image image_right;

    public character(int characterX, int characterY, boolean haveFun) {
        this.characterX = characterX;
        this.characterY = characterY;
        frameWidth = setDefine.width;
        frameHeight = setDefine.height;

        if(haveFun) {
            imageUrl_l = urls.CHARACTER_FUN_LEFT_IMAGE_URL;
            imageUrl_r = urls.CHARACTER_FUN_RIGHT_IMAGE_URL;
            characterWidth = setDefine.characterWidth_HJ;
            characterHeight = setDefine.characterHeight_HJ;
        }
        else {
            imageUrl_l = urls.CHARACTER_LEFT_IMAGE_URL;
            imageUrl_r = urls.CHARACTER_RIGHT_IMAGE_URL;
            characterWidth = setDefine.characterWidth;
            characterHeight = setDefine.characterHeight;
        }
        try {
            image_left = ImageIO.read(new File(imageUrl_l));// 读取背景图片
            image_right = ImageIO.read(new File(imageUrl_r));// 读取背景图片
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image getImage(boolean toLeft) {
        if(toLeft) return image_left;
        else return image_right;
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

    public int getCharacterWidth() {
        return this.characterWidth;
    }

    public int getCharacterHeight() {
        return this.characterHeight;
    }
}
