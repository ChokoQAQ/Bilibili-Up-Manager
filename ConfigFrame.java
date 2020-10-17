import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ConfigFrame extends JFrame {
    Configs c;
    Configs temp;
    Outputer o = new Outputer(this);
    Up u;
    public Frame frame;
    public ConfigFrame(Configs c,Up u,Frame frame) throws IOException {
        this.c = c;
        this.temp = c;
        this.u=u;
        this.frame=frame;
        this.setSize(720,480);
        this.setTitle("设置");
        this.setDefaultCloseOperation(Frame.HIDE_ON_CLOSE);
        int frameX = (Toolkit.getDefaultToolkit().getScreenSize().width-getWidth())/2;
        int frameY = (Toolkit.getDefaultToolkit().getScreenSize().height-getHeight())/2;
        this.setLocation(frameX,frameY);
        this.setAlwaysOnTop(true);
        Font f = new Font("微软雅黑",Font.PLAIN,15);
        JTextArea a = new JTextArea();
        a.setLineWrap(true);
        a.setAutoscrolls(true);
        a.setFont(f);
        a.setBackground(new Color(59,59,59));
        a.setForeground(new Color(255,255,255));
        this.add(a);
        JButton jb = new JButton("保存");
        jb.setFont(f);
        jb.setForeground(new Color(255,255,255));
        jb.setBackground(new Color(79, 113, 192));
        this.add(jb,BorderLayout.SOUTH);
        a.append(c.uuid+"\n");
        for(String s:c.homeInfoDisplay){a.append(s+" ");}
        a.append("\n");
        for(String s:c.VideoCardInfoDisplay){a.append(s+" ");}
        a.append("\n"+c.videoViewNum);
        a.append("\n"+c.backgoundAlpha);
        a.append("\n"+c.background);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] s = a.getText().split("\n");
                String[] home = s[1].split(" ");
                String[] video = s[2].split(" ");
                ArrayList<String> homeInfoDisplay = new ArrayList<>();
                ArrayList<String> videoInfoDisplay = new ArrayList<>();
                for(String c : home){ if(!c.equals("")){homeInfoDisplay.add(c);} }
                for(String c : video){ if(!c.equals("")){videoInfoDisplay.add(c);} }
                if(!c.uuid.equals(s[0])){
                    try {
                        o.out("dif");
                        c.lastVideoData=new WebReader(s[0]).readInfo().video;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                ConfigFrame.this.c.uuid=s[0];
                ConfigFrame.this.c.homeInfoDisplay=homeInfoDisplay;
                ConfigFrame.this.c.VideoCardInfoDisplay=videoInfoDisplay;
                ConfigFrame.this.c.videoViewNum=Integer.valueOf(s[3]);
                ConfigFrame.this.c.backgoundAlpha=Integer.valueOf(s[4]);
                ConfigFrame.this.c.background=s[5];
                try {
                    ConfigFrame.this.frame.writeConfigs(ConfigFrame.this.c);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //ConfigFrame.this.frame.paint(frame.getGraphics());
                ConfigFrame.this.setVisible(false);
                WebReader wr = null;
                try {
                    wr = new WebReader(c.uuid);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    writeConfigs(ConfigFrame.this.c);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    frame.up=wr.readInfo();
                    frame.updateData=frame.getUpdateDataWithoutSave();
                    frame.updateUI(wr.readInfo());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
    public class HomeCheckButton extends JCheckBox{
        public HomeCheckButton(String text){
            this.setText(text);
            //this.setSize(30,text.length()*10);
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(HomeCheckButton.this.isSelected() && !exsits(temp.homeInfoDisplay,HomeCheckButton.this.getText())){
                        temp.homeInfoDisplay.add(text);
                        o.out("Enabled "+text+" display");
                    }else if(!HomeCheckButton.this.isSelected() && exsits(temp.homeInfoDisplay,HomeCheckButton.this.getText())){
                        temp.homeInfoDisplay.remove(text);
                        o.out("Disabled "+text+" display");
                    }
                }
            });
        }
    }
    public boolean exsits(ArrayList<String> al,String index){
        for(String s : al){
            if(s.equals(index))
                return true;
        }
        return false;
    }
    public void writeConfigs(Configs c) throws IOException {
        File f = new File("Configs.cfg");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(c);
        oos.close();
    }
}
