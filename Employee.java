import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


public class Employee extends Thread {
	
	private static final long ONE_MINUTE = 10;
	
	private int teamNum;
	private int devNum;
	
	private CyclicBarrier managerLeadsBarrier;
	private CyclicBarrier teamBarrier;
	private AtomicInteger teamInConferenceRoom;
	
	private long startTime;
	private Random r;
	private long x;
	private long y;
	
	public Employee(int teamNum, int devNum, CyclicBarrier managerLeadsBarrier, CyclicBarrier teamBarrier, AtomicInteger teamInConferenceRoom) {
		this.teamNum = teamNum;
		this.devNum = devNum;
		this.managerLeadsBarrier = managerLeadsBarrier;
		this.teamBarrier = teamBarrier;
		this.teamInConferenceRoom = teamInConferenceRoom;
	}
	
	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		r = new Random();
		
		commute();
		
		if(devNum == 1)
			goToManagerMeeting();

		goToTeamMeeting();
	}
	
	private synchronized void commute() {
		x = TimeConverter.convertToLong(8, 0);
		y = TimeConverter.convertToLong(8, 30);
		long timeUntilArrival = x + ((long)(r.nextDouble() * (y - x)));

		try {
			Thread.sleep(timeUntilArrival);
		} catch (InterruptedException e) {}
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " has arrived");
	}
	
	private synchronized void goToManagerMeeting() {
		try {
			managerLeadsBarrier.await();
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {}

		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " enters manager meeting");
		try {
			Thread.sleep(ONE_MINUTE * 15);
		} catch (InterruptedException e) {}
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " leaves manager meeting");
		
		while(teamBarrier.getNumberWaiting() < teamBarrier.getParties() - 1) {
			System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " waiting for team memebers");
			try {
				Thread.sleep(ONE_MINUTE * 10);
			} catch (InterruptedException e) {}
		}	
	}
	
	private synchronized void goToTeamMeeting() {
		try {
			teamBarrier.await();
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {}

		if(teamBarrier.getNumberWaiting() > 0)
			teamBarrier.reset();
		
		if(teamInConferenceRoom.get() == 0)
			teamInConferenceRoom.set(teamNum);
		else {
			while(teamNum != teamInConferenceRoom.get()) {
				try {
					synchronized (teamInConferenceRoom) {
						teamInConferenceRoom.wait();
					}
				} catch (InterruptedException e) {}
				
				if(teamInConferenceRoom.get() == 0)
					teamInConferenceRoom.set(teamNum);
			}
		}
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " enters the conference room");
		try {
			Thread.sleep(ONE_MINUTE * 15);
		} catch (InterruptedException e) {}
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " leaves the conference room");
		
		try {
			teamBarrier.await();
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {}

		if(teamBarrier.getNumberWaiting() > 0)
			teamBarrier.reset();
		

		teamInConferenceRoom.set(0);
		synchronized (teamInConferenceRoom) {
			teamInConferenceRoom.notifyAll();
		}
	}
	
	//private void askQuestion() {
		//System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Developer " + teamNum + "" + devNum + " asks the team lead a question");
			
	//}
	
}
