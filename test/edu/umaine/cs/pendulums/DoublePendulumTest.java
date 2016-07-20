/**
 * 
 */
package edu.umaine.cs.pendulums;

import java.text.NumberFormat;

import org.junit.Test;

import edu.umaine.cs.pendulums.DoublePendulum;
import edu.umaine.cs.pendulums.Pendulum;

import static org.junit.Assert.*;

/**
 * A collection of tests to make sure the double pendulum behaves correctly.
 * 
 * @author Mark Royer
 * 
 */
public class DoublePendulumTest {

    /**
     * Check the methods that change the pendulum's angles.
     */
    @Test
    public void testSetAngle() {

        DoublePendulum p = new DoublePendulum();

        assertEquals(0.0, p.getSX());
        assertEquals(0.0, p.getSY());
        assertEquals(0.0, p.getSZ());

        double l = 10.0;

        assertEquals(0.0, p.getJointX());
        assertEquals(-l, p.getJointY());
        assertEquals(0.0, p.getJointZ());

        assertEquals(0.0, p.getBottomPendulum().getSX());
        assertEquals(-l, p.getBottomPendulum().getSY());
        assertEquals(0.0, p.getBottomPendulum().getSZ());

        assertEquals(0.0, p.getHeadX());
        assertEquals(-2 * l, p.getHeadY());
        assertEquals(0.0, p.getHeadZ());

        // 45 Degrees
        p.setCombinedPendulumAngle(Math.toRadians(45));

        assertEquals(0.0, p.getSX());
        assertEquals(0.0, p.getSY());
        assertEquals(0.0, p.getSZ());

        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(8);
        assertEquals(f.format(l * Math.sin(Math.toRadians(45))), f.format(p
                .getJointX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(45))), f.format(p
                .getJointY()));
        assertEquals(0.0, p.getJointZ());

        assertEquals(f.format(2 * l * Math.sin(Math.toRadians(45))), f.format(p
                .getHeadX()));
        assertEquals(f.format(-2 * l * Math.cos(Math.toRadians(45))), f
                .format(p.getHeadY()));
        assertEquals(0.0, p.getHeadZ());

        assertEquals(l, p.getTopRodLength());
        assertEquals(l, p.getBottomRodLength());

        // -45
        p.setCombinedPendulumAngle(Math.toRadians(-45));

        assertEquals(0.0, p.getSX());
        assertEquals(0.0, p.getSY());
        assertEquals(0.0, p.getSZ());

        f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(8);
        assertEquals(f.format(l * Math.sin(Math.toRadians(-45))), f.format(p
                .getJointX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(-45))), f.format(p
                .getJointY()));
        assertEquals(0.0, p.getJointZ());

        assertEquals(f.format(2 * l * Math.sin(Math.toRadians(-45))), f
                .format(p.getHeadX()));
        assertEquals(f.format(-2 * l * Math.cos(Math.toRadians(-45))), f
                .format(p.getHeadY()));
        assertEquals(0.0, p.getHeadZ());

        assertEquals(l, p.getTopRodLength());
        assertEquals(l, p.getBottomRodLength());

    }

    /**
     * Test the methods that change the pendulum's rod lengths.
     */
    @Test
    public void testSetLength() {

        DoublePendulum p = new DoublePendulum();

        assertEquals(0.0, p.getSX());
        assertEquals(0.0, p.getSY());
        assertEquals(0.0, p.getSZ());

        double l = 10.0;

        assertEquals(0.0, p.getJointX());
        assertEquals(-l, p.getJointY());
        assertEquals(0.0, p.getJointZ());

        assertEquals(0.0, p.getHeadX());
        assertEquals(-2 * l, p.getHeadY());
        assertEquals(0.0, p.getHeadZ());

        p.setCombinedPendulumAngle(Math.toRadians(180));

        Pendulum p1 = p.getTopPendulum();
        Pendulum p2 = p.getBottomPendulum();
        
        assertEquals(Math.toRadians(180), p1.getAngle());
        assertEquals(Math.toRadians(180), p2.getAngle());
        
        assertEquals(0.0, p.getSX());
        assertEquals(0.0, p.getSY());
        assertEquals(0.0, p.getSZ());

        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(3);

        assertEquals("-"+f.format(0.0), f.format(p.getJointX()));
        assertEquals(f.format(l), f.format(p.getJointY()));
        assertEquals(0.0, p.getJointZ());
        
        assertEquals("-"+f.format(0.0), f.format(p.getHeadX()));
        assertEquals(f.format(2 * l), f.format(p.getHeadY()));
        assertEquals(0.0, p.getHeadZ());

        assertEquals(2 * l, p.getTotalLength());

         double l2 = 20.0;
        
         p.setTopRodLength(l2);
        
         assertEquals(l2, p.getTopRodLength());
         
         assertEquals(0.0, p.getSX());
         assertEquals(0.0, p.getSY());
         assertEquals(0.0, p.getSZ());

         assertEquals("-"+f.format(0.0), f.format(p.getJointX()));
         assertEquals(f.format(l2), f.format(p.getJointY()));
         assertEquals(0.0, p.getHeadZ());
         
         assertEquals("-"+f.format(0.0), f.format(p2.getSX()));
         assertEquals(f.format(l2), f.format(p2.getSY()));
         assertEquals(0.0, p2.getSZ());
         
         assertEquals("-"+f.format(0.0), f.format(p2.getHX()));
         assertEquals(f.format(l2+l), f.format(p2.getHY()));
         
         assertEquals(Math.toRadians(180), p2.getAngle());
         
         assertEquals("-"+f.format(0.0), f.format(p.getHeadX()));
         assertEquals(f.format(l2+l), f.format(p.getHeadY()));

    }

}
