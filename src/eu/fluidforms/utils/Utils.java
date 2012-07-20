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
 
package eu.fluidforms.utils;

import java.io.*;
import java.util.Vector;



import eu.fluidforms.geom.*;
/**
 * @author williams
 * 
 * 
 * 
 */
public class Utils {
	public static String getFileExtension(String fileName){
		return fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase();
	}
	public static float round(float val, int places) {
		// Round to two decimal places.
		float roundDist = val * (float) Math.pow(10, places);
		roundDist = Math.round(roundDist);
		roundDist = roundDist / (float) Math.pow(10, places);
		return roundDist;
	}

	public static byte[] toBytes4(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n);
		n >>>= 8;
		b[1] = (byte) (n);
		n >>>= 8;
		b[2] = (byte) (n);
		n >>>= 8;
		b[3] = (byte) (n);

		return b;
	}

	public static float noise(int x, int i) {
		x = (x << 13) ^ x;
		return Math.abs(baseNoise(x, i));
	}

	public static float noise(int x, int y, int i) {
		int n = x + y * 57;
		n = (n << 13) ^ n;
		return Math.abs(baseNoise(n, i));
	}

	static float baseNoise(int x, int i) {

		switch (i) {
			case 0 :
				return (1.0f - ((x * (x * x * 15013 + 700001) + 1376310601) & 0x7fffffff) / 1073740847.0f);
			case 1 :
				return (1.0f - ((x * (x * x * 15149 + 700211) + 1376310953) & 0x7fffffff) / 1073740933.0f);
			case 2 :
				return (1.0f - ((x * (x * x * 15289 + 700391) + 1376311213) & 0x7fffffff) / 1073741077.0f);
			case 3 :
				return (1.0f - ((x * (x * x * 15427 + 700627) + 1376311487) & 0x7fffffff) / 1073741197.0f);
			case 4 :
				return (1.0f - ((x * (x * x * 15583 + 700849) + 1376311921) & 0x7fffffff) / 1073741287.0f);
			case 5 :
				return (1.0f - ((x * (x * x * 15733 + 701047) + 1376312323) & 0x7fffffff) / 1073741371.0f);
			case 6 :
				return (1.0f - ((x * (x * x * 15881 + 701341) + 1376312579) & 0x7fffffff) / 1073741419.0f);
			case 7 :
				return (1.0f - ((x * (x * x * 16057 + 701497) + 1376310613) & 0x7fffffff) / 1073741503.0f);
			default :
				return (1.0f - ((x * (x * x * 16057 + 701497) + 1376310613) & 0x7fffffff) / 1073741503.0f);
		}
	}

	public static float[][] fillMountainRange(float[][] mountainRange) {
		for (int i = 0; i < mountainRange.length; i++) {
			for (int j = 0; j < mountainRange[0].length; j++) {
				mountainRange[i][j] = noise(i, j, 5);
			}
		}
		return mountainRange;
	}

	public static FVertex normalise(FVertex p) {
		float x = p.x;
		float y = p.y;
		float z = p.z;
		float t = (float)Math.sqrt(x * x + y * y + z * z);
		if(t!=0){
			x /= t;
			y /= t;
			z /= t;
		}
		p.x = x;
		p.y = y;
		p.z = z;
		return p;
	}

	public static FVertex crossProduct(FVertex a, FVertex b) {
		float x = a.x;
		float y = a.y;
		float z = a.z;
		float xh = y * b.z - b.y * z;
		float yh = z * b.x - b.z * x;
		float zh = x * b.y - b.x * y;
		a.x = xh;
		a.y = yh;
		a.z = zh;
		return a;
	}

	public static float dot(float[] a, FVertex b) {
		float sum = 0;
		sum += a[0] * b.x;
		sum += a[1] * b.y;
		sum += a[2] * b.z;
		return sum;
	}


	public static void concatFiles(String sSrcDir, String sOutputFile,
			String sFilter) {
		try {

			// String filedir;
			String sLogDir = sSrcDir;
			// Create a file object for your root directory
			File logDir = new File(sLogDir);
			// Get all the files and directory under your diretcory
			File[] strFilesDirs = logDir.listFiles();
			File outputFile = new File(sLogDir + sOutputFile);
			outputFile.delete();
			FileOutputStream out = new FileOutputStream(outputFile, true);
			for (int i = 0; i < strFilesDirs.length; i++) {
				if (strFilesDirs[i].isDirectory()) {
					// System.out.println("Directory: " + strFilesDirs[i]);
				} else if (strFilesDirs[i].isFile()) {
					String fileName = strFilesDirs[i].getName();
					String fileType = fileName.substring(fileName.indexOf("."));
					if (!fileName.equals(sOutputFile)
							&& sFilter.indexOf(fileType) >= 0) {
						System.out.println("copying " + fileName + "...");
						File inputFile = new File(sLogDir + fileName);
						FileInputStream in = new FileInputStream(inputFile);
						// int c;

						byte[] bFile = getBytesFromFile(inputFile);
						// while ((c = in.read()) != -1)
						out.write(bFile);

						in.close();
						// strFilesDirs[i].delete();
					}
				}
			}
			out.close();
			// setScrollbar(1, 70);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * General Bezier curve. Number of control points is n+1 0 <= mu < 1
	 * IMPORTANT, the last point is not computed
	 */

	public static Point Bezier(Vector<Point> controlPoints, int numControlPoints,
			float mu, int offset) {

		if (controlPoints.size() <= 0) {
			System.err
					.println("The controll point array supplied to the Bezier function was empty");
			return new Point(0, 0);
		}

		float blend, muk, munk;
		// Point b = {0.0,0.0,0.0};
		Point b = new Point(0, 0);

		muk = 1;
		munk = (float)Math.pow(1.0f - mu, numControlPoints);
		for (int k = 0; k <= numControlPoints; k++) {
			int nn = numControlPoints;
			int kn = k;
			int nkn = numControlPoints - k;
			blend = muk * munk;
			muk *= mu;
			munk /= (1 - mu);
			while (nn >= 1) {
				blend *= nn;
				nn--;
				if (kn > 1) {
					blend /= (float) kn;
					kn--;
				}
				if (nkn > 1) {
					blend /= (float) nkn;
					nkn--;
				}
			}

			b.setLocation(
					b.getX()
							+ (controlPoints.elementAt(k
									+ offset)).getX() * blend, 
					b.getY()
							+ ((Point) controlPoints.elementAt(k
									+ offset)).getY() * blend);
			// b.z += p[k].z * blend;
		}

		return (b);
	}

	// General Bezier curve
	// Number of control points is n+1
	// 0 <= mu < 1 IMPORTANT, the last point is not computed

	public static Point getBezierPoint(Point[] controlPoints,
			int numControlPoints, float mu, int offset) {

		if (controlPoints.length <= 0) {
			System.err
					.println("The controll point array supplied to the Bezier function was empty");
			return new Point(0, 0);
		}

		float blend, muk, munk;
		// Point b = {0.0,0.0,0.0};
		Point b = new Point(0, 0);

		muk = 1;
		munk = (float)Math.pow(1.0f - mu, numControlPoints);
		for (int k = 0; k <= numControlPoints; k++) {
			int nn = numControlPoints;
			int kn = k;
			int nkn = numControlPoints - k;
			blend = muk * munk;
			muk *= mu;
			munk /= (1 - mu);
			while (nn >= 1) {
				blend *= nn;
				nn--;
				if (kn > 1) {
					blend /= (float) kn;
					kn--;
				}
				if (nkn > 1) {
					blend /= (float) nkn;
					nkn--;
				}
			}
			b.setLocation(b.getX() + controlPoints[k + offset].getX() * blend,
					b.getY() + controlPoints[k + offset].getY() * blend);
			// b.z += p[k].z * blend;
		}

		return (b);
	}

	
	public static float distance(Point p1, Point p2){
		return (float) Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) 
				+ Math.pow(p1.getY() - p2.getY(), 2));
	}
}
