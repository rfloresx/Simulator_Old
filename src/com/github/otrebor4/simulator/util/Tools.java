package com.github.otrebor4.simulator.util;

public class Tools {
	
	public enum ToolType {none, axe, pickaxe, shovel, sword}
	public enum ToolMaterial { none, wood, stone, iron ,diamond, gold}
	private static float [] Multi = {1, 2, 4, 6, 8, 12};
	
	//blocks ids that are affected by tool breaking.
	private static int [] blockidAxe = { 96, 64, 54, 58, 85, 107, 84, 17, 5, 125, 126, 47, 91, 63, 323,  25, 72, 99, 100  };
	private static int [] blockidSword = {26, 77, 81, 92, 30, 127,  20, 102, 89, 65, 69, 103, 33, 9, 123, 124  };
	private static int [] blockidShovel = {82, 03, 31, 13, 110, 12, 78, 80, 88 };
	private static int [] blockidPickaxe = { 49, 130, 57, 133, 42, 116, 101, 71, 23, 61, 62, 41, 16, 56, 122, 129, 121, 14, 15, 
		22, 21, 73, 74, 108, 109, 114, 128, 45, 118, 04, 48, 112, 113, 43, 44, 01, 98, 24, 66, 27, 28, 117, 79, 70, 87};
	
	public static ToolMaterial getMaterial( int id){
		if(id >= 256 && id <= 258 || id == 267 || id == 292)
			return ToolMaterial.iron;
		else if(id >= 268 && id <= 271 || id == 290 )
			return ToolMaterial.wood;
		else if(id >= 272 && id <= 275 || id == 291  )
			return ToolMaterial.stone;
		else if(id >= 276 && id <= 279 || id == 293 )
			return ToolMaterial.diamond;
		else if(id >= 283 && id <= 286  || id == 294 )
			return ToolMaterial.gold;
		return ToolMaterial.none;
	}
	
	public static ToolType getType(int id){
		int [] shovels = { 269,  273, 256, 284, 277 };
		int [] axes = { 271, 275, 258, 286, 279 };
		int [] pickaxe = { 270, 274, 257, 285, 278 };
		int [] swords = { 268, 283, 272, 267, 276 };
		for( int i : shovels){
			if(i == id)
				return ToolType.shovel;
		}
		for( int i : axes){
			if(i == id)
				return ToolType.axe;
		}
		for( int i : pickaxe){
			if(i == id)
				return ToolType.pickaxe;
		}
		for( int i : swords){
			if(i == id)
				return ToolType.sword;
		}
		return ToolType.none;
	}
	
	public static boolean isFast( int blockid, ToolType tool){
		switch(tool){		
		case axe:
			for(int id : blockidAxe){
				if(id == blockid)
					return true;
			}
		case sword:
			for(int id : blockidSword){
				if(id == blockid)
					return true;
			}
		case shovel:
			for(int id : blockidShovel){
				if(id == blockid)
					return true;
			}
		case pickaxe:
			for(int id : blockidPickaxe){
				if(id == blockid)
					return true;
			}
		case none:
			return false;
		}
		return false;
	}
	
	public static float getDameByTool( int blockid, int toolid ){
		ToolType type =  getType(toolid);
		
		if(isFast(blockid, type)){
			if(type == ToolType.pickaxe ){
				return getDameByPickaxe(blockid, toolid);
			}
			else if(type ==ToolType.sword){
				//sword don't follow the basic damage by material
				return 1.5f;
			}
			else{
				int index = getMaterial(toolid).ordinal();
				if(index >= 0 && index < Multi.length)
					return Multi[index];
			}
		}else if( type == ToolType.none){
			int [] handUnharver = { 78, 80 };
			for(int id : blockidPickaxe){
				if(id == blockid)
					return 1/3.333f;
			}
			for( int id : handUnharver){
				if(id == blockid)
					return 1/3.333f;
			}
		}
		
		return 1;
	}
	
	public static float getDameByPickaxe(int block, int tool){
		ToolMaterial m = getMaterial(tool);
		int NI = 49;
		int [] NS = { 57, 41, 56, 129, 14, 73, 74};
		int [] WG = { 42, 15, 22, 21};
		
		int index = getMaterial(tool).ordinal();
		if(m != ToolMaterial.diamond){
			if(block == NI)
				return (1/3.33f);
			if(m != ToolMaterial.iron){
				for(int i = 0; i < NS.length; i++){
					if(block == NS[i])
						return (1/3.33f);
				}
				if(m != ToolMaterial.stone){
					for(int i = 0; i < WG.length; i++){
						if(block == WG[i])
							return (1/3.33f);
					}
				}
			}
		}
		if(index >= 0 && index < Multi.length)
			return Multi[index];
		return Multi[0];
	}

	public static float getDurability(int blockid, int toolid ){
		float baseTime = -1;
		net.minecraft.server.Block block = net.minecraft.server.Block.byId[blockid];
		if(block != null)
			baseTime = block.m(null, 0, 0, 0) * 1.5f;
		
		return baseTime/getDameByTool(blockid, toolid);
	}
	
	
}
