package HOB.frame;

import HOB.Const.*;
import HOB.global.*;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class mainFrame extends JFrame{
    public Selections selection;//全局设置，只需要把this发过去即可访问
    public audioPlayer musicPlayer;//这个是游戏开始前的背景音乐。不是全局的背景音乐，因为游戏界面另有背景音乐
    public ioCrypto data;//这个是可以加密解密读写存档的模块
    public mainFrame() throws IOException {    //构造方法
        setTitle(stringConst.gameTitle);   //设置标题
        data = new ioCrypto(urls.dataUrl);//初始化读写模块
        selection = new Selections(this);//实例化全局设置
        musicPlayer = new audioPlayer(urls.MAIN_MUSIC_MUSIC, selection);//初始化背景音乐
        musicPlayer.setFilePath(urls.MAIN_MUSIC_MUSIC);//设定路径
        musicPlayer.play();//开始播放，一遍就够了
        setSize(setDefine.width, setDefine.height);  //设置宽高
        setResizable(false);    //不可调整大小
        Toolkit tool = Toolkit.getDefaultToolkit(); //创建系统默认工具包
        Dimension d = tool.getScreenSize(); // 获取屏幕尺寸，赋给一个二维坐标对象
        // 让主窗体在屏幕中间显示
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);//初始化选择框的位置
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);// 关闭窗体时无操作
        addListener();// 添加事件监听
        setPanel(new startPanel(this));// 添加登录面板
        setVisible(true);//可视
    }
    /**
     * 添加组件监听
     */
    private void addListener() {
        addWindowListener(new WindowAdapter() {// 添加窗体事件监听
            public void windowClosing(WindowEvent e) {// 窗体关闭时
                int closeCode = JOptionPane.showConfirmDialog(mainFrame.this, "确定退出游戏？", "提示！",
                        JOptionPane.YES_NO_OPTION);// 弹出选择对话框，并记录用户选择
                if (closeCode == JOptionPane.YES_OPTION) {// 如果用户选择确定
                    System.exit(0);// 关闭程序
                }
            }
        });
    }

    /**
     * 更换主容器中的面板
     *
     * @param panel
     *              更换的面板
     */
    public void setPanel(JPanel panel) {
        Container c = getContentPane();// 获取主容器对象
        c.removeAll();// 删除容器中所有组件
        c.add(panel);// 容器添加面板
        c.validate();// 容器重新验证所有组件
    }
}
