/*
  (c) copyright
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */
 
package eu.fluidforms.io;

public class RawExporter extends BinOrAsciiWriter implements Exporter{
	public RawExporter(String fileName){
		super(fileName, true);
	}
	
    public void beginShape(){
    	
    }
    public void endShape(){
    	closeWriter();
    }
    
    public void addPixel(int x, int y, float value){
		write(floatToBytes4(value));
    }

    public void write(){
    	
    }
}
