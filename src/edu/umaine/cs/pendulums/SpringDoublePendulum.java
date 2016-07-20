package edu.umaine.cs.pendulums;

import java.util.Arrays;

/**
 * Equations to generate the behavior of a double spring pendulum.
 * 
 * @author Mark Royer
 * 
 */
public class SpringDoublePendulum implements XPSYS, PendulumBehavior {

    static final double g = 9.8;

    DoublePendulum pendulum;

    private double k1 = 100;

    private double r01 = 5;

    private double k2 = 100;

    private double r02 = 5;

    private double m1 = 1;

    private double m2 = 1;

    private boolean updateTopRodAngle;

    private boolean updateBottomRodAngle;

    private boolean updateTopRodLength;

    private boolean updateBottomRodLength;

    /**
     * Create a new SpringDoublePendulum behavior for a double pendulum object.
     * 
     * @param pendulum
     *            The double pendulum that will behave according to this
     *            object's calculations
     */
    public SpringDoublePendulum(DoublePendulum pendulum) {
        this.pendulum = pendulum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see XPSYS#xpsys(double[], double[])
     */
    public void xpsys(double[] x, double[] f) {

        updateValues(x);
        // These were generated using Maxima.toJava
        f[0] = 1;
        f[1] = x[5]; // r1dot
        f[2] = x[6]; // theta1dot
        f[3] = x[7]; // r2dot
        f[4] = x[8]; // theta2dot
        f[5] = (Math.sin(x[2]) * Math.sin(x[4]) * (k2 * x[3] - k2 * r02)
                + Math.cos(x[2]) * Math.cos(x[4]) * (k2 * x[3] - k2 * r02)
                + Math.pow(x[6], 2) * m1 * x[1] - k1 * x[1] + k1 * r01 + Math
                .cos(x[2])
                * g * m1)
                / m1; // r1dotdot
        f[6] = (Math.cos(x[2]) * Math.sin(x[4]) * (k2 * x[3] - k2 * r02)
                + Math.sin(x[2]) * Math.cos(x[4]) * (k2 * r02 - k2 * x[3])
                - Math.sin(x[2]) * g * m1 - 2 * x[5] * x[6] * m1)
                / (m1 * x[1]); // theta1dotdot
        f[7] = ((-(k2) * m2 - k2 * m1) * x[3] + Math.pow(x[8], 2) * m1 * m2
                * x[3] + Math.sin(x[2]) * Math.sin(x[4])
                * (k1 * m2 * x[1] - k1 * m2 * r01) + Math.cos(x[2])
                * Math.cos(x[4]) * (k1 * m2 * x[1] - k1 * m2 * r01) + (k2 * m2 + k2
                * m1)
                * r02)
                / (m1 * m2); // r2dotdot
        f[8] = -(Math.cos(x[2]) * Math.sin(x[4]) * (k1 * x[1] - k1 * r01)
                + Math.sin(x[2]) * Math.cos(x[4]) * (k1 * r01 - k1 * x[1]) + 2
                * x[7] * x[8] * m1)
                / (m1 * x[3]); // theta2dotdot

    }

    /**
     * Update the values in the array if they have been altered by an outside
     * entity.
     * 
     * @param x
     *            Values array
     */
    private synchronized void updateValues(double[] x) {
        if (updateTopRodLength) {
            r01 = pendulum.getTopRodLength();
            x[1] = r01;
            updateTopRodLength = false;
        }
        if (updateBottomRodLength) {
            r02 = pendulum.getBottomRodLength();
            x[3] = r02;
            updateBottomRodLength = false;
        }
        if (updateTopRodAngle) {
            x[2] = pendulum.getTopRodAngle();
            updateTopRodAngle = false;
        }
        if (updateBottomRodAngle) {
            x[4] = pendulum.getBottomRodAngle();
            updateBottomRodAngle = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.XPSYS#updateGeometry(double[])
     */
    public void updateGeometry(double[] x) {
        pendulum.setTopRodLength(x[1]);
        pendulum.setTopRodAngle(x[2]);
        pendulum.setBottomRodLength(x[3]);
        pendulum.setBottomRodAngle(x[4]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.XPSYS#getInitX()
     */
    public double[] getInitX() {

        double[] x = new double[9];

        r01 = pendulum.getTopRodLength();
        r02 = pendulum.getBottomRodLength();

        x[1] = r01;
        x[2] = pendulum.getTopRodAngle();
        x[3] = r02;
        x[4] = pendulum.getBottomRodAngle();

        System.out.println("init: x = " + Arrays.toString(x));

        return x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setK(int, double)
     */
    public synchronized void setK(int index, double value) {
        if (index == 0) {
            this.k1 = value;
        } else if (index == 1) {
            this.k2 = value;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setMass(int, double)
     */
    public synchronized void setMass(int massIndex, double weight) {
        if (massIndex == 0) {
            this.m1 = weight;
        } else if (massIndex == 1) {
            this.m2 = weight;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setRodAngle(int, double)
     */
    public synchronized void setRodAngle(int rodIndex, double angle) {
        if (rodIndex == 0) {
            this.pendulum.setTopRodAngle(angle);
            updateTopRodAngle = true;
        } else if (rodIndex == 1) {
            this.pendulum.setBottomRodAngle(angle);
            updateBottomRodAngle = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setRodLength(int, double)
     */
    public synchronized void setRodLength(int rodIndex, double length) {
        if (rodIndex == 0) {
            this.pendulum.setTopRodLength(length);
            this.r01 = length;
            updateTopRodLength = true;
        } else if (rodIndex == 1) {
            this.pendulum.setBottomRodLength(length);
            this.r02 = length;
            updateBottomRodLength = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.umaine.cs.pendulums.PendulumBehavior#getTotalLength()
     */
    public synchronized double getTotalLength() {
        return this.pendulum.getTotalLength();
    }

}
