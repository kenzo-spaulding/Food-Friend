package com.interview.androidlib.interfaces;

import androidx.core.app.ActivityCompat;


public interface ActivityCountdownListener {
    void startCountdown(ActivityCompat activity, int milliseconds);
}
