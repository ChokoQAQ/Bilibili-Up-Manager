import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class FramePressedListener extends MouseAdapter {
        int x0,y0,x1,y1;
        Frame f;
        Outputer o = new Outputer(this);
        public FramePressedListener(Frame f){ this.f=f; }
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            f.resizeReady=-1;
            x0=e.getXOnScreen();
            y0=e.getYOnScreen();
            int x = e.getX();
            int y = e.getY();
            o.out(String.format("Pressed x=%d  y=%d",x,y));
            if(x>=6 && y>=f.getHeight()-(720-686) && x<=35 && y<=f.getHeight()-(720-709)){f.cf.setVisible(true);}
            if(x>=53 && y>=f.getHeight()-(720-686) && x<=83 && y<=f.getHeight()-(720-709)){System.exit(0);}
            if(x>15 && y>f.getHeight()-(720-655) && x<73 && y<f.getHeight()-(720-680)){f.setAlwaysOnTop(!f.isAlwaysOnTop());
                try {
                    f.updateUI();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                o.out("Always on top opened");
            }
            int mx = e.getX();
            int my = e.getY();
            int width =f.getWidth();
            int height =f.getHeight();
            int dif = 7;
            if (mx < dif && my > dif && my < height - dif) {
                f.setCursor(Cursor.W_RESIZE_CURSOR);
                f.resizeReady=1;
            } else if (mx > width - dif && my > dif && my < height - dif) {
                f.setCursor(Cursor.E_RESIZE_CURSOR);
                f.resizeReady=2;
            } else if (my < dif && mx > dif && mx < width - dif) {
                f.setCursor(Cursor.N_RESIZE_CURSOR);
                f.resizeReady=3;
            } else if (mx < width - dif && mx > dif && my > height - dif) {
                f.setCursor(Cursor.S_RESIZE_CURSOR);
                f.resizeReady=4;
            } else if (mx < dif && my < dif) {
                f.setCursor(Cursor.NW_RESIZE_CURSOR);
                f.resizeReady=5;
            } else if (mx > width - dif && my < dif) {
                f.setCursor(Cursor.NE_RESIZE_CURSOR);
                f.resizeReady=6;
            } else if (mx < dif && my > height - dif) {
                f.setCursor(Cursor.SW_RESIZE_CURSOR);
                f.resizeReady=7;
            } else if (mx > width - dif && my > height - dif) {
                f.setCursor(Cursor.SE_RESIZE_CURSOR);
                f.resizeReady=8;
            } else {
                f.setCursor(Cursor.DEFAULT_CURSOR);
                f.resizeReady=0;
            }
        }


}
