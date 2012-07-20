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
 
package eu.fluidforms.geom;

/**
 * Point is used to represent a point in 2 dimensional space.
 * 
 * @author Stephen Williams stephen.williams@algodes.com
 */
public class Point {
	/**
	 * The coordinates of the point.
	 */
	private float x, y;

	/**
	 */
	public Point() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * @param xPos
	 *            The x position of the point as a float.
	 * @param yPos
	 *            The y position of the point as a float.
	 */
	public Point(final float xPos, final float yPos) {
		this.x = xPos;
		this.y = yPos;
	}

	/**
	 * @param xPos
	 *            The x position of the point as a int.
	 * @param yPos
	 *            The y position of the point as a int.
	 */
	public Point(final int xPos, final int yPos) {
		this((float) xPos, (float) yPos);
	}

	/**
	 * @param xPos
	 *            The x position of the point as a float.
	 * @param yPos
	 *            The y position of the point as a float.
	 */
	public final void setLocation(final float xPos, final float yPos) {
		setX(xPos);
		setY(yPos);
	}

	/**
	 * @return x
	 */
	public final float getX() {
		return x;
	}
	
	/**
	 * Sets the x position of the point.
	 * @param xPos	The x position of the point.
	 */
	public final void setX(final float xPos) {
		this.x = xPos;
	}

	/**
	 * @return y
	 */
	public final float getY() {
		return y;
	}
	
	/**
	 * Sets the y position of the point.
	 * @param yPos 	The y position of the point.
	 */
	public final void setY(final float yPos) {
		this.y = yPos;
	}

}
