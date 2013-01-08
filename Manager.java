import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Manager extends Thread {

	private static final long ONE_MINUTE = 10;
	
	private CyclicBarrier managerLeadsBarrier;
	
	private long startTime;
	
	public Manager(CyclicBarrier managerLeadsBarrier) {
		this.managerLeadsBarrier = managerLeadsBarrier;
	}	
	
	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Manager has arrived");
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Manager is planning");
		
		goToManagerMeeting();
	}
	
	private synchronized void goToManagerMeeting() {
		while(managerLeadsBarrier.getNumberWaiting() < managerLeadsBarrier.getParties() - 1) {
			System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Manager is doing administrativia waiting for all team leads");
			try {
				Thread.sleep(ONE_MINUTE * 10);
			} catch (InterruptedException e) {}
		}
		try {
			managerLeadsBarrier.await();
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {}
		managerLeadsBarrier.reset();
		
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Manager enters manager meeting");
		try {
			Thread.sleep(ONE_MINUTE * 15);
		} catch (InterruptedException e) {}
		System.out.println(TimeConverter.convertToString(System.currentTimeMillis() - startTime) + " Manager leaves manager meeting");
	}
	
}
