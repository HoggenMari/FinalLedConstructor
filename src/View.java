import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class View extends JFrame implements Observer, MouseListener,
		MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton loadImgBtn = new JButton("Load Image");
	private JTextField ledMaxWidthField = new JTextField(5);
	private JTextField ledMaxHeightField = new JTextField(5);
	
	private JButton clearBtn = new JButton("Clear");
	private JButton nextBtn = new JButton("Next");

	private ImageIcon originalImage;

	private JLabel origImgLabel;

	private int x;

	private int y;

	private Model model;

	private BufferedImage bufImg;

	private Graphics2D g;

	private int maxWidthLed;

	private int maxHeightLed;

	private int pixelX;

	private int pixelY;

	private ArrayList<Point> point;

	private Graphics2D h;

	View(Controller controller) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(200, 600);
		this.setTitle("LED Constructor");
		
		JPanel panel = new JPanel();

		loadImgBtn.addActionListener(controller);
		clearBtn.addActionListener(controller);
		nextBtn.addActionListener(controller);

		panel.setLayout(new FlowLayout());
		panel.add(loadImgBtn);
		panel.add(new JLabel("Anzahl LEDs Breite:"));
		panel.add(ledMaxWidthField);
		panel.add(new JLabel("Anzahl LEDs Hšhe:"));
		panel.add(ledMaxHeightField);		
		panel.add(clearBtn);
		panel.add(nextBtn);
		
		

		originalImage = new ImageIcon();
		origImgLabel = new JLabel(originalImage);

		this.add(panel, BorderLayout.NORTH);
		this.add(origImgLabel, BorderLayout.WEST);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.pack();

	}

	@Override
	public void update(Observable o, Object arg1) {
		model = (Model) o;

		System.out.println(model.getOriginalImage().toString());

		if (model.receivedNewImage()) {
			originalImage.setImage(model.getOriginalImage());
		}

		System.out.println("Repaint");
		this.pack();
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		x = e.getX() - origImgLabel.getLocation().x;
		y = e.getY() - origImgLabel.getLocation().y;

		System.out.println("Pressed");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Released");
		/*for (Point p : point) {
			  g.drawRect(p.x, p.y, pixelX, pixelY);
			}*/
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Released");
		model.setPaintImage(model.getOriginalImage());
		g = (Graphics2D) model.getPaintImage().getGraphics();
		g.setStroke(new BasicStroke(10));
		g.setColor(Color.GREEN);
		int xTemp = e.getX() - origImgLabel.getLocation().x;
		int yTemp = e.getY() - origImgLabel.getLocation().y;
		
		model.min(x, y, xTemp, yTemp);
		
		/*minX = Math.min(minX, x);
		minY = Math.min(minY, y);
		maxX = Math.max(maxX, x);
		maxY = Math.max(maxY, y);
		minX = Math.min(minX, xTemp);
		minY = Math.min(minY, yTemp);
		maxX = Math.max(maxX, xTemp);
		maxY = Math.max(maxY, yTemp);*/
		
		g.drawLine(x, y, xTemp, yTemp);
		x = xTemp;
		y = yTemp;

		if (!ledMaxWidthField.getText().isEmpty()
				& !ledMaxHeightField.getText().isEmpty()) {
			
			maxWidthLed = Integer.parseInt(ledMaxWidthField.getText());
			maxHeightLed = Integer.parseInt(ledMaxHeightField.getText());
			
			pixelX = (model.getMaxX() - model.getMinX()) / maxWidthLed;
			pixelY = (model.getMaxY() - model.getMinY()) / maxHeightLed;
			
			System.out.println("pixelX:"+pixelX);
			System.out.println("pixelY"+pixelY);
			
						
			int ix = model.getMinX();
			point = new ArrayList<Point>();
			while (ix < model.getMaxX()) {
				int iy = model.getMinY();
				while (iy < model.getMaxY()) {
					System.out.println(checkColor(ix, iy));
					if(checkColor(ix,iy)) {
						//g.drawRect(ix, iy, pixelX, pixelY);
						point.add(new Point(ix, iy));
					}
					//System.out.println("ix+"+ix+"iy:"+iy);
					iy = iy + Math.max(pixelY, 1);
				}
				ix = ix + Math.max(pixelX, 1);
			}
		} else {
			System.out.println("Leeres Textfeld");
		}
		
		model.setOutputImage(model.getPaintImage());
		h = (Graphics2D) model.getPaintImage().getGraphics();
		h.setStroke(new BasicStroke(2));
		h.setColor(Color.CYAN);
		
		
		for (Point p : point) {
			  h.drawRect(p.x, p.y, pixelX, pixelY);
			}

		this.pack();
		this.repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean checkColor(int x, int y) {
		int counter = 0;
		for (int ix = x; ix < x + pixelX; ix++) {
			for (int iy = y; iy < y + pixelY; iy++) {
				if (model.getOriginalImage().getRGB(ix, iy) == Color.GREEN
						.getRGB()) {
					counter++;
				}
			}
		}
		if (counter > (pixelX + pixelY) / 2) {
			return true;
		} else
			return false;
	}

}
