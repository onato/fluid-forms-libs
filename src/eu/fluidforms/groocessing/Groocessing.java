/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
 Part of the Processing project - http://processing.org

 Copyright (c) 2008 Ben Fry and Casey Reas

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package eu.fluidforms.groocessing;

import processing.app.Editor;
import processing.app.tools.Tool;

/**
 * Example Tools menu entry.
 */
public class Groocessing implements Tool {
	Editor editor;
	long lastModified = 0;
	

	public void init(Editor editor) {
		this.editor = editor;
	}

	public String getMenuTitle() {
		return "Run Groocessing";
	}

	public void run() {
		//String sketchName = editor.getSketch().getName();
		//String groovyCode = editor.getSketch().getCode(0).getProgram();
		if(editor.getSketch().getCode()[0].getFile().getAbsolutePath().indexOf(".tmp")>0){
			StageHolder.runSketch(editor.getSketch().getCurrentCode().getFile());
			System.err.println("Save your sketch before running it.");
		}else{
			StageHolder.runSketch(editor.getSketch().getCode()[0].getFile());
			//editor.statusNotice("SketchName:" + sketchName);
		}
		StageHolder.getStage().sketchPath = editor.getSketch().getCode()[0].getFile().getParent();
	}


	protected void mangleSelection() {
		if (editor.isSelectionActive()) {
			String selection = editor.getSelectedText();
			char[] stuff = selection.toCharArray();
			// Randomly swap a bunch of characters in the text
			for (int i = 0; i < stuff.length / 10; i++) {
				int a = (int) (Math.random() * stuff.length);
				int b = (int) (Math.random() * stuff.length);
				if (stuff[a] == '\n' || stuff[b] == '\n') {
					continue; // skip newline characters
				}
				stuff[a] = selection.charAt(b);
				stuff[b] = selection.charAt(a);
			}
			editor.startCompoundEdit();
			editor.setSelectedText(new String(stuff));
			editor.stopCompoundEdit();
			editor.statusNotice("Now that feels better, doesn't it?");

		} else {
			editor.statusError("No selection, no dice.");
		}
	}
	
}
