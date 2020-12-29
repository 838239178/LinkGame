package gm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class IconButton extends JButton {
    private String text;


    public IconButton(){
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

    public IconButton(String text){
        this();
        this.setText(text);
    }

    @Override
    public void setText(String text){
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float strHei = g2d.getFontMetrics().getHeight();
        float strWid = g2d.getFontMetrics().stringWidth(text);
        Dimension size = getSize();
        g2d.drawString(text, size.width/2-strWid/2, size.height/2 + strHei/4);
    }

}
