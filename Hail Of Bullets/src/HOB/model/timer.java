package HOB.model;

public class timer {
    private long startTime = 0;
    private static long nowTime = 0;
    private static long usedTime = 0;


    public static long getUsedTime() {
        return usedTime;
    }

    public void timerStart() { //开始计时
        startTime = System.currentTimeMillis();
    }

    public void timerPause() {  //暂停计时
        nowTime = System.currentTimeMillis();
        usedTime = usedTime + (nowTime - startTime);
    }

    public void timerResume() {  //继续计时
        startTime = System.currentTimeMillis();
    }

    public void timerStop() {  //停止计时
        nowTime = System.currentTimeMillis();
        usedTime = usedTime + (nowTime - startTime);
    }
}
