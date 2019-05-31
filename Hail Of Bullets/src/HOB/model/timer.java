package HOB.model;

public class timer {

    private long startTime = 0;
    private static long nowTime = 0;
    private static long usedTime = 0;
    private long[] pausedTime = {0, 0, 0}; //暂停状态开始的时间，暂停状态结束的时间，暂停所用的时间

    /**
     * 获取所用时间：getUsedTime(),返回Str类型数据： XX s
     * 去掉了timerStop()，因为没啥用
     */

    public String getUsedTime() {
        nowTime = System.currentTimeMillis();
        usedTime = (nowTime - startTime - pausedTime[2]);
        return (usedTime / 1000) + " s";
    }

    public void timerStart() { //开始计时
        startTime = System.currentTimeMillis();
    }

    public void timerPause() {  //暂停计时
        pausedTime[0] = System.currentTimeMillis();
    }

    public void timerResume() {  //继续计时
        nowTime = System.currentTimeMillis();
        pausedTime[1] = nowTime;
        pausedTime[2] += pausedTime[1] - pausedTime[0];
    }
}
