package com.ntk.darkmoor.engine.graphics;

import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;

public class TileDrawing {
	private int ID;
	private Vector2 location;
	public SpriteEffects effect;
	public CardinalPoint side;
	private float xScale;
	private float yScale;

	public TileDrawing(int id, Vector2 location, CardinalPoint side) {
		this(id, location, side, SpriteEffects.NONE);
	}

	public TileDrawing(int id, Vector2 location, CardinalPoint side, SpriteEffects effect) {
		this.ID = id;
		this.location = location;
		this.effect = effect;
		this.side = side;
	}
	
	public TileDrawing(int id, Vector2 location, CardinalPoint side, SpriteEffects effect, float xScale, float yScale) {
		this.ID = id;
		this.location = location;
		this.effect = effect;
		this.side = side;
		this.xScale = xScale;
		this.yScale = yScale;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Vector2 getLocation() {
		return location;
	}

	public void setLocation(Vector2 location) {
		this.location = location;
	}

	public SpriteEffects getEffect() {
		return effect;
	}

	public void setEffect(SpriteEffects effect) {
		this.effect = effect;
	}

	public CardinalPoint getSide() {
		return side;
	}

	public void setSide(CardinalPoint side) {
		this.side = side;
	}

	public float getXScale() {
		return xScale;
	}

	public void setXScale(float xScale) {
		this.xScale = xScale;
	}

	public float getYScale() {
		return yScale;
	}

	public void setYScale(float yScale) {
		this.yScale = yScale;
	}
}