import java.awt.*;

public class VideoCardColorChangeThread implements Runnable{
    Frame.VideoCard vc;
    int changeType = 0;
    public VideoCardColorChangeThread(Frame.VideoCard vc,int changeType){
        this.vc=vc;
        this.changeType=changeType;
    }
    public synchronized void run() { //changeType: 0:appear 1:disapper
        if (changeType == 0) {
            while (vc.alpha<20) {
                vc.alpha++;
                    vc.paint(vc.getGraphics());
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            while(vc.alpha>0) {
                System.out.println(vc.alpha);
                vc.alpha--;
                    vc.paint(vc.getGraphics());
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
