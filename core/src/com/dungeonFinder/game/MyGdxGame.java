package com.dungeonFinder.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	public static final int HEIGHT = 595; //Gdx.graphics.getHeight();
	public static final int WIDTH = 700; //Gdx.graphics.getWidth();
	public int[][] map = new int[4][4];
	Texture img;
	OrthographicCamera hpbarCamera;
	ShapeRenderer sr;
	Texture[] texture;
	private OrthographicCamera camera;
	private Sprite centerPiece;
	private Sprite character;
	private float distanceNum;
	private float distanceDenom;
	private float ratio;
	private int stepsAvailable;
	private String stepsAvailableDraw;
	private SpriteBatch batch;
	private BitmapFont font;
	private Random rand = new Random();

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();
		centerPiece = new Sprite(new Texture("food.png"));
		character = new Sprite(new Texture("badlogic.jpg"));

		batch = new SpriteBatch();
		img = new Texture(Gdx.files.internal("grass.png"));
		font = new BitmapFont();
		font.setColor(Color.WHITE);

		//sets size of centerPiece
		centerPiece.setSize(30, 30);

		//sets size of character
		character.setSize(30, 30);

		//set position of centerpiece-very annoying and long to right
		centerPiece.setPosition(rand.nextInt(map.length) * img.getWidth() + img.getWidth() / 2 - centerPiece.getWidth() / 2, rand.nextInt(map.length) * img.getHeight() + img.getHeight() / 2 - centerPiece.getHeight() / 2);

		//set position of character
		character.setPosition(img.getWidth() / 2 - character.getWidth() / 2, 73 - character.getHeight() / 2);

		//sets steps available
		stepsAvailable = 4;

		texture = new Texture[]{img};
		System.out.println(img.getHeight());//gets height of texture
		System.out.println(img.getWidth());//gets width of texture
	}

	@Override
	public void render() {
		//start of character movement
		if (Gdx.input.isKeyJustPressed(Keys.LEFT)) { //moves character 50px left when left arrow key is pressed
			character.setX(character.getX() - img.getWidth());
			stepsAvailable--;
		}

		if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) { //moves character 50px right when right arrow key is pressed
			character.setX(character.getX() + img.getWidth());
			stepsAvailable--;
		}

		if (Gdx.input.isKeyJustPressed(Keys.UP)) { //moves character 50px up when up arrow is pressed
			character.setY(character.getY() + img.getHeight());
			stepsAvailable--;
		}

		if (Gdx.input.isKeyJustPressed(Keys.DOWN)) { // moves character 50px down when down arrow is pressed
			character.setY(character.getY() - img.getHeight());
			stepsAvailable--;
		}
		//end of character movement

		//start of pick up system for determining and winning
		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && character.getX() == centerPiece.getX() && character.getY() == centerPiece.getY()) {
			System.out.println("YOU WIN");
			stepsAvailable += 5;
			centerPiece.setPosition(rand.nextInt(map.length) * img.getWidth() + img.getWidth() / 2 - centerPiece.getWidth() / 2, rand.nextInt(map.length) * img.getHeight() + img.getHeight() / 2 - centerPiece.getHeight() / 2);
		}

		//System.out.println(stepsAvailable);

		if (stepsAvailable == -1)
			System.out.println("you lose try again?");

		//System.out.println(centerPiece.getX() + "," + centerPiece.getY());

		//Hot N Cold bar in game
		distanceNum = pythoTheorem(character, centerPiece);//performs pythogorean Theorem to determine distance from object
		distanceDenom = HotNColdDenom(HEIGHT, WIDTH, centerPiece);
		ratio = distanceNum / distanceDenom;
		System.out.println(ratio);//prints to console


		stepsAvailableDraw = Integer.toString(stepsAvailable);

		//		Gdx.gl.glClearColor(0, 0, 0, 1);
		//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(1 * ratio, 0, 1 * (1 / ratio), 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		for (int x = 0; x < map[0].length; x++) {
			for (int y = 0; y < map.length; y++) {
				if (map[x][y] == 0)
					batch.draw(texture[map[x][y]], x * img.getWidth(), y * img.getHeight());
			}
		}
		//     	 centerPiece.draw(batch);//draws image to screen
		font.draw(batch, stepsAvailableDraw, 100, 800);
		character.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		img.dispose();
		batch.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}

	/*
	 * This method performs the pythogorean theorem on the character. I will soon have to add another object for the random object location.
	 */
	public float pythoTheorem(Sprite character, Sprite centerPiece) {
		float positionDistance;
		float positionX = character.getX();
		float PositionY = character.getY();
		float centerHeight = centerPiece.getY();
		float centerWidth = centerPiece.getX();
		positionDistance = (float) Math.sqrt(Math.pow(positionX - centerWidth, 2) + Math.pow(PositionY - centerHeight, 2));
		return positionDistance;
	}

	/*
	 * gets the maximum distance possible and returns it as a ratio from current user location
	 */
	public float HotNColdDenom(int height, int width, Sprite centerPiece) {
		float maxDistance;
		float xDistance;
		float yDistance;
		float distanceHeight = (height - centerPiece.getHeight() / 2);
		float distanceWidth = (width - centerPiece.getWidth() / 2);
		float xPosition = centerPiece.getX();
		float yPosition = centerPiece.getY();

		if (distanceWidth - xPosition >= xPosition - centerPiece.getWidth() / 2)
			xDistance = distanceWidth - xPosition;
		else
			xDistance = xPosition - centerPiece.getWidth() / 2;

		if (distanceHeight - yPosition >= yPosition - centerPiece.getHeight() / 2)
			yDistance = distanceHeight - yPosition;
		else
			yDistance = yPosition - centerPiece.getHeight() / 2;

		maxDistance = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

		return maxDistance;

	}
}
