import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observable;

import javax.imageio.ImageIO;


public class Model extends Observable {

	private BufferedImage originalImage, scaledImage, paintImage, outputImage;


	private boolean gotNewImage;
	private String imageName;
	private int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
	private int maxX = 0, maxY = 0;
	
	public BufferedImage getScaledImage() {
		return scaledImage;
	}

	public void setScaledImage(BufferedImage scaledImage) {
		this.scaledImage = scaledImage;
	}
	
	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

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
		
		/*int originalImgWidth = originalImage.getWidth();
		double scaleFactor = (double)width/(double)originalImgWidth;
		
		System.out.println(originalImgWidth);
		
		
		resizedImage = new BufferedImage(width, (int)(originalImage.getHeight()*scaleFactor), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(resizedImage, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(), null);
		g.dispose();
		
		originalImage = resizedImage;*/
		
		
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
	
	public void min(int x, int y){
		minX = Math.min(minX, x);
		minY = Math.min(minY, y);
		maxX = Math.max(maxX, x);
		maxY = Math.max(maxY, y);
	}

	public BufferedImage getPaintImage() {
		return paintImage;
	}

	public void setPaintImage(BufferedImage paintImage) {
		this.paintImage = paintImage;
	}

	public BufferedImage getOutputImage() {
		return outputImage;
	}

	public void setOutputImage(BufferedImage outputImage) {
		this.outputImage = outputImage;
	}
	
	
}
