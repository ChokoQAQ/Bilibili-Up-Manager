import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WebReader {
    public String id;
    public Outputer o;
    public WebReader(String id) throws IOException {
        this.id=id;
        this.o=new Outputer(this);
    }
    public Up readInfo() throws IOException {
        //Read stats
        String addr = "https://api.bilibili.com/x/relation/stat?vmid=%s&jsonp=jsonp";
        addr=addr.replace("%s",id);
        URL url = new URL(addr); //316693770
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
        StringBuffer json = new StringBuffer();
        String get = "";
        while((get=in.readLine())!=null){
            json.append(get);
        }
        in.close();
        JSONObject jo = JSONObject.fromObject(json.toString());
        o.out(jo.toString());
        JSONObject data = jo.getJSONObject("data");
        o.out(data.toString());
        String uuid = data.getString("mid");
        String following = data.getString("following");
        String follower = data.getString("follower");
        o.out("Readed uuid="+uuid+" following="+following+" "+"follower="+follower);

        //Read info
        addr = "https://api.bilibili.com/x/space/acc/info?mid=%s&jsonp=jsonp";
        addr=addr.replace("%s",id);
        url = new URL(addr); //316693770
        uc = url.openConnection();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
        json = new StringBuffer();
        get = "";
        while((get=in.readLine())!=null){
            json.append(get);
        }
        in.close();
        jo = JSONObject.fromObject(json.toString());
        o.out(jo.toString());
        data = jo.getJSONObject("data");
        o.out(data.toString());
        String name = data.getString("name");
        String face = data.getString("face");
        String level = data.getString("level");
        String coins = data.getString("coins");
        JSONObject vipJson = data.getJSONObject("vip");
        String vipStatus = vipJson.getString("status");
        String vipType = vipJson.getString("type");

        o.out(String.format("Readed name=%s face=%s level=%s coins=%s vipStatus=%s vipType=%s",name,face,level,coins,vipStatus,vipType));

        //Read upstats
        //Read info
        addr = "https://api.bilibili.com/x/space/upstat?mid=%s&jsonp=jsonp";
        addr=addr.replace("%s",id);
        url = new URL(addr); //316693770
        uc = url.openConnection();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
        json = new StringBuffer();
        get = "";
        while((get=in.readLine())!=null){
            json.append(get);
        }
        in.close();
        jo = JSONObject.fromObject(json.toString());
        data = jo.getJSONObject("data");
        JSONObject archive = data.getJSONObject("archive");
        JSONObject article = data.getJSONObject("article");
        String archiveView = archive.getString("view");
        String articleView = article.getString("view");
        String likes = data.getString("likes");

        //Read videos
        addr = "https://api.bilibili.com/x/space/arc/search?mid=%s&pn=1&ps=25&jsonp=jsonp";
        addr=addr.replace("%s",id);
        url = new URL(addr); //316693770
        uc = url.openConnection();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
        json = new StringBuffer();
        get = "";
        while((get=in.readLine())!=null){
            json.append(get);
        }
        in.close();
        jo=JSONObject.fromObject(json.toString()); //data list vlist
        JSONObject vlist = jo.getJSONObject("data").getJSONObject("list");//.getJSONObject("vlist");
        JSONArray ja = vlist.getJSONArray("vlist");
        ArrayList<Video> videos =  new ArrayList<>();
        for(int i = 0;i<readConfigs().videoViewNum;i++){
            JSONObject videoJson = null;
            try {
                videoJson = ja.getJSONObject(i);
            }catch(IndexOutOfBoundsException e){
                break;
            }
            //o.out(videoJson.toString());
            String aid = videoJson.getString("aid");

            addr = "https://api.bilibili.com/x/web-interface/view?aid=%s";
            addr=addr.replace("%s",aid);
            url = new URL(addr); //316693770
            uc = url.openConnection();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
            json = new StringBuffer();
            get = "";
            while((get=in.readLine())!=null){
                json.append(get);
            }
            in.close();

            JSONObject vj = JSONObject.fromObject(json.toString());
            JSONObject vdata = vj.getJSONObject("data");
            String pic = vdata.getString("pic");
            String title = vdata.getString("title");
            String desc = vdata.getString("desc");
            JSONObject stat = vdata.getJSONObject("stat");
            String play = stat.getString("view");
            String reply = stat.getString("reply");
            String favorite = stat.getString("favorite");
            String coin = stat.getString("coin");
            String share = stat.getString("share");
            String like = stat.getString("like");
            String danmaku = stat.getString("danmaku");
            o.out(String.format("Readed Video: title=%s aid=%s play=%s like=%s coin=%s share=%s favorite=%s dest=%s danmaku=%s",title,aid,play,like,coin,share,favorite,desc,danmaku));
            videos.add(new Video(title,aid,play,like,coin,share,favorite,desc,danmaku,pic,reply));
        }
        return new Up(uuid,name,following,follower,coins,vipStatus,vipType,videos,face,level,archiveView,articleView,likes);

    }
    public Configs readConfigs() {
        try {
            File f = new File("Configs.cfg");
            if (!f.exists()) {
                Configs c = new Configs();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(c);
                oos.close();
                return c;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Configs c = (Configs) ois.readObject();
            ois.close();
            return c;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
