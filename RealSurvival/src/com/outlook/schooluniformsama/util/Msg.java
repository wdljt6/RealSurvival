package com.outlook.schooluniformsama.util;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.Data;

public class Msg {
	private static YamlConfiguration msg;
	/**
	 * You must run this method when the plugin is loaded.
	 */
	public static void init(){
		msg=YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/messages.yml"));
	}
	
	public static String getPrefix(){
		return msg.getString("prefix");
	}
	
	/**
	 * Get a message from messages.yml. you can let some variable change to what string you want.
	 * The variable and value are string array. You must let the variable and value at the same index, 
	 * for example: a message's id is "hello" and the message is "Hello %player%! now is %time%" -> 
	 * getRandomMsg(hello,new String[]{"%player%","%time%"},new String[]{"School_Uniform","14:36"},false);
	 * so you will get a string that is "Hello School_Uniform! now is 14:36"
	 * @param id
	 * @param variable
	 * @param value
	 * @param addPrefix
	 * @return
	 */
	public static String getRandomMsg(String id,String[] variable ,String[] value,boolean addPrefix){
		List<String> list = msg.getStringList("list."+id);
		String msg = list.get((int)Util.randomNum(0, list.size()-1));
		for(int i=0;i<variable.length;i++)
			msg=msg.replaceAll(variable[i], value[i]);
		if(addPrefix)
			return getPrefix()+msg;
		return msg;
	}
	
	public static String getRandomMsg(String id,boolean addPrefix){
		List<String> list = msg.getStringList("list."+id);
		String msg = list.get((int)Util.randomNum(0, list.size()-1));
		if(addPrefix)
			return getPrefix()+msg;
		return msg;
	}
	
	public static String getMsg(String id,String[] variable ,String[] value,boolean addPrefix){
		String m=msg.getString("line."+id);
		for(int i=0;i<variable.length;i++)
			m=m.replaceAll(variable[i], value[i]);
		if(addPrefix)
			return getPrefix()+m;
		return m;
	}
	
	public static String getMsg(String id,boolean addPrefix){
		String m=msg.getString("line."+id);
		if(addPrefix)
			return getPrefix()+m;
		return m;
	}
	
	public static String getRandomMsgArray(String id,String[] variable ,String[] value,boolean addPrefix){
		String lines="";
		for(String temp:msg.getStringList(id)){
			for(int i=0;i<variable.length;i++)
				temp=temp.replaceAll(variable[i], value[i]);
			lines+=temp+"\n";
		}
		if(addPrefix)
			return getPrefix()+lines.substring(0, lines.length()-2);
		return lines.substring(0, lines.length()-2);
	}

	public static void sendRandomArrayToPlayer(Player p, String id,String[] variable , String[] value,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getRandomMsgArray(id, variable, value, addPrefix));
	}
	
	public static void sendRandomTitleToPlayer(Player p, String id,String[] variable , String[] value,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		String msg=getRandomMsg(id, variable, value, false);
		if(Data.tmapi!=null){
			if(addPrefix)
				Data.tmapi.sendTitles(p,getPrefix(),msg,10,30,10 );
			else
				Data.tmapi.sendTitles(p," ",msg,10,30,10 );
		}else{
			if(addPrefix)
				p.sendMessage(getPrefix()+msg);
			else
				p.sendMessage(msg);
		}
	}
	
	public static void sendRandomTitleToPlayer(Player p, String id,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		String msg = getRandomMsg(id, false);
		if(Data.tmapi!=null){
			if(addPrefix)
				Data.tmapi.sendTitles(p,getPrefix(),msg,10,30,10 );
			else
				Data.tmapi.sendTitles(p," ",msg,10,30,10 );
		}else{
			if(addPrefix)
				p.sendMessage(getPrefix()+msg);
			else
				p.sendMessage(msg);
		}
	}
	
	public static void sendTitleToPlayer(Player p, String id,String[] variable , String[] value,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		String msg = getMsg(id, variable, value, false);
		if(Data.tmapi!=null){
			if(addPrefix)
				Data.tmapi.sendTitles(p,getPrefix(),msg,10,30,10 );
			else
				Data.tmapi.sendTitles(p," ",msg,10,30,10 );
		}else{
			if(addPrefix)
				p.sendMessage(getPrefix()+msg);
			else
				p.sendMessage(msg);
		}
	}
	
	public static void sendTitleToPlayer(Player p, String id,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		String msg = getMsg(id, false);
		if(Data.tmapi!=null){
			if(addPrefix)
				Data.tmapi.sendTitles(p,getPrefix(),msg,10,30,10 );
			else
				Data.tmapi.sendTitles(p," ",msg,10,30,10 );
		}else{
			if(addPrefix)
				p.sendMessage(getPrefix()+msg);
			else
				p.sendMessage(msg);
		}
	}
	
	public static void sendRandomMsgToPlayer(Player p, String id,String[] variable , String[] value,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
			p.sendMessage(getRandomMsg(id, variable, value, addPrefix));
	}
	
	public static void sendRandomMsgToPlayer(Player p, String id,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getRandomMsg(id, addPrefix));
	}
	
	public static void sendRandomMsgToPlayer(String playerName, String id,String[] variable , String[] value,boolean addPrefix){
		Player p = Bukkit.getPlayer(playerName);
		if(!p.isOnline() || id==null)return;
		String msg = getRandomMsg(id, variable, value, addPrefix);
		p.sendMessage(msg);
	}
	
	public static void sendRandomMsgToPlayer(String playerName, String id,boolean addPrefix){
		Player p = Bukkit.getPlayer(playerName);
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getRandomMsg(id, addPrefix));
	}
	
	public static void sendMsgToPlayer(Player p, String id,String[] variable , String[] value,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getRandomMsg(id, variable, value, addPrefix));
	}
	
	public static void sendMsgToPlayer(Player p, String id,boolean addPrefix){
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getMsg(id, addPrefix));
	}
	
	public static void sendMsgToPlayer(String playerName, String id,String[] variable , String[] value,boolean addPrefix){
		Player p = Bukkit.getPlayer(playerName);
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getMsg(id, variable, value, addPrefix));
	}
	
	public static void sendMsgToPlayer(String playerName, String id,boolean addPrefix){
		Player p = Bukkit.getPlayer(playerName);
		if(!p.isOnline() || id==null)return;
		p.sendMessage(getMsg(id,  addPrefix));
	}
	
}