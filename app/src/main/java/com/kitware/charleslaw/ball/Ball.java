package com.kitware.charleslaw.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by charles.law on 1/16/2015.
 */
public class Ball {
    protected float X = -1;
    protected float Y = -1;
    protected double XVelocity = 20;
    protected double YVelocity = 10;
    private float Radius = 100;
    protected float Rotation = 0;
    protected double AngularVelocity = 0.0;

    public Ball(int x, int y, int vx, int vy) {
        this.X = x;
        this.Y = y;
        this.XVelocity = vx;
        this.YVelocity = vy;

        this.AngularVelocity = 5.0;
    }

    public float GetRadius() {
        return this.Radius;
    }

    public void SetRadius(float radius) {
        this.Radius = radius;
    }

    public boolean Touch(float x, float y) {
        float dx = x - this.X;
        float dy = y - this.Y;
        if (dx*dx + dy*dy < this.Radius * this.Radius) {
            this.X = x;
            this.Y = y;
            this.XVelocity = 0.0;
            this.YVelocity = 0.0;
            this.AngularVelocity = 0.0;
            return true;
        }
        return false;
    }

    public void Move(float x, float y) {
        this.XVelocity = x - this.X;
        this.YVelocity = y - this.Y;
        this.X = x;
        this.Y = y;
    }

    protected void Collide(Ball otherBall) {
        float dx = this.X - otherBall.X;
        float dy = this.Y - otherBall.Y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        float penetration = (float)(this.Radius + otherBall.Radius - dist) / 2;
        if (penetration < 0) {
            return;
        }
        // Normalize
        dx = (float)(dx / dist);
        dy = (float)(dy / dist);

        // Remove any interpenetration.
        this.X += dx*penetration;
        this.Y += dy*penetration;
        otherBall.X -= dx*penetration;
        otherBall.Y -= dy*penetration;
        // Now for the bounce.
        double momentum1 = this.XVelocity*dx + this.YVelocity*dy;
        double momentum2 = otherBall.XVelocity*dx + otherBall.YVelocity*dy;
        this.XVelocity += (momentum2-momentum1) * dx;
        this.YVelocity += (momentum2-momentum1) * dy;
        // now for the other ball.
        otherBall.XVelocity += (momentum1-momentum2) * dx;
        otherBall.YVelocity += (momentum1-momentum2) * dy;
        // Compute angular velocity with no angular momentum.
        // There should be some velocity consideration.
        double dAngularVelocity = this.AngularVelocity + otherBall.AngularVelocity;
        this.AngularVelocity -= dAngularVelocity;
        otherBall.AngularVelocity -= dAngularVelocity;
    }

    protected void Draw(Canvas c, int width, int height, Context ctx) {
        BitmapDrawable ball = (BitmapDrawable) ctx.getResources().getDrawable(R.drawable.ball);

        // Zero angular momentum for now.
        this.X += (float)(this.XVelocity);
        this.Y += (float)(this.YVelocity);
        // Cannot simply reverse velocity because it can get trapped.
        if (this.X > width - this.Radius) {
            this.X = width - this.Radius;
            this.XVelocity = -Math.abs(this.XVelocity);
            // Compute new angular velocity.
            this.AngularVelocity = -180.0 * this.YVelocity / (Math.PI*this.Radius);
        } else if (this.X < this.Radius) {
            this.X = this.Radius;
            this.XVelocity = Math.abs(this.XVelocity);
            this.AngularVelocity = 180.0 * this.YVelocity / (Math.PI*this.Radius);
        }
        if (this.Y > height - this.Radius) {
            this.Y = height - this.Radius;
            this.YVelocity = -Math.abs(this.YVelocity);
            this.AngularVelocity = 180.0 * this.XVelocity / (Math.PI*this.Radius);
        } else if (this.Y < this.Radius) {
            this.Y = this.Radius;
            this.YVelocity = Math.abs(this.YVelocity);
            this.AngularVelocity = -180.0 * this.XVelocity / (Math.PI*this.Radius);
        }
        // Gravity
        this.YVelocity += 0.5;

        c.save();
        this.Rotation += this.AngularVelocity;
        c.rotate((float)(this.Rotation), (float)(this.X), (float)(this.Y));
        float scaleFactor = (float)(2.5 * this.Radius / ball.getBitmap().getWidth());
        c.scale(scaleFactor, scaleFactor, (float)(this.X), (float)(this.Y));

        c.drawBitmap(
            ball.getBitmap(),
                (int)(this.X)-(ball.getBitmap().getWidth()/2),
                (int)(this.Y)-(ball.getBitmap().getWidth()/2),
                null);
        c.restore();
    }
}
