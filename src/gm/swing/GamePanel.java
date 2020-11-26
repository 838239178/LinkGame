package gm.swing;

//import gm.game.GameMap;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel{
	//private GameMap map;
	private Block[] blocks;
	private EventListenerList allBlocksClearListeners;
	private EventListenerList onceBlocksClearListeners;
	
	public GamePanel(int level) {
		//this.map = new GameMap();
		this.setLayout(new GridLayout(10,10,10,10));
		blocks = new Block[level*level];
		for (Block b : blocks) {
			b = BlockFactory.INSTANCE.getBlock(0);
			this.add(b);
		}
	}

	public void addClearBlocksListener(ActionListener l){
		allBlocksClearListeners.add(ActionListener.class, l);
	}
}
