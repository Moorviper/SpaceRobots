
import com.capgemini.spacerobots.engine.Robot;

/**
 * Dummy for a Robot. To be implemented
 */
@SuppressWarnings("serial")
public class UserRobot extends Robot {

	public static final String NAME = "UserRobot";

	public UserRobot() {
		super(NAME);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		System.out.println(getLevelInfo().getIteration());
		System.out.println(getDashboard().getPosition());
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

}
