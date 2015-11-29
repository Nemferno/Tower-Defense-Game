package org.xodia.td.entity.turret;

import org.xodia.td.entity.turret.tier1.AntiTroll;
import org.xodia.td.entity.turret.tier1.Canon;
import org.xodia.td.entity.turret.tier1.FrostByte;
import org.xodia.td.entity.turret.tier1.PIDar;
import org.xodia.td.entity.turret.tier1.ReCyNergy;
import org.xodia.td.entity.turret.tier1.Tera;
import org.xodia.td.entity.turret.tier2.AntiTrollII;
import org.xodia.td.entity.turret.tier2.CanonII;
import org.xodia.td.entity.turret.tier2.FrostByteII;
import org.xodia.td.entity.turret.tier2.PIDarII;
import org.xodia.td.entity.turret.tier2.ReCyNergyII;
import org.xodia.td.entity.turret.tier2.TeraII;
import org.xodia.td.entity.turret.tier3.AntiTrollIII;
import org.xodia.td.entity.turret.tier3.CanonIII;
import org.xodia.td.entity.turret.tier3.FrostByteIII;
import org.xodia.td.entity.turret.tier3.PIDarIII;
import org.xodia.td.entity.turret.tier3.ReCyNergyIII;
import org.xodia.td.entity.turret.tier3.TeraIII;
import org.xodia.td.entity.turret.tier4.AntiTrollIV;
import org.xodia.td.entity.turret.tier4.CanonIV;
import org.xodia.td.entity.turret.tier4.PIDarIV;
import org.xodia.td.entity.turret.tier4.TeraIV;

// Creates Turrets according to x, y, and type
public class TurretFactory {

	private TurretFactory(){}
	
	public static BasicTurret getTurret(float x, float y, Turret type){
		switch(type){
		case WALL:
			return new Wall(x, y);
		case CANON:
			return new Canon(x, y);
		case FROSTBYTE:
			return new FrostByte(x, y);
		case TERA:
			return new Tera(x, y);
		case PIDAR:
			return new PIDar(x, y);
		case ANTITROLL:
			return new AntiTroll(x, y);
		case RECYNERGY:
			return new ReCyNergy(x, y);
		case CANONII:
			return new CanonII(x, y);
		case FROSTBYTEII:
			return new FrostByteII(x, y);
		case TERAII:
			return new TeraII(x, y);
		case PIDARII:
			return new PIDarII(x, y);
		case ANTITROLLII:
			return new AntiTrollII(x, y);
		case RECYNERGYII:
			return new ReCyNergyII(x, y);
		case ANTIMINE:
			return new AntiMine(x, y);
		case ANTITROLLIII:
			return new AntiTrollIII(x, y);
		case ANTITROLLIV:
			return new AntiTrollIV(x, y);
		case CANONIII:
			return new CanonIII(x, y);
		case CANONIV:
			return new CanonIV(x, y);
		case FROSTBYTEIII:
			return new FrostByteIII(x, y);
		case PIDARIII:
			return new PIDarIII(x, y);
		case PIDARIV:
			return new PIDarIV(x, y);
		case RECYNERGYIII:
			return new ReCyNergyIII(x, y);
		case TERAIII:
			return new TeraIII(x, y);
		case TERAIV:
			return new TeraIV(x, y);
		}
		
		return null;
	}
	
}
