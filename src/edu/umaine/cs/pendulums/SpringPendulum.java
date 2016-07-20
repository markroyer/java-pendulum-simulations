/**
 * 
 */
package edu.umaine.cs.pendulums;

import java.util.Arrays;

/**
 * Contains the equations used to represent a single pendulum where the rod
 * behaves as if it were a spring.
 * 
 * @author Mark Royer
 * 
 */
public class SpringPendulum implements XPSYS, PendulumBehavior {
    static final double g = 9.8;

    private double m = 3;

    private double k = 10;

    private double r0 = 6;

    private boolean updateRodLength;

    private boolean updateRodAngle;

    private Pendulum pendulum;

    public SpringPendulum(Pendulum pendulum) {
        this.pendulum = pendulum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see XPSYS#xpsys(double[], double[])
     */
    public void xpsys(double[] x, double[] f) {

        updateValues(x);

        f[0] = 1; // dt
        f[1] = x[3]; // rdot
        f[2] = x[4]; // thetadot
        f[3] = -((x[4] * x[3]) / x[2]) - (g / x[2]) * Math.sin(x[1]); // rdotdot
        f[4] = x[2] * x[3] * x[3] + g * Math.cos(x[1]) - (2 * k / m)
                * (x[2] - r0); // thetadotdot
    }

    /**
     * Updates either the rod length or the rod angle in the given array.
     * 
     * @param x
     *            Current values
     */
    private synchronized void updateValues(double[] x) {
        if (updateRodLength) {
            x[2] = pendulum.getLength();
            updateRodLength = false;
        }
        if (updateRodAngle) {
            x[1] = pendulum.getAngle();
            updateRodAngle = false;
        }
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.XPSYS#updateGeometry(double[])
     */
    public void updateGeometry(double[] x) {
        pendulum.setRodLength(x[2]);
        pendulum.setPendulumHeadPosition(x[1]);
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.XPSYS#getInitX()
     */
    public double[] getInitX() {

        double[] x = new double[5];

        r0 = pendulum.getLength();

        x[1] = pendulum.getAngle();
        x[2] = r0;

        System.out.println("init: x = " + Arrays.toString(x));

        return x;
    }

    /**
     * Sets the graphical representations angle.
     * 
     * @param angle rod angle (radians)
     */
    public void setPendulumAngle(double angle) {
        pendulum.setPendulumHeadPosition(angle);
    }

    /**
     * Sets the graphical representations rod length.
     * 
     * @param length The rod length
     */
    public void setPendulumLength(double length) {
        pendulum.setRodLength(length);
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setK(int, double)
     */
    public synchronized void setK(int index, double value) {
        if (index == 0) {
            this.k = value;
        }
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setMass(int, double)
     */
    public synchronized void setMass(int massIndex, double weight) {
        if (massIndex == 0) {
            this.m = weight;
        }

    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setRodAngle(int, double)
     */
    public synchronized void setRodAngle(int rodIndex, double angle) {
        if (rodIndex == 0) {
            this.pendulum.setPendulumHeadPosition(angle);
            updateRodAngle = true;
        }
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setRodLength(int, double)
     */
    public synchronized void setRodLength(int rodIndex, double length) {
        if (rodIndex == 0) {
            this.pendulum.setRodLength(length);
            updateRodLength = true;
        }
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#getTotalLength()
     */
    public synchronized double getTotalLength() {
        return this.pendulum.getLength();
    }

}
