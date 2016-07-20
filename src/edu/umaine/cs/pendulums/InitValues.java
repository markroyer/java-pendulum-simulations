/**
 * 
 */
package edu.umaine.cs.pendulums;

/**
 * An object that contains all of the necessary values to initialize a run of
 * the simulator.
 * 
 * @author Mark Royer
 * 
 */
public class InitValues {

    /**
     * The inner rod's angle (degrees)
     */
    private double a1;

    /**
     * The outer rod's angle (degrees)
     */
    private double a2;

    /**
     * The length of the inner rod
     */
    private double l1;

    /**
     * The length of the outer rod
     */
    private double l2;

    /**
     * The inner pendulum's weight
     */
    private double m1;

    /**
     * The outer pendulum's weight
     */
    private double m2;

    /**
     * The spring constant for the inner pendulum
     */
    private double k1;

    /**
     * The spring constant for the outer pendulum
     */
    private double k2;

    /**
     * @param a1
     *            Inner pendulum angle (degrees)
     * @param a2
     *            Outer pendulum angle (degrees)
     * @param l1
     *            Inner pendulum length
     * @param l2
     *            Outer pendulum length
     * @param m1
     *            Inner pendulum mass
     * @param m2
     *            Outer pendulum mass
     * @param k1
     *            Inner pendulum spring constant
     * @param k2
     *            Outer pendulum spring constant
     */
    public InitValues(double a1, double a2, double l1, double l2, double m1,
            double m2, double k1, double k2) {
        this.a1 = a1;
        this.a2 = a2;
        this.l1 = l1;
        this.l2 = l2;
        this.m1 = m1;
        this.m2 = m2;
        this.k1 = k1;
        this.k2 = k2;
    }

    /**
     * @return Inner pendulum length
     */
    public double getL1() {
        return l1;
    }

    /**
     * @return Outer pendulum length
     */
    public double getL2() {
        return l2;
    }

    /**
     * @return Inner pendulum mass
     */
    public double getM1() {
        return m1;
    }

    /**
     * @return Outer pendulum mass
     */
    public double getM2() {
        return m2;
    }

    /**
     * @return Inner pendulum spring constant
     */
    public double getK1() {
        return k1;
    }

    /**
     * @return Outer pendulum spring constant
     */
    public double getK2() {
        return k2;
    }

    /**
     * @return Inner pendulum angle (degrees)
     */
    public double getA1() {
        return a1;
    }

    /**
     * @return Outer pendulum angle (degrees)
     */
    public double getA2() {
        return a2;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "a1:" + a1 + "," + " a2:" + a2 + "," + " l1:" + l1 + ","
                + " l2:" + l2 + "," + " m1:" + m1 + "," + " m2:" + m2 + ","
                + " k1:" + k1 + "," + " k2:" + k2;
    }

}
