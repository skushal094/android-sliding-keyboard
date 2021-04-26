package com.hci.projectkeyboard;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.List;

public class KeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener, SensorEventListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private List<Keyboard.Key> keyList;

    private boolean isCaps = false;

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

//        retrieveKeys();

//        kv.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                // For each key in the key list
//                for (Keyboard.Key k : keyList) {
//                    // If the coordinates from the Motion event are inside of the key
//                    if (k.isInside((int) event.getX(), (int) event.getY())) {
//                        // k is the key pressed
//                        Log.d("Debugging",
//                                "Key pressed: X=" + k.x + " - Y=" + k.y);
//                        int centreX, centreY;
//                        centreX = (k.width / 2) + k.x;
//                        centreY = (k.height / 2) + k.y;
//                        // These values are relative to the Keyboard View
//                        Log.d("Debugging",
//                                "Centre of the key pressed: X=" + centreX + " - Y=" + centreY);
//                    }
//                }
//            }
//            // Return false to avoid consuming the touch event
//            return false;
//        });

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, 20000000);

        return kv;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
//        retrieveKeys();
    }

    public void retrieveKeys() {
        keyList = kv.getKeyboard().getKeys();
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        new Thread(() -> {
            InputConnection ic = getCurrentInputConnection();
//        playClick(primaryCode);
            switch (primaryCode) {
                case -8:
                    break;
                case Keyboard.KEYCODE_DELETE:
                    ic.deleteSurroundingText(1, 0);
                    break;
//                case Keyboard.KEYCODE_SHIFT:
//                    isCaps = !isCaps;
//                    keyboard.setShifted(isCaps);
//                    kv.invalidateAllKeys();
//                    break;
                case Keyboard.KEYCODE_DONE:
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;
                default:
                    char code = (char) primaryCode;
                    if (Character.isLetter(code) && isCaps)
                        code = Character.toUpperCase(code);
                    ic.commitText(String.valueOf(code), 1);
            }
        }).start();
    }

    private void playClick(int i) {

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (i) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) < 1000) {
            return;
        }

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

//            if (((curTime - lastUpdate) > 100)) {
//                long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

//                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

//                if (speed > SHAKE_THRESHOLD) {
//
//                }

            last_x = x;
            last_y = y;
            last_z = z;
//                Log.d("Accelerometer", "" + last_x + " - " + last_y + " - " + last_z);

            if (last_x > 3.5) {
                kv.setKeyboard(new Keyboard(KeyboardService.this, R.xml.qwerty_left));
            } else if (last_x < -3.5) {
                kv.setKeyboard(new Keyboard(KeyboardService.this, R.xml.qwerty_right));
            } else {
                kv.setKeyboard(new Keyboard(KeyboardService.this, R.xml.qwerty));
            }
        }
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
