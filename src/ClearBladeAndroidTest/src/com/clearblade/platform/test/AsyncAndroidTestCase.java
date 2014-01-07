package com.clearblade.platform.api.test;

import android.test.AndroidTestCase;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class AsyncAndroidTestCase extends AndroidTestCase {
    private final Semaphore asyncSemaphore = new Semaphore(0);

    public void waitForAsync() {
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
