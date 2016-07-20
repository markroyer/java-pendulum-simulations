/**
 * 
 */
package edu.umaine.cs.pendulums;

import javax.media.j3d.BranchGroup;

/**
 * Represents a pendulum that has two rods.
 * 
 * @author Mark Royer
 * 
 */
public class DoublePendulum {

    /**
     * The inner pendulum
     */
    private Pendulum topPendulum;

    /**
     * The outer pendulum
     */
    private Pendulum bottomPendulum;

    /**
     * The sum of all the objects that make up a double pendulum
     */
    private BranchGroup finalGroup;

    /**
     * Create a double pendulum at the origin. Each rod is length 10 units and
     * the angles are 0. Thus the rods are hanging directly downward.
     */
    public DoublePendulum() {

        topPendulum = new Pendulum();
        topPendulum.setPendulumHeadGeometry(new PendulumJoint().getBG());
        topPendulum.setPendulumRodGeometry(new PendulumTwoRods().getBG());
        bottomPendulum = new Pendulum();

        bottomPendulum.setPendulumBase(topPendulum.getHX(),
                topPendulum.getHY(), topPendulum.getHZ());

        finalGroup = new BranchGroup();
        finalGroup.addChild(topPendulum.getBG());
        finalGroup.addChild(bottomPendulum.getBG());
        finalGroup.compile();

    }

    /**
     * The location of the base of the pendulum.
     * 
     * @param x
     * @param y
     * @param z
     */
    public synchronized void setStartLocation(double x, double y, double z) {
        topPendulum.setPendulumBase(x, y, z);
        bottomPendulum.setPendulumBase(topPendulum.getHX(),
                topPendulum.getHY(), topPendulum.getHZ());
    }

    /**
     * The location where the two pendulums come together.
     * 
     * @param x
     * @param y
     * @param z
     */
    public synchronized void setJointLocation(double x, double y, double z) {
        topPendulum.setPendulumHeadPosition(x, y, z);
        bottomPendulum.setPendulumBase(topPendulum.getHX(),
                topPendulum.getHY(), topPendulum.getHZ());
    }

    /**
     * The location of the outer most pendulum's weight.
     * 
     * @param x
     * @param y
     * @param z
     */
    public synchronized void setHeadLocation(double x, double y, double z) {
        bottomPendulum.setPendulumHeadPosition(x, y, z);
    }

    /**
     * Sets the top and bottom pendulums rods' to length.
     * 
     * @param length
     */
    public synchronized void setRodLength(double length) {
        setTopRodLength(length);
        setBottomRodLength(length);
    }

    /**
     * Sets the inner pendulum's rod length to length.
     * 
     * @param length
     */
    public synchronized void setTopRodLength(double length) {
        topPendulum.setRodLength(length);
        bottomPendulum.setPendulumBase(topPendulum.getHX(),
                topPendulum.getHY(), topPendulum.getHZ());
    }

    /**
     * Sets the outer pendulum's rod length to length.
     * 
     * @param length
     */
    public synchronized void setBottomRodLength(double length) {
        bottomPendulum.setRodLength(length);
    }

    /**
     * Sets both pendulums angles' to angle. This makes the double pendulum
     * appear straight.
     * 
     * @param angle
     *            radians
     */
    public synchronized void setCombinedPendulumAngle(double angle) {
        setTopRodAngle(angle);
        bottomPendulum.setPendulumBase(topPendulum.getHX(),
                topPendulum.getHY(), topPendulum.getHZ());
        setBottomRodAngle(angle);
    }

    /**
     * Sets the inner pendulum's angle to the given angle.
     * 
     * @param angle
     *            radians
     */
    public synchronized void setTopRodAngle(double angle) {
        topPendulum.setPendulumHeadPosition(angle);
        bottomPendulum.setPendulumBase(topPendulum.getHX(),
                topPendulum.getHY(), topPendulum.getHZ());
    }

    /**
     * Sets the outer pendulum's angle to the give angle.
     * 
     * @param angle
     *            radians
     */
    public synchronized void setBottomRodAngle(double angle) {
        bottomPendulum.setPendulumHeadPosition(angle);
    }

    /**
     * @return The composite graphical representation of a double pendulum
     */
    public BranchGroup getBG() {
        return finalGroup;
    }

    /**
     * @return the inner pendulum length + the outer pendulum length
     */
    public synchronized double getTotalLength() {
        return topPendulum.getLength() + bottomPendulum.getLength();
    }

    /**
     * @return the x position of the joint
     */
    public synchronized double getJointX() {
        return topPendulum.getHX();
    }

    /**
     * @return the y position of the joint
     */
    public synchronized double getJointY() {
        return topPendulum.getHY();
    }

    /**
     * @return the z position of the joint
     */
    public synchronized double getJointZ() {
        return topPendulum.getHZ();
    }

    /**
     * @return the x position of the outer pendulum weight
     */
    public synchronized double getHeadX() {
        return bottomPendulum.getHX();
    }

    /**
     * @return the y position of the outer pendulum weight
     */
    public synchronized double getHeadY() {
        return bottomPendulum.getHY();
    }

    /**
     * @return the z position of the outer pendulum weight
     */
    public synchronized double getHeadZ() {
        return bottomPendulum.getHZ();
    }

    /**
     * @return the inner rod angle (radians)
     */
    public synchronized double getTopRodAngle() {
        return topPendulum.getAngle();
    }

    /**
     * @return the outer rod angle (radians)
     */
    public synchronized double getBottomRodAngle() {
        return bottomPendulum.getAngle();
    }

    /**
     * @return the inner rod length
     */
    public synchronized double getTopRodLength() {
        return topPendulum.getLength();
    }

    /**
     * @return the outer rod length
     */
    public synchronized double getBottomRodLength() {
        return bottomPendulum.getLength();
    }

    /**
     * @return the base of the double pendulum's x position
     */
    public synchronized double getSX() {
        return topPendulum.getSX();
    }

    /**
     * @return the base of the double pendulum's y position
     */
    public synchronized double getSY() {
        return topPendulum.getSY();
    }

    /**
     * @return the base of the double pendulum's z position
     */
    public synchronized double getSZ() {
        return topPendulum.getSZ();
    }

    /**
     * @return The underlying outer pendulum object
     */
    public Pendulum getBottomPendulum() {
        return bottomPendulum;
    }

    /**
     * @return The underlying inner pendulum object
     */
    public Pendulum getTopPendulum() {
        return topPendulum;
    }
}
