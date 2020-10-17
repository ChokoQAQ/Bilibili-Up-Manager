import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Configs implements Serializable {
    public String uuid;
    public ArrayList<String> homeInfoDisplay;
    public ArrayList<String> VideoCardInfoDisplay;
    public int frameX;
    public int frameY;
    public int frameWidth;
    public int frameHeight;
    public int videoViewNum;
    ArrayList<Video> lastVideoData;
    long lastUpdateTime = new Date().getTime();
    public int backgoundAlpha;
    public boolean isAlwaysOnTop;
    public String background;
    public Configs(){
        uuid = "316693770";
        homeInfoDisplay=new ArrayList<>();
        homeInfoDisplay.add("name");
        homeInfoDisplay.add("level");
        homeInfoDisplay.add("archiveView");
        homeInfoDisplay.add("articleView");
        homeInfoDisplay.add("likes");

        VideoCardInfoDisplay=new ArrayList<>();
        VideoCardInfoDisplay.add("play");
        VideoCardInfoDisplay.add("like");
        VideoCardInfoDisplay.add("reply");
        VideoCardInfoDisplay.add("danmaku");

        frameX=(Toolkit.getDefaultToolkit().getScreenSize().width-1280)/2;
        frameY=(Toolkit.getDefaultToolkit().getScreenSize().height-720)/2;

        frameWidth=1280;
        frameHeight=720;

        videoViewNum=6;

        lastVideoData=new ArrayList<>();

        backgoundAlpha=200;

        isAlwaysOnTop=false;

        background=null;
    }
}
