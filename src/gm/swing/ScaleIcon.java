package gm.swing;

import org.intellij.lang.annotations.MagicConstant;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.NoSuchElementException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 用于实现图片随容器缩放
 *
 * @author zhongweijian
 * @modify 施嘉宏
 * from: https://blog.csdn.net/zhongweijian/article/details/7668926
 * Copyright: CC 4.0 BY-SA
 */
public class ScaleIcon implements Icon {
    //scaling mode
    public static final int WIDTH_FIXED = 0;
    public static final int HEIGHT_FIXED = 1;
    public static final int SCALE_FULL = 3;

    //alignment
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_LEFT = 3;

    private final Icon icon;
    private int MODE;
    private int alignment;

    public ScaleIcon(String file) {
        this.icon = new ImageIcon(file);
        this.MODE = SCALE_FULL;
        alignment = ALIGN_LEFT;
    }

    public ScaleIcon(String file, @MagicConstant(intValues = {WIDTH_FIXED, HEIGHT_FIXED, SCALE_FULL}) int mode) {
        this(file);
        this.MODE = mode;
    }

    public ScaleIcon(String file, @MagicConstant(intValues = {WIDTH_FIXED, HEIGHT_FIXED, SCALE_FULL})int mode,
                     @MagicConstant(intValues = {ALIGN_CENTER, ALIGN_RIGHT, ALIGN_LEFT})int alignment) {
        this(file, mode);
        this.setAlignment(alignment);
    }

    public void setAlignment(@MagicConstant(intValues = {ALIGN_CENTER, ALIGN_RIGHT, ALIGN_LEFT}) int alignment){
        this.alignment = alignment;
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
        float scaleWid = wid/iconWid;
        float scaleHei = hei/iconHei;
        switch (MODE) {
            case SCALE_FULL -> {
                g2d.scale(scaleWid, scaleHei);
                iconWid *= scaleWid;
                iconHei *= scaleHei;
            }
            case WIDTH_FIXED -> {
                g2d.scale(scaleWid, scaleWid);
                iconWid *= scaleWid;
                iconHei *= scaleWid;
            }
            case HEIGHT_FIXED -> {
                g2d.scale(scaleHei, scaleHei);
                iconWid *= scaleHei;
                iconHei *= scaleHei;
            }
            default -> throw new NoSuchElementException("no such scaling mode");
        }
        Point2D.Float pos = new Point2D.Float(0, 0);
        pos.y = hei / 2 - iconHei / 2f;
        switch (alignment) {
            case ALIGN_CENTER -> pos.x = wid / 2 - iconWid / 2f;
            case ALIGN_RIGHT -> pos.x = wid - iconWid;
            case ALIGN_LEFT -> pos.x = 0;
            default -> throw new NoSuchElementException("no such alignment");
        }
        icon.paintIcon(c, g2d, (int)pos.x, (int)pos.y);
    }
}
