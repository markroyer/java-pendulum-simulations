package edu.umaine.cs.pendulums;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;

/**
 * A graphical representation of a joint that combines multiple pendulum rods.
 * This particular representation looks similar to a pulley.
 * 
 * @author Mark Royer
 * 
 */
public class PendulumJoint {

    /**
     * Total graphical representation.
     */
    private BranchGroup pendulumHeadBG;

    /**
     * Create a new joint.
     */
    public PendulumJoint() {

        pendulumHeadBG = new BranchGroup();
        pendulumHeadBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        pendulumHeadBG.setCapability(BranchGroup.ALLOW_DETACH);

        Appearance endCap = new Appearance();

        endCap.setColoringAttributes(new ColoringAttributes(new Color3f(
                Color.yellow.darker()), ColoringAttributes.NICEST));

        endCap.setMaterial(new Material(new Color3f(.24725f, .1995f, .0745f),
                new Color3f(.75164f, .60648f, .22648f), new Color3f(.628281f,
                        .555802f, .366065f), new Color3f(0f, 0f, 0f), 51.2f));

        Appearance endInner = new Appearance();

        endInner.setColoringAttributes(new ColoringAttributes(new Color3f(
                Color.yellow.darker().darker()), ColoringAttributes.NICEST));

        endInner.setMaterial(new Material(new Color3f(.24725f, .1995f, .0745f),
                new Color3f(.75164f, .60648f, .22648f), new Color3f(.628281f,
                        .555802f, .366065f), new Color3f(0f, 0f, 0f), 51.2f));

        Transform3D rotate = new Transform3D();
        Transform3D translate = new Transform3D();

        translate.set(new Vector3f(0.0f, 0.0f, -0.1f));
        TransformGroup capTGT1 = new TransformGroup(translate);
        pendulumHeadBG.addChild(capTGT1);

        rotate.rotX(Math.toRadians(90));
        TransformGroup pendulumTGR1 = new TransformGroup(rotate);
        Cone cone1 = new Cone(0.6f, 0.5f);
        cone1.getShape(Cone.CAP).setAppearance(endCap);
        cone1.getShape(Cone.BODY).setAppearance(endInner);
        pendulumTGR1.addChild(cone1);
        capTGT1.addChild(pendulumTGR1);

        translate.set(new Vector3f(0f, 0.0f, 0.1f));
        TransformGroup pendulumTGT2 = new TransformGroup(translate);
        pendulumHeadBG.addChild(pendulumTGT2);

        rotate.rotX(Math.toRadians(-90));
        TransformGroup capTGR2 = new TransformGroup(rotate);
        Cone cone2 = new Cone(0.6f, 0.5f);
        cone2.getShape(Cone.CAP).setAppearance(endCap);
        cone2.getShape(Cone.BODY).setAppearance(endInner);
        capTGR2.addChild(cone2);
        pendulumTGT2.addChild(capTGR2);

        pendulumHeadBG.compile();

    }

    /**
     * @return The total graphical representation of a pendulum joint
     */
    public BranchGroup getBG() {
        return pendulumHeadBG;
    }

}
