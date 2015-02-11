package com.kitware.charleslaw.ball;

/**
 * Created by charles.law on 1/14/2015.
 */

//package com.kitware.charleslaw.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.ScaleGestureDetector;



public class AnimatedView extends ImageView{
    private Ball[] Balls = new Ball[4];
    private Context MContext;
    int X = -1;
    int Y = -1;
    private Handler H;
    private final int FRAME_RATE = 30;
    private ScaleGestureDetector ScaleDetector;
    private Ball TouchedBall = null;


    public AnimatedView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        this.MContext = context;
        this.H = new Handler();
        this.Balls[0] = new Ball(500, 200, 0, 15);
        this.Balls[1] = new Ball(400, 900, 0, -15);
        this.Balls[2] = new Ball(400, 500, 15, 0);
        this.Balls[3] = new Ball(600, 300, -15, 0);

        ScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }
    private Runnable Rn = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
    protected void onDraw(Canvas c) {
        for (int i = 0; i < this.Balls.length; ++i) {
            Balls[i].Draw(c, this.getWidth(), this.getHeight(), this.MContext);
            for (int j=0; j < i; ++j) {
                Balls[i].Collide(Balls[j]);
            }
        }
        this.H.postDelayed(this.Rn, FRAME_RATE);
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
                    this.TouchedBall.Move(x,y);
                }
                break;
        }

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




