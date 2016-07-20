/**
 * 
 */
package edu.umaine.cs.pendulums;

import static org.junit.Assert.assertEquals;

import java.text.NumberFormat;

import org.junit.Test;

import edu.umaine.cs.pendulums.Pendulum;

/**
 * Tests various parts of a pendulum to make sure they behave correctly.
 * 
 * @author Mark Royer
 * 
 */
public class PendulumTest {

    /**
     * Test the methods that change the pendulum's angle.
     */
    @Test
    public void testSetAngle() {

        Pendulum p = new Pendulum();

        assertEquals(0.0, p.getSX(), 0.001);
        assertEquals(0.0, p.getSY(), 0.001);
        assertEquals(0.0, p.getSZ(), 0.001);

        double l = 10.0;

        assertEquals(0.0, p.getHX(), 0.001);
        assertEquals(-l, p.getHY(), 0.001);
        assertEquals(0.0, p.getHZ(), 0.001);

        p.setPendulumHeadPosition(Math.toRadians(45));

        assertEquals(0.0, p.getSX(), 0.001);
        assertEquals(0.0, p.getSY(), 0.001);
        assertEquals(0.0, p.getSZ(), 0.001);

        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(8);
        assertEquals(f.format(l * Math.sin(Math.toRadians(45))), f.format(p
                .getHX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(45))), f.format(p
                .getHY()));
        assertEquals(0.0, p.getHZ(), 0.001);

        assertEquals(l, p.getLength(), 0.001);

    }

    /**
     * Test the methods that change the pendulum's rod length.
     */
    @Test
    public void testSetLength() {

        Pendulum p = new Pendulum();

        assertEquals(0.0, p.getSX(), 0.001);
        assertEquals(0.0, p.getSY(), 0.001);
        assertEquals(0.0, p.getSZ(), 0.001);

        double l = 10.0;

        assertEquals(0.0, p.getHX(), 0.001);
        assertEquals(-l, p.getHY(), 0.001);
        assertEquals(0.0, p.getHZ(), 0.001);

        p.setPendulumHeadPosition(Math.toRadians(45));

        assertEquals(0.0, p.getSX(), 0.001);
        assertEquals(0.0, p.getSY(), 0.001);
        assertEquals(0.0, p.getSZ(), 0.001);

        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(8);

        assertEquals(f.format(l * Math.sin(Math.toRadians(45))), f.format(p
                .getHX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(45))), f.format(p
                .getHY()));
        assertEquals(0.0, p.getHZ(), 0.001);

        assertEquals(l, p.getLength(), 0.001);

        l = 20.0;

        p.setRodLength(l);

        assertEquals(l, p.getLength(), 0.001);

        assertEquals(f.format(l * Math.sin(Math.toRadians(45))), f.format(p
                .getHX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(45))), f.format(p
                .getHY()));
        assertEquals(0.0, p.getHZ(), 0.001);

    }

    /**
     * Test the methods that change the pendulum's mass position.
     */
    @Test
    public void testSetPendulumHeadPosition() {

        Pendulum p = new Pendulum();

        p.setPendulumHeadPosition(Math.toRadians(-45));

        double l = p.getLength();

        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(8);

        assertEquals(f.format(-l * Math.sin(Math.toRadians(45))), f.format(p
                .getHX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(45))), f.format(p
                .getHY()));
        assertEquals(0.0, p.getHZ(), 0.001);

        p = new Pendulum();
        p.setPendulumHeadPosition(-10, -10, 0);
        l = Math.sqrt(200.0);

        assertEquals(f.format(-l * Math.sin(Math.toRadians(45))), f.format(p
                .getHX()));
        assertEquals(f.format(-l * Math.cos(Math.toRadians(45))), f.format(p
                .getHY()));
        assertEquals(0.0, p.getHZ(), 0.001);

        assertEquals(f.format(Math.toRadians(-45)), f.format(p.getAngle()));

    }
}
