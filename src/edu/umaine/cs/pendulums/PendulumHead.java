package edu.umaine.cs.pendulums;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * A graphical representation of a pendulum's mass. This representation is
 * simply a disc.
 * 
 * @author Mark Royer
 * 
 */
public class PendulumHead {

    /**
     * Total graphical representation.
     */
    private BranchGroup pendulumHeadBG;

    /**
     * Create a new pendulum mass that is yellowish in color and can be detached
     * from the pendulum rod.
     */
    public PendulumHead() {

        pendulumHeadBG = new BranchGroup();
        pendulumHeadBG.setCapability(BranchGroup.ALLOW_DETACH);

        Appearance diskColor = new Appearance();

        diskColor.setColoringAttributes(new ColoringAttributes(new Color3f(
                Color.yellow.darker()), ColoringAttributes.NICEST));

        diskColor.setMaterial(new Material(
                new Color3f(.24725f, .1995f, .0745f), new Color3f(.75164f,
                        .60648f, .22648f), new Color3f(.628281f, .555802f,
                        .366065f), new Color3f(0f, 0f, 0f), 51.2f));

        Sphere disk = new Sphere(1f);
        disk.setAppearance(diskColor);

        Transform3D smoosh = new Transform3D();
        smoosh.setScale(new Vector3d(.8, .8, .15));

        TransformGroup sG = new TransformGroup(smoosh);
        sG.addChild(disk);
        pendulumHeadBG.addChild(sG);

        pendulumHeadBG.compile();

    }

    /**
     * @return The graphical representation of a pendulum's head
     */
    public BranchGroup getBG() {
        return pendulumHeadBG;
    }
}
