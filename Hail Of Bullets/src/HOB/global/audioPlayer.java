package HOB.global;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class audioPlayer {//音乐播放装置
    Selections selections;//选项
    Clip clip;//核心

    AudioInputStream audioInputStream;//读入流
    static String filePath;//文件路径

    /**
     * 初始化
     * @param selections 主界面的全局设置
     */
    public audioPlayer(Selections selections){
        this.selections = selections;
    }
    public audioPlayer(String Path, Selections selections){
        setFilePath(Path);
        this.selections = selections;
    }

    /**
     * 设置文件路径
     * @param filePath 相对绝对路径都行
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 读入音频
     */
    public void readFile() {
        try {
            audioInputStream =
                    AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());// create AudioInputStream object
            clip = AudioSystem.getClip();// create clip reference
            clip.open(audioInputStream);// open audioInputStream to the clip
        }catch (Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    /**
     * 播放一遍音频
     */
    public void play() {
        if(selections.musicOff) return;//如果音乐关闭就不播放了。
        readFile();
        clip.start();
    }
    public void play(String url) {
        if(selections.musicOff) return;//如果音乐关闭就不播放了。
        setFilePath(url);
        readFile();
        clip.start();
    }

    /**
     * 停止
     */
    public void stop() {
        clip.stop();;
    }
    /**
     * 循环
     */
    public void loop(int n) {
        clip.loop(n);
    }
}