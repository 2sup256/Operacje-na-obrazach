package obrazki;

import java.awt.*;

public class WindowDrawing extends Canvas {
    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        g.drawImage(Main.img1, 50, 100, null);
        g.drawImage(Main.edited, 300, 100, null);

        if (Main.Mr != 0) {
            int tmp_y = 1;
            double mn;
            tmp_y *= (200 - (200 * Main.R[0])) / Main.Mr;
            for (int i = 0; i < 256; i++) {
                int y = 200 - 200 * Main.R[i] / Main.Mr;
                g.setColor(Color.red);
                mn = (double) Main.Mr / 200;
                g.fillRect(i, 500 + y, 1, (int) (Main.R[i] / mn));
                tmp_y = y;
            }
            tmp_y *= (200 - (200 * Main.G[0])) / Main.Mr;
            for (int i = 0; i < 256; i++) {
                int y = 200 - 200 * Main.G[i] / Main.Mg;
                g.setColor(Color.green);
                mn = (double) Main.Mg / 200;
                g.fillRect(i + 300, 500 + y, 1, (int) (Main.G[i] / mn));
                tmp_y = y;
            }
            tmp_y *= (200 - (200 * Main.B[0])) / Main.Mr;
            for (int i = 0; i < 256; i++) {
                int y = 200 - 200 * Main.B[i] / Main.Mb;
                g.setColor(Color.blue);
                mn = (double) Main.Mb / 200;
                g.fillRect(i + 600, 500 + y, 1, (int) (Main.B[i] / mn));
                tmp_y = y;
            }
        }
    }
}
