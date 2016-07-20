package edu.umaine.cs.pendulums;

import java.util.Arrays;

/**
 * The mathematical calculations for a single pendulum's movements.
 * 
 * @author Mark Royer
 * 
 */
public class SimplePendulum implements XPSYS, PendulumBehavior {

    static final double g = 9.8;

    private Pendulum pendulum;

    private boolean updateRodLength;

    private boolean updateRodAngle;

    /**
     * Create a new behavior for the given graphical representation.
     * 
     * @param pendulum
     *            The graphical representation of a pendulum
     */
    public SimplePendulum(Pendulum pendulum) {
        this.pendulum = pendulum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see XPSYS#xpsys(double[], double[])
     */
    public void xpsys(double[] x, double[] f) {

        double r = pendulum.getLength();

        updateValues(x);

        f[0] = 1;
        f[1] = x[2];
        f[2] = (-g / r * Math.sin(x[5])) * r * Math.cos(x[5]) - x[6] * x[6] * r
                * Math.sin(x[5]);
        f[3] = x[4];
        f[4] = (-g / r * Math.sin(x[5])) * r * Math.sin(x[5]) + x[6] * x[6] * r
                * Math.cos(x[5]);
        f[5] = x[6];
        f[6] = -g / r * Math.sin(x[5]);
    }

    /**
     * Updates either the rod angle or rod length.
     * 
     * @param x
     *            Current values for the system
     */
    private synchronized void updateValues(double[] x) {
        if (updateRodLength) {
            x[1] = pendulum.getHX();
            x[3] = pendulum.getHY();
            updateRodLength = false;
        }
        if (updateRodAngle) {
            x[5] = pendulum.getAngle();
            updateRodAngle = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.XPSYS#updateGeometry(double[])
     */
    public void updateGeometry(double[] x) {
        pendulum.setPendulumHeadPosition(x[5]);
    }

    /**
     * Update the graphical view based on the analytical solution.
     * 
     * @param theta
     *            Current rod angle (radians)
     */
    public void analyticSolution(double theta) {

        double r = pendulum.getLength();

        pendulum.setPendulumHeadPosition(r * Math.sin(theta), r
                * Math.cos(theta), 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.XPSYS#getInitX()
     */
    public double[] getInitX() {

        double[] x = new double[7];

        x[1] = pendulum.getHX();
        x[3] = pendulum.getHY();
        x[5] = pendulum.getAngle();

        System.out.println("init: x = " + Arrays.toString(x));

        return x;
    }

    /**
     * Sets the graphical representation to the given angle.
     * 
     * @param angle
     *            radians
     */
    public void setPendulumAngle(double angle) {
        pendulum.setPendulumHeadPosition(angle);
    }

    /**
     * Sets the graphical representation to the length given.
     * 
     * @param length
     *            The length of the rod
     */
    public void setPendulumLength(double length) {
        pendulum.setRodLength(length);
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setK(int, double)
     */
    public synchronized void setK(int index, double value) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setMass(int, double)
     */
    public synchronized void setMass(int massIndex, double weight) {
        // Do nothing
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
