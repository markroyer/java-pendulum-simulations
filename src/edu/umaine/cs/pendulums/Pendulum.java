package edu.umaine.cs.pendulums;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * 
 */

/**
 * Represents a single pendulum. A single pendulum is simply a rod and a mass at
 * the end of the rod.
 * 
 * @author Mark Royer
 * 
 */
public class Pendulum {

    /**
     * All of the graphical components of a pendulum
     */
    private BranchGroup pendulumBG;

    /**
     * The pendulum's base location
     */
    private double sX, sY, sZ;

    /**
     * The location of the pendulum's mass
     */
    private double hX, hY, hZ;

    /**
     * The pendulum's rod length
     */
    private double length;

    /**
     * The graphical representation of a pendulum's rod
     */
    private Cylinder rod;

    /**
     * The graphical representation of a pendulum's mass
     */
    private BranchGroup pHead;

    // Pendulum graphics transforms

    private Transform3D translateToStart = new Transform3D();

    private Transform3D rotatePendulum = new Transform3D();

    private Transform3D pHeadTranslate = new Transform3D();

    private Transform3D translateRodDown = new Transform3D();

    private Transform3D stretchRod = new Transform3D();

    private TransformGroup pHeadGroup;

    private BranchGroup rodGroup;

    private TransformGroup stretchRodGroup;

    private TransformGroup translateRodDownGroup;

    private TransformGroup finalRotateGroup;

    /**
     * Create a new pendulum at the origin with the rod length = 10 and the
     * angle = 0. This give the effectively makes the pendulum appear to be
     * hanging straight down from the origin.
     */
    public Pendulum() {

        createPendulum();

        setPendulumBase(0, 0, 0);
        setPendulumHeadPosition(0, -length, 0);

    }

    /**
     * Create the graphics components of the pendulum.
     */
    private void createPendulum() {
        pendulumBG = new BranchGroup();
        pendulumBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

        length = 10;

        translateRodDown = new Transform3D();
        translateRodDown.setTranslation(new Vector3d(0, -length / 2., 0));
        translateRodDownGroup = new TransformGroup(translateRodDown);
        translateRodDownGroup
                .setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        rod = new Cylinder(.1f, 1f);
        rodGroup = new BranchGroup();
        rodGroup.setCapability(BranchGroup.ALLOW_DETACH);
        rodGroup.addChild(rod);

        setRodColor();

        stretchRod.setScale(new Vector3d(1, length, 1));
        stretchRodGroup = new TransformGroup(stretchRod);
        stretchRodGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        stretchRodGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        stretchRodGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        stretchRodGroup.addChild(rodGroup);

        pHeadTranslate = new Transform3D();
        pHeadTranslate.setTranslation(new Vector3d(0.0, -length, 0.0));
        pHeadGroup = new TransformGroup(pHeadTranslate);
        pHeadGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        pHeadGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        pHeadGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        pHead = new PendulumHead().getBG();
        setPendulumHeadGeometry(pHead);

        translateRodDownGroup.addChild(stretchRodGroup);

        translateToStart.mul(rotatePendulum);
        finalRotateGroup = new TransformGroup(translateToStart);
        finalRotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        finalRotateGroup.addChild(translateRodDownGroup);
        finalRotateGroup.addChild(pHeadGroup);

        pendulumBG.addChild(finalRotateGroup);
        pendulumBG.compile();
    }

    /**
     * Sets this pendulum's mass appearance to the given branch group.
     * 
     * @param pendulumHead
     *            The appearance of this pendulum's mass
     */
    public void setPendulumHeadGeometry(BranchGroup pendulumHead) {

        if (pHead != null) {
            pHead.detach();
        }
        pHeadGroup.addChild(pendulumHead);
        pHead = pendulumHead;
    }

    /**
     * Sets this pendulum's rod appearance to the given branch group.
     * 
     * @param pendulumRod
     *            The graphical representation of the pendulum
     */
    public void setPendulumRodGeometry(BranchGroup pendulumRod) {

        if (rodGroup != null) {
            rodGroup.detach();
        }

        stretchRodGroup.addChild(pendulumRod);

    }

    /**
     * Makes the rod appear silvery
     * 
     * @return The appearance of the rod
     */
    public Appearance setRodColor() {

        Appearance rodAppearance = new Appearance();

        rodAppearance.setMaterial(new Material(new Color3f(.10588f, .058824f,
                .113725f), new Color3f(.427451f, .470588f, .541176f),
                new Color3f(.3333f, .3333f, .521569f), new Color3f(0f, 0f, 0f),
                9.84615f));

        rod.setAppearance(rodAppearance);

        return rodAppearance;
    }

    /**
     * @return The graphical representation of a pendulum
     */
    public BranchGroup getBG() {
        return pendulumBG;
    }

    /**
     * Set the base location of the pendulum to the given coordinates.
     * 
     * @param sX
     * @param sY
     * @param sZ
     */
    public synchronized void setPendulumBase(double sX, double sY, double sZ) {
        assert !Double.isNaN(sX) : "requires: sX = " + sX;
        assert !Double.isInfinite(sX) : "requires: sX = " + sX;
        assert !Double.isNaN(sY) : "requires: sY = " + sY;
        assert !Double.isInfinite(sY) : "requires: sY = " + sY;
        assert !Double.isNaN(sZ) : "requires: sZ = " + sZ;
        assert !Double.isInfinite(sZ) : "requires: sZ = " + sZ;

        double dx = sX - this.sX;
        double dy = sY - this.sY;
        double dz = sZ - this.sZ;

        this.sX = sX;
        this.sY = sY;
        this.sZ = sZ;

        this.hX = this.hX + dx;
        this.hY = this.hY + dy;
        this.hZ = this.hZ + dz;

        translateToStartVector.set(sX, sY, sZ);
        translateToStart.setTranslation(translateToStartVector);

        trans.set(translateToStart);
        trans.mul(rotatePendulum);
        finalRotateGroup.setTransform(trans);

    }

    // Used to translate the pendulum to the start location

    private Vector3d translateToStartVector = new Vector3d();

    /**
     * Sets the location of the pendulum's mass to the given coordinates.
     * 
     * @param hX
     * @param hY
     * @param hZ
     */
    public synchronized void setPendulumHeadPosition(double hX, double hY,
            double hZ) {
        assert !Double.isNaN(hX) : "requires: hX = " + hX;
        assert !Double.isInfinite(hX) : "requires: hX = " + hX;
        assert !Double.isNaN(hY) : "requires: hY = " + hY;
        assert !Double.isInfinite(hY) : "requires: hY = " + hY;
        assert !Double.isNaN(hZ) : "requires: hZ = " + hZ;
        assert !Double.isInfinite(hZ) : "requires: hZ = " + hZ;

        this.hX = hX;
        this.hY = hY;
        this.hZ = hZ;

        double a = sY - hY;
        double b = hX - sX;
        double c = Math.sqrt(a * a + b * b);

        double theta = Math.PI / 2.;

        if (a != 0) {
            theta = Math.atan(b / a);
        }

        if (hY > sY) {
            this.rotatePendulum.rotZ(theta - Math.PI);
        } else {
            this.rotatePendulum.rotZ(theta);
        }

        this.setRodLength(c);

    }

    /**
     * @return The pendulum's base location x position
     */
    public synchronized double getSX() {
        return sX;
    }

    /**
     * @return The pendulum's base location y position
     */
    public synchronized double getSY() {
        return sY;
    }

    /**
     * @return The pendulum's base location z position
     */
    public synchronized double getSZ() {
        return sZ;
    }

    /**
     * @return The pendulum's mass location x position
     */
    public synchronized double getHX() {
        return hX;
    }

    /**
     * @return The pendulum's mass location y position
     */
    public synchronized double getHY() {
        return hY;
    }

    /**
     * @return The pendulum's mass location z position
     */
    public synchronized double getHZ() {
        return hZ;
    }

    /**
     * Sets the pendulum's mass position based on the given angle.
     * 
     * @param degrees
     *            radians
     */
    public synchronized void setPendulumHeadPosition(double degrees) {
        assert !Double.isNaN(degrees) : "requires: degrees = " + degrees;
        assert !Double.isInfinite(degrees) : "requires: degrees = " + degrees;

        double d = degrees - Math.PI / 2.;

        double x = this.sX + length * Math.cos(d);

        double y = this.sY + length * Math.sin(d);

        this.setPendulumHeadPosition(x, y, 0);
    }

    /**
     * @return The length of the pendulum's rod
     */
    public synchronized double getLength() {
        return length;
    }

    /**
     * Sets this pendulum's rod length to the given length and updates the
     * graphical representation.
     * 
     * @param length
     */
    public synchronized void setRodLength(double length) {
        assert !Double.isNaN(length) : "requires: length = " + length;
        assert !Double.isInfinite(length) : "requires: length = " + length;

        this.length = length;

        double a = hX - sX;
        double b = sY - hY;

        double angle = Math.PI / 2;

        if (b != 0) {
            angle = Math.atan(a / b);
        }

        if (b < 0) {
            angle = angle - Math.PI;
        }

        this.hX = sX + this.length * Math.sin(angle);
        this.hY = sY - this.length * Math.cos(angle);

        pHeadTranslateVector.set(0, -this.length, hZ);
        this.pHeadTranslate.setTranslation(pHeadTranslateVector);
        this.pHeadGroup.setTransform(pHeadTranslate);

        translateRodDownVector.set(0, -this.length / 2., 0);
        translateRodDown.setTranslation(translateRodDownVector);

        stretchRod.setScale(new Vector3d(1, this.length, 1));
        stretchRodGroup.setTransform(stretchRod);
        translateRodDownGroup.setTransform(translateRodDown);

        trans.set(translateToStart);
        trans.mul(rotatePendulum);
        finalRotateGroup.setTransform(trans);
    }

    // Used for transforming the pendulum's rod and head
    
    private Transform3D trans = new Transform3D();
    private Vector3d translateRodDownVector = new Vector3d();
    private Vector3d pHeadTranslateVector = new Vector3d();

    /**
     * @return Angle of pendulum in radians
     */
    public synchronized double getAngle() {
        if (length != 0) {

            if (hX + 0.000001 > sX) {
                return Math.acos((sY - hY) / length);
            } else {
                return -Math.acos((sY - hY) / length);
            }
        } else {
            return 0;
        }
    }

}
