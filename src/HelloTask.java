import java.util.TimerTask;

public class HelloTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Hello");
    }
}
