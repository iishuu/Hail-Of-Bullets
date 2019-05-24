package HOB.global;

import java.io.*;
import HOB.Const.setDefine;

public class ioCrypto {//读写加密模块
    private final int sMX = setDefine.dataSize;//超过这个大小就会重建文件
    private final int MX = setDefine.dataSize-1;//不急嘛，小一点，小一点。

    private File file;
    private InputStream in;
    private FileWriter out;

    private byte[] str;//读入并解密的数据就存在这里
    private int len = 0;//记录下来数据的长度
    private byte[] tempStr = new byte[MX];//临时处理用

    /**
     * 将byte数组处理为String，遇0结束
     * @param fromBytes
     * @return
     */
    private String byteToString(byte[] fromBytes) {
        String string = new String("");
        int i=0;
        while(fromBytes[i] != 0) {
            string += (char)fromBytes[i];
            i++;
        }
        return string;
    }

    /**
     * 将String复制到byte数组，遇到0结束
     * @param toBytes 目的
     * @param fromString 源
     */
    private void stringToBytes(byte[] toBytes, String fromString) {
        byte[] temp = fromString.getBytes();
        for(int i=0; i<fromString.length(); i++) {
            toBytes[i] = temp[i];
        }
    }

    /**
     * 构造函数
     * @param path 文件路径
     */
    public ioCrypto(String path) throws IOException {
        file = new File(path);
        if(file.length()>sMX) {//如果文件超长，说明不是我的孩子，删掉。
            file.delete();
        }
        if(!file.exists()) {//如果文件不存在，那就新建一个
            file.createNewFile();
        }
        str = new byte[MX];
        read();
    }

    /**
     * 加解密，互逆
     * @param B 输入的byte
     * @param i byte的位置
     * @return B^i
     */
    private byte encrypt(byte B, int i) {
        B ^= (byte)i;
        return B;
    }

    private byte decrypt(byte B, int i) {
        B ^= (byte)i;
        return B;
    }

    /**
     * 读文档到str
     */
    private void read() throws IOException{
        in = new FileInputStream(file);
        in.read(str);
        len = 0;
        while(decrypt(str[len], len)!=0) len++;//统计文件长度
        //如果是刚刚新建的文件，那么第0字节异或0之后还是零，所以可以。
        for(int i=0; i<len; i++) {
            str[i] = decrypt(str[i],i);
        }
        in.close();
    }

    /**
     * 写入到文件
     */
    private void write() throws IOException{
        out = new FileWriter(file.getName(), false);//覆盖写入
        for(int i=0; i<len; i++) {
            tempStr[i] = encrypt(str[i],i);
        }
        tempStr[len++] = encrypt((byte)'\'', len);//末尾补单引号
        tempStr[len] = encrypt((byte)0, len);//末尾置停止标志
        out.write(new String(tempStr));//写入
        out.close();
    }

    /**
     * 在文档中搜索关键词为key的long
     * @param key 关键词，
     *            关键词必须以单引号'开头，为了和字符串区别开
     *            包含在了stringConst的开头
     *            数据均以单引号'结束，我规定的，没为什么
     *            ioCrypto会自己在数据后面加引号的
     *            所以单引号是保留字，不许用作数据的内容
     * @return 搜索到的数据（数字）
     */
    public long searchLong(String key) {
        stringToBytes(tempStr, key);
        int tot = 0;
        long ans = 0;
        for(int i=0; i<len; i++) {
            if(str[i] == tempStr[tot]) {
                tot++;
            }
            else tot=0;
            if(tot==key.length()) {//匹配到了符合的标示
                i++;
                while(str[i]>='0' && str[i]<='9') {//读数据到ans
                    ans += str[i]-'0';
                    i++;
                }
                break;
            }
        }
        return ans;
    }
    //原理和上一个差不多
    public String searchString(String key) {
        stringToBytes(tempStr, key);
        int tot = 0, j=0;
        for(int i=0; i<len; i++) {
            if(str[i] == tempStr[tot]) {
                tot++;
            }
            else tot = 0;
            if(tot == key.length()) {
                i++;
                while(str[i+j]!='\0' && str[i+j]!='\'') {
                    tempStr[j] = str[i+j];
                    j++;
                }
                break;
            }
        }
        tempStr[j] = '\0';
        return byteToString(tempStr);
    }

    /**
     * 写入数据，末尾的单引号在write方法里补上，而不是这里
     * @param key 数据名称
     * @param num 数据
     */
    public void writeInt(String key, int num) throws IOException{
        writeString(key, num + "");
    }
    public void writeLong(String key, long num) throws IOException{
        writeString(key, num + "");
    }
    public void writeString(String key, String string) throws IOException{
        stringToBytes(tempStr, key);
        int i, tot = 0, tmpLen = 0;
        for(i=0; i<len; i++) {
            if(str[i] == tempStr[tot]) {
                tot++;
            }
            else tot=0;
            if(tot==key.length()) {
                break;
            }
        }
        tmpLen = i+1;
        if(i>=len) {//i匹配到末尾就说明没有找到，那就新建一个标签
            for (i = 0; i < key.length(); i++) {
                str[len + i] = tempStr[i];
            }
            len += i;
            tmpLen = len;
        }
        stringToBytes(tempStr, string);
        for (i = 0; i < string.length(); i++) {
            str[tmpLen+ i] = tempStr[i];
        }
        len += i;
        write();
        write();
    }
}
