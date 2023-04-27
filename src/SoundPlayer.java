import javax.sound.sampled.Clip;

public class SoundPlayer implements Runnable {
    private Clip clip;

    public SoundPlayer(Clip clip) {
        this.clip = clip;
    }

    public void run() {
        clip.setFramePosition(0);
        clip.start();
    }
}