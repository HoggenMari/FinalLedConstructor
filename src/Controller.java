import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;


public class Controller implements ActionListener {
	
	private View view;
	private Model model;
	
	public Controller() {
		this.model = new Model();
		this.view = new View(this);
		model.addObserver(view);
		
		view.setVisible(true);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals("Load Image")){
			System.out.println("load");
			openFile();
		} else if (cmd.equals("Clear")){
			System.out.println("clear");
			clear();
		} else if (cmd.equals("Play")){
			System.out.println("play");
			play();
		}
		
	}
	
	private void openFile() {
		JFileChooser fc = new JFileChooser();
				
		int state = fc.showOpenDialog( null );
		
	    if ( state == JFileChooser.APPROVE_OPTION )
	    {
		    model.setImage(fc.getSelectedFile().getPath(), view.getWidth());
	    }
	}
	
	private void play() {
		model.play();
	}
	
	private void clear() {
		model.setImage(model.getImageName(), view.getWidth());
		model.setMinX(Integer.MAX_VALUE);
		model.setMinY(Integer.MAX_VALUE);
		model.setMaxX(0);
		model.setMaxY(0);
	}

}
