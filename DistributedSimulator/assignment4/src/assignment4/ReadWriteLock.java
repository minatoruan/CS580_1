package assignment4;

public class ReadWriteLock {
	private int readers = 0;
	private boolean writeRequest = false;

	public synchronized void lockRead() throws InterruptedException {
		while (writeRequest) {
			wait();
		}
		readers++;
	}
	
	public synchronized void unlockRead() {
		readers--;
		notifyAll();
	}
	
	public synchronized void lockWrite() throws InterruptedException {
		writeRequest = true;
		while(readers > 0) {
			System.out.printf("Thread %s - waiting for the semaphore to add a word\n", Thread.currentThread().getId());
			wait();
		}
		System.out.printf("Thread %s - get the semaphore to add a word\n", Thread.currentThread().getId());
	}
	
	public synchronized void unlockWrite() throws InterruptedException {
		System.out.printf("Thread %s - release the semaphore\n", Thread.currentThread().getId());
		writeRequest = false;
		notifyAll();
	}
}
