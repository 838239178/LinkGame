package gm.swing;

import gm.game.GameMap;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel{
	private GameMap map;
	private Block[] units;
	private EventListenerList onClearBlocksListeners;
	
	public GamePanel(LayoutManager layout) {
		super(layout);
	}

	public void addClearBlocksListener(ActionListener l){
		onClearBlocksListeners.add(ActionListener.class, l);
	}
}
