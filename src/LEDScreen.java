import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class LEDScreen extends PApplet {
	private int[] y;
	private int xRes, yRes;
	private boolean[][] matrix;
	private PImage image;
	public byte[] data;
	private int ledNum;


	//Constructor for variable y-Resolution
	public LEDScreen(int[] y, boolean[][] matrix) {
		this.y = y;
		this.xRes = y.length;
		this.yRes = getMaxY(y);
		this.matrix = matrix;
		
		for(int ix=0; ix<matrix.length; ix++){
			for(int iy=0; iy<matrix[ix].length; iy++){
				if(matrix[ix][iy]){
				ledNum = ledNum + 1;
				}
			}
		}
	}
	
	//Constructor for rect y-Resolution
	public LEDScreen(int x, int y) {
		this.y = new int[x];
		for(int ix=0; ix<x; ix++) {
			this.y[ix] = y;
		}
		this.xRes = x;
		this.yRes = y;
	}
	
	public void update() {
		PGraphics pg = createGraphics(10,10,JAVA2D);
		pg.beginDraw();
		pg.background(255,130,2);
		pg.endDraw();
		//image = image;
		
		
		data = new byte[3*ledNum];
		
		int dataIndex = 0;
		
		for(int ix=0; ix<matrix.length; ix++) {
			for(int iy=0; iy<matrix[ix].length; iy++) {	
				if(matrix[ix][iy]){
				int rgb = pg.get(ix, iy);
				data[dataIndex+2] = (byte) (rgb & 0xff);
				data[dataIndex+1] = (byte) ((rgb >> 8) & 0xff);
				data[dataIndex] = (byte) ((rgb >> 16) & 0xff);
				//System.out.println("i: "+ix+" j:"+iy+" r: "+data[(ix*heightA+iy)+2]+" g:"+data[(ix*heightA+iy)+1]+" b:"+data[(ix*heightA+iy)]);
				dataIndex +=3;
				}
			}
			ix++;
			for (int iy = matrix[ix].length-1; iy >= 0; iy--) {
				if(matrix[ix][iy]){
				int rgb = pg.get(ix, iy);	
				data[dataIndex+2] = (byte) (rgb & 0xff);
				data[dataIndex+1] = (byte) ((rgb >> 8) & 0xff);
				data[dataIndex] = (byte) ((rgb >> 16) & 0xff);
				//System.out.println("i: "+ix+" j:"+iy+" r: "+data[(ix*heightA+iy)+2]+" g:"+data[(ix*heightA+iy)+1]+" b:"+data[(ix*heightA+iy)]);
				dataIndex +=3;
				}
			}
		}
		
		for(byte d : data){
			System.out.println("DATA: "+d+" LEDNum: "+ledNum);
		}

	}
	
	private int getMaxY(int []y) {
		 int maximum = y[0];   // start with the first value
		    for (int i=1; i<y.length; i++) {
		        if (y[i] > maximum) {
		            maximum = y[i];   // new maximum
		        }
		    }
		    return maximum;
	}
	
	//getter & setter
	/*public int[] getY() {
		return y;
	}*/
	
	public int getxRes() {
		return xRes;
	}

	public void setxRes(int xRes) {
		this.xRes = xRes;
	}

	public int getyRes() {
		return yRes;
	}

	public void setyRes(int yRes) {
		this.yRes = yRes;
	}

}