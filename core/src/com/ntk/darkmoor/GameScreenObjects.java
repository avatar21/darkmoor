package com.ntk.darkmoor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.Hero.InventoryPosition;
import com.ntk.darkmoor.resource.GraphicAssets;
import com.ntk.darkmoor.resource.ItemAssets;

public class GameScreenObjects implements Disposable {

	private static final int HERO_FACE_HEIGHT = 202;
	private static final int HERO_FACE_WIDTH = 128;
	private static final int HERO_WINDOW_X = 736;
	private static final int HAND_X = HERO_WINDOW_X + HERO_FACE_WIDTH + 24;
	private static final int HERO_WINDOW_HEIGHT = 334;
	private static final int HERO_WINDOW_WIDTH = 288;

	public Stage stage;
	public Team team;
	public Skin uiSkin;

	// internal graphics cache

	/** default hero heads in normal state */
	private SpriteDrawable[] heroHeads;
	private SpriteDrawable unconsciousHero;
	private SpriteDrawable deadHero;
	private SpriteDrawable emptyHandLeft, emptyHandRight;

	// Scene2d Actors
	public Image[] heads;
	public Label[] heroNameLabels;
	public Label[] heroHitPointLabels;
	public ProgressBar[] heroHitPointProgressBars;
	public Image[] rightHands;
	public Image[] leftHands;

	// temporary objects
	private Hero heroToSwap;

	/**
	 * Initializes the structures needed for the hero windows. Has to be called when initializing the game screen and
	 * not on every frame. On every frame call updateHeroes()
	 * 
	 */
	public void initializeHeroRectangles() {
		// Hero heads
		// don't initialize to current (e.g. 4 heroes) but to max in order to avoid resizing
		heads = new Image[Team.MAX_HEROES];
		heroHeads = new SpriteDrawable[Team.MAX_HEROES];
		heroNameLabels = new Label[Team.MAX_HEROES];
		heroHitPointLabels = new Label[Team.MAX_HEROES];
		heroHitPointProgressBars = new ProgressBar[Team.MAX_HEROES];
		rightHands = new Image[Team.MAX_HEROES];
		leftHands = new Image[Team.MAX_HEROES];

		unconsciousHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(2));
		deadHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(4));
		emptyHandRight = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("items")
				.getSprite(ItemAssets.getItem("right hand").getTextureID()));
		emptyHandLeft = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("items")
				.getSprite(ItemAssets.getItem("left hand").getTextureID()));

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {

				initializeHero(x, y);

			}
		}
	}

	private void initializeHero(int x, int y) {
		int index = getHeroIndexInArrays(x, y);
		if (index > team.getHeroCount())
			return;
		Hero hero = team.getHeroes()[index];
		if (hero == null)
			return;

		// Heads
		Sprite sprite = GraphicAssets.getDefault().getTextureSet("heads").getSprite(hero.getHead());
		heroHeads[index] = new SpriteDrawable(sprite);
		heads[index] = new Image(heroHeads[index]);

		// initialize the positions once, not on every frame
		heads[index].setPosition(HERO_WINDOW_X + HERO_WINDOW_WIDTH * x, 1024 - (y * HERO_WINDOW_HEIGHT + 18));
		heads[index].setSize(HERO_FACE_WIDTH, HERO_FACE_HEIGHT);

		stage.addActor(heads[index]);

		// Names
		heroNameLabels[index] = new Label(team.getHeroes()[index].getName(), uiSkin, "gameFont48", GameColors.Black);
		heroNameLabels[index].setPosition(HERO_WINDOW_X + 4 + HERO_WINDOW_WIDTH * y, 1210 - (x * HERO_WINDOW_HEIGHT));
		stage.addActor(heroNameLabels[index]);

		// Hit points
		heroHitPointLabels[index] = new Label(team.getHeroes()[index].getName(), uiSkin, "gameFont48", GameColors.Black);
		heroHitPointLabels[index]
				.setPosition(HERO_WINDOW_X + 4 + HERO_WINDOW_WIDTH * y, 948 - (x * HERO_WINDOW_HEIGHT));
		stage.addActor(heroHitPointLabels[index]);

		heroHitPointProgressBars[index] = new ProgressBar(hero.getHitPoint().getCurrent(), hero.getHitPoint().getMax(),
				1, false, uiSkin); // TODO: ntk: regenerate progress bar when a hero gains additional HP!
		heroHitPointProgressBars[index].setPosition(HERO_WINDOW_X + 4 + HERO_WINDOW_WIDTH * y,
				948 - (x * HERO_WINDOW_HEIGHT));
		heroHitPointProgressBars[index].setHeight(60);
		heroHitPointProgressBars[index].setWidth(240);
		stage.addActor(heroHitPointProgressBars[index]);

		// Hands // TODO: ntk: change the image when a different item is selected
		Item item = hero.getInventoryItem(InventoryPosition.Primary);
		rightHands[index] = new Image(item == null ? emptyHandRight : new SpriteDrawable(GraphicAssets.getDefault()
				.getTextureSet("items").getSprite(item.getTextureID())));
		rightHands[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1110 - (y * HERO_WINDOW_HEIGHT));
		rightHands[index].setSize(64, 96);
		stage.addActor(rightHands[index]);

		item = hero.getInventoryItem(InventoryPosition.Secondary);
		leftHands[index] = new Image(item == null ? emptyHandLeft : new SpriteDrawable(GraphicAssets.getDefault()
				.getTextureSet("items").getSprite(item.getTextureID())));
		leftHands[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1012 - (y * HERO_WINDOW_HEIGHT));
		leftHands[index].setSize(64, 96);
		stage.addActor(leftHands[index]);
	}

	private int getHeroIndexInArrays(int x, int y) {
		return y * 2 + x;
	}

	/**
	 * Updates the hero information/images on screen. This method is intended to be called on every frame. It presumes
	 * that all the structures have already been initialized with a call to initializeHeroRectangles, otherwise will
	 * throw exception.
	 */
	public void updateHeroes() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {

				int index = getHeroIndexInArrays(x, y);

				if (index > team.getHeroCount())
					continue;
				if (team.getHeroes()[index] == null)
					continue;

				updateHero(index);
			}
		}
	}

	private Hero updateHero(int index) {
		Hero hero = team.getHeroes()[index];
		if (hero == null)
			return hero;

		// Head
		if (hero.isDead())
			heads[index].setDrawable(deadHero);
		else if (hero.isUnconscious())
			heads[index].setDrawable(unconsciousHero);
		else
			heads[index].setDrawable(heroHeads[index]);

		// Name
		if (heroToSwap == hero) {
			heroNameLabels[index].setColor(GameColors.Red);
			heroNameLabels[index].setText(" Swapping");
		} else if (team.getSelectedHero() == hero) {
			heroNameLabels[index].setColor(GameColors.White);
			heroNameLabels[index].setText(hero.getName());
		} else {
			heroNameLabels[index].setColor(GameColors.Black);
			heroNameLabels[index].setText(hero.getName());
		}

		// HP
		if (Settings.getLastLoadedInstance().isHPAsBar()) {
			float percent = (float) hero.getHitPoint().getCurrent() / (float) hero.getHitPoint().getMax();
			Color color = GameColors.Green;
			if (percent < 0.15)
				color = GameColors.Red;
			else if (percent < 0.4)
				color = GameColors.Yellow;

			heroHitPointProgressBars[index].setColor(color);
			heroHitPointProgressBars[index].setVisible(true);
			heroHitPointLabels[index].setVisible(false);
			heroHitPointProgressBars[index].setRange(0, hero.getHitPoint().getMax());
			heroHitPointProgressBars[index].setValue(hero.getHitPoint().getCurrent());
		} else {
			heroHitPointLabels[index].setText(hero.getHitPoint().getCurrent() + " of " + hero.getHitPoint().getMax());
			heroHitPointProgressBars[index].setVisible(false);
			heroHitPointLabels[index].setVisible(true);
		}

		return hero;
	}

	@Override
	public void dispose() {
		heads = null;
	}
}
