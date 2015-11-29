package org.xodia.td;

import java.awt.Font;

import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
import org.xodia.td.level.LevelData;

public class Resources {

	public static Image NORMAL_BUTTON,
						PRESS_BUTTON,
						HOVER_BUTTON;
	
	public static Image BLEEDING_ICON;
	public static Image SLOWNESS_ICON;
	public static Image FREEZE_ICON;
	public static Image BUFF_ICON;
	public static Image NOAMMO_ICON;
	public static Image POWERED_ICON;
	public static Image UNPOWERED_ICON;
	public static Image REPAIR_ICON;
	public static Image STEALTHSPOTTER_ICON;
	public static Image UNABLEFIRE_ICON;
	
	public static Image CANON_IMAGE,
						TERA_IMAGE,
						FROSTBYTE_IMAGE,
						ANTITROLL_IMAGE,
						PIDAR_IMAGE,
						RECYNERGY_IMAGE,
						WALL_IMAGE;
	
	public static Image DARKROVER_IMAGE,
						WORM_IMAGE,
						BROODLING_IMAGE,
						IMMORTAL_IMAGE,
						TROJAN_IMAGE,
						VIRUS_IMAGE,
						CONTROLLEDTURRET_IMAGE;
	
	public static Image UNAVAILABLE_PORTRAIT;
	
	public static Image UNAVAILABLE_TURRET_UI_BUTTON,
						TURRET_UI;
	
	public static LevelData EndlessData;
	public static LevelData AlphaData;
	
	public static TrueTypeFont 	ARIAL_16_FONT,
								ARIAL_12_FONT;
	
	public static void loadResources() throws Exception{
		AnimationLoader.load();
		
		ARIAL_16_FONT = new TrueTypeFont(new Font("Arial", Font.PLAIN, 16), false);
		ARIAL_12_FONT = new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), false);
		
		NORMAL_BUTTON = new Image(ResourceLoader.getResourceAsStream("asset/Button.png"), "Button Image", false);
		HOVER_BUTTON = new Image(ResourceLoader.getResourceAsStream("asset/HoverButton.png"), "Hover Button Image", false);
		PRESS_BUTTON = new Image(ResourceLoader.getResourceAsStream("asset/PressButton.png"), "Press Button Image", false);
		
		SLOWNESS_ICON = new Image(ResourceLoader.getResourceAsStream("asset/SlowIcon.png"), "Slowness Icon", false);
		BLEEDING_ICON = new Image(ResourceLoader.getResourceAsStream("asset/BleedingIcon.png"), "Bleeding Icon", false);
		FREEZE_ICON = new Image(ResourceLoader.getResourceAsStream("asset/FreezingIcon.png"), "Frozen Icon", false);
		BUFF_ICON = new Image(ResourceLoader.getResourceAsStream("asset/BuffIcon.png"), "Buff Icon", false);
		NOAMMO_ICON = new Image(ResourceLoader.getResourceAsStream("asset/NoAmmoIcon.png"), "No Ammo Icon", false);
		POWERED_ICON = new Image(ResourceLoader.getResourceAsStream("asset/PoweredIcon.png"), "Powered Icon", false);
		UNPOWERED_ICON = new Image(ResourceLoader.getResourceAsStream("asset/UnpoweredIcon.png"), "UnPowered Icon", false);
		REPAIR_ICON = new Image(ResourceLoader.getResourceAsStream("asset/RepairIcon.png"), "Repair Icon", false);
		STEALTHSPOTTER_ICON = new Image(ResourceLoader.getResourceAsStream("asset/StealthSpotterIcon.png"), "Stealth Spotter Icon", false);
		UNABLEFIRE_ICON = new Image(ResourceLoader.getResourceAsStream("asset/UnableFireIcon.png"), "Unable Fire Icon", false);
		
		CANON_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Canon.png"), "Canon Image", false);
		TERA_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Tera.png"), "Tera Image", false);
		FROSTBYTE_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/FrostByte.png"), "FrostByte Image", false);
		ANTITROLL_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/AntiTroll.png"), "AntiTroll Image", false);
		PIDAR_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/PiDar.png"), "PIDAR Image", false);
		RECYNERGY_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/ReCyNergy.png"), "ReCyNergy Image", false);
		WALL_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Wall.png"), "Wall Image", false);
		
		DARKROVER_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/DarkRover.png"), "Dark Rover Image", false);
		WORM_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Worm.png"), "Worm Image", false);
		BROODLING_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Broodling.png"), "Broodling Image", false);
		IMMORTAL_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Immortal.png"), "Immortal Image", false);
		TROJAN_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Trojan.png"), "Trojan Image", false);
		VIRUS_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/Virus.png"), "Virus Image", false);
		CONTROLLEDTURRET_IMAGE = new Image(ResourceLoader.getResourceAsStream("asset/ControlledTower.png"), "Controlled Tower Image", false);
		
		UNAVAILABLE_PORTRAIT = new Image(ResourceLoader.getResourceAsStream("asset/portraits/unavailablePortrait.png"), "Unavailable Portrait Image", false);
		
		UNAVAILABLE_TURRET_UI_BUTTON = new Image(ResourceLoader.getResourceAsStream("asset/TurretButtonUI.png"), "Turret Button UI", false);
		TURRET_UI = new Image(ResourceLoader.getResourceAsStream("asset/TurretUI.png"), "Turret UI", false);
		
		EndlessData = new LevelData(ResourceLoader.getResourceAsStream("asset/LevelData/EndlessData.xml"));
		AlphaData = new LevelData(ResourceLoader.getResourceAsStream("asset/LevelData/AlphaMapData.xml"));
	}
	
}
