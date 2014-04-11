package com.gearlles.fss.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gearlles.fss.core.FSSSearch;
import com.gearlles.fss.core.Fish;

public class WindowController {
	
	Logger logger = LoggerFactory.getLogger(WindowController.class);
	
	private MainWindow mainWindow;
	private FSSSearch search;
	private boolean isRunning = true;
	
	public int frames;
	public int fps;
	private int maxFps = 300;
	public long lastTime;
	
	public WindowController() {
		mainWindow = new MainWindow();
		search = new FSSSearch();
	}
	
	public void render() {
		long fpsWait = (long) (1.0 / maxFps * 1000);
		
		while(isRunning) {
			long renderStart = System.nanoTime();
//			updateLogic();
			updateUi(search.getSchool());
			
    		// FPS limiting here
    		long renderTime = (System.nanoTime() - renderStart) / 1000000;
    		try {
    			Thread.sleep(Math.max(0, fpsWait - renderTime));
    		} catch (InterruptedException e) {
    			Thread.interrupted();
    			break;
    		}
    		
    		calculateFPs();
		}
	}

	private void calculateFPs() {
		if(System.currentTimeMillis() - lastTime >= 1000){
		    fps = frames;
		    frames = 0;
		    lastTime = System.currentTimeMillis();
		}
		frames++;
	}

	private void updateUi(List<Fish> school) {
		BufferStrategy bs = mainWindow.getCanvas().getBufferStrategy();
	    if(bs == null){
	    	mainWindow.getCanvas().createBufferStrategy(2);
	        return;
	    }
	    
	    logger.debug("> " + fps);
	    
	    Graphics2D g = (Graphics2D)bs.getDrawGraphics();
	    BufferedImage toDrawImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
	    Graphics toDrawG = toDrawImage.getGraphics();
	    toDrawG.setColor(Color.BLUE);
//	    toDrawG.fillRect(0, 0, 100,100);
	    toDrawG.drawString(String.format("FPS: %d", this.fps), 15, 15);
	    g.drawImage(toDrawImage, 0, 0, null);
	    g.dispose();
	    bs.show();
	}
}
