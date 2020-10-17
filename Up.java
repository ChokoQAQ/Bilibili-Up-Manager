import java.util.ArrayList;
import java.util.HashMap;

public class Up {
    public String uuid;
    public String following;
    public String coins;
    public String follower;
    public String name;
    public String vipStatus;
    public String vipType;
    public ArrayList<Video> video;
    public String head;
    public String level;
    public String archiveView;
    public String articleView;
    public String likes;
    public HashMap<String,String> data = new HashMap<>();
    public Up(String uuid, String name, String following, String follower, String coins, String vipStatus, String vipType, ArrayList<Video> video,String head,
              String level,String archiveView,String articleView, String likes){
        this.uuid=uuid;
        this.name=name;
        this.following=following;
        this.follower=follower;
        this.coins=coins;
        this.vipStatus=vipStatus;
        this.vipType=vipType;
        this.video=video;
        this.head=head;
        this.level="Lv."+level;
        this.archiveView=archiveView;
        this.articleView=articleView;
        this.likes=likes;
        this.data.put("uuid",this.uuid);
        this.data.put("name",this.name);
        this.data.put("following",this.following);
        this.data.put("follower",this.follower);
        this.data.put("coins",this.coins);
        this.data.put("vipStatus",this.vipStatus);
        this.data.put("vipType",this.vipType);
        this.data.put("head",this.head);
        this.data.put("level",this.level);
        this.data.put("archiveView",this.archiveView);
        this.data.put("articleView",this.articleView);
        this.data.put("likes",likes);
    }
}
