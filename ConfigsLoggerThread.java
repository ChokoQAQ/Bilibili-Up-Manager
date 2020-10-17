import java.io.*;
import java.util.Date;

public class ConfigsLoggerThread implements Runnable {
    Frame f;
    public ConfigsLoggerThread(Frame f){
        this.f=f;
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Configs c = f.c;
            c.frameWidth=f.getWidth();
            c.frameHeight=f.getHeight();
            c.frameX=f.getX();
            c.frameY=f.getY();
            f.updateData=f.getUpdateDataWithoutSave();
            try {
                f.updateUI(new WebReader(c.uuid).readInfo());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(new Date().getTime()-c.lastUpdateTime>1000*60*24){
                try {
                    f.updateUI(new WebReader(c.uuid).readInfo());
                    f.updateData=f.getUpdateData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                writeConfigs(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeConfigs(Configs c) throws IOException {
        File f = new File("Configs.cfg");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(c);
        oos.close();
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
