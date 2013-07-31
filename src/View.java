import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

	private ImageIcon Image;

	private JLabel origImgLabel;

	private int x;

	private int y;

	private Model model;

	private Graphics2D g;

	private int maxWidthLed;

	private int maxHeightLed;

	private int pixelX;

	private int pixelY;

	private ArrayList<Point> point;

	private int newWidth;

	private int newHeight;

	private Graphics2D h;

	private Graphics2D i;

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

		Image = new ImageIcon();
		origImgLabel = new JLabel(Image);

		this.add(panel, BorderLayout.NORTH);
		this.add(origImgLabel, BorderLayout.WEST);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.pack();

	}

	@Override
	public void update(Observable o, Object arg1) {
		model = (Model) o;

		if (model.receivedNewImage()) {
			
			newWidth = this.getWidth();
			double scaleFactor = (double) newWidth
					/ model.getOriginalImage().getWidth();
			newHeight = (int) (model.getOriginalImage().getHeight() * scaleFactor);

			Image.setImage(model.getOriginalImage());
			Image img = Image.getImage();
			BufferedImage tmpImage = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			g = (Graphics2D) tmpImage.getGraphics();
			g.scale(scaleFactor, scaleFactor);
			g.drawImage(img, 0, 0, null);
			g.dispose();
			Image.setImage(tmpImage);
			model.setScaledImage(tmpImage);
			model.setPaintImage(tmpImage);

		}

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

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		//draw with GREEN Color on Canvas and model.paintImage
		Image img = model.getPaintImage();
		BufferedImage tmpImage = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_RGB);
		h = (Graphics2D) tmpImage.getGraphics();
		h.drawImage(img, 0, 0, newWidth, newHeight, null);

		//mouseDragged-Coordinates
		int xTemp = e.getX() - origImgLabel.getLocation().x;
		int yTemp = e.getY() - origImgLabel.getLocation().y;
		
		//color settings
		h.setStroke(new BasicStroke(10));
		h.setColor(Color.GREEN);

		//draw line from mousePressed(x,y) to mouseDragged(xTemp, yTemp)
		h.drawLine(x, y, xTemp, yTemp);
		x = xTemp;
		y = yTemp;

		//paintedImage on canvas and save in model.paintImage
		Image.setImage(tmpImage);
		model.setPaintImage(tmpImage);
		h.dispose();
		
		//check for new min, max of painted area
		model.min(x, y);

		//draw rects for LEDs
		if (!ledMaxWidthField.getText().isEmpty()
				& !ledMaxHeightField.getText().isEmpty()) {

			maxWidthLed = Integer.parseInt(ledMaxWidthField.getText());
			maxHeightLed = Integer.parseInt(ledMaxHeightField.getText());

			pixelX = (model.getMaxX() - model.getMinX()) / maxWidthLed;
			pixelY = (model.getMaxY() - model.getMinY()) / maxHeightLed;

			System.out.println("pixelX:" + pixelX);
			System.out.println("pixelY" + pixelY);

			int ix = model.getMinX();
			point = new ArrayList<Point>();
			while (ix < model.getMaxX()) {
				int iy = model.getMinY();
				while (iy < model.getMaxY()) {
					System.out.println(checkColor(ix, iy));
					if (checkColor(ix, iy)) {
						// g.drawRect(ix, iy, pixelX, pixelY);
						point.add(new Point(ix, iy));
					}
					// System.out.println("ix+"+ix+"iy:"+iy);
					iy = iy + Math.max(pixelY, 1);
				}
				ix = ix + Math.max(pixelX, 1);
			}
			Image img2 = model.getPaintImage();
			BufferedImage tmpImage2 = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			i = (Graphics2D) tmpImage2.getGraphics();
			i.drawImage(img2, 0, 0, newWidth, newHeight, null);

			i.setStroke(new BasicStroke(1));

			for (Point p : point) {
				i.setColor(Color.GREEN);
				i.fillRect(p.x, p.y, pixelX, pixelY);
				i.setColor(Color.DARK_GRAY);
				i.drawRect(p.x, p.y, pixelX, pixelY);
			}

			i.dispose();
			Image.setImage(tmpImage2);
			// model.setScaledImage(tmpImage2);

		} else {
			System.out.println("Leeres Textfeld");
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
				if (model.getPaintImage().getRGB(ix, iy) == Color.GREEN
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
