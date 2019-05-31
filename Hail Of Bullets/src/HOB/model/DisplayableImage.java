package HOB.model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class DisplayableImage
{

    public int x;// 图像横坐标
    public int y;//图像纵坐标
    int width;//图像的宽
    int height;//图像的高
    BufferedImage image;//图像对象

    public DisplayableImage(int x, int y, int width, int height)
    {
        this.x = x;// 横坐标
        this.y = y;// 纵坐标
        this.width = width;// 宽
        this.height = height;// 高
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);// 实例化图片
    }

    /**
     * 构造方法
     * @param x  横坐标
     * @param y  纵坐标
     * @param url  图片路径
     */
    public DisplayableImage(int x, int y, String url)
    {
        this.x = x;// 横坐标
        this.y = y;// 纵坐标
        try
        {
            image = ImageIO.read(new File(url));// 获取此路径的图片对象
            this.width = image.getWidth();// 宽为图片宽
            this.height = image.getHeight();// 高为图片高
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public DisplayableImage()
    {}
    /**
     * 获取图片
     * @return 所显示的图片
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * 设置图片
     * @param image   所显示的图片
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * 设置图片
     */
    public void setImage(String url) {
        try {
            this.image = ImageIO.read(new File(url));// 读取指定位置的图片
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取边界对象
     */
    public Rectangle getRect() {
        // 创建一个坐标在(x,y)位置，宽高为(width, height)的矩形边界对象并返回
        return new Rectangle(x, y, width, height);
    }

    /**
     * 获取图像的宽
     */
    public int getWidth() {
        return width;
    }

    /**
     * 设置宽
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 获取高
     */
    public int getHeight() {
        return height;
    }

    /**
     * 设置高
     */
    public void setHeight(int height) {
        this.height = height;
    }

}

