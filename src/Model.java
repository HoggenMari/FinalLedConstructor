import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observable;

import javax.imageio.ImageIO;


public class Model extends Observable {

	private BufferedImage originalImage;
	private boolean gotNewImage;
	private String imageName;
	
	public Model() {
		gotNewImage = false;
	}
	
	public void setImage(String newImage, int width) {
		imageName = newImage;
		
		try {
			originalImage = ImageIO.read(new File(imageName));
		}
		catch (Exception e) {
			
		}
		
		int originalImgWidth = originalImage.getWidth();
		double scaleFactor = (double)width/(double)originalImgWidth;
		
		System.out.println(originalImgWidth);
		
		BufferedImage resizedImage = new BufferedImage(width, (int)(originalImage.getHeight()*scaleFactor), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, (int)(originalImage.getHeight()*scaleFactor), null);
		g.dispose();
		
		originalImage = resizedImage;
		
		
		gotNewImage = true;
		
		setChanged();
		notifyObservers();
	}
	
	
	public boolean receivedNewImage() {
		return gotNewImage;
	}

	public BufferedImage getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(BufferedImage originalImage) {
		this.originalImage = originalImage;
	}
	
	
	
}
