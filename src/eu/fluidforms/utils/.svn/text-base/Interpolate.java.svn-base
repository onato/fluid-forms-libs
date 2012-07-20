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

/**
 * @author williams
 * @param cp1 is the first controll point
 * @param cp2 is the second controll point
 * @param mu is how far you are from the first to the second control point 0.1-1.0
 */
public class Interpolate {
    public static double Cosine_Interpolate(double controlPoint1, double controlPoint2, double mu) {
        double ft = mu * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5f;

        return controlPoint1 * (1 - f) + controlPoint2 * f;
    }

    public static double Cubic_Interpolate(double v0, double v1, double v2,
            double v3, double x) {
        double P = (v3 - v2) - (v0 - v1);
        double Q = (v0 - v1) - P;
        double R = v2 - v0;
        double S = v1;

        return P * Math.pow(x, 3) + Q * Math.pow(x, 2) + R * x + S;
    }
}