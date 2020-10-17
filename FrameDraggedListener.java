import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class FrameDraggedListener extends MouseAdapter {
    int x0,y0,x1,y1;
    Frame f;
    FramePressedListener fpl;
    public FrameDraggedListener(Frame f,FramePressedListener fpl){
        this.x0=fpl.x0;
        this.x1=fpl.x1;
        this.y0=fpl.y0;
        this.y1=fpl.y1;
        this.f=f;
        this.fpl=fpl;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        x1=e.getXOnScreen();
        y1=e.getYOnScreen();
        Point p = f.getLocation();
        double px = p.getX();
        double py = p.getY();
        //o.out(String.format("y0:%d py:%d", y0,(int)py));
        if(x1!=x0 || y1!=y0){
            f.setLocation((int)(px + (x1 - fpl.x0)),(int)(py+(y1-fpl.y0)));
            fpl.x0=x1;
            fpl.y0=y1;
        }
    }
}
