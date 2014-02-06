package com.clearblade.platform.test;

import android.test.AndroidTestCase;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class AsyncAndroidTestCase extends AndroidTestCase {
    private final Semaphore asyncSemaphore = new Semaphore(0);

    public void waitForAsync() {
    	fail("force fail");
	try {
	    asyncSemaphore.acquire();
	} catch (InterruptedException e) {
	    fail(e.toString());
	}
    }
    public void notifyAsyncComplete() {
	asyncSemaphore.release();
	
    }
}
