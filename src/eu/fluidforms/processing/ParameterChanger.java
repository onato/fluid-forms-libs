/*
 * This Class is too long. Split the different parameter types into classes.
 */

package eu.fluidforms.processing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import processing.core.PApplet;
import processing.xml.XMLElement;
import processing.xml.XMLWriter;

public class ParameterChanger implements ChangeListener, KeyListener,
		ActionListener, MouseListener, ComponentListener {
	private static final long serialVersionUID = 19981017L;
	private Preferences preferences;

	private static PApplet p5 = null;
	private long time = 0;
	protected boolean forceShow = false;

	public ParameterChanger() {
	}

	JFrame frameParams;
	JPanel paramsPanel;
	HashMap<JComponent, JLabel> valueMap;
	HashMap<String, JComponent> nameMap = new HashMap<String, JComponent>();
	HashMap<String, String> labelMap = new HashMap<String, String>();
	Vector<JComponent> components = new Vector<JComponent>();
	XMLElement xml;
	Timer timer;
	JButton currentButton;

	public void setup(PApplet processingSketch) {
		p5 = processingSketch;

		try {
			preferences = Preferences.userNodeForPackage(p5.getClass());
		} catch (Exception e) {
		}

		if (p5.createInput("params.xml") != null) {
			xml = new XMLElement(p5, "params.xml");
		} else {
			xml = new XMLElement();
			xml.setName("paramSets");
		}

		timer = new Timer(2000, this);
		timer.setRepeats(false);

		processingSketch.registerDraw(this);
		valueMap = new HashMap<JComponent, JLabel>();
		Field[] flds = processingSketch.getClass().getDeclaredFields();
		for (Field f : flds) {
			if (f.getName().indexOf("param") == 0 && f.getType().isPrimitive()) {
				initFrame();
			}
		}

		// Create the label.
		int paramCount = 0;
		for (Field f : flds) {
			if (f.getName().indexOf("param") == 0 && f.getType().isPrimitive()) {
				String name = f.getName();
				String label = name.substring(5);
				String labelWithSpaces = "";
				labelWithSpaces = parseCamelCase(label);

				float value = 50;
				Boolean bool = true, isBool = false;
				try {
					value = f.getInt(processingSketch);
				} catch (IllegalAccessException iae) {
					reportPublicError(name);
				} catch (Exception e) {
					try {
						value = (int) f.getFloat(processingSketch);
					} catch (IllegalAccessException iae) {
						reportPublicError(name);
					} catch (Exception e2) {
						try {
							isBool = true;
							bool = f.getBoolean(processingSketch);
						} catch (Exception e3) {// give up
						}
					}
				}
				if (isBool) {
					add(labelWithSpaces, name, bool);
				} else {
					float max = 100;
					float min = 0;
					try {
						try {
							int[] range = (int[]) p5.getClass()
									.getDeclaredField(name + "Range").get(p5);
							min = range[0];
							if (range.length > 1) {
								max = range[1];
							}
						} catch (ClassCastException cce) {
							float[] range = (float[]) p5.getClass()
									.getDeclaredField(name + "Range").get(p5);
							min = range[0];
							if (range.length > 1) {
								max = range[1];
							}
						}
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
					}

					value = Math.max(min, value);
					value = Math.min(max, value);

					add(labelWithSpaces, name, (int) value, (int) min,
							(int) max);

				}

				paramCount++;

			}

		}
		if (!forceShow && paramCount == 0) {
			frameParams = null;
		} else {
			showFrame();
		}

	}

	private void showFrame() {
		if (frameParams == null) {
			initFrame();
		}
		int width = 350;
		int height = 400;
		int x = p5.getBounds().x + p5.getWidth() + 8;
		int y = p5.getY();
		if (preferences != null) {
			width = preferences.getInt("width", 350);
			height = preferences.getInt("height", 400);
			x = preferences.getInt("x", p5.getBounds().x + p5.getWidth() + 8);
			y = preferences.getInt("y", p5.getY());
		}

		frameParams.setBounds(x, y, width, height);
		frameParams.validate();
		frameParams.setVisible(true);
	}

	private String parseCamelCase(String label) {
		String labelWithSpaces = "";
		for (int i = 0; i < label.length(); i++) {
			char cur = label.charAt(i);
			if (Character.isUpperCase(cur)) {
				labelWithSpaces += " ";
			}
			labelWithSpaces += cur;
		}
		return labelWithSpaces;
	}

	private void initFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// System.out.println("Error setting native LAF: " + e);
		}

		frameParams = new JFrame("Fluid Forms Libs ~ Parameter Changer");
		if (!p5.online) {
			frameParams.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		frameParams.setBackground(new Color(255, 255, 255));
		frameParams.setLayout(new BorderLayout());
		Image icon = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/data/icon.gif"));
		frameParams.setIconImage(icon);
		frameParams.addComponentListener(this);

		JPanel buttons = new JPanel();
		// buttons.setBounds(0, 0, 200, 10);
		for (int i = 0; i < 10; i++) {
			XMLElement paramSet = null;
			if (i < xml.getChildCount()) {
				paramSet = xml.getChild(i);
			}
			JButton button = new JButton(String.valueOf(i));
			button.setPreferredSize(new Dimension(45, 45));
			button.setActionCommand(button.getText());
			button.addActionListener(this);
			button.addMouseListener(this);
			button.addKeyListener(this);
			if (paramSet == null || paramSet.getChildCount() == 0) {
				button.setForeground(new Color(180, 180, 180));
			}
			button.setToolTipText("Click and hold to save current values.");
			buttons.add(button);
		}
		frameParams.add(buttons, BorderLayout.NORTH);
		frameParams.addKeyListener(this);

		paramsPanel = new JPanel();
		paramsPanel.setLayout(new GridLayout(0, 3));

		JScrollPane panel = new JScrollPane(paramsPanel);
		frameParams.add(panel);
		frameParams.addKeyListener(this);

	}

	public void reorganise() {
		Set<String> s = nameMap.keySet();
		TreeSet<String> ts = new TreeSet<String>();
		ts.addAll(s);
		Iterator<String> it = ts.iterator();
		boolean usePanes = false;
		while (it.hasNext()) {
			String name = it.next();
			if (labelMap.get(name).indexOf(':') > 0) {
				usePanes = true;
				break;
			}
		}
		if (usePanes) {
			paramsPanel.removeAll();
			paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));
			String lastCategory = null;
			JPanel currentPane = null;
			for (JComponent component : components) {
				String name = component.getName();
				String value = "";
				if (component instanceof JSlider) {
					value = String.valueOf(((JSlider) component).getValue());
				}
				String label = labelMap.get(name);
				if (label.indexOf(':') < 0) {
					currentPane = new JPanel();
					currentPane.setLayout(new GridLayout(0, 3));
					placeComponent(currentPane, labelMap.get(name), value,
							component);
					paramsPanel.add(currentPane);
					currentPane = null;
				} else {
					String currentCategory = label.substring(0, label
							.indexOf(':'));
					String newLabel = label.substring(label.indexOf(':') + 1);
					if (!currentCategory.equals(lastCategory)) {
						if (currentPane != null) {
							paramsPanel.add(currentPane);
						}
						currentPane = new JPanel();
						currentPane.setLayout(new GridLayout(0, 3));
						currentPane.setBorder(BorderFactory
								.createTitledBorder(currentCategory));
					}
					placeComponent(currentPane, newLabel, value, component);

					lastCategory = currentCategory;
				}
			}
			if (currentPane != null) {
				paramsPanel.add(currentPane);
			}

		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == null) {
			int buttonIndex = Integer.parseInt(currentButton.getText());
			while (xml.getChildCount() <= buttonIndex) {
				XMLElement paramSet = new XMLElement();
				paramSet.setName("paramSet");
				// old API paramSet.setAttribute("id", String.valueOf(xml.getChildCount()));
				paramSet.setString("id", String.valueOf(xml.getChildCount()));
				xml.addChild(paramSet);
			}
			XMLElement paramSet = xml.getChild(buttonIndex);
			while (paramSet.hasChildren()) {
				paramSet.removeChild(0);
			}
			paramSet.setString("id", String.valueOf(buttonIndex));
			for (int i = 0; i < components.size(); i++) {
				XMLElement param = new XMLElement();
				param.setName("param");
				JComponent component = components.elementAt(i);
				param.setString("name", String.valueOf(component.getName()));
				if (component instanceof JSlider) {
					JSlider slider = (JSlider) component;
					param.setString("value", String.valueOf(slider
							.getValue()));
					paramSet.addChild(param);
				} else if (component instanceof JCheckBox) {
					JCheckBox cb = (JCheckBox) component;
					param.setString("value", String.valueOf(cb
									.isSelected()));
					paramSet.addChild(param);
				}
			}
			exportXML();
		} else {
			timer.stop();
			Date date = new Date();
			int buttonIndex = Integer.parseInt(e.getActionCommand());
			if (buttonIndex >= xml.getChildCount())
				return;
			XMLElement paramSet = xml.getChild(buttonIndex);
			if (date.getTime() - time < 2000) {
				HashMap<String, String> params = new HashMap<String, String>();
				for (int i = Math.min(components.size(), paramSet
						.getChildCount()) - 1; i >= 0; i--) {
					params.put(paramSet.getChild(i).getString("name"), 
							paramSet.getChild(i).getString("value"));
				}
				for (int i = Math.min(components.size(), paramSet
						.getChildCount()) - 1; i >= 0; i--) {
					JComponent component = components.elementAt(i);
					if (component instanceof JSlider) {
						int value = Integer.parseInt(params.get(component
								.getName()));
						JSlider slider = (JSlider) component;
						slider.setValue(value);
					} else if (component instanceof JCheckBox) {
						String sValue = params.get(component.getName());
						if (sValue != null) {
							boolean value = Boolean.parseBoolean(sValue);
							JCheckBox cb = (JCheckBox) component;
							cb.setSelected(value);
						}
					}
				}
			}
		}
	}

	private void exportXML() {
//		PrintWriter output = p5.createWriter("data/params.xml");
//		xml.write(output);
//		output.flush(); // Writes the remaining data to the file
//		output.close(); // Finishes the file
//		if (currentButton != null) {
//			currentButton.setForeground(Color.BLACK);
//			Toolkit.getDefaultToolkit().beep();
//		}
		XMLWriter xmlWriter = new XMLWriter(p5.createOutput(p5
				.dataPath("params.xml")));
		try {
			xmlWriter.write (xml, true, 1);
			if (currentButton != null) {
				currentButton.setForeground(Color.BLACK);
				Toolkit.getDefaultToolkit().beep();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/** Listen to the slider. */
	public void stateChanged(ChangeEvent e) {
		try {
			JSlider source = (JSlider) e.getSource();
			JLabel valueLabel = valueMap.get(source);
			valueLabel.setText(String.valueOf(source.getValue()));

			// if (!source.getValueIsAdjusting()) {
			try {
				Field field = p5.getClass().getDeclaredField(source.getName());
				field.set(p5, source.getValue());
			} catch (IllegalAccessException e1) {
				reportPublicError(source.getName());
			} catch (NoSuchFieldException e1) {
				// ignore, we will be asked for the value later.
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// }
		} catch (ClassCastException eBool) {
			JCheckBox source = (JCheckBox) e.getSource();
			try {
				Field field = p5.getClass().getDeclaredField(source.getName());
				field.set(p5, source.isSelected());
			} catch (IllegalAccessException e1) {
				System.err.println(source.getName()
						+ " must be public. In other words...\n   - public "
						+ source.getName() + ";");
			} catch (NoSuchFieldException e1) {
				// ignore, we will be asked for the value later.
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public void hide() {
		frameParams.setVisible(false);
		frameParams = null;
	}

	public void draw() {
		if (frameParams != null && p5.frameCount < 10) {
			frameParams.setVisible(true);
		}
	}

	public static float[] getRange(String name) {
		if (p5 == null) {
			p5 = FF.p5;
		}
		if (p5 == null) {
			System.out.println("No handle on the PApplet");
		}
		float[] range = { 0, 100 };
		try {
			Field field = p5.getClass().getDeclaredField(name + "Range");
			try {
				range = (float[]) field.get(p5);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					range = (float[]) field.get(p5);
				} catch (Exception e2) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		return range;
	}

	public static float get(String name, float valueBetween0And1) {
		float[] range = getRange(name);
		return range[0] + valueBetween0And1 * (range[1] - range[0]);
	}

	public static float getNormalised(String name, float value) {
		float[] range = getRange(name);
		return (value - range[0]) / (range[1] - range[0]);
	}

	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == KeyEvent.VK_ESCAPE) {
			p5.exit();
		} else if (key == 'R' && e.isAltDown()) {
			if (preferences != null) {
				preferences.remove("x");
				preferences.remove("y");
				preferences.remove("width");
				preferences.remove("height");
			}
			Toolkit.getDefaultToolkit().beep();
		}
		if (e.isControlDown() || e.isMetaDown()) {
			p5.keyPressed(e);
		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		Date date = new Date();
		time = date.getTime();
		timer.start();
		currentButton = (JButton) e.getSource();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent e) {
		if (preferences != null) {
			preferences.putInt("x", frameParams.getBounds().x);
			preferences.putInt("y", frameParams.getBounds().y);
		}
	}

	public void componentResized(ComponentEvent e) {
		if (preferences != null) {
			preferences.putInt("width", frameParams.getBounds().width);
			preferences.putInt("height", frameParams.getBounds().height);
		}
	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void add(String name, boolean value) {
		add(name, name, value);
	}

	public void add(String niceName, String name, boolean value) {
		JCheckBox cb = new JCheckBox();
		cb.setName(name);
		cb.setSelected(value);
		cb.addChangeListener(this);
		cb.addKeyListener(this);

		components.add(cb);
		nameMap.put(name, cb);
		labelMap.put(name, niceName);

		placeComponent(paramsPanel, niceName, "", cb);
		showFrame();
	}

	public void add(String name, float value) {
		add(name, name, (int) value);
	}

	public void add(String niceName, String name, float value) {
		add(niceName, name, (int) value);
	}

	public void add(String niceName, String name, int value) {
		add(niceName, name, value, 0, 100);
	}

	public void add(String name, int value) {
		add(name, name, value);
	}

	public void add(String niceName, String name, int value, int min, int max) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
		slider.setName(name);
		slider.addChangeListener(this);
		slider.addKeyListener(this);

		components.add(slider);
		nameMap.put(name, slider);
		labelMap.put(name, niceName);

		placeComponent(paramsPanel, niceName, String.valueOf(value), slider);
		showFrame();
	}

	private void placeComponent(JPanel container, String niceName,
			String value, JComponent component) {

		JLabel valueLabel = new JLabel(value, JLabel.LEFT);
		valueLabel.setPreferredSize(new Dimension(20, 0));
		valueMap.put(component, valueLabel);

		JLabel label = new JLabel(niceName, JLabel.RIGHT);

		if (container == null) {
			initFrame();
			container = paramsPanel;
		}
		container.add(label);
		container.add(component);
		container.add(valueLabel);
	}

	public boolean getBoolean(String name) {
		JCheckBox cb = (JCheckBox) nameMap.get(name);
		if (cb == null) {
			System.err
					.println("Couldn't find parameter: "
							+ name
							+ ". Make sure you have created it already with add(\"paramName\", true).");
			return false;
		}
		return cb.isSelected();
	}

	public float getFloat(String name) {
		return (float) getInt(name);
	}

	public int getInt(String name) {
		JSlider cb = (JSlider) nameMap.get(name);
		if (cb == null) {
			System.err
					.println("Couldn't find parameter: "
							+ name
							+ ". Make sure you have created it already with add(\"paramName\", 50).");
			return 0;
		}
		return cb.getValue();
	}

	private void reportPublicError(String name) {
		System.err.println(name
				+ " must be public. In other words...\n   - public " + name
				+ ";");
	}
}
