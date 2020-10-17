import java.io.Serializable;
import java.util.HashMap;

public class Video implements Serializable {
    public String name;
    public String aid;
    public String play;
    public String like;
    public String coins;
    public String share;
    public String favorite;
    public String desc;
    public String danmaku;
    public String pic;
    public String reply;
    public HashMap<String,String> data = new HashMap<>();
    public Video(String name,String aid,String play,String like,String coins,String share,String favorite,String desc,String danmaku,String pic,String reply){
        this.name=name;
        this.aid=aid;
        this.play=play;
        this.like=like;
        this.coins=coins;
        this.share=share;
        this.favorite=favorite;
        this.desc=desc;
        this.danmaku=danmaku;
        this.pic=pic;
        this.reply=reply;
        data.put("name",this.name);
        data.put("aid",this.aid);
        data.put("play",this.play);
        data.put("like",this.like);
        data.put("coins",this.coins);
        data.put("share",this.share);
        data.put("favorite",this.favorite);
        data.put("desc",this.desc);
        data.put("danmaku",this.danmaku);
        data.put("pic",this.pic);
        data.put("reply",this.reply);

    }
}
