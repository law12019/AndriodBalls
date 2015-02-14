package com.kitware.charleslaw.ball;

/**
 * Created by charles.law on 1/14/2015.
 */

//package com.kitware.charleslaw.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.ScaleGestureDetector;



public class AnimatedView extends ImageView implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //private Display mDisplay;
    private float[] ExternalAcceleration = new float[3];

    private Ball[] Balls = new Ball[4];
    private Context MContext;
    int X = -1;
    int Y = -1;
    private Handler H;
    private final int FRAME_RATE = 30;
    private ScaleGestureDetector ScaleDetector;
    float LastTouchX;
    float LastTouchY;
    private Ball TouchedBall = null;

    public AnimatedView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        this.MContext = context;
        this.H = new Handler();
        this.Balls[0] = new Ball(500, 200, 0, 15);
        this.Balls[0].SetRadius(150);
        this.Balls[0].SetAngularVelocity(0.0);
        this.Balls[1] = new Ball(698, 1200, 0, -5);
        //this.Balls[1].SetRadius(100);
        //this.Balls[1].SetAngularVelocity(0.0);
        this.Balls[2] = new Ball(200, 500, 15, 0);
        this.Balls[3] = new Ball(600, 300, -15, 0);
        this.Balls[3].SetRadius(50);

        ScaleDetector = new ScaleGestureDetector(context, new ScaleListener());


        //WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.ExternalAcceleration[0] = 0;
        this.ExternalAcceleration[1] = 5;
        this.ExternalAcceleration[2] = 0;

        this.startSimulation();
    }

    public void startSimulation() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopSimulation() {
        mSensorManager.unregisterListener(this);
    }

    private Runnable Rn = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
    protected void onDraw(Canvas c) {
        for (int i = 0; i < this.Balls.length; ++i) {
            Balls[i].Draw(c, this.getWidth(), this.getHeight(), this.ExternalAcceleration,
                          this.MContext);
            for (int j=0; j < i; ++j) {
                Balls[i].Collide(Balls[j]);
            }
        }
        this.H.postDelayed(this.Rn, FRAME_RATE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }
        /*
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = event.values[0];
                mSensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -event.values[0];
                mSensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = event.values[1];
                mSensorY = -event.values[0];
                break;
        } */
        this.ExternalAcceleration[0] = -event.values[0];
        this.ExternalAcceleration[1] = event.values[1];
        this.ExternalAcceleration[2] = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        this.ScaleDetector.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Find if we are touching a ball.
                this.TouchedBall = null;
                for (int i = 0; i < this.Balls.length; ++i) {
                    if (this.Balls[i].Touch(x,y)) {
                        this.TouchedBall = this.Balls[i];
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.TouchedBall != null) {
                    this.TouchedBall.Move(x-this.LastTouchX,y-this.LastTouchY);
                }
                break;
        }
        this.LastTouchX = x;
        this.LastTouchY = y;

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (TouchedBall == null) {
                return false;
            }
            float radius = TouchedBall.GetRadius();
            radius *= detector.getScaleFactor();
            // Don't let the radius get too small or too large.
            radius = Math.max(50.0f, Math.min(radius, 200.0f));
            TouchedBall.SetRadius(radius);
            return true;
        }
    }

}




