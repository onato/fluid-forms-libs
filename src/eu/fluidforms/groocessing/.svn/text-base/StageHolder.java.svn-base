package eu.fluidforms.groocessing;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

public class StageHolder implements ComponentListener, WindowListener {
	private static Stage stage;
	private static JFrame frame;
	
	public static void setStage(Stage stage) {
		StageHolder.stage = stage;
	}
	public static Stage getStage() {
		return stage;
	}
	private static StageHolder myInstance;
	
	
	public static void runSketch(File groovyCodeFile) {
		System.out.println("runSketch");
		if(myInstance == null){
			myInstance = new StageHolder();
		}
		if(frame==null || stage==null){
			myInstance.initFrame(groovyCodeFile);
		}else{
			stage.reloadScript(groovyCodeFile);
		}
		
	}
	private void initFrame(File groovyCodeFile){
		System.out.println("Build the frame");
		frame = new JFrame("Groocessing");
		frame.setName("StageHoldingFrame");
		frame.setLayout(null);

		stage = new Stage(groovyCodeFile);
		stage.init();
		stage.frame = frame;
		//stage.setupFrameResizeListener();
		frame.add(stage);
		//stage.setup();
		preferences = Preferences.userNodeForPackage(myInstance.getClass());
		int width = preferences.getInt("width", 350);
		int height = preferences.getInt("height", 400);
		int x = preferences.getInt("x", 500);
		int y = preferences.getInt("y", 500);
		frame.setBounds(x, y, width, height);
		frame.validate();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setResizable(false);
		frame.setVisible(true);
		frame.addComponentListener(myInstance);
		frame.addWindowListener(myInstance);
	}
	public void componentMoved(ComponentEvent e) {
		preferences.putInt("x", frame.getBounds().x);
		preferences.putInt("y", frame.getBounds().y);
	}
	public void componentResized(ComponentEvent e) {
		if(frame!=null){
			preferences.putInt("width", frame.getBounds().width);
			preferences.putInt("height", frame.getBounds().height);
		}
	}
	private static Preferences preferences;

	private static final long serialVersionUID = 2504756881826186869L;

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}
		
	public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		stage.destroy();
		frame = null;
	}

	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
