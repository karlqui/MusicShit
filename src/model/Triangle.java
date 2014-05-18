package model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Triangle extends Polygon{
	
	public Triangle(int x, int y, int size) {
		int [] xPoints = new int[3];
		int [] yPoints = new int[3];
			xPoints[0] = x;
			yPoints[0] = y;
			xPoints[1] = x;
			yPoints[1] = y-size;
			xPoints[2] = x-size;
			yPoints[2] = y;

		super.npoints = 3;
		super.xpoints = xPoints;
		super.ypoints = yPoints;
	}	
	

}
