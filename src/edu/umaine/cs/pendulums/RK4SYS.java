package edu.umaine.cs.pendulums;

import javax.media.j3d.BadTransformException;
import javax.swing.JOptionPane;

/**
 * A representation of the Runge-Kutta Method of order 4 based on
 * Cheney/Kincaid, numerical mathematics and computing, 1985.
 * 
 * @author Mark Royer
 */
public class RK4SYS implements Runnable {

    public static final int RUNINDEFINATELY = -1;

    private XPSYS xpsysFunction;

    private double time;

    private double dt;

    private int nsteps;

    private boolean finishRequested;

    private boolean pause = false;

    private int delay;

    /**
     * Create a new RK4SYS object to operate on the given function.
     * 
     * @param xpsysFunction
     *            Functor of the problem to solve
     * @param time
     *            Initial time
     * @param dt
     *            The time step
     * @param nsteps
     *            The number of steps
     * @param delay
     *            The amount of time in between each time through the loop.
     *            Measured in milliseconds delay >= 0.
     */
    public RK4SYS(XPSYS xpsysFunction, double time, double dt, int nsteps,
            int delay) {
        this.xpsysFunction = xpsysFunction;
        this.time = time;
        this.dt = dt;
        this.nsteps = nsteps;
        this.delay = delay;
    }

    /**
     * Runge-Kutta Method of order 4 from Cheney/Kincaid. Also based on notes
     * from COS 515.
     * 
     * @param time
     *            Current time
     * @param x
     *            Variables
     * @param dt
     *            Time step
     * @param nstep
     *            Number of time steps, a negative value indicates to loop
     *            forever.
     * @param xpsysFunction
     *            Abstract functor that the user can define depending on the
     *            problem.
     */
    public void rk4sys(double time, double[] x, double dt, int nstep,
            XPSYS xpsysFunction) {

        double dt2 = .5 * dt;
        int numberOfVariables = x.length;
        double[] y = new double[numberOfVariables];
        double[] f1 = new double[numberOfVariables];
        double[] f2 = new double[numberOfVariables];
        double[] f3 = new double[numberOfVariables];
        double[] f4 = new double[numberOfVariables];

        boolean done = false;

        for (int k = 0; !done; k++) {

            if (isPaused()) {
                sleep();
            }

            xpsysFunction.xpsys(x, f1);

            for (int i = 0; i < numberOfVariables; i++) {
                y[i] = x[i] + dt2 * f1[i];
            }

            xpsysFunction.xpsys(y, f2);

            for (int i = 0; i < numberOfVariables; i++) {
                y[i] = x[i] + dt2 * f2[i];
            }

            xpsysFunction.xpsys(y, f3);

            for (int i = 0; i < numberOfVariables; i++) {
                y[i] = x[i] + dt * f3[i];
            }

            xpsysFunction.xpsys(y, f4);

            for (int i = 0; i < numberOfVariables; i++) {
                x[i] = x[i] + dt * (f1[i] + 2.0 * (f2[i] + f3[i]) + f4[i])
                        / 6.0;
            }

            time += dt * nstep;

            try {
                Thread.sleep(getDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                xpsysFunction.updateGeometry(x);
            } catch (BadTransformException e) {
                errorInCalculationMessage();
            }

            if (nstep > 0) {
                if (k >= nstep) {
                    requestToFinish();
                }
            }

            if (hasRequestToFinish()) {
                done = true;
            }

        }
    }

    /**
     * Display a message if a NaN is encountered and quit calculations
     * gracefully.
     */
    public void errorInCalculationMessage() {
        JOptionPane.showMessageDialog(null, "Error in calculations.", "Error",
                JOptionPane.ERROR_MESSAGE);
        requestToFinish();
    }

    /**
     * @return true iff RK4SYS has been requested to stop
     */
    public synchronized boolean hasRequestToFinish() {
        return this.finishRequested;
    }

    /**
     * Tells this RK4SYS to stop calculations.
     */
    public synchronized void requestToFinish() {
        this.finishRequested = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        rk4sys(this.time, xpsysFunction.getInitX(), this.dt, this.nsteps,
                xpsysFunction);
    }

    /**
     * Make calculations stop momementarily.
     */
    public synchronized void pause() {
        this.pause = true;
    }

    /**
     * If calculations have been paused, then they will continue from the last
     * calculation.
     */
    public synchronized void resume() {
        this.pause = false;
    }

    /**
     * @return true iff calculations are momentarily halted
     */
    public synchronized boolean isPaused() {
        return this.pause;
    }

    /**
     * Cause the thread to wait momentarily.
     */
    private void sleep() {
        try {
            while (isPaused()) {
                Thread.sleep(250);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The number of milliseconds between each calculation loop.
     * 
     * @param speed
     *            Must be >= 0
     */
    public synchronized void setDelay(int speed) {
        this.delay = speed;
    }

    /**
     * @return The delay between loop calculations in milliseconds.
     */
    public synchronized int getDelay() {
        return this.delay;
    }
}
