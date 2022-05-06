package dhbw.sose2022.softwareengineering.api.simulation.entity;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.api.geometry.Point;
import dhbw.sose2022.softwareengineering.api.simulation.World;

public abstract sealed class Entity permits MovingEntity, StaticEntity {
	
	private final World world;
	private int posX;
	private int posY;
	private int width;
	private int height;
	
	Entity(World world, int posX, int posY, int width, int height) {
		Validate.isTrue(posX + width < world.getWidth(), "Entity out of bounds");
		Validate.isTrue(posY + height < world.getHeight(), "Entity out of bounds");
		this.world = world;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public Point getPosition() {
		return new Point(this.posX, this.posY);
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setPosition(Point pos) {
		Validate.notNull(pos, "Position cannot be null");
		Validate.isTrue(pos.getX() + this.width < this.world.getWidth(), "Entity out of bounds");
		Validate.isTrue(pos.getY() + this.height < this.world.getHeight(), "Entity out of bounds");
		this.posX = pos.getX();
		this.posY = pos.getY();
	}
	
	public void setWidth(int width) {
		Validate.isTrue(this.posX + width < this.world.getWidth(), "Entity out of bounds");
		this.width = width;
	}
	
	public void setHeight(int height) {
		Validate.isTrue(this.posY + height < this.world.getHeight(), "Entity out of bounds");
		this.height = height;
	}
	
	public void kill() {
		// TODO implement
	}
	
}