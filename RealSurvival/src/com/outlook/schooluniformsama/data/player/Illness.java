package com.outlook.schooluniformsama.data.player;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

public class Illness {
	/**
	 * 记录着一个病种的名字,该病的恢复状态,玩家是否吃了药,药效多少,药效持续时间又是多少
	 */
	private String name;
	private double recovery;
	private double medicineEfficacy;
	private long duratio;
	private boolean isTakeMedicine;
	
	public Illness(String name, double recovery, double medicineEfficacy, long duratio, boolean isTakeMedicine) {
		super();
		this.name = name;
		this.recovery = recovery;
		this.medicineEfficacy = medicineEfficacy;
		this.duratio = duratio;
		this.isTakeMedicine = isTakeMedicine;
	}
	
	public Illness(String name) {
		this.name = name;
		recovery=0;
		medicineEfficacy=0;
		duratio=0;
		isTakeMedicine=false;
	}
	
	public void eatMedicine(double medicineEfficacy, long duratio ){
		this.isTakeMedicine=true;
		this.duratio+=duratio;
		this.medicineEfficacy=(this.medicineEfficacy+medicineEfficacy)*0.5;
	}
	
	public void change(){
		if(recovery>0){
			recovery-=medicineEfficacy*Util.randomNum(0.2, 0.8);
			if(recovery<0)
				recovery=0;			
		}
	}
	
	public boolean subTime(double buff,Player p){
		if(duratio<=0&&recovery>0){
			recovery-=medicineEfficacy*buff*Util.randomNum(0.1, 0.8);
			if(recovery<0)
				recovery=0;
			return false;
		}
		//TODO
		recovery+=medicineEfficacy*buff;
		if(--duratio<=0){
			duratio=0;
			Msg.sendRandomTitleToPlayer(p, "efficacy-over", new String[]{"%sick%"}, new String[]{name}, true);
			isTakeMedicine=false;
		}
		
		if(recovery>=100){
			Msg.sendRandomTitleToPlayer(p, "recovery-sick", new String[]{"%sick%"}, new String[]{name}, true);
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public double getRecovery() {
		return recovery;
	}

	public double getMedicineEfficacy() {
		return medicineEfficacy;
	}

	public long getDuratio() {
		return duratio;
	}

	public boolean isTakeMedicine() {
		return isTakeMedicine;
	}
}
