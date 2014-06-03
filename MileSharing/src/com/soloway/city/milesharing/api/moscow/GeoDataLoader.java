package com.soloway.city.milesharing.api.moscow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

public class GeoDataLoader {
	
	public static void loadBusStations(Activity context){
		
		SQLiteDatabase db;
		
		
		
		
        db = context.openOrCreateDatabase(
            "geodata.db"
            , SQLiteDatabase.CREATE_IF_NECESSARY
            , null
            );

        db.execSQL("DROP TABLE IF EXISTS BusStationsData;");
        
        final String Create_BusStops =
            "CREATE TABLE IF NOT EXISTS  BusStationsData ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "Name TEXT," 
            + "LAT REAL NOT NULL," 
            + "LNG REAL NOT NULL);";

        db.execSQL(Create_BusStops);  
        
		
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(
		        new InputStreamReader(context.getAssets().open("busstations.csv")));

		    
		    String line;
		    line = reader.readLine();
	        while ((line = reader.readLine()) != null) {
		             String[] RowData = line.split(";");
		             String num = RowData[0];
		             String name = RowData[1];
		             String Lat = RowData[3];
		             String Lon = RowData[2];
		             final String Insert_Data="INSERT INTO BusStationsData VALUES("+num+",'"+name+"',"+Lat+","+Lon+")";
		             db.execSQL(Insert_Data);
	        	 
		    }
	        db.close();
		} catch (IOException e) {
		    //log the exception
		} finally {
		    if (reader != null) {
		        try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
		    }
		}
	}
	
	
	public static void loadTubeStations(Activity context){
		
		SQLiteDatabase db;

        db = context.openOrCreateDatabase(
            "geodata.db"
            , SQLiteDatabase.CREATE_IF_NECESSARY
            , null
            );
        
        db.execSQL("DROP TABLE IF EXISTS TubeStationsData;");
        
        final String Create_TubeStops =
            "CREATE TABLE IF NOT EXISTS TubeStationsData ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "Name TEXT," 
            + "LAT REAL NOT NULL," 
            + "LNG REAL NOT NULL);";

        db.execSQL(Create_TubeStops);
		
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(
		        new InputStreamReader(context.getAssets().open("tubestations.csv")));

		    
		    String line;
		    line = reader.readLine();
	        while ((line = reader.readLine()) != null) {
		             String[] RowData = line.split(";");
		             String num = RowData[0];
		             String name = RowData[1];
		             String Lat = RowData[3];
		             String Lon = RowData[2];
			       
		             final String Insert_Data="INSERT INTO TubeStationsData VALUES("+num+",'"+name+"',"+Lat+","+Lon+")";
		             db.execSQL(Insert_Data);
	        	
		    }
	        db.close();
	        
		} catch (IOException e) {
		    //log the exception
		} finally {
		    if (reader != null) {
		        try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
		    }
		}
	}
	
	
}
