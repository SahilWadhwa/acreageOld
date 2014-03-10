package com.deere.acreage;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class CalcArea {

	public static double calcArea(List<Location> vertices) {
		
		if (vertices.size() < 2) {
			return 0.0;
		}
		double radius = 6378137;
		double circumference = radius * 2 * 22 / 7;

		if ((vertices.get(0).getLatitude() != vertices.get(
				vertices.size() - 1).getLatitude())
				|| (vertices.get(0).getLongitude() != vertices.get(
						vertices.size() - 1).getLongitude())) {

			vertices.add(vertices.get(0));
		}

		List<CartesianCoordinates> coordinates = new ArrayList<CartesianCoordinates>();
		//GeoPoint baseVertex = vertices.get(0);
		Location baseVertex=vertices.get(0);
		for (Location vertex : vertices)
		{
			double x = (vertex.getLatitude() - baseVertex.getLatitude()) / 360 * circumference;
			double y = (vertex.getLongitude() - baseVertex.getLongitude())/ 360 * circumference; 
			coordinates.add(new CartesianCoordinates(x, y));
		}
		
		double sum = 0;
		
		for(int i=0; i<(coordinates.size() -1); i++){
			double x1 =  coordinates.get(i).getX();
			double x2 =  coordinates.get(i+1).getX();
			double y1 =  coordinates.get(i).getY();
			double y2 =  coordinates.get(i+1).getY();
			sum = sum + ((x1*y2) - (x2*y1));
		}
		double area = sum/2;
		if(area < 0 ){
			area = area * (-1);
		}
		
		System.out.println(area);
		return area;

	}

}
