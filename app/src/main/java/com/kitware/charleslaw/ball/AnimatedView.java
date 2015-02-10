package com.kitware.charleslaw.ball;

/**
 * Created by charles.law on 1/14/2015.
 */

//package com.kitware.charleslaw.ball;
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Handler;
        import android.util.AttributeSet;
        import android.widget.ImageView;
        import android.view.MotionEvent;


public class AnimatedView extends ImageView{
    private Ball[] Balls = new Ball[4];
    private Ball TouchedBall = null;
    private Context MContext;
    int X = -1;
    int Y = -1;
    private Handler H;
    private final int FRAME_RATE = 30;

    public AnimatedView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        this.MContext = context;
        this.H = new Handler();
        this.Balls[0] = new Ball(500, 200, 0, 15);
        this.Balls[1] = new Ball(400, 900, 0, -15);
        this.Balls[2] = new Ball(400, 500, 15, 0);
        this.Balls[3] = new Ball(600, 300, -15, 0);
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
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

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
}




