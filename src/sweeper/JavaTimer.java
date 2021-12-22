package sweeper;


public class JavaTimer {

    private long startTime = 0;
    private long stopTime = 0;
    private long countMis;
    private boolean running = false;


    public void start() {
        if (!this.running){
            this.startTime = System.currentTimeMillis();
            this.running = true;
        }
    }


    public void reset() {
        this.running = false;
        start();
    }


    String formatTime(long time, boolean millis){
        if (millis){
            if (time < 10)
                return "00" + time;
            if (time < 100)
                return "0" + time;

        }
        if (time < 10)
            return "0" + time;
        return "" + time;
    }


    public String getElapsedTime() {
        if (running) {
            countMis = (System.currentTimeMillis() - startTime);
        } else {
            countMis = (stopTime - startTime);
        }
        return millisToFormat(countMis);
    }


    public long formatToMillis(String time){
        String[] substr;
        substr = time.split(":");
        int Hour = Integer.parseInt(substr[0]);
        int Min = Integer.parseInt(substr[1]);
        int Sec = Integer.parseInt(substr[2]);
        long Mil = Integer.parseInt(substr[3]);
        Mil += (long)Sec * 1000 + (long)Min * 60000 + (long)Hour * 60000 * 60;
        return Mil;
    }


    public String millisToFormat(long Mil){
        int Sec = (int)Mil / 1000;
        Mil = Mil % 1000;
        int Min = Sec / 60;
        Sec = Sec % 60;
        int Hour = Min / 60;
        Min = Min % 60;
        return formatTime(Hour, false) + ":" + formatTime(Min, false) + ":"
                + formatTime(Sec, false) + ":" + formatTime(Mil, true);
    }


    public void printCurrentTimer(){
        if (this.running)
            System.out.println(getElapsedTime());
    }


    public void endIfLoseOrWin(GameState state) {
        if (state == GameState.BOMBED || state == GameState.WINNER) {
            this.running = false;
            this.stopTime = System.currentTimeMillis();
            System.out.println("Game time: " + getElapsedTime());
            System.out.println("Game time in mils: " + formatToMillis(getElapsedTime()));
        }
    }
}