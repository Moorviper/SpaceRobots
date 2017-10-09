
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.capgemini.spacerobots.engine.Robot;
import com.capgemini.spacerobots.engine.ScannedObject;
import com.capgemini.spacerobots.engine.playerclasses.EBorderDirection;
import com.capgemini.spacerobots.engine.playerclasses.IRandomGenerator;
import com.capgemini.spacerobots.util.LinearPredictor;
import com.capgemini.spacerobots.util.SpaceRobotsMathUtil;

/**
 * Dummy for a Robot. To be implemented
 */
@SuppressWarnings("serial")
public class SooSBot extends Robot {

	public static final String NAME = "SooS-Bot";
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

	private int scanDir = 0;
	private int scanStep = 15;
	private boolean invertScan = false;

	private boolean einmal = true;

	private Vector2 predictedEnemyPos;

	SpaceRobotsMathUtil ut;

	public SooSBot() {
		super(NAME);
	}

	/**
	 * VORSICHT! Kann Timeout werfen
	 */
	@Override
	public void update() {
		// scan, move, shoot
		/*
		 * getWeaponController().shootRocket(direction)
		 * getPilot().moveRobot(heading, acceleration)
		 * getScanController().scan(scanDirection, scanLength, scanAperture)
		 */
		// rainbow();
		// System.out.println(getLevelInfo().getIteration());
		// System.out.println(getDashboard().getPosition());
		// System.out.println(getWeaponController().getEnergyConsumptionShoot());
		curHead = getDashboard().getHeading();

		if (enemySpotted) {
			scanFront();
		} else {
			// bigScan();
			float len = 300;
			if (!(getDashboard().getBorderCollision() != EBorderDirection.NONE))
				len = calcLength(getDashboard().getPosition());
			// System.out.println("len : " + len);
			float energy = getDashboard().getEnergy();
			scanCircle(len); // * (energy / 100));
			// scanCircle((len / 10) * energy);
		}
		ScannedObject scanres = getScanController().getLastScanResult();
		if (scanres.getPosition() != null) { // Wenn Gegner gefunden wurde
			enemySpotted = true;
			if (lastKnownEnemyPos != null) {
				predictedEnemyPos = LinearPredictor.predict(lastKnownEnemyPos, scanres.getPosition(),
						lastKnownEnemyPosIter, scanres.getIteration(), 12);
			}
			lastKnownEnemyPos = scanres.getPosition();
			lastKnownEnemyPosIter = scanres.getIteration();
			float angle = moveToEnemy(lastKnownEnemyPos);
			shootAtDir(angle);
			// shootAt(lastKnownEnemyPos);
		} else {
			enemySpotted = false;
			moveRandom();
		}
		Vector2 currPos = getDashboard().getPosition();

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
		int length = 1000;
		// if (getPosX() < (xmax - 50) && getPosY())
		System.out.println(getDashboard().getHeading());
		getScanController().scan(getDashboard().getHeading(), length, 15);
	}

	private void bigScan() {
		getScanController().scan(getDashboard().getHeading() + rand.getRandomNumber(180), 500, 150);
	}

	private void scanCircle(float length) {
		if (einmal) {
			// getScanController().scan(360, length, 150);
			getScanController().scan(scanDir, length, 15);
			einmal = !einmal;
		}
		if (invertScan) {
			scanDir += scanStep;
			// invertScan = false;
		} else {
			scanDir -= scanStep;
			// invertScan = true;
		}
		getScanController().scan(scanDir, length, 15);

		scanDir *= -1;
		invertScan = !invertScan;
		if (scanDir > 180) {
			scanDir = 180;
			scanStep *= -1;
		}
		System.out.println("dir " + scanDir);
	}

	// MOVING

	private float moveToEnemy(Vector2 enemyPos) {
		float angleToEnemy = ut.getAngleBetweenTwoVectors(getDashboard().getPosition(), enemyPos);
		getPilot().moveRobot(angleToEnemy, (float) 0.2); // curHead +
		return angleToEnemy;
	}

	private void moveRandom() {
		getPilot().moveRobot(curHead + rand.getRandomNumber(100), 5);
	}

	// KILLING

	private void shootAtDir(float direction) {
		getWeaponController().shootRocket(direction);
	}

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

	// 0 = x, 1= y int axis
	private float calcLength(Vector2 currPos) {

		/*
		 * float len = 0.0f; if (scanDir > 90 || scanDir < -90) len = currPos.x
		 * - xMax; else len = xMax - currPos.x;
		 */
		float lup, ldown, lleft, lright;

		lup = yMax - currPos.y;
		ldown = currPos.y;
		lleft = currPos.x;
		lright = xMax - currPos.x;
		float[] nums = { lup, ldown, lleft, lright };
		return findMin(nums);
	}

	private static float findMin(float[] nums) {
		float min = nums[0];
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] < min) {
				min = nums[i];
			}
		}
		return min;
	}

	private void rainbow() {
		setColor(new Color((float) ((getColor().r + 0.1) % 1), (float) ((getColor().g + 0.1) % 1),
				(float) ((getColor().b + 0.1) % 1), 1));
	}
}
