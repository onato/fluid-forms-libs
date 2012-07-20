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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import eu.fluidforms.utils.Utils;

public abstract class BinOrAsciiWriter {
	String namePrepend = "";

	private String fileName = "algodes";

	private File file;

	private PrintWriter textWriter;

	private OutputStream binaryWriter;

	private RandomAccessFile randomAccess;

	private boolean binary = false;

	public BinOrAsciiWriter() {
		this(true);
	}
	public BinOrAsciiWriter(boolean binary) {
		setBinary(binary);
	}
	public BinOrAsciiWriter(String path, boolean binary) {
		this(binary);
		setFileName(path);
		if (path != null) {
			file = new File(path);
		}
		if (file == null) {
			throw new RuntimeException("Couldn't write to " + path);
		}
		try {
			if (binary) {
				binaryWriter = new BufferedOutputStream(new FileOutputStream(
						file), 32768);
			} else {
				textWriter = new PrintWriter(new FileWriter(file));
			}
		} catch (IOException e) {
			throw new RuntimeException(e); // java 1.4+
		}

	}

	public void openReadWrite() {
		try {
			randomAccess = new RandomAccessFile(file, "rw");
		} catch (IOException e) {
			throw new RuntimeException(e); // java 1.4+
		}
	}

	public void dispose() {
		closeWriter();
	}
	protected void println(String line) {
		textWriter.write(line + "\n");
	}
	protected void print(String line) {
		textWriter.write(line);
	}

	public abstract void write();

	protected void write(byte[] b) {
		try {
			binaryWriter.write(b);
		} catch (IOException e) {
			throw new RuntimeException(e); // java 1.4+
		}
	}
	protected void write(byte[] b, int off, int len) {
		try {
			if (binaryWriter != null) {
				binaryWriter.write(b, off, len);
			} else {
				randomAccess.write(b, off, len);
			}
		} catch (IOException e) {
			throw new RuntimeException(e); // java 1.4+
		}
	}

	protected PrintWriter getWriter() {
		return textWriter;
	}
	protected void closeWriter() {
		if (isBinary()) {
			try {
				if (binaryWriter != null) {
					binaryWriter.flush();
					binaryWriter.close();
					binaryWriter = null;
				}
				if (randomAccess != null) {
					randomAccess.close();
					randomAccess = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (textWriter != null) {
				textWriter.flush();
				textWriter.close();
				textWriter = null;
			}
		}
	}

	public static byte[] floatToBytes4(float f) {
		return toBytes4(Float.floatToIntBits(f));
	}

	public static byte[] toBytes4(int n) {
		byte[] b = new byte[4];
		int offset = 0;
		b[offset + 0] = (byte) ((n & 0x000000ff) >> 0);
		b[offset + 1] = (byte) ((n & 0x0000ff00) >> 8);
		b[offset + 2] = (byte) ((n & 0x00ff0000) >> 16);
		b[offset + 3] = (byte) ((n & 0xff000000) >> 24);

		return b;
	}

	// public static byte[] toBytes4(int n) {
	// byte[] b = new byte[4];
	// b[0] = (byte) (n);
	// n >>>= 8;
	// b[1] = (byte) (n);
	// n >>>= 8;
	// b[2] = (byte) (n);
	// n >>>= 8;
	// b[3] = (byte) (n);
	//
	// return b;
	// }

	public boolean isBinary() {
		return binary;
	}

	public void setBinary(boolean binary) {
		this.binary = binary;
	}

	public String getFileExtension() {
		return Utils.getFileExtension(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNamePrepend() {
		return namePrepend;
	}

	public void setNamePrepend(String namePrepend) {
		this.namePrepend = namePrepend;
	}

	public void setBinaryWriter(OutputStream binaryWriter) {
		this.binaryWriter = binaryWriter;
	}

}
