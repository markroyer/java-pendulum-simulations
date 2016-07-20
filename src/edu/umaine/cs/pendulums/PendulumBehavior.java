/**
 * 
 */
package edu.umaine.cs.pendulums;

/**
 * Methods that control how a pendulum moves.
 * 
 * @author Mark Royer
 * 
 */
public interface PendulumBehavior extends XPSYS {

    /**
     * Sets the mass of the pendulum at the given index to the given mass. The
     * index is 0 based and increases from the closest pendulum to the base
     * outward.
     * 
     * @param massIndex
     *            pendulum index
     * @param weight
     *            mass of the pendulum
     */
    public void setMass(int massIndex, double weight);

    /**
     * Sets the spring constant k of the rod at the given index to the given
     * value. The index is 0 based and increases from the closest pendulum to
     * the base outward.
     * 
     * @param kIndex
     *            pendulum index
     * @param value
     *            the k value
     */
    public void setK(int kIndex, double value);

    /**
     * Sets the angle of the rod at the given index to the given angle. The
     * index is 0 based and increases from the closest pendulum to the base
     * outward.
     * 
     * @param rodIndex
     *            pendulum index
     * @param angle
     *            the rod's angle (radians)
     */
    public void setRodAngle(int rodIndex, double angle);

    /**
     * Sets the length of the rod at the given index to the given length. The
     * index is 0 based and increases from the closest pendulum to the base
     * outward.
     * 
     * @param rodIndex
     *            pendulum index
     * @param length
     *            rod length
     */
    public void setRodLength(int rodIndex, double length);

    /**
     * @return The sum of all the rod lengths for this pendulum
     */
    public double getTotalLength();

}
