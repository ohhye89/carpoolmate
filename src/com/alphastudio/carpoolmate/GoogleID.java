package com.alphastudio.carpoolmate;

public class GoogleID {
	private static String googleID="";
	
	public static void setID(String userID) {
		GoogleID.googleID = userID;
	}
	public static String getID() {
		return googleID;
	}
}
