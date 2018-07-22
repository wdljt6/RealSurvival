package com.outlook.schooluniformsama.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.effect.Effect;
import com.outlook.schooluniformsama.data.effect.Food;
import com.outlook.schooluniformsama.data.effect.Mob;
import com.outlook.schooluniformsama.data.item.ItemData;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.data.timer.Timer;
import com.outlook.schooluniformsama.util.ArrayList;
import com.outlook.schooluniformsama.util.HashMap;

public class Data {
	public static String DATAFOLDER;
	public static List<String> worlds;
	/**
	 * randomData &nbsp; 
	 * deathData &nbsp; 
	 * Sleep &nbsp; 
	 * Thirst &nbsp; 
	 * Energy &nbsp; 
	 * Fracture &nbsp; 
	 * Illness &nbsp; 
	 * Weight &nbsp; 
	 * Temperature &nbsp;
	 */
	public static boolean[] switchs = new boolean[9];
	/**
	 * int - min;&nbsp;
	 * int - max;&nbsp;
	 */
	public static int[] randomData;
	/**
	 * sleep(double) &nbsp; 
	 * thirst(double) &nbsp; 
	 * energy(double) &nbsp; 
	 * temperature(double) &nbsp; 
	 * illness(boolean 1=true 0=false) &nbsp;
	 */
	public static double[] deathData;
	/**
	 * max,mid,min,sub,add
	 */
	public static double[] sleep;
	/**
	 * max,mid,min,sub
	 */
	public static double[] thirst;
	/**
	 * max,min,add,sneaking,sprinting,jumping,swimming
	 */
	public static double[] energy;
	/**
	 * slight: high chance <p> severe: high chance <p> damage slight-chance severe-chance
	 */
	public static double[] fracture;
	public static double weight;
	/**
	 * Key: ItemName Value: ItemData
	 */
	public static HashMap<String, ItemData> itemData=new HashMap<>();
	/**
	 * long,width,high,heat-source-fix,distance-effect cold fever
	 */
	public static double[] temperature;
	public static String split;
	public static HashMap<String, String> label=new HashMap<>();
	public static HashMap<String,WorkbenchShape> workbenchs=new HashMap<>();
	public static HashMap<String,Food> foodEffect=new HashMap<>();
	public static HashMap<String, Mob> mobEffect=new HashMap<>();
	public static HashMap<String,ArrayList<Effect>> illnessEffects=new HashMap<>();
	
	public static HashMap<UUID, PlayerData> playerData= new HashMap<>();
	public static HashMap<String, Timer> timer = new HashMap<>();
	public static ArrayList<String>workbenchRecipe=new ArrayList<>();
	public static ArrayList<String>furnaceRecipe=new ArrayList<>();
	
	public static boolean enablePrefixInTitle;
	public static HashMap<String, Integer> strainer=new HashMap<>();
	public static long stateCD;
	/**v1_[0]_R[1]*/
	public static int versionData[];
	
	public static void clear(){
		worlds.clear();
		itemData.clear();
		label.clear();
		workbenchs.clear();
		foodEffect.clear();
		mobEffect.clear();
		illnessEffects.clear();
		playerData.clear();
		timer.clear();
		workbenchRecipe.clear();
		furnaceRecipe.clear();
		strainer.clear();
	}
	
	public static void addPlayer(Player p){
		if(enableInWorld(p.getWorld().getName())&&!p.hasMetadata("NPC")&&!enableInPlayer(p.getUniqueId()))
			addPlayer(p.getUniqueId(), PlayerData.load(p.getUniqueId()));
	}
	
	public static boolean enableInWorld(String worldName){
		return worlds.contains(worldName.toLowerCase());
	}
	
	public static boolean enableInPlayer(UUID uuid){
		return playerData.containsKey(uuid);
	}
	
	public static PlayerData getPlayerData(UUID uuid){
		if(enableInPlayer(uuid))return playerData.get(uuid);
		return null;
	}
	
	public static void removePlayer(UUID uuid){
		if(!enableInPlayer(uuid))return;
		playerData.get(uuid).save();
		playerData.remove(uuid);
	}
	
	public static void saveAllPlayerData(){
		for(PlayerData pd : Data.playerData.values())
			pd.save();
	}
	
	public static void addPlayer(UUID uuid,PlayerData pd){
		if(pd.isUnlimited())return;
		Data.playerData.put(uuid, pd);
	}
}
