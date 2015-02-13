package com.kitware.charleslaw.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import java.security.spec.RSAOtherPrimeInfo;

/**
 * Created by charles.law on 1/16/2015.
 */
public class Ball {
    protected float X = -1;
    protected float Y = -1;
    protected double XVelocity = 20;
    protected double YVelocity = 10;
    protected float Mass = 10000;
    private float Radius = 100;
    protected float Rotation = 0;

    // in degrees
    protected double AngularVelocity = 0.0;

    public Ball(int x, int y, int vx, int vy) {
        this.X = x;
        this.Y = y;
        this.XVelocity = vx;
        this.YVelocity = vy;

        this.AngularVelocity = 0.0;
    }

    public void setAngularVelocity(double angularVelocity) {
        AngularVelocity = angularVelocity;
    }

    public float GetRadius() {
        return this.Radius;
    }

    public void SetRadius(float radius) {
        this.Radius = radius;
        // Cube seems to much.  We could set the mass separately (large and light)
        // maybe change color with mass.
        this.Mass = radius*radius;
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
        // Now for the bounce (momentum normal to bounce plane).
        // Magic equation to get the impulse magnitude:
        double frac = 2.0*this.Mass*otherBall.Mass / (this.Mass + otherBall.Mass);
        double k = frac*(dx*(otherBall.XVelocity-this.XVelocity) +
                         dy*(otherBall.YVelocity-this.YVelocity));

        // Pseudo physics for rotation.
        // Compute angular velocity with no angular momentum.
        // Perform computation in linear velocity
        double tx = dy;
        double ty = -dx;
        double v1 = this.AngularVelocity*this.Radius*Math.PI/180.0
                      +(this.XVelocity*tx + this.YVelocity*ty);
        double v2 = -otherBall.AngularVelocity*otherBall.Radius*Math.PI/180.0
                      +(otherBall.XVelocity*tx + otherBall.YVelocity*ty);
        double dv = v2-v1;
        v1 += dv*frac/this.Mass;
        v2 -= dv*frac/otherBall.Mass;

        this.AngularVelocity = v1 * 180.0 / (this.Radius*Math.PI);
        otherBall.AngularVelocity = -v2 * 180.0 / (otherBall.Radius*Math.PI);

        // Adjust velocity from impulse
        this.XVelocity += dx * k / this.Mass;
        this.YVelocity += dy * k / this.Mass;
        // now for the other ball.
        otherBall.XVelocity -= dx * k / otherBall.Mass;
        otherBall.YVelocity -= dy * k / otherBall.Mass;
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
