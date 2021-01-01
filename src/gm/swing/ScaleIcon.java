package gm.swing;

import java.awt.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 用于实现图片随容器缩放
 *
 * @author zhongweijian
 * from: https://blog.csdn.net/zhongweijian/article/details/7668926
 * Copyright Agreement: CC 4.0 BY-SA
 */
public class ScaleIcon implements Icon {
    private final Icon icon;

    public ScaleIcon(String file) {
        this.icon = new ImageIcon(file);
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        float wid = c.getWidth();
        float hei = c.getHeight();
        int iconWid = icon.getIconWidth();
        int iconHei = icon.getIconHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.scale(wid / iconWid, hei / iconHei);
        icon.paintIcon(c, g2d, 0, 0);
    }
}
