package HOB.global;
import HOB.Const.stringConst;
import HOB.frame.mainFrame;

import java.io.IOException;

public class Selections {//全局设置类
    private int Level = 0;//难度
    public boolean musicOff;//音乐是不是关闭的
    private mainFrame frame;

    /**
     * 构造方法
     * @param frame 主界面
     */
    public Selections(mainFrame frame) throws IOException {
        this.frame = frame;
        readOptions();
    }

    /**
     * 返回level+offset
     * @param offset 偏移，因为显示时需要从1开始计数
     * @return
     */
    public int getLevel(int offset) {
        return Level + offset;//人类从1开始数数//n是偏移量
    }
    //level++
    public void addLevel() {
        Level = (Level + 1) % 3;
    }
    //level--
    public void subLevel() {
        Level = (Level + 1) % 3;
    }
    //判断音乐开关，用于写入存档时
    public int getMusicOn() {
        if(musicOff)return 0;
        else return 1;
    }

    /**
     * 设置难度
     * @param level 难度
     */
    public void setLevel(int level) {
        this.Level = level;
    }
    public void setLevel(long level) {
        this.Level = (int)level;
    }

    /**
     * 设置这个设定所持有的音乐播放器，用于在切换音乐开关后实时调整开关
     * @param player
     */
    public void setPlayer(audioPlayer player) {
        this.frame.musicPlayer = player;
    }

    /**
     * 调整音乐开关
     */
    public void switchMusic() {
        if(musicOff) {
            musicOff = false;
            frame.musicPlayer.readFile();//播放之前需要读一下
            frame.musicPlayer.play();
        }
        else {
            musicOff = true;
            frame.musicPlayer.stop();
        }
    }

    /**
     * 从存档读入设定
     */
    private void readOptions() throws IOException {
        if (frame.data.searchLong(stringConst.optionKey[0]) == 0) {
            musicOff = true;
        } else musicOff = false;
        setLevel(frame.data.searchLong(stringConst.optionKey[1]));
    }

    /**
     * 用于在设定界面显示当前音乐开关状态
     * @return 文字形式的音乐开关状态
     */
    public String Music() {
        if(musicOff)
            return stringConst.optionPanel[1];
        else
            return stringConst.optionPanel[2];
    }
}
