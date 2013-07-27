import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

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

	private ImageIcon originalImage;

	private JLabel origImgLabel;

	private int x;

	private int y;

	private Model model;

	private BufferedImage bufImg;

	private Graphics2D g;

	private int maxWidthLed;

	private int maxHeightLed;

	private int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0,
			maxY = 0;

	private int pixelX;

	private int pixelY;

	View(Controller controller) {

		JPanel panel = new JPanel();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 200);

		loadImgBtn.addActionListener(controller);

		panel.setLayout(new FlowLayout());
		panel.add(loadImgBtn);
		panel.add(new JLabel("Anzahl LEDs Breite:"));
		panel.add(ledMaxWidthField);
		panel.add(new JLabel("Anzahl LEDs Hšhe:"));
		panel.add(ledMaxHeightField);

		originalImage = new ImageIcon();
		origImgLabel = new JLabel(originalImage);

		this.add(panel, BorderLayout.NORTH);
		this.add(origImgLabel, BorderLayout.WEST);
		// this.add(new Freihandzeichnen());

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
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Released");
		bufImg = model.getOriginalImage();
		g = (Graphics2D) bufImg.getGraphics();
		g.setStroke(new BasicStroke(10));
		g.setColor(Color.GREEN);
		int xTemp = e.getX() - origImgLabel.getLocation().x;
		int yTemp = e.getY() - origImgLabel.getLocation().y;
		minX = Math.min(minX, x);
		minY = Math.min(minY, y);
		maxX = Math.max(maxX, x);
		maxY = Math.max(maxY, y);
		minX = Math.min(minX, xTemp);
		minY = Math.min(minY, yTemp);
		maxX = Math.max(maxX, xTemp);
		maxY = Math.max(maxY, yTemp);
		g.drawLine(x, y, xTemp, yTemp);
		x = xTemp;
		y = yTemp;

		if (!ledMaxWidthField.getText().isEmpty()
				& !ledMaxHeightField.getText().isEmpty()) {
			maxWidthLed = Integer.parseInt(ledMaxWidthField.getText());
			maxHeightLed = Integer.parseInt(ledMaxHeightField.getText());
			pixelX = (maxX - minX) / maxWidthLed;
			pixelY = (maxY - minY) / maxHeightLed;
			System.out.println("pixelX:"+pixelX);
			System.out.println("pixelY"+pixelY);
			System.out.println("minX:" + minX);
			System.out.println("minY:" + minY);
			System.out.println("maxX:" + maxX);
			System.out.println("maxY:" + maxY);
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.CYAN);
			g.drawRect(minX, minY, maxX - minX, maxY - minY);
			int ix = minX;
			while (ix < maxX) {
				int iy = minY;
				while (iy < maxY) {
					System.out.println(checkColor(ix, iy));
					//System.out.println("ix+"+ix+"iy:"+iy);
					iy = iy + Math.max(pixelY, 1);
				}
				ix = ix + Math.max(pixelX, 1);
			}
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
