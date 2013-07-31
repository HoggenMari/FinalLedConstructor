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

}
