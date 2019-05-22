package HOB;

import HOB.Const.stringConst;
public class Selections {//全局设置类
    private int Level = 0;//难度
    private boolean musicOpen = true;//音乐开关

    public int getLevel() {
        return Level + 1;//人类从1开始数数
    }

    public void addLevel() {
        Level = (Level + 1) % 3;
    }

    public void switchMusic() {
        if(musicOpen) musicOpen = false;
        else musicOpen = true;
    }

    public String Music() {
        if(musicOpen)
            return stringConst.selectPanel[1];
        else
            return stringConst.selectPanel[2];
    }
}
