package HOB.global;
import HOB.Const.stringConst;
import HOB.frame.mainFrame;

import java.io.IOException;

public class Selections {//全局设置类
    private int Level = 0;//难度
    public boolean musicOpen = true;//音乐开关
    private mainFrame frame;

    public Selections(mainFrame frame) throws IOException {
        this.frame = frame;
        readOptions();
    }

    public int getLevel(int n) {
        return Level + n;//人类从1开始数数//n是偏移量
    }
    public void addLevel() {
        Level = (Level + 1) % 3;
    }

    public int getMusicOn() {
        if(musicOpen)return 1;
        else return 0;
    }
    public void setLevel(int level) {
        this.Level = level;
    }
    public void setLevel(long level) {
        this.Level = (int)level;
    }
    public void setPlayer(audioPlayer player) {
        this.frame.musicPlayer = player;
    }
    public void switchMusic() {
        if(musicOpen) {
            musicOpen = false;
            frame.musicPlayer.stop();
        }
        else {
            musicOpen = true;
            frame.musicPlayer.play();
        }
    }
    private void readOptions() throws IOException {
        if (frame.data.searchLong(stringConst.optionKey[0]) == 0) {
            musicOpen = false;
        } else musicOpen = true;
        setLevel(frame.data.searchLong(stringConst.optionKey[1]));
    }
    public String Music() {
        if(musicOpen)
            return stringConst.optionPanel[1];
        else
            return stringConst.optionPanel[2];
    }
}
