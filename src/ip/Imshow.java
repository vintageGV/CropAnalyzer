package ip;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by g on 12/21/16.
 */
public class Imshow extends JFrame{
    private BufferedImage disp;
    private int w;
    private int h;

    public Imshow(BufferedImage disp){
        this.disp = disp;
        this.w = disp.getWidth();
        this.h = disp.getHeight();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void paint(Graphics g){
        g.drawImage(disp,0,0,w,h,null);
    }
}
