package com.outlook.schooluniformsama.data.recipes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.gui.Furnace;

public class FurnaceRecipe extends Recipe{
	private int saveTime;
	private double minTemperature;
	private double maxTemperature;
	private HashMap<Character,ItemStack> materials;
	private ItemStack[] product;
	
	public FurnaceRecipe(String name,int needTime,int saveTime,double minTemperature,
			double maxTemperature,List<String> shape, HashMap<Character, ItemStack> materials,ItemStack[] product,String tableType){
		super(name, WorkbenchType.FURNACE,shape,needTime,tableType);
		this.materials = materials;
		this.product = product;
		this.saveTime=saveTime;
		this.minTemperature=minTemperature;
		this.maxTemperature=maxTemperature;
		
	}
	
	public FurnaceRecipe(String name,int needTime,int saveTime,double minTemperature,double maxTemperature,String tableType){
		super(name,WorkbenchType.FURNACE,needTime,tableType);
		this.saveTime=saveTime;
		this.minTemperature=minTemperature;
		this.maxTemperature=maxTemperature;
		
	}
	
	public static String getRecipePath(String recipeName){
		return Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+recipeName+".yml";
	}
	
	public static  String getRecipePath(){
		return Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator;
	}
	
	public boolean save(){
		YamlConfiguration recipe;
		 File f=new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+name+".yml");
		if(!f.exists())try {f.createNewFile();} catch (IOException e1) {return false;}
		
		String saveName;
		if(name.contains("/"))saveName=name.substring(name.lastIndexOf("/")+1);
		else if(name.contains("\\"))saveName=name.substring(name.lastIndexOf("\\")+1);
		else saveName = name;
		
		recipe=YamlConfiguration.loadConfiguration(f);
		recipe.set(saveName+".type", type.name());
		recipe.set(saveName+".name", name);
		recipe.set(saveName+".needTime", needTime);
		recipe.set(saveName+".saveTime", saveTime);
		recipe.set(saveName+".minTemperature", minTemperature);
		recipe.set(saveName+".maxTemperature", maxTemperature);
		recipe.set(saveName+".shape", shape);
		recipe.set(saveName+".tableType", tableType);
		for(Character c:materials.keySet())
			recipe.set(saveName+".material."+c,RSItem.loadItem(materials.get(c)).getSaveItem() );
		for(int i=0;i<3;i++)
				recipe.set(saveName+".product."+i, RSItem.loadItem(product[i]).getSaveItem());
		try {recipe.save(f);} catch (IOException e) {return false;}
		return true;
	}
	
	public static FurnaceRecipe load(String name){
		 File f=new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+name+".yml");
		 if(!f.exists())return null;
		 
		 String saveName;
		if(name.contains("/"))saveName=name.substring(name.lastIndexOf("/")+1);
		else if(name.contains("\\"))saveName=name.substring(name.lastIndexOf("\\")+1);
		else saveName = name;
		
		 YamlConfiguration recipe=YamlConfiguration.loadConfiguration(f);
		 HashMap<Character,ItemStack> materials=new HashMap<>();
		 char c=' ';
		 for(String s:recipe.getStringList(saveName+".shape"))
			 for(char temp:s.toCharArray())
				 if(temp!=' '&&temp!=c){
					 c=temp;
					 materials.put(c, RSItem.loadItem(recipe.getItemStack(saveName+".material."+c)).getItem());
				 }
		 ItemStack[] product=new ItemStack[4];
		 for(int i=0;i<3;i++)
			 product[i]=RSItem.loadItem(recipe.getItemStack(saveName+".product."+i)).getItem();
		 
		 return new FurnaceRecipe(name,recipe.getInt(saveName+".needTime"),recipe.getInt(saveName+".saveTime"),
				 recipe.getDouble(saveName+".minTemperature"),recipe.getDouble(saveName+".maxTemperature"),
				 recipe.getStringList(saveName+".shape"), materials, product,recipe.getString(saveName+".tableType"));
	}
	
	public static boolean createFurnaceRecipe(Inventory inv,FurnaceRecipe fr){
		short index=0;
		char charArray[]="ABCDEFGHIJKLMNOP".toCharArray();
		String s="";
		HashMap<ItemStack,Character> m=new HashMap<>();
		ItemStack is=inv.getItem(Furnace.mSlot.get(0));
		if(new File(Data.DATAFOLDER+File.separator+"recipe"+File.separator+"furnace"+File.separator+fr.name+".yml").exists())
			return false;
		
		if(is!=null)
			m.put(is,charArray[index]);

		for(int i:Furnace.mSlot){
			ItemStack temp=inv.getItem(i);
			if(temp==null){
				s+=" ";
				continue;
			}
			if(!temp.equals(is)){
				if(m.containsKey(temp))
					s+=m.get(temp);
				else{
					is=temp;
					index++;
					s+=charArray[index];
					m.put(is,charArray[index]);
				}
			}else{
				s+=charArray[index];
			}
		}
		
		HashMap<Character,ItemStack> m2=new HashMap<>();
		for(Entry<ItemStack, Character> entity:m.entrySet())
			m2.put(entity.getValue(), entity.getKey());
		
		FurnaceRecipe wr=new FurnaceRecipe(fr.name ,fr.needTime,fr.saveTime,fr.minTemperature,fr.maxTemperature,
				Arrays.asList(s.substring(0,3),s.substring(3, 6)),m2, new ItemStack[]{inv.getItem(Furnace.pSlot.get(0)),
						inv.getItem(Furnace.pSlot.get(1)),inv.getItem(Furnace.pSlot.get(2))},fr.tableType);
		return wr.save();
		
	}
	
	public boolean containsShape(Inventory inv){
		/*
		short index=0;
		char charArray[]="ABCDEFGHIJKLMNOP".toCharArray();
		String s="";
		HashMap<ItemStack,Character> m=new HashMap<>();
		ItemStack is=inv.getItem(Furnace.mSlot.get(0));
		if(is!=null)
			m.put(is,charArray[index]);

		for(int i:Furnace.mSlot){
			ItemStack temp=inv.getItem(i);
			if(temp==null){
				s+=" ";
				continue;
			}
			if(!temp.equals(is)){
				if(m.containsKey(temp))
					s+=m.get(temp);
				else{
					is=temp;
					index++;
					s+=charArray[index];
					m.put(is,charArray[index]);
				}
			}else{
				s+=charArray[index];
			}
		}
		
		if(!shape.equals(Arrays.asList(s.substring(0,3),s.substring(3, 6))))return null;
		if(materials.size()!=m.size())return null;
		HashMap<Character,ItemStack> m2=new HashMap<>();
		for(Entry<ItemStack, Character> entity:m.entrySet())
			m2.put(entity.getValue(), entity.getKey());
		if(!materials.equals(m2))return null;
		return this;*/
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' ')
					if(inv.getItem(Furnace.mSlot.get(index++))==null)
						continue;
					else 
						return false;
				else{
					ItemStack is=inv.getItem(Furnace.mSlot.get(index++));
					if(is==null)return false;
					if(is.equals(materials.get(c)))
						continue;
					else 
						return false;
				}
			}
		return true;
	}
	
	public boolean isTrue(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' ')
					if(inv.getItem(Furnace.mSlot.get(index++))==null)
						continue;
					else 
						return false;
				else{
					ItemStack is=inv.getItem(Furnace.mSlot.get(index++));
					if(is==null)return false;
					if(is.equals(materials.get(c)))
						continue;
					else 
						return false;
				}
			}
		return true;
	}
	
	public Inventory setInv(Inventory inv){
		short index=0;
		for(String s:shape)
			for(char c:s.toCharArray()){
				if(c==' '){
					index++;
					continue;
				}
				inv.setItem(Furnace.mSlot.get(index++), materials.get(c));
			}
		index=0;
		while(index!=3)
			inv.setItem(Furnace.pSlot.get(index), product[index++]);
		inv.setItem(4, Items.createPItem((short)15, " "));
		return inv;
	}
	
	public int getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(int saveTime) {
		this.saveTime = saveTime;
	}

	public double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public HashMap<Character, ItemStack> getMaterials() {
		return materials;
	}

	public void setMaterials(HashMap<Character, ItemStack> materials) {
		this.materials = materials;
	}

	public ItemStack[] getProduct() {
		return product;
	}

	public void setProduct(ItemStack[] product) {
		this.product = product;
	}

	public String getPermission(){
/*		if(name.contains("/"))saveName=name.substring(name.lastIndexOf("/")+1);
		else if(name.contains("\\"))saveName=name.substring(name.lastIndexOf("\\")+1);
		else saveName = name;*/
		if(name.contains("/"))
			return "RealSurvival.Recipe.Furnace."+name.replace("/", ".").replace("..", ".");
		else if(name.contains("\\"))
			return "RealSurvival.Recipe.Furnace."+name.replace("\\", ".").replace("..", ".");
		else
			return "RealSurvival.Recipe.Furnace."+name;
	}
	
}
