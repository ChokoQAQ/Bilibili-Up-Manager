import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.shape.Shape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Frame extends JFrame {
    public Up up;
    public Outputer o;
    public ConfigFrame cf;
    public int resizeReady;
    public ArrayList<String> playUp;
    public Configs c;
    public HashMap<String,ArrayList<String>> updateData;

    public Frame(Up up) throws IOException {
        this.up=up;
        this.c = readConfigs();
        Thread t = new Thread(new ConfigsLoggerThread(this));
        t.start();
        this.setContentPane(new mContentPane());
        this.o = new Outputer(this);
        this.setSize(1280, 720);
        this.setTitle("Bilibili UP小助手");
        int frameX = (Toolkit.getDefaultToolkit().getScreenSize().width-getWidth())/2;
        int frameY = (Toolkit.getDefaultToolkit().getScreenSize().height-getHeight())/2;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
        this.setUndecorated(true);
        this.setResizable(true);
        this.getContentPane().setBackground(new Color(53, 53, 53));
        this.setLayout(new BorderLayout());
        this.setLocation(frameX,frameY);
        FramePressedListener fpl = new FramePressedListener(this);
        this.addMouseListener(fpl);
        this.addMouseMotionListener(new FrameResizeListener(this,fpl));
        updateUI(up);
        this.updateData = getUpdateDataWithoutSave();
        this.setAlwaysOnTop(c.isAlwaysOnTop);
        this.setSize(c.frameWidth,c.frameHeight);
        if(c.frameX>Toolkit.getDefaultToolkit().getScreenSize().width || c.frameY>Toolkit.getDefaultToolkit().getScreenSize().height){
            this.setLocation(frameX,frameY);
        }else {
            this.setLocation(c.frameX, c.frameY);
        }
    }
    public void updateUI(Up up) throws IOException {
        this.up = up;
        this.getContentPane().removeAll();
        JPanel videos = new JPanel(new GridLayout(c.videoViewNum,1) );
        videos.setBackground(null);
        videos.setOpaque(false);
        for (int i = 0; i < c.videoViewNum; i++) {
            try {
                VideoCard vc = new VideoCard(up.video.get(i), i);
                videos.add(vc);
            }catch(IndexOutOfBoundsException e){
                break;
            }
        }
        UpPane upPane  = new UpPane();
        upPane.setLayout(null);
        upPane.setPreferredSize(new Dimension(90,this.getHeight()));
        this.add(videos,BorderLayout.CENTER);
        this.add(upPane,BorderLayout.WEST);
        this.cf = new ConfigFrame(c,up,this);
        this.setSize(c.frameWidth,c.frameHeight);
        this.setLocation(c.frameX,c.frameY);
        if(!isVisible()) {
            this.setVisible(true);
        }else {
            repaint();
            paintComponents(getGraphics());
        }
    }
    public void updateUI() throws IOException {
        this.getContentPane().removeAll();
        this.setSize(c.frameWidth,c.frameHeight);
        this.setLocation(c.frameX,c.frameY);
        JPanel videos = new JPanel(new GridLayout(c.videoViewNum,1) );
        videos.setBackground(null);
        videos.setOpaque(false);
        for (int i = 0; i < c.videoViewNum; i++) {
            try {
                VideoCard vc = new VideoCard(up.video.get(i), i);
                videos.add(vc);
            }catch(IndexOutOfBoundsException e){
                break;
            }
        }
        UpPane upPane  = new UpPane();
        upPane.setLayout(null);
        upPane.setPreferredSize(new Dimension(90,this.getHeight()));
        this.add(videos,BorderLayout.CENTER);
        this.add(upPane,BorderLayout.WEST);
        this.cf = new ConfigFrame(c,up,this);
        this.setSize(c.frameWidth,c.frameHeight);
        this.setLocation(c.frameX,c.frameY);
        if(!isVisible()){
            setVisible(true);
        }else{
            repaint();
            paintComponents(getGraphics());
        }
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
    public void writeConfigs(Configs c) throws IOException {
        File f = new File("Configs.cfg");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(c);
        oos.close();
    }

    public class VideoCard extends JPanel {
        public Video video;
        public int alpha;
        public int num;

        public VideoCard(Video v,int num) {
            this.video = v;
            this.num=num;
            this.setBackground(new Color(255, 255, 255, 0));
        }
        public void paintComponent(Graphics g) {
           super.paintComponent(g) ;
            double picSize = 0.13*((double)(getHeight())/120);
            int picWidth = (int) (1093 * picSize);
            int picHeight = (int) (682 * picSize);
            int picTopSpace = (getHeight()-picHeight)/2;
            int picLeftSpace = picTopSpace;
            int plusNum = 0;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(158, 158, 158));
            g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND));
            g2d.drawRoundRect(2,2,this.getWidth()-4,this.getHeight()-4,3,3);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//设置抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);
            g2d.setClip(new RoundRectangle2D.Double(picLeftSpace,picTopSpace,picWidth,picHeight,13,13));
            g2d.setPaint(new Color(0, 0, 0, 64));//阴影颜色
            g2d.setColor(new Color(255, 255, 255));
            try {
                g2d.drawImage(new ImageIcon(new URL(video.pic)).getImage(), picLeftSpace, picTopSpace, picWidth, picHeight, null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            g2d.setClip(null);
            //Paint infos
            int xSpace = 10;
            int xNow = picWidth + picLeftSpace + xSpace+(int)(50*((double)getWidth()/1280));
            int iconSize = (int)(this.getHeight()*0.4);
            if(iconSize>64){
                iconSize=64;
            }
            //o.out(iconSize+"");
            int yTopSpace = (int)((getHeight()-iconSize)/2);
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, Math.max((int) (getHeight() * 0.125), 10)));
            ArrayList<String> vcd = c.VideoCardInfoDisplay;
            int maxPlusNum = 0;
            for(int i1 = 0;i1<c.videoViewNum;i1++) {
                Video v = null;
                try {
                    v = up.video.get(i1);
                }catch(IndexOutOfBoundsException e){
                    Up u = null;
                    try {
                        u = new WebReader(c.uuid).readInfo();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Frame.this.updateUI(u);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if(v!=null) {
                    for (int i = 0; i < vcd.size(); i++) {
                        String info = v.data.get(vcd.get(i));
                        if (info.length() > maxPlusNum) {
                            maxPlusNum = info.length();
                        }
                    }
                }
            }
            plusNum=maxPlusNum*7;
            for (int i = 0; i < vcd.size(); i++) {
                String info = video.data.get(vcd.get(i));
                String image = "src/Icons/PNGs/"+vcd.get(i)+"_48px.png";
                if(iconSize>48) {
                    image = "src/Icons/PNGs/"+vcd.get(i)+"_64px.png";
                }
                //o.out(image);
                g2d.drawImage(new ImageIcon(image).getImage(), xNow, yTopSpace, iconSize, iconSize, null);
                //o.out(String.format("draw at %d %d",xNow,yTopSpace));
                g2d.drawString(info, (float) (xNow*1.00-(info.length()*g2d.getFont().getSize()*0.6-iconSize)/2), yTopSpace+iconSize+g2d.getFont().getSize());
                Font f = g2d.getFont();
                if(updateData.get(c.VideoCardInfoDisplay.get(i))!=null && updateData.get(c.VideoCardInfoDisplay.get(i)).size() >= num+1) {
                    g2d.setColor(new Color(255, 102, 134));
                    String writeUpdateData = updateData.get(c.VideoCardInfoDisplay.get(i)).get(num);
                    if(!writeUpdateData.equals("0")) {
                        g2d.setFont(new Font("微软雅黑",Font.PLAIN,Math.max(f.getSize()-10,12)));
                        g2d.fillPolygon(new int[]{xNow + iconSize , xNow + iconSize+ g2d.getFont().getSize()/2, xNow + iconSize + g2d.getFont().getSize()}, new int[]{yTopSpace, yTopSpace - g2d.getFont().getSize(), yTopSpace}, 3);
                        g2d.drawString(writeUpdateData,xNow+iconSize+g2d.getFont().getSize()+5,yTopSpace);
                    }
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.setFont(f);
                }
                xNow=xNow+iconSize+xSpace+plusNum;
            }
            //g2d.drawString(video.like, picLeftSpace + picWidth + picLeftSpace, (float) (picTopSpace + g2d.getFont().getSize() * 3));
        }

    }
    public class UpPane extends JPanel {
        public UpPane(){
            this.setBackground(null);
            this.setOpaque(false);
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g) ;
            final int headSpace = 8;
            final int headSize = getWidth()-2*headSpace;
            final int textX = 8;
            final int nameY = 90;
            int textWritingY = headSize + headSpace * 4; //文字绘制的垂直位置变量=
            try {
                o.out(up.head);
                g.setColor(Color.WHITE);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//设置抗锯齿
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
                g2d.setColor(new Color(158, 158, 158));
                g2d.drawRoundRect(4,4,getWidth()-8,getHeight()-8,13,13);
                g2d.setPaint(new Color(0, 0, 0, 64));//阴影颜色
                g2d.setColor(new Color(255, 255, 255));
                g.drawImage(new ImageIcon(new URL(up.head)).getImage(), headSpace, headSpace, headSize, headSize, null);
                g2d.setFont(new Font("微软雅黑", Font.PLAIN, 15));
                if(Frame.this.isAlwaysOnTop()){
                    g2d.setColor(new Color(255, 173, 219,150));
                    g2d.fillRoundRect((getWidth()-4*15)/2,getHeight()-32-35,4*15,20,5,5);
                    g2d.setColor(Color.WHITE );
                    c.isAlwaysOnTop=true;
                    writeConfigs(c);
                }else{
                    c.isAlwaysOnTop=false;
                    writeConfigs(c);
                }
                g2d.drawString("总在最前",(getWidth()-4*15)/2,getHeight()-32-20);
                g2d.drawImage(new ImageIcon("src/Icons/PNGs/configs.png").getImage(),5,getHeight()-32-5,32,32,null);
                g2d.drawImage(new ImageIcon("src/Icons/PNGs/close.png").getImage(),getWidth()-5-32,getHeight()-32-5,32,32,null);


                for (int i = 0; i < c.homeInfoDisplay.size(); i++) {
                    String text =c.homeInfoDisplay.get(i);
                    String info = up.data.get(text);
                    if(text.equals("archiveView")) {
                        info = reFormat(info);
                        o.out(info);
                    }
                    g2d.drawString(info, textX, textWritingY);
                    textWritingY += 20;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class mButton extends JButton{
        ImageIcon image;
        public mButton(ImageIcon image){
            this.image = image;
            this.setBackground(null);
            this.setOpaque(false);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//设置抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
            g2d.drawImage(image.getImage(),0,0,getWidth(),getHeight(),null);
        }
    }
    public String reFormat(String s){
        if(s.length()>4 && s.length()<8){
            DecimalFormat df  = new DecimalFormat("######0.000");
            return df.format(Double.valueOf(s)/10000)+"万";
        }else if(s.length()>8){
            DecimalFormat df  = new DecimalFormat("######0.000");
            return df.format(Double.valueOf(s)/100000000)+"亿";
        }
        return s;
    }
    public HashMap<String,ArrayList<String>> getUpdateData(){
        ArrayList<Video> v1 = c.lastVideoData;
        HashMap<String,ArrayList<String>> ret = new HashMap<>();
        for(int i = 0;i<c.VideoCardInfoDisplay.size();i++){
            ArrayList<String> al = new ArrayList<>();
            for(int i1 = 0;i1<Math.min(v1.size(),up.video.size());i1++) {
                if(up.video.get(i1).aid.equals(v1.get(i1).aid)) {
                    try {
                        al.add(Integer.valueOf(up.video.get(i1).data.get(c.VideoCardInfoDisplay.get(i))) - Integer.valueOf(v1.get(i1).data.get(c.VideoCardInfoDisplay.get(i))) + "");
                    } catch (IndexOutOfBoundsException e) {
                        c.lastUpdateTime = new Date().getTime();
                        c.lastVideoData = up.video;
                        return null;
                    }
                }else{
                    c.lastVideoData=up.video;
                }
            }
            ret.put(c.VideoCardInfoDisplay.get(i),al);
        }
        return ret;
    }
    public HashMap<String, ArrayList<String>> getUpdateDataWithoutSave(){
        ArrayList<Video> v1 = c.lastVideoData;
        HashMap<String,ArrayList<String>> ret = new HashMap<>();
        for(int i = 0;i<c.VideoCardInfoDisplay.size();i++){
            ArrayList<String> al = new ArrayList<>();
            for(int i1 = 0;i1<Math.min(v1.size(),up.video.size());i1++) {
                if(up.video.get(i1).aid.equals(v1.get(i1).aid)) {
                    try {
                        al.add(Integer.valueOf(up.video.get(i1 ).data.get(c.VideoCardInfoDisplay.get(i))) - Integer.valueOf(v1.get(i1).data.get(c.VideoCardInfoDisplay.get(i))) + "");
                    } catch (IndexOutOfBoundsException e) {
                        return null;
                    }
                }else{
                    c.lastVideoData=up.video;
                }
            }
            ret.put(c.VideoCardInfoDisplay.get(i),al);
        }
        return ret;
    }
    public class mContentPane extends JPanel{
        public BufferedImage bufferedImage;
        public mContentPane(){
            this.setOpaque(true);
            if(c.background!=null) {
                File f = new File(c.background);
                if (f.exists()) {
                    int alpha = c.backgoundAlpha;
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(new FileInputStream(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
                        for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                            int rgb = bufferedImage.getRGB(x, y);
                            rgb = (alpha << 24) | (rgb & 0x00ffffff);
                            bufferedImage.setRGB(x, y, rgb);
                        }
                    }
                    this.bufferedImage = bufferedImage;
                }
            }
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g) ;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//设置抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(c.background!="null" && c.background != null && bufferedImage!=null) {
                g2d.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            }
        }
    }
}

