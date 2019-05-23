package HOB.global;

import java.io.*;

public class ioCrypto {
    private final int sMX = 0xffff;
    private final int MX = 0xfffe;
    private File file;
    private InputStream in;
    private FileWriter out;
    private byte[] str;
    private int len = 0, oldLen = 0;
    private byte[] tempStr = new byte[MX];
    private String byteToString(byte[] bytes) {
        String string = new String("");
        int i=0;
        while(bytes[i] != 0) {
            string += (char)bytes[i];
            i++;
        }
        return string;
    }
    private void copy(byte[] bytes, String string) {
        byte[] temp = string.getBytes();
        for(int i=0; i<string.length(); i++) {
            bytes[i] = temp[i];
        }
    }
    public ioCrypto(String path) throws IOException {
        file = new File(path);
        if(file.length()>sMX) {
            file.delete();
        }
        if(!file.exists()) {
            file.createNewFile();
        }
        str = new byte[MX];
        read();
    }
    private byte encrypt(byte B, int i) {
        B ^= (byte)i;
        return B;
    }
    private byte decrypt(byte B, int i) {
        B ^= (byte)i;
        return B;
    }

    private void read() throws IOException{
        in = new FileInputStream(file);
        in.read(str);
        len = 0;
        while(decrypt(str[len], len)!=0) len++;
        for(int i=0; i<len; i++) {
            str[i] = decrypt(str[i],i);
        }
        in.close();
    }
    private void write() throws IOException{
        out = new FileWriter(file.getName(), false);
        for(int i=0; i<len; i++) {
            tempStr[i] = encrypt(str[i],i);
        }
        tempStr[len++] = encrypt((byte)'\'', len);
        tempStr[len] = encrypt((byte)0, len);
        out.write(new String(tempStr));
        out.close();
    }
    public long searchLong(String key) {
        copy(tempStr, key);
        int tot = 0;
        long ans = 0;
        for(int i=0; i<len; i++) {
            if(str[i] == tempStr[tot]) {
                tot++;
            }
            else tot=0;
            if(tot==key.length()) {
                i++;
                while(str[i]>='0' && str[i]<='9') {
                    ans += str[i]-'0';
                    i++;
                }
                break;
            }
        }
        return ans;
    }
    public String searchString(String key) {
        copy(tempStr, key);
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
    public void writeInt(String key, int num) throws IOException{
        writeString(key, num + "");
    }
    public void writeLong(String key, long num) throws IOException{
        writeString(key, num + "");
    }
    public void writeString(String key, String string) throws IOException{
        copy(tempStr, key);
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
        if(i>=len) {
            for (i = 0; i < key.length(); i++) {
                str[len + i] = tempStr[i];
            }
            len += i;
            tmpLen = len;
        }
        copy(tempStr, string);
        for (i = 0; i < string.length(); i++) {
            str[tmpLen+ i] = tempStr[i];
        }
        len += i;
        write();

    }
}
