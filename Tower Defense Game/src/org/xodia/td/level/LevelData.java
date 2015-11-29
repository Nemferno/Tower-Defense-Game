package org.xodia.td.level;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xodia.td.entity.enemy.EnemyType;
import org.xodia.td.entity.enemy.boss.BossType;
import org.xodia.td.level.Level.LevelType;
import org.xodia.td.util.DualList;

// Takes In XML Data Into Objects!!!
/*                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
 * In order for the level to know which type of game level
 * we are playing, we need to create a type in the XML code.
 */
public class LevelData {
	
	private String name;
	private int id;
	private int subid;
	private int startCurrency;
	
	private boolean containsBossRound;
	private BossType boss;
	private int bossRound;
	private int rBossCurrency;
	
	private String mapLoc;
	private String mapTileLoc;
	
	private LevelType type;
	
	private DualList<Integer, EnemyType[]> enemyList;
	private int[] unitAmountList;
	private int[] restTimeList;
	private int[] respawnTimeList;
	private int[] rCurrencyList;
	private DualList<Integer, Boolean> loopList;
	
	public LevelData(InputStream input) throws Exception{
		enemyList = new DualList<Integer, EnemyType[]>();
		loopList = new DualList<Integer, Boolean>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(input);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Level");
		
		for(int temp = 0; temp < nList.getLength(); temp++){
			Node nNode = nList.item(temp);
			
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) nNode;
				
				name = eElement.getAttribute("name");
				id = Integer.parseInt(eElement.getAttribute("id"));
				subid = Integer.parseInt(eElement.getAttribute("subid"));
				mapLoc = eElement.getAttribute("mapLoc");
				mapTileLoc = eElement.getAttribute("mapTileLoc");
				startCurrency = Integer.parseInt(eElement.getAttribute("startCurrency"));
				
				String tempType = eElement.getAttribute("type");
				
				for(LevelType ltype : LevelType.values()){
					if(tempType.toLowerCase().equals(ltype.toString().toLowerCase())){
						type = ltype;
						break;
					}
				}
				
				if(!eElement.getAttribute("bossRound").trim().equals("")){
					containsBossRound = true;
					
					bossRound = Integer.parseInt(eElement.getAttribute("bossRound"));
					rBossCurrency = Integer.parseInt(eElement.getAttribute("cBReward"));
				}else{
					// Shows the game system that it is in the end of the whole level
					containsBossRound = false;
					
					bossRound = -1;
				}
				
				String btype = eElement.getAttribute("bossType");
				
				for(BossType type : BossType.values()){
					if(btype.toLowerCase().equals(type.toString().toLowerCase())){
						boss = type;
						break;
					}
				}
				
				NodeList rList = eElement.getElementsByTagName("Round");
				
				unitAmountList = new int[rList.getLength()];
				restTimeList = new int[rList.getLength()];
				respawnTimeList = new int[rList.getLength()];
				rCurrencyList = new int[rList.getLength()];
				
				int roundID = 1;
				int offSet = 0;
				
				for(int r = 0; r < rList.getLength(); r++){
					Node rNode = rList.item(r);
					
					if(rNode.getNodeType() == Node.ELEMENT_NODE){
						Element rElement = (Element) rNode;
						
						if(type == LevelType.SURVIVAL){
							if(bossRound != -1 && roundID % bossRound == 0){
								this.enemyList.add(roundID, null);
								roundID++;
								offSet++;
							}
							
							if(rElement.getAttribute("loop").trim().equals("")){
								loopList.add(roundID - offSet, false);
							}else{
								loopList.add(roundID - offSet, Boolean.valueOf(rElement.getAttribute("loop")));
							}
						}
						
						unitAmountList[roundID - 1 - offSet] = Integer.parseInt(rElement.getAttribute("unitAmount"));
						rCurrencyList[roundID - 1 - offSet] = Integer.parseInt(rElement.getAttribute("cReward"));
						restTimeList[roundID - 1 - offSet] = Integer.parseInt(rElement.getAttribute("restTime"));

						if(rElement.getAttribute("respawnTime").trim().equals("")){	
							respawnTimeList[roundID - 1 - offSet] = 1000;
						}else{
							respawnTimeList[roundID - 1 - offSet] = Integer.parseInt(rElement.getAttribute("respawnTime"));
						}
						
						String[] enumLists = rElement.getElementsByTagName("Enemies").item(0).getTextContent().split(",");
						
						EnemyType[] enemyList = new EnemyType[enumLists.length];
						
						for(int i = 0; i < enumLists.length; i++){
							enemyList[i] = getType(enumLists[i]);
						}
						
						this.enemyList.add(roundID, enemyList);
						
						roundID++;
					}
				}
				
				if(type == LevelType.REGULAR){
					if(bossRound == -1){
						this.enemyList.add(roundID, null);
					}
				}
			}
		}
	}
	
	public BossType getBossType(){
		return boss;
	}
	
	public int getBossRound(){
		return bossRound;
	}
	
	public boolean containsBossRound(){
		return containsBossRound;
	}
	
	public LevelType getType(){
		return type;
	}
	
	public DualList<Integer, Boolean> getLoopList(){
		return loopList;
	}
	
	public int[] getRestTimeList(){
		return restTimeList;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMapLoc(){
		return mapLoc;
	}
	
	public String getMapTileLoc(){
		return mapTileLoc;
	}
	
	public int getID(){
		return id;
	}
	
	public int getSubID(){
		return subid;
	}
	
	public int getStartCurrency(){
		return startCurrency;
	}
	
	public DualList<Integer, EnemyType[]> getEnemiesList(){
		return enemyList;
	}
	
	public int[] getUnitAmountList(){
		return unitAmountList;
	}
	
	public int[] getRespawnTimeList(){
		return respawnTimeList;
	}
	
	public int[] getRewardCurrencyList(){
		return rCurrencyList;
	}
	
	public int getRBossCurrency(){
		return rBossCurrency;
	}
	
	private EnemyType getType(String name){
		EnemyType type = null;
		
		if(EnemyType.VIRUS.toString().toLowerCase().equals(name.toLowerCase())){
			type = EnemyType.VIRUS;
		}else if(EnemyType.DARKROVER.toString().toLowerCase().equals(name.toLowerCase())){
			type = EnemyType.DARKROVER;
		}else if(EnemyType.IMMORTAL.toString().toLowerCase().equals(name.toLowerCase())){
			type = EnemyType.IMMORTAL;
		}else if(EnemyType.WORM.toString().toLowerCase().equals(name.toLowerCase())){
			type = EnemyType.WORM;
		}else if(EnemyType.TROJAN.toString().toLowerCase().equals(name.toLowerCase())){
			type = EnemyType.TROJAN;
		}
		
		return type;
	}
	
}
