/**
 * 
 */
package edu.umaine.cs.pendulums;

import java.util.Arrays;

/**
 * The mathematical calculations that represent how a double pendulum moves.
 * 
 * @author Mark Royer
 * 
 */
public class SimpleDoublePendulum implements XPSYS, PendulumBehavior {

    static final double g = 9.8;

    private DoublePendulum pendulum;

    private double r1 = 5;

    private double r2 = 5;

    private double m1 = 1;

    private double m2 = 1;

    private boolean updateTopRodAngle;

    private boolean updateBottomRodAngle;

    public SimpleDoublePendulum(DoublePendulum pendulum) {
        this.pendulum = pendulum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see XPSYS#xpsys(double[], double[])
     */
    public void xpsys(double[] x, double[] f) {

        updateValues(x);

        f[0] = 1;
        f[1] = x[2];
        f[2] = -(Math.pow(x[4], 2)
                * (Math.cos(x[1]) * Math.sin(x[3]) * m2 * r2 - Math.sin(x[1])
                        * Math.cos(x[3]) * m2 * r2)
                + Math.cos(x[3])
                * Math.sin(x[3])
                * (Math.pow(x[2], 2)
                        * (2 * Math.pow(Math.cos(x[1]), 2) * m2 * r1 - m2 * r1) + Math
                        .cos(x[1])
                        * g * m2)
                + Math.pow(Math.cos(x[3]), 2)
                * (-(2) * Math.pow(x[2], 2) * Math.cos(x[1]) * Math.sin(x[1])
                        * m2 * r1 - Math.sin(x[1]) * g * m2)
                + Math.pow(x[2], 2) * Math.cos(x[1]) * Math.sin(x[1]) * m2 * r1 - Math
                .sin(x[1])
                * g * m1)
                / (Math.pow(Math.cos(x[3]), 2)
                        * (2 * Math.pow(Math.cos(x[1]), 2) * m2 * r1 - m2 * r1)
                        + 2 * Math.cos(x[1]) * Math.sin(x[1]) * Math.cos(x[3])
                        * Math.sin(x[3]) * m2 * r1
                        - Math.pow(Math.cos(x[1]), 2) * m2 * r1 - m1 * r1);
        f[3] = x[4];
        f[4] = (Math.pow(x[4], 2)
                * (Math.cos(x[3]) * Math.sin(x[3])
                        * (2 * Math.pow(Math.cos(x[1]), 2) * m2 * r2 - m2 * r2)
                        - 2 * Math.cos(x[1]) * Math.sin(x[1])
                        * Math.pow(Math.cos(x[3]), 2) * m2 * r2 + Math
                        .cos(x[1])
                        * Math.sin(x[1]) * m2 * r2)
                + Math.sin(x[3])
                * (Math.pow(x[2], 2) * Math.cos(x[1]) * (m2 + m1) * r1 + Math
                        .pow(Math.cos(x[1]), 2)
                        * (g * m2 + g * m1)) + Math.cos(x[3])
                * (Math.pow(x[2], 2) * Math.sin(x[1]) * (-(m2) - m1) * r1 + Math
                        .cos(x[1])
                        * Math.sin(x[1]) * (-(g) * m2 - g * m1)))
                / (Math.pow(Math.cos(x[3]), 2)
                        * (2 * Math.pow(Math.cos(x[1]), 2) * m2 * r2 - m2 * r2)
                        + 2 * Math.cos(x[1]) * Math.sin(x[1]) * Math.cos(x[3])
                        * Math.sin(x[3]) * m2 * r2
                        - Math.pow(Math.cos(x[1]), 2) * m2 * r2 - m1 * r2);
    }

    /**
     * Updates either of the rod angles.
     * 
     * @param x current values
     */
    private synchronized void updateValues(double[] x) {
        if (updateTopRodAngle) {
            x[1] = pendulum.getTopRodAngle();
            updateTopRodAngle = false;
        }
        if (updateBottomRodAngle) {
            x[3] = pendulum.getBottomRodAngle();
            updateBottomRodAngle = false;
        }
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.XPSYS#updateGeometry(double[])
     */
    public void updateGeometry(double[] x) {
        pendulum.setTopRodAngle(x[1]);
        pendulum.setBottomRodAngle(x[3]);
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.XPSYS#getInitX()
     */
    public double[] getInitX() {

        double[] x = new double[5];

        r1 = pendulum.getTopRodLength();
        r2 = pendulum.getBottomRodLength();

        x[1] = pendulum.getTopRodAngle();
        x[3] = pendulum.getBottomRodAngle();

        System.out.println("init: x = " + Arrays.toString(x));

        return x;
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
        if (massIndex == 0) {
            this.m1 = weight;
        } else if (massIndex == 1) {
            this.m2 = weight;
        }
    }

    /* (non-Javadoc)
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

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#setRodLength(int, double)
     */
    public synchronized void setRodLength(int rodIndex, double length) {
        if (rodIndex == 0) {
            this.pendulum.setTopRodLength(length);
        } else if (rodIndex == 1) {
            this.pendulum.setBottomRodLength(length);
        }
    }

    /* (non-Javadoc)
     * @see edu.umaine.cs.pendulums.PendulumBehavior#getTotalLength()
     */
    public synchronized double getTotalLength() {
        return this.pendulum.getTotalLength();
    }

}
