package com.outlook.schooluniformsama.data.player;

public class Temperature {
	private double temperature;
	private double temperatureBuff=1;
	private int temperatureLevel=0;
	private double addTemperature;
	private double oldTemperature;
	private float effect=0;
	
	public Temperature(double temperature, double temperatureBuff, int temperatureLevel, double addTemperature) {
		super();
		this.temperature = temperature;
		this.temperatureBuff = temperatureBuff;
		this.temperatureLevel = temperatureLevel;
		this.addTemperature = addTemperature;
	}
	
	public void changeEffect(float f){
		effect+=f;
	}
	
	public double getTemperature(){
		return temperature;
	}
	
	public void levelUp(int i){
		temperatureLevel+=i;
		double sum = 0;
		for(int j=1;j<=temperatureLevel;j++){
			sum+=Math.log(j);
		}
		addTemperature=Math.sqrt(sum)*0.05;
	}
	
	public String change(double num){
		oldTemperature = temperature;
		temperature+=num*temperatureBuff+num*effect;
		if(temperature<0)
			temperature=0;
		
		if(oldTemperature>=(36-addTemperature)&&temperature<(36-addTemperature))
			return "cold";
		else if(oldTemperature<=(38+addTemperature)&&temperature>(38+addTemperature))
			return  "fever";
		return null;
	}
	
	private void reCheckLevel(){
		double sum = 0;
		for(int j=1;j<=temperatureLevel;j++){
			sum+=Math.log(j);
		}
		addTemperature=Math.sqrt(sum)*0.05;
	}
	
	public String errorTemperature(){
		if(temperature>(38+addTemperature))
			return "Fever";
		else if(temperature<(36-addTemperature))
			return "Cold";
		return null;
	}

	public double getTemperatureBuff() {
		return temperatureBuff;
	}

	public void setTemperatureBuff(double temperatureBuff) {
		this.temperatureBuff = temperatureBuff;
	}

	public int getTemperatureLevel() {
		return temperatureLevel;
	}

	public void setTemperatureLevel(int temperatureLevel) {
		this.temperatureLevel = temperatureLevel;
		reCheckLevel();
	}

	public double getAddTemperature() {
		return addTemperature;
	}

	public void setAddTemperature(double addTemperature) {
		this.addTemperature = addTemperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getOldTemperature() {
		return oldTemperature;
	}

	public void setOldTemperature(double oldTemperature) {
		this.oldTemperature = oldTemperature;
	}
}
