import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Configs c = readConfigs();
        WebReader wr = null;
        if(c.uuid!="") {
            wr = new WebReader(c.uuid);
        }else{
            wr = new WebReader("316693770");
        }
        Up up = wr.readInfo();
        Frame f = new Frame(up);
    }
    public static Configs readConfigs() {
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
            Configs c;
            try {
                c = (Configs) ois.readObject();
            }catch(InvalidClassException e){
                f.delete();
                Configs c1 = new Configs();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(c1);
                oos.close();
                return c1;
            }
            ois.close();
            return c;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
