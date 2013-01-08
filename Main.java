import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
	
	private static final int NUM_MANAGERS = 1;
	private static final int NUM_EMPLOYEES = 12;
	private static final int NUM_TEAM_LEADS = 3;
	private static final int NUM_TEAMS = 3;
	private static final int EMPLOYEES_PER_TEAM = 4;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CyclicBarrier managerLeadsBarrier = new CyclicBarrier(NUM_TEAM_LEADS + NUM_MANAGERS);
		CyclicBarrier[] teamBarriers = new CyclicBarrier[NUM_TEAMS];
		for(int i = 0; i < NUM_TEAMS; i++)
			teamBarriers[i] = new CyclicBarrier(EMPLOYEES_PER_TEAM);
		AtomicInteger teamInConferenceRoom = new AtomicInteger(0);
		
		Manager manager = new Manager(managerLeadsBarrier);
		
		Employee[] employees = new Employee[NUM_EMPLOYEES];
		int teamNum = 1;
		int devNum;
		for(int i = 0; i < NUM_EMPLOYEES; i++) {
			devNum = (i % 4) + 1;
			employees[i] = new Employee(teamNum, devNum, managerLeadsBarrier, teamBarriers[teamNum - 1], teamInConferenceRoom);
			if(devNum >= EMPLOYEES_PER_TEAM)
				teamNum++;
		}
		
		manager.start();
		
		for(int i = 0; i < NUM_EMPLOYEES; i++)
			employees[i].start();
		
		try {
			manager.join();
		} catch (InterruptedException e) {}
		
		for(int i = 0; i < NUM_EMPLOYEES; i++) {
			try {
				employees[i].join();
			} catch (InterruptedException e) {}
		}
	}

}
