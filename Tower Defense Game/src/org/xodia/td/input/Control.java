package org.xodia.td.input;

import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.xodia.td.entity.turret.Turret;

// Has Static Keys and can change its keys' key
public final class Control {

	public static String 	CONTROL_MOVE_SCREEN_LEFT = "MoveScreenLeft",
							CONTROL_MOVE_SCREEN_RIGHT = "MoveScreenRight",
							CONTROL_MOVE_SCREEN_UP = "MoveScreenUp",
							CONTROL_MOVE_SCREEN_DOWN = "MoveScreenDown";
	
	public static String 	CONTROL_SELECT_WALL = "SelectWall",
							CONTROL_SELECT_CANON = "SelectCanon",
							CONTROL_SELECT_FROSTBYTE = "SelectFrostByte",
							CONTROL_SELECT_TERA = "SelectTera",
							CONTROL_SELECT_PIDAR = "SelectPIDar",
							CONTROL_SELECT_ANTITROLL = "SelectAntiTroll",
							CONTROL_SELECT_RECYNERGY = "SelectReCyNergy",
							CONTROL_SELECT_MINE = "SelectMine";
	
	public static String 	CONTROL_UPGRADE_CANON = "UpgradeCanon",
							CONTROL_UPGRADE_FROSTBYTE = "UpgradeFrostByte",
							CONTROL_UPGRADE_TERA = "UpgradeTera",
							CONTROL_UPGRADE_PIDAR = "UpgradePIDar",
							CONTROL_UPGRADE_ANTITROLL = "UpgradeAntiTroll",
							CONTROL_UPGRADE_RECYNERGY = "UpgradeReCyNergy";
	
	public static String 	CONTROL_MANUAL_CONTROL_OF_TURRET = "ManualControlOfTurret",
							CONTROL_EXIT_MANUAL_CONTROL_OF_TURRET = "ExitManualControlOfTurret";
	
	public static String	CONTROL_REPAIR_TURRET = "RepairTurret",
							CONTROL_RESTOCK_TURRET = "RestockTurret";
	
	private static Control instance;
	
	private ArrayList<Key> keyList;
	
	private boolean isRebooted;

	private Control(){
		keyList = new ArrayList<Key>();
		
		reboot();
	}
	
	// Reboots or starts up the whole keys
	private void reboot(){
		if(!isRebooted){
			keyList.add(new Key(CONTROL_MOVE_SCREEN_DOWN, Input.KEY_DOWN));
			keyList.add(new Key(CONTROL_MOVE_SCREEN_UP, Input.KEY_UP));
			keyList.add(new Key(CONTROL_MOVE_SCREEN_LEFT, Input.KEY_LEFT));
			keyList.add(new Key(CONTROL_MOVE_SCREEN_RIGHT, Input.KEY_RIGHT));
			
			keyList.add(new Key(CONTROL_MANUAL_CONTROL_OF_TURRET, Input.KEY_C));
			keyList.add(new Key(CONTROL_EXIT_MANUAL_CONTROL_OF_TURRET, Input.KEY_ESCAPE));
			
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_WALL, Input.KEY_1, Turret.WALL));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_CANON, Input.KEY_2, Turret.CANON));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_FROSTBYTE, Input.KEY_3, Turret.FROSTBYTE));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_TERA, Input.KEY_4, Turret.TERA));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_PIDAR, Input.KEY_5, Turret.PIDAR));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_ANTITROLL, Input.KEY_6, Turret.ANTITROLL));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_RECYNERGY, Input.KEY_7, Turret.RECYNERGY));
			keyList.add(new SelectKey<Turret>(CONTROL_SELECT_MINE, Input.KEY_8, Turret.ANTIMINE));
			
			keyList.add(new DualKey(CONTROL_UPGRADE_CANON, Input.KEY_LCONTROL, Input.KEY_2));
			keyList.add(new DualKey(CONTROL_UPGRADE_FROSTBYTE, Input.KEY_LCONTROL, Input.KEY_3));
			keyList.add(new DualKey(CONTROL_UPGRADE_TERA, Input.KEY_LCONTROL, Input.KEY_4));
			keyList.add(new DualKey(CONTROL_UPGRADE_PIDAR, Input.KEY_LCONTROL, Input.KEY_5));
			keyList.add(new DualKey(CONTROL_UPGRADE_ANTITROLL, Input.KEY_LCONTROL, Input.KEY_6));
			keyList.add(new DualKey(CONTROL_UPGRADE_RECYNERGY, Input.KEY_LCONTROL, Input.KEY_7));
			
			keyList.add(new Key(CONTROL_REPAIR_TURRET, Input.KEY_R));
			keyList.add(new Key(CONTROL_RESTOCK_TURRET, Input.KEY_E));
			
			isRebooted = true;
		}
	}
	
	public void resetTypeStats(){
		// Resets all type information to tier 1
		setKeyType(CONTROL_SELECT_ANTITROLL, Turret.ANTITROLL);
		setKeyType(CONTROL_SELECT_CANON, Turret.CANON);
		setKeyType(CONTROL_SELECT_FROSTBYTE, Turret.FROSTBYTE);
		setKeyType(CONTROL_SELECT_PIDAR, Turret.PIDAR);
		setKeyType(CONTROL_SELECT_RECYNERGY, Turret.RECYNERGY);
		setKeyType(CONTROL_SELECT_TERA, Turret.TERA);
		setKeyType(CONTROL_SELECT_WALL, Turret.WALL);
	}
	
	public void resetControlStats(){
		setKeyInput(CONTROL_MOVE_SCREEN_DOWN, Input.KEY_DOWN);
		setKeyInput(CONTROL_MOVE_SCREEN_UP, Input.KEY_UP);
		setKeyInput(CONTROL_MOVE_SCREEN_LEFT, Input.KEY_LEFT);
		setKeyInput(CONTROL_MOVE_SCREEN_RIGHT, Input.KEY_RIGHT);
		setKeyInput(CONTROL_SELECT_ANTITROLL, Input.KEY_6);
		setKeyInput(CONTROL_SELECT_CANON, Input.KEY_2);
		setKeyInput(CONTROL_SELECT_FROSTBYTE, Input.KEY_3);
		setKeyInput(CONTROL_SELECT_PIDAR, Input.KEY_5);
		setKeyInput(CONTROL_SELECT_RECYNERGY, Input.KEY_7);
		setKeyInput(CONTROL_SELECT_TERA, Input.KEY_4);
		setKeyInput(CONTROL_SELECT_WALL, Input.KEY_1);
		setKeyInput(CONTROL_SELECT_MINE, Input.KEY_8);
		setKeyInput(CONTROL_MANUAL_CONTROL_OF_TURRET, Input.KEY_C);
		setKeyInput(CONTROL_EXIT_MANUAL_CONTROL_OF_TURRET, Input.KEY_ESCAPE);
		setKeyInput(CONTROL_REPAIR_TURRET, Input.KEY_R);
		setKeyInput(CONTROL_RESTOCK_TURRET, Input.KEY_E);
	}
	
	public void setKeyInput(String keyName, int input){
		Key tempKey = null;
		int index = 0;
		
		for(Key key : keyList){
			if(key.getName().equals(keyName)){
				tempKey = key;
				break;
			}
			
			index++;
		}
		
		if(tempKey != null){
			tempKey.setKey(input);
			
			keyList.set(index, tempKey);
		}
	}
	
	// Can only do dualkeys
	public int[] getKeyInputs(String name){
		DualKey tempKey = null;
		
		for(Key key : keyList){
			if(key instanceof DualKey){
				if(key.getName().equals(name)){
					tempKey = (DualKey) key;
				}
			}
		}
		
		if(tempKey != null){
			int[] keyInputs = new int[2];
			
			keyInputs[0] = tempKey.getKey();
			keyInputs[1] = tempKey.getKey2();
			
			return keyInputs;
		}else{
			return null;
		}

	}
	
	public void setKeyType(String keyName, Turret type){
		SelectKey<Turret> tempKey = null;
		int index = 0;
		
		for(Key key : keyList){
			if(key instanceof SelectKey){
				if(key.getName().equals(keyName)){
					tempKey = (SelectKey<Turret>) key;
					break;
				}
			}
			
			index++;
		}
		
		if(tempKey != null){
			tempKey.setType(type);
			
			keyList.set(index, tempKey);
		}
	}
	
	public int getInput(String keyName){
		for(Key key : keyList){
			if(key.getName().equals(keyName)){
				return key.getKey();
			}
		}
		
		return -1;
	}
	
	public Turret getType(String keyName){
		for(Key key : keyList){
			if(key instanceof SelectKey){
				if(key.getName().equals(keyName)){
					return ((SelectKey<Turret>) key).getType();
				}
			}
		}
		
		return null;
	}
	
	public String getInputName(Turret turret){
		for(Key key : keyList){
			if(key instanceof SelectKey){
				SelectKey<Turret> select = (SelectKey<Turret>) key;
				
				if(select.getType() == turret){
					return select.name;
				}
			}
		}
		
		return null;
	}
	
	public static Control getControl(){
		if(instance == null){
			instance = new Control();
		}
		
		return instance;
	}
	
}
