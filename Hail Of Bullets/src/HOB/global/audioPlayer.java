package HOB.global;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class audioPlayer {
    // to store current position
    Selections selections;
    Clip clip;
    // current status of clip

    AudioInputStream audioInputStream;
    static String filePath;
    // constructor to initialize streams and clip

    public audioPlayer(Selections selections){
        this.selections = selections;
    }
    public audioPlayer(String Path, Selections selections){
        setFilePath(Path);
        this.selections = selections;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
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
    public void play() {
        if(!selections.musicOpen) return;//如果音乐关闭就不播放了。
        readFile();
        clip.start();
    }
    public void stop() {
        clip.stop();;
    }
    public void play(String url) {
        if(!selections.musicOpen) return;//如果音乐关闭就不播放了。
        setFilePath(url);
        readFile();
        clip.start();
    }
}