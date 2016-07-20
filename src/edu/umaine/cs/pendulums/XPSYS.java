package edu.umaine.cs.pendulums;

/**
 * Represents an object that can be used by the RK4SYS to calculate derivatives.
 * 
 * @author Mark Royer
 * 
 */
public interface XPSYS {

    /**
     * This is where the calculations for the derivatives are done. The newly
     * calculated values are stored in <code>f</code>. These values are
     * calculated based on the values in <code>x</code>.
     * 
     * @param x
     *            The current x values
     * @param f
     *            The functional values
     */
    public void xpsys(double[] x, double[] f);

    /**
     * Updates the graphical representation with the current values.
     * 
     * @param x
     *            current values
     */
    public void updateGeometry(double[] x);

    /**
     * The values that are initially used to calculate the position of the
     * object.
     * 
     * @return The initial x values
     */
    public double[] getInitX();

}
