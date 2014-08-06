import processing.core.PApplet;
import processing.core.PGraphics;


public class ProcesingMain extends PApplet {

	private PGraphics pg;

	public void setup(){
		pg = createGraphics(10,10,JAVA2D);
	}
	
	public void draw(){
		println("Bla");
		pg.beginDraw();
		pg.background(0);
		pg.endDraw();
	}
}
