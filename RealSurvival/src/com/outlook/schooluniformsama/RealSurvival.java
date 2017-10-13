package com.outlook.schooluniformsama;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.ItemData;
import com.outlook.schooluniformsama.data.WorkbenchShape;
import com.outlook.schooluniformsama.data.effect.Effect;
import com.outlook.schooluniformsama.data.effect.Food;
import com.outlook.schooluniformsama.data.effect.Mob;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.data.recipe.WorkbenchType;
import com.outlook.schooluniformsama.event.DamageEvent;
import com.outlook.schooluniformsama.event.EnergyEvent;
import com.outlook.schooluniformsama.event.FractureEvent;
import com.outlook.schooluniformsama.event.SitEvent;
import com.outlook.schooluniformsama.event.SleepEvent;
import com.outlook.schooluniformsama.event.ThirstEvent;
import com.outlook.schooluniformsama.event.basic.*;
import com.outlook.schooluniformsama.papi.Papi;
import com.outlook.schooluniformsama.task.EffectTask;
import com.outlook.schooluniformsama.task.EnergyTask;
import com.outlook.schooluniformsama.task.SaveConfigTask;
import com.outlook.schooluniformsama.task.SickTask;
import com.outlook.schooluniformsama.task.SleepTask;
import com.outlook.schooluniformsama.task.TemperatureTask;
import com.outlook.schooluniformsama.task.ThirstTask;
import com.outlook.schooluniformsama.task.WeightTask;
import com.outlook.schooluniformsama.task.WorkbenchTask;
import com.outlook.schooluniformsama.util.ArrayList;
import com.outlook.schooluniformsama.util.Msg;

import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;

public class RealSurvival extends JavaPlugin{
	@Override
	public void onEnable() {
		firstLoad();
		loadConfig();
		registerListeners();
		addPlayers();
		getLogger().info("加载完成");
	}
	
	@Override
	public void onDisable() {
		for(PlayerData pd : Data.playerData.values())
			pd.save();
		SaveConfigTask.saveWorkbench();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return super.onCommand(sender, command, label, args);
	}
	
	private void firstLoad(){
		Data.DATAFOLDER=getDataFolder()+"";
		if(!getDataFolder().exists()) 
	        getDataFolder().mkdir();
		if(!new File(getDataFolder()+File.separator+"playerdata").exists())
			new File(getDataFolder()+File.separator+"playerdata").mkdir();
		if(!new File(getDataFolder()+File.separator+"items").exists())
			new File(getDataFolder()+File.separator+"items").mkdir();
		if(!new File(getDataFolder()+File.separator+"recipe/furnace").exists())
			new File(getDataFolder()+File.separator+"recipe/furnace").mkdirs();
		if(!new File(getDataFolder()+File.separator+"recipe/workbench").exists())
			new File(getDataFolder()+File.separator+"recipe/workbench").mkdirs();
		if(!new File(getDataFolder()+File.separator+"config.yml").exists())
			saveDefaultConfig();
		try{reloadConfig();}catch (Exception e){}
		
		
		if(!new File(getDataFolder()+File.separator+"messages.yml").exists()){
			this.saveResource("lang/"+getConfig().getString("language")+".yml", true);
			new File(getDataFolder()+File.separator+"lang/"+getConfig().getString("language")+".yml").renameTo(
					new File(getDataFolder()+File.separator+"messages.yml"));
		}else if(YamlConfiguration.loadConfiguration(new File(getDataFolder()+File.separator+"messages.yml")).
					getString("language")!=getConfig().getString("language")){
			new File(getDataFolder()+File.separator+"messages.yml").renameTo(
					new File(getDataFolder()+File.separator+YamlConfiguration.loadConfiguration(new File(
							getDataFolder()+File.separator+"messages.yml")).getString("language")+".yml"));
			this.saveResource("lang/"+getConfig().getString("language")+".yml", true);
			new File(getDataFolder()+File.separator+getConfig().getString("language")+".yml").renameTo(
					new File(getDataFolder()+File.separator+"messages.yml"));
		}
		Msg.init();
		
		
		if(!new File(getDataFolder()+File.separator+"timer.yml").exists())
			try {new File(getDataFolder()+File.separator+"timer.yml").createNewFile();}catch (IOException e1) {}
		
	}
	
	private void loadConfig(){
		Data.worlds=getConfig().getStringList("worlds");
		
		Data.switchs[0]=getConfig().getBoolean("create-random-data.enable");
		Data.switchs[1]=getConfig().getBoolean("death.enable");
		Data.switchs[2]=getConfig().getBoolean("state.sleep.enable");
		Data.switchs[3]=getConfig().getBoolean("state.thirst.enable");
		Data.switchs[4]=getConfig().getBoolean("state.energy.enable");
		Data.switchs[5]=getConfig().getBoolean("state.fracture.enable");
		Data.switchs[6]=getConfig().getBoolean("state.illness.enable");
		Data.switchs[7]=getConfig().getBoolean("state.weight.enable");
		Data.switchs[8]=getConfig().getBoolean("state.temperature.enable");
		
		if(Data.switchs[0])
			Data.randomData=new int[]{
					getConfig().getInt("create-random-data.min"), 
					getConfig().getInt("create-random-data.max")};
		
		if(Data.switchs[1])
			Data.deathData=new double[]{
					getConfig().getDouble("death.sleep"),
					getConfig().getDouble("death.thirst"),
					getConfig().getDouble("death.energy"),
					getConfig().getDouble("death.temperature"),
					getConfig().getBoolean("death.illness")?1:2};
		
		if(Data.switchs[2])
			Data.sleep=new double[]{
					getConfig().getDouble("state.sleep.max"),
					getConfig().getDouble("state.sleep.mid"),
					getConfig().getDouble("state.sleep.min"),
					getConfig().getDouble("state.sleep.sub"),
					getConfig().getDouble("state.sleep.add")};
		
		if(Data.switchs[3])
			Data.thirst=new double[]{
					getConfig().getDouble("state.thirst.max"),
					getConfig().getDouble("state.thirst.mid"),
					getConfig().getDouble("state.thirst.min"),
					getConfig().getDouble("state.thirst.sub")};
		
		if(Data.switchs[4])
			Data.energy=new double[]{
					getConfig().getDouble("state.energy.max"),
					getConfig().getDouble("state.energy.min"),
					getConfig().getDouble("state.energy.add"),
					getConfig().getDouble("state.energy.sneaking"),
					getConfig().getDouble("state.energy.sprinting")};
		
		if(Data.switchs[5])
			Data.fracture=new double[]{
					getConfig().getDouble("state.fracture.slight.high"),
					getConfig().getDouble("state.fracture.slight.chance"),
					getConfig().getDouble("state.fracture.severe.high"),
					getConfig().getDouble("state.fracture.severe.chance"),
					getConfig().getDouble("state.fracture.damage"),
					getConfig().getDouble("state.fracture.slight-chance"),
					getConfig().getDouble("state.fracture.severe-chance")};
		
		if(Data.switchs[6])
			Data.defualtIllness=getConfig().getStringList("state.illness.default-illness").toArray(
					new String[getConfig().getStringList("state.illness.default-illness").size()]);
		
		if(Data.switchs[7]){
			Data.weight=getConfig().getDouble("state.weight.max");
			for(String items:getConfig().getStringList("state.weight.item"))
				Data.itemData.put(items.split(":")[0], ItemData.createData(Double.parseDouble(items.split(":")[1]), 0));
		}
		
		if(Data.switchs[8]){
			Data.temperature=new double[]{
					getConfig().getDouble("state.temperature.long"),
					getConfig().getDouble("state.temperature.width"),
					getConfig().getDouble("state.temperature.high"),
					getConfig().getDouble("state.temperature.heat-source-fix"),
					getConfig().getDouble("state.temperature.distance-effect")};
			for(String items:getConfig().getStringList("state.temperature.heat-source"))
				if(Data.itemData.containsKey(items.split(":")[0]))
					Data.itemData.get(items.split(":")[0]).setHeat(Double.parseDouble(items.split(":")[1]));
				else
					Data.itemData.put(items.split(":")[0], ItemData.createData(0,Double.parseDouble(items.split(":")[1])));
		}
		
		Data.split=getConfig().getString("label.split");
		Data.removeChars=getConfig().getStringList("label.remove");
		for(String label:getConfig().getStringList("label.labels"))
			Data.label.put(label.split(":")[0], label.split(":")[1]);

		for(String workbench:getConfig().getStringList("workbenchs.workbenchs-type")){
			String name=workbench.split(":")[0],type=workbench.split(":")[1];
			Data.workbenchs.put(name, new WorkbenchShape(WorkbenchType.valueOf(type), name, 
							getConfig().getString("workbenchs.workbenchs."+name+".title"),
							getConfig().getString("workbenchs.workbenchs."+name+".main-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".left-block"), 
							getConfig().getString("workbenchs.workbenchs."+name+".right-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".up-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".down-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".front-block"),
							getConfig().getString("workbenchs.workbenchs."+name+".behind-block")));
		}
		
		for(String food:getConfig().getStringList("effect.food-effects.foods")){
			Data.foodEffect.put(food,new Food(Material.getMaterial(food),
					getConfig().getString("effect.food-effects.effects."+food+".sleep"),
					getConfig().getString("effect.food-effects.effects."+food+".thirst"),
					getConfig().getString("effect.food-effects.effects."+food+".energy"),
					getConfig().getDouble("effect.food-effects.effects."+food+".temperature"),
					getConfig().getStringList("effect.food-effects.effects."+food+".illnesses"),
					getConfig().getBoolean("effect.food-effects.effects."+food+".has-illness")));
		}
		for(String mob:getConfig().getStringList("effect.mob-effects"))
			Data.mobEffect.put(mob.split(":")[0], new Mob(mob.split(":")[0], mob.split(":")[1]));
		for(String effect:getConfig().getStringList("effect.illness-effects")){
			ArrayList<Effect> l=new ArrayList<>();
			for(String effects:effect.split(":")[1].split(";"))
				l.add(new Effect(effects.split(",")[0], Integer.parseInt(effects.split(",")[1]), Integer.parseInt(effects.split(",")[2])));
			Data.illnessEffects.put(effect.split(":")[0],l );
		}
	}
	
	//Load Online Player's Data
	private void addPlayers(){
		Iterator<? extends Player> ps= Bukkit.getOnlinePlayers().iterator();
	    while (ps.hasNext()){
	    	Player p = (Player)ps.next();
	    	if(Data.worlds.contains(p.getWorld().getName())&&!p.hasMetadata("NPC"))
	    		Data.playerData.put(p.getUniqueId(), PlayerData.load(p.getUniqueId()));
	    }
	}
	
	//Register Listeners
	private void registerListeners(){
		getServer().getPluginManager().registerEvents(new BasicEvent(), this);
		//getServer().getPluginManager().registerEvents(new CraftItemEvent(), this);
		getServer().getPluginManager().registerEvents(new UseItemEvent(), this);
		//getServer().getPluginManager().registerEvents(new CreateWorkbenchEvent(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new EffectTask(this), 20L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorkbenchTask(), 20L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveConfigTask(), 20L, 60*20L);
		
		if(Data.switchs[2]){
			getServer().getPluginManager().registerEvents(new SleepEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SleepTask(), 20L, 1*20L);
		}
		if(Data.switchs[3]){
			getServer().getPluginManager().registerEvents(new ThirstEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ThirstTask(), 20L, 1*20L);
		}
		if(Data.switchs[4]){
			getServer().getPluginManager().registerEvents(new EnergyEvent() , this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new EnergyTask(), 20L, 20L);
		}
		if(Data.switchs[8]){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TemperatureTask(), 20L, 10*20L);
		}
		if(Data.switchs[7]){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WeightTask(), 20L, 1*20L);
		}
		if(Data.switchs[6]){
			getServer().getPluginManager().registerEvents(new DamageEvent(), this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SickTask(), 20L, 1*20L);
		}
		if(Data.switchs[5]){
			getServer().getPluginManager().registerEvents(new FractureEvent(), this);
		}
		
		//PlaceholderAPI
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			new Papi(this).hook();
			getLogger().info("§9[RealSurvival] 成功加载PlaceholderAPI功能! ");
		}
		//TitleManager
		if(Bukkit.getPluginManager().isPluginEnabled("TitleManager")){
			Data.tmapi = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
			getLogger().info("§9[RealSurvival] 成功加载TitleManager功能! ");
		}
		//Chairs
		if(Bukkit.getPluginManager().isPluginEnabled("Chairs")){
			getServer().getPluginManager().registerEvents(new SitEvent(), this);
			getLogger().info("§9[RealSurvival] 成功加载Chairs功能! ");
		}
	}
	
	public static Player getPlayer(UUID uuid){
		return Data.playerData.get(uuid).getPlayer();
	}
	
}
