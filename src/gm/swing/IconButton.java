package gm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class IconButton extends JButton {
    private String text;

    public IconButton() {
        super();
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setVerticalTextPosition(CENTER);
    }

    public IconButton(Icon defaultIcon, Icon pressedIcon, Icon rolloverIcon) {
        this();
        this.setIcon(defaultIcon);
        this.setRolloverIcon(rolloverIcon);
        this.setPressedIcon(pressedIcon);
    }

    public IconButton(Icon defaultIcon, Icon pressedIcon, Icon rolloverIcon, String text) {
        this(defaultIcon, pressedIcon, rolloverIcon);
        this.setText(text);
    }

    public IconButton(String text) {
        this();
        this.setText(text);
    }

    /**
     * 由于JButton本身关于Text的绘制与Icon不能很好的兼容，这个方法阻断了对父类text的赋值, 但保留ActionCommand默认为text的设定。
     * 使用子类的text，在paint中绘制。
     *
     * @param text 要显示的文字
     */
    @Override
    public void setText(String text) {
        super.setText("");
        this.text = text;
        this.setActionCommand(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = getFont();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        //抗锯齿模式
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float strHei = g2d.getFontMetrics().getHeight();
        float strWid = g2d.getFontMetrics().stringWidth(text);
        Dimension size = getSize();
        g2d.drawString(text, size.width / 2f - strWid / 2, size.height / 2f + strHei / 4);
    }

}
