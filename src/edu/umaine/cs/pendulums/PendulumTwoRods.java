/**
 * 
 */
package edu.umaine.cs.pendulums;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Represents a pendulum that has two rods.
 * 
 * @author Mark Royer
 * 
 */
public class PendulumTwoRods {

    /**
     * The entire graphical representation.
     */
    private BranchGroup pendulumRods;

    /**
     * Creates a new pendulum with two rods.
     */
    public PendulumTwoRods() {

        pendulumRods = new BranchGroup();
        pendulumRods.setCapability(BranchGroup.ALLOW_DETACH);

        Cylinder rod1 = new Cylinder(.1f, 1f);
        this.setRodAppearance(rod1);
        Cylinder rod2 = new Cylinder(.1f, 1f);
        this.setRodAppearance(rod2);

        Transform3D translate1 = new Transform3D();
        Transform3D translate2 = new Transform3D();

        translate1.set(new Vector3f(0.0f, 0.0f, -0.45f));
        TransformGroup t1 = new TransformGroup(translate1);
        t1.addChild(rod1);
        pendulumRods.addChild(t1);

        translate2.set(new Vector3f(0.0f, 0.0f, 0.45f));
        TransformGroup t2 = new TransformGroup(translate2);
        t2.addChild(rod2);
        pendulumRods.addChild(t2);

        pendulumRods.compile();

    }

    /**
     * @return The graphical representation of the two rods
     */
    public BranchGroup getBG() {
        return pendulumRods;
    }

    /**
     * Sets the appearance of the rod to be silvery.
     * 
     * @param rod
     *            The rod to set the appearance of
     * @return The appearance of the rod
     */
    public Appearance setRodAppearance(Cylinder rod) {

        Appearance rodAppearance = new Appearance();

        rodAppearance.setMaterial(new Material(new Color3f(.10588f, .058824f,
                .113725f), new Color3f(.427451f, .470588f, .541176f),
                new Color3f(.3333f, .3333f, .521569f), new Color3f(0f, 0f, 0f),
                9.84615f));

        rod.setAppearance(rodAppearance);

        return rodAppearance;
    }

}
