package com.github.otrebor4.simulator.properties;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.otrebor4.simulator.util.Construction;
import com.github.otrebor4.simulator.util.Vector3;

public class Structure {
	private static final String Path = "plugins/Simulator/Structure/";
	private static final String fileName = "Structure.txt";
	
	public static Construction getConstruction(){
		Construction data = new Construction();
		int x = 0, y = 0, z = 0;
		
		if(!fileExist(Path + fileName) ){
			defaulStructure( fileName, Path);
		}
		try {
		    BufferedReader in = new BufferedReader(new FileReader(Path + fileName));
		    while(in.ready())
		    {
		    	String sourceLine = in.readLine();
		    	//empty line start new floor.
		    	if(sourceLine.length() == 0 || sourceLine.charAt(0) == '#'){
		    		x = 0;
		    		z = 0;
		    		y++;
		    		continue;
		    	}
		    	
			    String [] tokens = sourceLine.split(",");
			    for(String val : tokens){
			    	//Messaging.log("StringVal = " + val);
			    	
			    	Integer blockid = Integer.decode( val);
			    	if( blockid != null && blockid != 0){
			    		Vector3 pos = new Vector3(x, y, z );
			    		
			    		data.addBlock( pos , blockid);
			    	}
			    	x++;
			    }
			    z++;
			    x = 0;
		    }
		    in.close();
		} catch (IOException e) {}
			
		
		
		return data;
	}
	
	public static boolean fileExist( String path){
		try {
			@SuppressWarnings("unused")
			FileReader file = new FileReader(path);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		return true;
	}
	
	public static void defaulStructure(String fileName, String dir){
		try {
			String path = dir + fileName;
			File dire = new File(dir);
			File file = new File(path);
			if(!file.exists()){
				dire.mkdirs();
				file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter( new FileWriter(file) );
			String data = "";
			//set up walls 
			for(int y = 0; y < 5; y++){
				for(int z = 0; z < 5; z++){
					for(int x = 0; x < 5; x++){
						if( y == 0){
							if(z == 0){
								data += "04";
							}
							else if(z == 4){
								if( x == 2){
									data +="00";
								}
								else{
									data += "04";
								}
							}
							else{
								if(x == 0 || x == 4){
									data += "04";
								}else{
									data += "00";
								}
							}
						}
						else if( y == 1){
							if( z == 0 || z == 4 ){
								if( x == 2){
									data +="00";
								}
								else{
									data += "04";
								}
							}
							else if( z != 2){
								if(x == 0 || x == 4){
									data += "04";
								}else{
									data += "00";
								}
							}
							else {
								data += "00";
							}
						}
						else if( y == 4){
							data += "04";
						}
						else{
							if( z == 0 || z == 4 ){
								data += "04";
							}
							else {
								if(x == 0 || x == 4){
									data += "04";
								}else{
									data += "00";
								}
							}
						}
						data += ",";
					}
					data += "\n";
				}
				data += "\n";
			}
			out.write(data);
			out.close();
			
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
}
