
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.capgemini.spacerobots.engine.Robot;
import com.capgemini.spacerobots.engine.playerclasses.IRandomGenerator;
import com.capgemini.spacerobots.util.SpaceRobotsMathUtil;

/**
 * Dummy for a Robot. To be implemented
 */
@SuppressWarnings("serial")
public class MoveBot extends Robot {

	public static final String NAME = "Moov-Bot";
	private IRandomGenerator rand;
	private float xMin = 0;
	private float yMin = 0;
	private float xMax = getLevelInfo().getLevelWidth();
	private float yMax = getLevelInfo().getLevelHeight();
	private float curHead = 0;

	private boolean enemySpotted = false;
	private boolean isMoving = false;

	private Vector2 lastKnownEnemyPos;
	private int lastKnownEnemyPosIter;

	SpaceRobotsMathUtil ut;

	public MoveBot() {
		super(NAME);
	}

	/**
	 * VORSICHT! Kann Timeout werfen
	 */
	@Override
	public void update() {

		// rainbow();
		// System.out.println(getLevelInfo().getIteration());
		// System.out.println(getDashboard().getPosition());
		moveToEnemy(new Vector2(300, 300));

	}

	@Override
	public void init() {
		setColor(new Color((float) 0.5, (float) 0.3, (float) 0.1, (float) 1));
		rand = getRandomGenerator();
		// get Maxalues

	}

	// SCANNING

	// Verbrauch ca 0,3e
	private void scanFront() { // Vector2 pos) {
		int length = 200;
		// if (getPosX() < (xmax - 50) && getPosY())
		getScanController().scan(getDashboard().getHeading(), length, 75);
	}

	private void bigScan() {
		getScanController().scan(getDashboard().getHeading() + rand.getRandomNumber(180), 500, 150);
	}

	private void scanCircle(float length) {
		getScanController().scan(360, length, 150);
	}

	// MOVING

	private void moveToEnemy(Vector2 enemyPos) {
		float angleToEnemy = ut.getAngleBetweenTwoVectors(getDashboard().getPosition(), enemyPos);
		getPilot().moveRobot(curHead + angleToEnemy, 10);
	}

	private void moveRandom() {
		getPilot().moveRobot(curHead + rand.getRandomNumber(100), 5);
	}

	// KILLING

	private void shootAt(Vector2 pos) {
		getWeaponController().shootRocket(pos);
	}

	// UTILS
	private float getPosX() {
		return getDashboard().getPosition().x;
	}

	private float getPosY() {
		return getDashboard().getPosition().y;
	}

	private float getDistToEnemy(Vector2 enemyPos) {
		return ut.distanceBetweenTwoPoints(getDashboard().getPosition(), enemyPos);
	}

	/*
	 * private float calcLength(Vector2 currPos) { float len = 0.0; currPos.x +
	 * xMax return len; }
	 */
	private void rainbow() {
		setColor(new Color((float) ((getColor().r + 0.1) % 1), (float) ((getColor().g + 0.1) % 1),
				(float) ((getColor().b + 0.1) % 1), 1));
	}
}
