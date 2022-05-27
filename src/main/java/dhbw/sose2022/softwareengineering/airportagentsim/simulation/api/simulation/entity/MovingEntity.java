package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

import java.util.ArrayList;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public abstract non-sealed class MovingEntity extends Entity {
	
	private double direction;
	private double speed;
	
	private double xFraction = 0.0D;
	private double yFraction = 0.0D;
	
	public MovingEntity() {}
	
	public final double getDirection() {
		return this.direction;
	}
	
	public final double getSpeed() {
		return this.speed;
	}
	
	public final void setSpeed(double speed) {
		Validate.isTrue(speed >= 0.0D, "Speed cannot be negative");
		this.speed = speed;
	}
	
	public final void turn(Point p) {
		
		Validate.notNull(p);
		Validate.isTrue(!p.equals(getPosition()), "Cannot turn towards the own position");
		
		double dx = p.getX() - this.posX;
		double dy = p.getY() - this.posY;
		
		this.direction = (dx < 0 && dy >= 0 ? 2.5 : 0.5) * Math.PI - Math.atan2(dy, dx);
		
	}
	
	@Override
	public final void update() {
		
		pluginUpdate();
		
		// The order of moving entities is relevant
		
		this.xFraction += Math.sin(this.direction) * this.speed;
		this.yFraction += Math.cos(this.direction) * this.speed;
		
		int dx = (int) Math.round(this.xFraction);
		int dy = (int) Math.round(this.yFraction);
		
		dx = Math.min(Math.max(dx, -this.posX), this.world.getWidth() - this.width - this.posX);
		dy = Math.min(Math.max(dy, -this.posY), this.world.getHeight() - this.height - this.posY);
		
		// Collision detection.
		// Note: This check only applies to the target position of the entity.
		// Therefore, the entity may walk though another entity if it is moving
		// sufficiently fast.
		ArrayList<Entity> collisions = new ArrayList<Entity>();
		this.world.findEntities(collisions, this.posX + dy, this.posY + dy, this.width, this.height, true);
		collisions.remove(this);
		if(collisions.isEmpty()) {
			// Fraction position can simply be updated by using the delta
			this.xFraction -= dx;
			this.yFraction -= dy;
		} else {
			
			double theta = Math.atan2(this.yFraction, this.xFraction);
			
			int correctionStepX = dx < 0 ? 1 : -1;
			int correctionStepY = dy < 0 ? 1 : -1;
			
			while(true) {
				
				// Choose one conflicting entity and alter this entities move to
				// prevent the conflict, by stepping backwards unit by unit
				Entity other = collisions.get(0);
				
				while(this.world.collides(this.posX + dx, this.posY + dy, this.width, this.height, other.posX, other.posY, other.width, other.height, true)) {
					if(dx == 0) {
						if(dy == 0)
							break;
						dy += correctionStepY;
					} else if(dy == 0) {
						dx += correctionStepX;
					} else {
						double errX = Math.abs(Math.atan2(dy, dx + correctionStepX) - theta);
						double errY = Math.abs(Math.atan2(dy + correctionStepY, dx) - theta);
						if(errX <= errY)
							dx += correctionStepX;
						else
							dy += correctionStepY;
					}
				}
				
				// Update collision list
				collisions.clear();
				this.world.findEntities(collisions, this.posX + dx, this.posY + dy, this.width, this.height, true);
				collisions.remove(this);
				
				// If there are no entities in the way or if the move is
				// completely cancelled, movement can continue
				if(collisions.isEmpty() || (dx == 0 && dy == 0))
					break;
				
			}
			
			// Fraction position needs to be updated in a way that honors the
			// altered delta
			this.xFraction -= dx;
			this.yFraction -= dy;
			if(Math.abs(this.xFraction) > 0.5)
				this.xFraction = this.xFraction < 0 ? -0.5 : 0.5;
			if(Math.abs(this.yFraction) > 0.5)
				this.yFraction = this.yFraction < 0 ? -0.5 : 0.5;
			
		}
		
		this.posX += dx;
		this.posY += dy;
		
	}
	
}
