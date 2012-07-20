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


/**
 * This is a small utility class for working with numbers on different
 * platforms and languages (mainly Java and C/C++). There are methods for
 * converting from/to little/big endians, converting integers and floats to byte
 * arrays etc. 
 **/

public abstract class BitUtils
{
   // -------- Constants -------------

   /**
    * Constant to represent the big endian format (the format used internally
    * in Java).
    **/
   public static boolean BIG_ENDIAN = true;
   /**
    * Constant to represent the little endian format (the format normaly used
    * on PCs).
    **/
   public static boolean LITTLE_ENDIAN = false;

   /**
    * Constructor that suppress a default constructor, to enforce
    * non-instantiability.
    *
    * See: Effective Java item 3
    **/
   private BitUtils()
   {
      // This constructor will never be invoked
   }

   

   /**
    * Returns a String representation of the bits in a byte. The String
    * begins with the most significant bit in the byte. The bits are
    * represented as '0' and '1's in the String.
    *
    * @param n the byte to be converted.
    * @return a string representation of the argument in base 2.
    **/
   public static String toBitString(final byte n)
   {
      final int tmp = n;
      final StringBuffer sb = new StringBuffer(8);
      for (int i = 7; i >= 0; i--) 
      {
         if ((tmp & (1 << i)) >> i == 1) 
         {
            sb.append('1');
         } 
         else 
         {
            sb.append('0');
         }
      }
      return sb.toString();
   }


   /**
    * Returns a String representation of the bits in a byte array. The
    * String begins with the representation of the first byte in the array
    * (byte[0]). The most significant comes first in each byte. The bits are
    * represented by '0' and '1's in the String.
    *
    * @param ba the byte[] to be converted.
    * @return a string representation of the argument in base 2.
    **/
   public static String toBitString(final byte[] ba)
   {
      return toBitString(ba, null);
   }


   /**
    * Returns a String representation of the bits in a byte array. The
    * String begins with the representation of the first byte in the array
    * (byte[0]) Bytes are separated by byteSpace. The most significant comes
    * first in each byte. The bits are represented by '0' and '1's in the
    * String.
    *
    * @param ba the byte[] to be converted.
    * @param byteSpace the spacing to use between each byte.
    * @return a string representation of the argument in base 2.
    **/
   public static String toBitString(final byte[] ba, final String byteSpace)
   {
      final StringBuffer sb = new StringBuffer();
      for (int i = 0; i < ba.length; i++)
      {
         sb.append(toBitString(ba[i]));
         if (byteSpace != null && i < ba.length - 1) 
         {
            sb.append(byteSpace);
         }
      }
      return sb.toString();
   }


   /**
    * Returns a String representation of the bits in a float. The String
    * begins with the representation of the "most" significant bit in the
    * float (the sign bit). The bits are represented by '0' and '1's in the
    * String.
    *
    * @param f the float to be converted.
    * @return a string representation of the argument in base 2.
    **/
   public static String toBitString(final float f)
   {
      return toBitString(toByteArray(f));
   }


   /**
    * Returns a String representation of the bits in a float. The String
    * begins with the representation of the "most" significant bit in the
    * float (the sign bit). The bits are represented by '0' and '1's in the
    * String.
    *
    * @param f the float to be converted.
    * @param byteSpace the spacing to use between each byte.
    * @return a string representation of the argument in base 2.
    **/
   public static String toBitString(final float f, final String byteSpace)
   {
      final byte[] ba = toByteArray(f);
      return toBitString(ba, byteSpace);
   }


   /**
    * Returns a String representation of the bits in an int. The String
    * begins with the representation of the "most" significant bit in the int
    * (the sign bit). The bits are represented by '0' and '1's in the
    * String.
    *
    * Note: The difference between this method and Integer.toBinaryString()
    * is that this one does not remove leading '0's.
    *
    * @param i the int to be converted.
    * @return a string representation of the argument in base 2.
    **/
   public static String toBitString(final int i)
   {
      return toBitString(toByteArray(i));
   }


   /**
    * Converts an int to a byte[4] array. The
    * first byte (0) in the returned array contain the most significant
    * eigth bits of the supplied int, ie big-endian.
    *
    * @param i the int to be converted.
    * @return a byte[4] array.
    **/
   public static byte[] toByteArray(final int i)
   {
      return toByteArray(i, BIG_ENDIAN);
   }


   /**
    * Converts an int to a byte[4] array. If
    * format is true the first byte (0) in the returned array contain the
    * least significant eigth bits of the supplied int, ie little-endian. if
    * format is false the conversion is big-endian.
    *
    * Note: the BIG_ENDIAN and LITTLE_ENDIAN should be used in the
    * format parameter.
    *
    * @param i the int to be converted.
    * @param format the wanted format of the retured byte array.
    * @return a byte[4] array.
    **/
   public static byte[] toByteArray(final int i, final boolean format)
   {
      final byte[] ba = new byte[4];
      if (format) 
      { // little-endian
         ba[0] = (byte) (i & 0x000000ff);
         ba[1] = (byte) ((i & 0x0000ff00) >> 8);
         ba[2] = (byte) ((i & 0x00ff0000) >> (8 * 2));
         ba[3] = (byte) ((i & 0xff000000) >> (8 * 3));
      } 
      else 
      { // big-endian
         ba[0] = (byte) ((i & 0xff000000) >> (8 * 3));
         ba[1] = (byte) ((i & 0x00ff0000) >> (8 * 2));
         ba[2] = (byte) ((i & 0x0000ff00) >> 8);
         ba[3] = (byte) (i & 0x000000ff);
      }
      return ba;
   }


   /**
    * Converts a float to a byte[4] array by placing the bits
    * in the float into bytes and placing them after each other in the same
    * order as Java stores floats, ie big-endian.
    *
    * @param f the float to be converted.
    * @return the resulting value as an byte[4] array.
    **/
   public static byte[] toByteArray(final float f)
   {
      final int i = Float.floatToRawIntBits(f);
      return toByteArray(i);
   }


   /**
    * Converts a float to a byte[4] array by placing the bits
    * in the float into bytes and placing them after each other in the same
    * order as Java stores floats, ie big-endian.
    *
    * Note: the constants BIG_ENDIAN and LITTLE_ENDIAN (format) should be
    * used in the format parameter.
    *
    * @param f the float to be converted.
    * @param format the wanted format of the returned byte array.
    * @return the resulting value as an byte[4] array.
    **/
   public static byte[] toByteArray(final float f, final boolean format)
   {
      final int i = Float.floatToRawIntBits(f);
      return toByteArray(i, format);
   }


   /**
    * Converts an unsigned byte to an int, ie treating the bits in the byte
    * as the low 8 bits in the int (ie the eigth bit is not treated as a
    * sign bit).
    *
    * @param b the byte to convert
    * @return the resulting value as an int.
    **/
   public static int toInt(final byte b)
   {
      return b & 0xff;
   }


   /**
    * Converts a byte[4] array to a integer by placing the bytes after
    * each other. The first byte in the array (byte[0]) becomes the most
    * significant byte in the resulting int ie big-endian
    * conversion.
    *
    * @param ba the array to be converted.
    * @return the resulting value as an int.
    **/
   public static int toInt(final byte[] ba)
   {
      return toInt(ba, 0, BIG_ENDIAN);
   }

   /**
    * Converts a byte[offset+4] array to a integer by placing the bytes
    * after each other. The first byte in the array (byte[offset]) becomes the
    * most significant byte in the resulting int ie big-endian
    * conversion.
    *
    * @param ba the array to be converted.
    * @param offset starting offset in the array to be converted.
    * @return the resulting value as an int.
    **/
   public static int toInt(final byte[] ba, final int offset)
   {
      return toInt(ba, offset, BIG_ENDIAN);
   }

   /**
    * Converts a byte[offset+4] array to a integer by placing the bytes
    * after each other. The first byte in the array (byte[offset]) becomes the
    * most significant byte in the resulting int ie big-endian
    * conversion.
    *
    * @param ba the array to be converted.
    * @param format the format of the byte array.
    * @return the resulting value as an int.
    **/
   public static int toInt(final byte[] ba, final boolean format)
   {
      return toInt(ba, 0, format);
   }

   /**
    * Converts four bytes in a byte array to a integer by placing the
    * bytes after each other. I format is false the offset byte in the
    * array (byte[0]) becomes the most and byte offset+3 the least
    * significant byte in the resulting int, ie big-endian
    * conversion. if format is true the conversion is little-endian.
    *
    * Note: the BIG_ENDIAN and LITTLE_ENDIAN should be used in
    * the format parameter.
    *
    * @param ba the array to be converted.
    * @param offset starting offset in the array to be converted.
    * @param format the byte order of the byte array (see note above).
    * @return the resulting value as an int.
    **/
   public static int toInt(final byte[] ba, final int offset, final boolean format)
   {
      if (format) 
      {
         return
               (ba[0 + offset] & 0xff) |
               ((ba[1 + offset] & 0xff) << 8) |
               ((ba[2 + offset] & 0xff) << (8 * 2)) |
               ((ba[3 + offset] & 0xff) << (8 * 3));
      }
      
      return
            ((ba[0 + offset] & 0xff) << (8 * 3)) |
            ((ba[1 + offset] & 0xff) << (8 * 2)) |
            ((ba[2 + offset] & 0xff) << 8) |
            (ba[3 + offset] & 0xff);
   }


   /**
    * Converts a byte[4] array to a float by
    * placing the bytes after each other and interpreting is according to the
    * IEEE 754 floating-point "single precision" bit layout (the one used
    * by java and several other programming languages).
    *
    * The first byte in the array (byte[0]) becomes the "most" significant
    * byte in bit array (an int) to be converted, ie big-endian
    * interpretation.
    *
    * @param ba the array to be converted.
    * @return the resulting value as an float.
    **/
   public static float toFloat(final byte[] ba)
   {
      return Float.intBitsToFloat(toInt(ba));
   }


   /**
    * Changes the byte order from little-endian to big- endian or vice
    * versa.
    *
    * @param i the int to reverse the byte order of.
    * @return the int i with reversed byte order.
    **/
   public static int reverseByteOrder(final int i)
   {
      final int ri =
            ((i & 0xff000000) >> (8 * 3)) |
            ((i & 0x00ff0000) >> (8)) |
            ((i & 0x0000ff00) << (8)) |
            ((i & 0x000000ff) << (8 * 3));
      return ri;
   }


   /**
    * Changes the byte order from little-endian to big- endian or vice
    * versa.
    *
    * @param f the float to reverse the byte order of.
    * @return the float f with reversed byte order.
    **/
   public static float reverseByteOrder(final float f)
   {
      final int i = Float.floatToRawIntBits(f);
      final int ri =
            ((i & 0xff000000) >> (8 * 3)) |
            ((i & 0x00ff0000) >> (8)) |
            ((i & 0x0000ff00) << (8)) |
            ((i & 0x000000ff) << (8 * 3));
      return Float.intBitsToFloat(ri);
   }


   /**
    * Returns true if the specified bit is set,
    * false otherwise. Bits are numbered 0-15.
    *
    * @param source the int value to check bits on.
    * @param bit the number of the bit to check.
    * @return true if the bit is set, false
    *         otherwise.
    **/
   public static boolean getBit(final int source, final int bit)
   {
      return (source & (1 << bit)) != 0;
   }


   /**
    * Sets the specified bit to 1 if value parameter is true, unsets the
    * bit otherwise. Bits are numbered 0-15.
    *
    * @param source the int value to set bits in.
    * @param bit the number of the bit to set.
    * @param value true if the bit should be set false otherwise.
    * @return an int with the specified bit set/unset.
    **/
   public static int setBit(final int source, final int bit, final boolean value)
   {
      if (value)
      {
         return source | (1 << bit);
      }
      
      return source & ~(1 << bit);
   }


   public static String convertBytesToString(byte[] value) {
       return convertBytesToString(value, value.length);
   }

   public static String convertBytesToString(byte[] value, int len) {
       char[] buff = new char[len+len];
       char[] hex = HEX;
       for (int i = 0; i < len; i++) {
           int c = value[i] & 0xff;
           buff[i+i] = hex[c >> 4];
           buff[i+i+1] = hex[c & 0xf];
       }
       return new String(buff);
   }
   private static final char[] HEX = "0123456789abcdef".toCharArray();
}// BitUtils


