import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;


public class TestGame extends BasicGame {

	private TiledMap map;
	
	public TestGame() {
		super("Test game");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		map.render(0, 0);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Load tilemap
		this.map = new TiledMap("data/testmap.tmx");
		
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new TestGame());

		app.setDisplayMode(800, 600, false);
		app.start();
	}
}
