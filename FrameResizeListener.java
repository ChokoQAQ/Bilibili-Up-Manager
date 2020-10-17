import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class FrameResizeListener extends MouseAdapter {
    int x0,y0,x1,y1;
    Frame f;
    FramePressedListener fpl;
    public FrameResizeListener(Frame f, FramePressedListener fpl){
        this.x0=fpl.x0;
        this.x1=fpl.x1;
        this.y0=fpl.y0;
        this.y1=fpl.y1;
        this.f=f;
        this.fpl=fpl;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        x1 = e.getXOnScreen();
        y1 = e.getYOnScreen();
        Point p = f.getLocation();
        double px = p.getX();
        double py = p.getY();
        //o.out(String.format("y0:%d py:%d", y0,(int)py));
        if (f.resizeReady != 0) {
            if (x1 != x0 || y1 != y0) {
                if (f.resizeReady == 1) {
                    f.setSize(f.getWidth() - (x1 - fpl.x0), f.getHeight());
                    f.setLocation((int) (px + (x1 - fpl.x0)), f.getY());
                } else if (f.resizeReady == 2) {
                    f.setSize(f.getWidth() + (x1 - fpl.x0), f.getHeight());
                } else if (f.resizeReady == 3) {
                    f.setSize(f.getWidth(), f.getHeight() - (y1 - fpl.y0));
                    f.setLocation(f.getX(), (int) (py + (y1 - fpl.y0)));
                }else if (f.resizeReady == 4) {
                    f.setSize(f.getWidth(), f.getHeight() + (y1 - fpl.y0));
                }else if (f.resizeReady == 5) {
                    f.setSize(f.getWidth()-(x1 - fpl.x0), f.getHeight() - (y1 - fpl.y0));
                    f.setLocation((int) (px + (x1 - fpl.x0)), (int) (py + (y1 - fpl.y0)));
                } else if (f.resizeReady == 6) {
                    f.setSize(f.getWidth() + (x1 - fpl.x0), f.getHeight() - (y1 - fpl.y0));
                    f.setLocation(f.getX(), (int) (py + (y1 - fpl.y0)));
                } else if (f.resizeReady == 7) {
                    f.setSize(f.getWidth() - (x1 - fpl.x0), f.getHeight() + (y1 - fpl.y0));
                    f.setLocation((int) (px + (x1 - fpl.x0)), f.getY());
                } else if (f.resizeReady == 8) {
                    f.setSize(f.getWidth() + (x1 - fpl.x0), f.getHeight() + (y1 - fpl.y0));
                    f.setLocation(f.getX(), f.getY());
                }
                f.c.frameWidth=f.getWidth();
                f.c.frameHeight=f.getHeight();
                try {
                    f.writeConfigs(f.c);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            } else {
                if (x1 != x0 || y1 != y0) {
                    f.setLocation((int) (px + (x1 - fpl.x0)), (int) (py + (y1 - fpl.y0)));
                    f.c.frameX=f.getX();
                    f.c.frameY=f.getY();
                    try {
                        f.writeConfigs(f.c);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        fpl.x0 = x1;
        fpl.y0 = y1;
        }
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        int fx = f.getX();
        int fy = f.getY();
        int mx = e.getX();
        int my = e.getY();
        int width =f.getWidth();
        int height =f.getHeight();
        int dif = 7;
        if (mx < dif && my > dif && my < height - dif) {
            f.setCursor(Cursor.W_RESIZE_CURSOR);
        } else if (mx > width - dif && my > dif && my < height - dif) {
            f.setCursor(Cursor.E_RESIZE_CURSOR);
        } else if (my < dif && mx > dif && mx < width - dif) {
            f.setCursor(Cursor.N_RESIZE_CURSOR);
        } else if (mx < width - dif && mx > dif && my > height - dif) {
            f.setCursor(Cursor.S_RESIZE_CURSOR);
        } else if (mx < dif && my < dif) {
            f.setCursor(Cursor.NW_RESIZE_CURSOR);
        } else if (mx > width - dif && my < dif) {
            f.setCursor(Cursor.NE_RESIZE_CURSOR);
        } else if (mx < dif && my > height - dif) {
            f.setCursor(Cursor.SW_RESIZE_CURSOR);
        } else if (mx > width - dif && my > height - dif) {
            f.setCursor(Cursor.SE_RESIZE_CURSOR);
        } else {
            f.setCursor(Cursor.DEFAULT_CURSOR);
        }
    }
}
