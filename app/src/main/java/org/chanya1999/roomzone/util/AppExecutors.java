package org.chanya1999.roomzone.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//คลาสสำหรับการแบ่งเธรดการทำงาน
public class AppExecutors {

  private final Executor diskIO, mainThread;

  //เมธอดเริ่มต้นสำหรับการคำนวณเธรด
  AppExecutors(Executor diskIO, Executor mainThread) {
    this.diskIO = diskIO;
    this.mainThread = mainThread;
  }

  //เมธอดคำนวณเธรด, สร้างเธรดใหม่
  public AppExecutors() {
    this(
        Executors.newSingleThreadExecutor(),
        new MainThreadExecutor()
    );
  }

  public Executor diskIO() {
    return diskIO;
  }

  //เมธอดส่งคืนเธรดหลัก
  public Executor mainThread() {
    return mainThread;
  }

  //คลาส handler เธรด
  private static class MainThreadExecutor implements Executor {
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
      mainThreadHandler.post(command);
    }
  }
}