package com.interview.androidlib.interfaces;

public interface ProgramState extends Runnable {
    enum State{STARTED, RUNNING, DESTROYED, FOCUSED, STOPPED};
    void start();
    void destroy();
    void focus();
    void stop();

    boolean isStarted();
    boolean isDestroyed();
    boolean isFocused();
    boolean isRunning();
    boolean isStopped();
    boolean isChanged();
}
