public class Outputer {
    Object myClass;
    public Outputer(Object o){
        this.myClass=o;
    }
    public void out(String message){
        System.out.format("[%s]%s\n",myClass.getClass().getName(),message);
    }
}
