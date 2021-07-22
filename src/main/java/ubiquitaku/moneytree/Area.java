package ubiquitaku.moneytree;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Area {

	private double startX, startY, startZ;
	private double rangeX, rangeY, rangeZ;
	private double chance;
	private World world;
	private static Random rnd = new Random();


	public Area(String area) {
		String[] tmp = area.split("/");
		if (tmp.length != 8) throw new IllegalArgumentException("Invalid Area Config "+area);
		try {
			world = Bukkit.getWorld(tmp[0]);
			double[] pos = new double[6];
			for(int i=0; i<6; i++)
				pos[i] = Double.parseDouble(tmp[i+1]);
			startX = Math.min(pos[0], pos[3]);
			startY = Math.min(pos[1], pos[4]);
			startZ = Math.min(pos[2], pos[5]);
			rangeX = Math.abs(pos[3]-pos[0]);
			rangeY = Math.abs(pos[4]-pos[1]);
			rangeZ = Math.abs(pos[5]-pos[2]);
			chance = Double.parseDouble(tmp[7])/100;
		} catch(Exception e) {
			throw new IllegalArgumentException("Invalid Area Config "+area);
		}
	}


	public double getX() {
		return startX;
	}


	public double getY() {
		return startY;
	}


	public double getZ() {
		return startZ;
	}


	public double getRangeX() {
		return rangeX;
	}


	public double getRangeY() {
		return rangeY;
	}


	public double getRangeZ() {
		return rangeZ;
	}


	public double getChance() {
		return chance;
	}


	public World getWorld() {
		return world;
	}


	public Location randomLocation() {
		return new Location(world, startX+rnd.nextDouble()*rangeX, startY+rnd.nextDouble()*rangeY, startZ+rnd.nextDouble()*rangeZ);
	}

}
