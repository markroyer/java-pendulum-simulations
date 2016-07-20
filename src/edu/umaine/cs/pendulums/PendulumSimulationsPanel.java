package edu.umaine.cs.pendulums;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.media.j3d.Alpha;
import javax.media.j3d.BadTransformException;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * A panel that contains all of the graphical user interface for running a
 * pendulum simulation. It also contains the a panel that draws the 3d
 * representation of the pendulum.
 * 
 * @author Mark Royer
 * 
 */
public class PendulumSimulationsPanel extends Panel implements ActionListener,
        ChangeListener, ItemListener, KeyListener, MouseListener {

    /**
     * For serializability
     */
    private static final long serialVersionUID = 445689538693254936L;

    /**
     * Contains radio buttons for the simulation type.
     */
    private JPanel topPanel;

    /**
     * Contains spinners and values that change the initial conditions for the
     * simulation.
     */
    private JPanel middlePanel;

    /**
     * Contains buttons that manipulate a running simulation.
     */
    private JPanel bottomPanel;

    /**
     * Inner pendulum angle (degrees).
     */
    private JSpinner angle1Spinner;

    /**
     * Outer pendulum angle (degrees).
     */
    private JSpinner angle2Spinner;

    /**
     * Inner pendulum length.
     */
    private JSpinner l1Spinner;

    /**
     * Outer pendulum length.
     */
    private JSpinner l2Spinner;

    /**
     * Inner pendulum mass.
     */
    private JSpinner m1Spinner;

    /**
     * Outer pendulum mass.
     */
    private JSpinner m2Spinner;

    /**
     * Inner pendulum spring constant.
     */
    private JSpinner k1Spinner;

    /**
     * Outer pendulum spring constant.
     */
    private JSpinner k2Spinner;

    /**
     * Run a single pendulum simulation.
     */
    private JRadioButton simplePendulum;

    /**
     * Run a single pendulum simulation as if the rod were a spring.
     */
    private JRadioButton springPendulum;

    /**
     * Run a double pendulum simulation.
     */
    private JRadioButton doublePendulum;

    /**
     * Run a double pendulum simulation as if the rods were springs.
     */
    private JRadioButton doubleSpringPendulum;

    /**
     * Contains the various pendulum simulation types.
     */
    private ButtonGroup pendulumButtonGroup;

    /**
     * Make the pendulum 3d window black.
     */
    private JButton clearScreenButton;

    /**
     * Show a window with helpful information about the GUI.
     */
    private JButton helpButton;

    /**
     * Restart/Start the simulation with the current initial values.
     */
    private JButton resetButton;

    /**
     * Add the current initial values to the combination box.
     */
    private JButton saveInitialValuesButton;

    /**
     * A list of saved initial values.
     */
    private JComboBox initialValuesCombo;

    /**
     * The current simulation object.
     */
    private RK4SYS currentSimulation;

    /**
     * The 3d canvas that is currently being displayed to the user.
     */
    private Canvas3D currentCanvas;

    /**
     * The graphical representation of an x,y and z axis.
     */
    private Axis axis;

    /**
     * If checked, the axis is drawn.
     */
    private JCheckBox showAxis;

    /**
     * This is a Java3d transform that makes the pendulum rotate.
     */
    private Alpha alphaRotateAxis;

    /**
     * If checked, the pendulum will rotated about the y axis.
     */
    private JCheckBox rotateAxis;

    /**
     * If checked, the simulation will run. If unchecked, the simulation will
     * pause.
     */
    private JCheckBox runSimulation;

    /**
     * How fast the simulation will run. 0 is the slowest value, 10 is the
     * fastest value.
     */
    private JSpinner simulationSpeed;

    /**
     * The thread the current simulation is being run in.
     */
    private Thread currentThread;

    /**
     * The calculations for how the pendulum moves.
     */
    private PendulumBehavior currentPendulum;

    /**
     * The panel that contains the 3d visualization of the pendulum.
     */
    private Panel canvasPanel;

    /**
     * Initial values for different simulations. These are loaded into the
     * combination box when the simulation is selected.
     */
    private Map<JRadioButton, Vector<InitValues>> initValuesMap;

    /**
     * Run the pendulum simulation program in a window.
     * 
     * @param args
     *            Not Used
     */
    public static void main(String[] args) {
        Frame frame = new Frame("Pendulum Simulations");

        PendulumSimulationsPanel ex = new PendulumSimulationsPanel();
        frame.add(ex);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Dimension dimension = new Dimension(660, 660);
        frame.setPreferredSize(dimension);
        frame.setMinimumSize(dimension);

        frame.pack();
        frame.validate();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        frame.requestFocus();
    }

    /**
     * Create and initialize all of the components required for pendulum
     * simulations.
     */
    public PendulumSimulationsPanel() {

        JPanel bottomSplit = new JPanel();
        bottomSplit.setLayout(new BoxLayout(bottomSplit, BoxLayout.Y_AXIS));

        topPanel = new JPanel();
        FlowLayout topLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
        topPanel.setLayout(topLayout);
        pendulumButtonGroup = new ButtonGroup();

        simplePendulum = new JRadioButton("Pendulum");
        pendulumButtonGroup.add(simplePendulum);
        topPanel.add(simplePendulum);
        simplePendulum.addActionListener(this);

        springPendulum = new JRadioButton("Spring Pendulum");
        pendulumButtonGroup.add(springPendulum);
        topPanel.add(springPendulum);
        springPendulum.addActionListener(this);

        doublePendulum = new JRadioButton("Double Pendulum");
        pendulumButtonGroup.add(doublePendulum);
        topPanel.add(doublePendulum);
        doublePendulum.addActionListener(this);

        doubleSpringPendulum = new JRadioButton("Double Spring Pendulum");
        pendulumButtonGroup.add(doubleSpringPendulum);
        topPanel.add(doubleSpringPendulum);
        doubleSpringPendulum.addActionListener(this);

        JPanel midTop = new JPanel();
        GridLayout middleLayout = new GridLayout(2, 4, 0, 5);
        midTop.setLayout(middleLayout);
        angle1Spinner = addAngleSpinner(midTop, 90, "a1");
        angle2Spinner = addAngleSpinner(midTop, 90, "a2");
        l1Spinner = addSpinner(midTop, 5, "l1");
        l2Spinner = addSpinner(midTop, 5, "l2");
        m1Spinner = addSpinner(midTop, 1, "m1");
        m2Spinner = addSpinner(midTop, 1, "m2");
        k1Spinner = addSpinner(midTop, 1, "k1");
        k2Spinner = addSpinner(midTop, 1, "k2");

        JPanel midBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));

        saveInitialValuesButton = new JButton("Save Values");
        addButton(midBottom, saveInitialValuesButton);
        saveInitialValuesButton.addActionListener(this);

        initialValuesCombo = new JComboBox();
        initialValuesCombo.setPrototypeDisplayValue("WWWWWWWWWWWWWWWWWWWW"
                + "WWWWWWWWWWWWWW");
        addButton(midBottom, initialValuesCombo);
        initialValuesCombo.addItemListener(this);

        middlePanel = new JPanel(new GridLayout(2, 1, 10, 5));
        middlePanel.add(midTop);
        middlePanel.add(midBottom);

        bottomPanel = new JPanel();
        FlowLayout bottomLayout = new FlowLayout();
        bottomPanel.setLayout(bottomLayout);

        helpButton = new JButton(UIManager.getIcon("OptionPane.questionIcon"));
        addButton(bottomPanel, helpButton);
        helpButton.addActionListener(this);

        clearScreenButton = new JButton("Clear Screen");
        addButton(bottomPanel, clearScreenButton);
        clearScreenButton.addActionListener(this);

        resetButton = new JButton("Reset/Start");
        addButton(bottomPanel, resetButton);
        resetButton.addActionListener(this);

        JPanel bottomRight = new JPanel(new GridLayout(2, 2));

        showAxis = new JCheckBox("Show Axis", true);
        bottomRight.add(showAxis);
        showAxis.addItemListener(this);

        rotateAxis = new JCheckBox("Rotate", true);
        bottomRight.add(rotateAxis);
        rotateAxis.addItemListener(this);

        runSimulation = new JCheckBox("Run Sim", true);
        bottomRight.add(runSimulation);
        runSimulation.addItemListener(this);

        simulationSpeed = addSpinner(bottomRight, 1, "Speed");
        simulationSpeed.setModel(new SpinnerNumberModel(8, 1, 10, 1));

        bottomPanel.add(bottomRight);

        simplePendulum.setSelected(true);
        createDefaultValues();
        updateInitialValuesCombo(simplePendulum);

        JPanel borderedPanel = new JPanel();
        borderedPanel
                .setBorder(BorderFactory.createTitledBorder("Simulations"));
        borderedPanel.add(topPanel);
        bottomSplit.add(borderedPanel);

        borderedPanel = new JPanel();
        borderedPanel.setBorder(BorderFactory
                .createTitledBorder("Editable Values"));
        borderedPanel.add(middlePanel);
        bottomSplit.add(borderedPanel);

        borderedPanel = new JPanel();
        borderedPanel.setBorder(BorderFactory
                .createTitledBorder("Simulation Display"));
        borderedPanel.add(bottomPanel);
        bottomSplit.add(borderedPanel);

        canvasPanel = new Panel(new GridLayout(1, 1));
        canvasPanel.setLayout(new BoxLayout(canvasPanel, 1));
        canvasPanel.setBackground(Color.black);
        setNewCanvas(createSimplePendulumCanvas());

        this.setLayout(new BorderLayout());
        this.add(canvasPanel, BorderLayout.CENTER);
        bottomSplit.setMinimumSize(new Dimension(600, 250));
        this.add(bottomSplit, BorderLayout.SOUTH);

        this.validate();
    }

    /**
     * Wrap the given component with another panel and then add that to the
     * given panel.
     * 
     * @param panel
     *            Panel to add the component to
     * @param component
     *            The component
     */
    private void addButton(JPanel panel, JComponent component) {
        JPanel tmp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tmp.add(component);
        panel.add(tmp);
    }

    /**
     * Create a spinner and a label and add them to the given panel.
     * 
     * @param panel
     *            The panel to add the spinner to
     * @param initValue
     *            The initial value of the spinner
     * @param label
     *            The spinner's label
     * @return The newly created spinner
     */
    private JSpinner addSpinner(JPanel panel, double initValue, String label) {
        JLabel l = new JLabel(label + ":");
        FlowLayout layout = new FlowLayout(FlowLayout.RIGHT, 5, 0);
        JPanel thisPanel = new JPanel(layout);
        thisPanel.add(l);
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(initValue, 0.1, 1000000, 0.1));
        JFormattedTextField tf = ((JSpinner.NumberEditor) spinner.getEditor())
                .getTextField();
        tf.setColumns(6);
        spinner.addChangeListener(this);
        l.setLabelFor(spinner);
        thisPanel.add(spinner);
        panel.add(thisPanel);

        return spinner;
    }

    /**
     * Add an angle spinner to the given panel.
     * 
     * @param panel
     *            The panel to have the spinner added to
     * @param initValue
     *            The initial value of the spinner
     * @param label
     *            The spinner's label
     * @return The newly created spinner
     */
    private JSpinner addAngleSpinner(JPanel panel, double initValue,
            String label) {
        FlowLayout layout = new FlowLayout(FlowLayout.RIGHT, 5, 0);
        JPanel thisPanel = new JPanel(layout);
        JLabel l = new JLabel(label + ":");
        thisPanel.add(l);
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(initValue, -360, 360, 0.1));
        JFormattedTextField tf = ((JSpinner.NumberEditor) spinner.getEditor())
                .getTextField();
        tf.setColumns(6);
        spinner.addChangeListener(this);
        l.setLabelFor(spinner);
        thisPanel.add(spinner);
        panel.add(thisPanel);

        return spinner;
    }

    /**
     * Create the objects that will be in the scene.
     * 
     * @param pendulum
     *            The pendulum that will be placed in the scene
     * @return The entire scene
     */
    public BranchGroup createSceneGraph(BranchGroup pendulum) {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

        TransformGroup objRotate = new TransformGroup();
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // objRoot.addChild(objRotate);

        TransformGroup spinner = new TransformGroup();
        spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        spinner.addChild(pendulum);

        objRotate.addChild(spinner);

        objRoot.addChild(objRotate);

        alphaRotateAxis = new Alpha(-1, 14000);
        alphaRotateAxis.setTriggerTime(System.currentTimeMillis()
                - alphaRotateAxis.getStartTime());
        setRotate(rotateAxis.isSelected());
        Transform3D rot = new Transform3D();

        RotationInterpolator rotate;

        rotate = new RotationInterpolator(alphaRotateAxis, spinner, rot, 0f,
                (float) (-2 * Math.PI));

        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 101.0);
        rotate.setSchedulingBounds(bounds);

        spinner.addChild(rotate);

        axis = new Axis();
        axis.setVisible(showAxis.isSelected());
        spinner.addChild(axis);

        DirectionalLight dl = new DirectionalLight(new Color3f(Color.white),
                new Vector3f(-10f, -10f, -10f));
        BoundingSphere bounds2 = new BoundingSphere(new Point3d(10.0, 10.0,
                10.0), 100.0);
        dl.setInfluencingBounds(bounds2);
        objRoot.addChild(dl);

        // Let Java 3D perform optimizations on this scene graph.
        objRoot.compile();

        return objRoot;
    }

    /**
     * Rotates the pendulum if and only if selected = true.
     * 
     * @param selected
     */
    private void setRotate(boolean selected) {
        if (selected) {
            alphaRotateAxis.resume();
        } else {
            alphaRotateAxis.pause();
        }
    }

    /**
     * Creates a new 3d environment to display the simulation.
     * 
     * @param function
     *            How the pendulum will behave
     * @param pbg
     *            The scene
     * @return The newly created 3d environment
     */
    public Canvas3D createPendulumApp(PendulumBehavior function, BranchGroup pbg) {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);

        BranchGroup scene = createSceneGraph(pbg);

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        simpleU.getViewer().getView().setBackClipDistance(100);

        setInitialConditions(function);

        // Move the view back proportional to the length of the pendulum's rods.
        simpleU.getViewingPlatform().setNominalViewingTransform();
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3d(0.0, 0, Math.sqrt(4 * Math.pow(2.6 * function
                .getTotalLength(), 2))));
        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(
                t3d);

        // Allow the user to click on the scene and spin the pendulum.
        OrbitBehavior orbit = new OrbitBehavior(simpleU.getCanvas(),
                OrbitBehavior.REVERSE_ROTATE);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                200.0);
        orbit.setSchedulingBounds(bounds);
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);

        simpleU.addBranchGraph(scene);

        currentSimulation = new RK4SYS(function, 0, 0.003,
                RK4SYS.RUNINDEFINATELY, getSimulationSpeed());

        currentThread = new Thread(currentSimulation);
        updateRunSimulation();

        return canvas3D;
    }

    /**
     * Setup the pendulum's movement.
     * 
     * @param function
     *            The way that the pendulum moves
     */
    private void setInitialConditions(PendulumBehavior function) {

        function.setRodAngle(0, Math.toRadians(((Double) angle1Spinner
                .getValue())));
        function.setRodAngle(1, Math.toRadians(((Double) angle2Spinner
                .getValue())));
        function.setRodLength(0, ((Double) l1Spinner.getValue()));
        function.setRodLength(1, ((Double) l2Spinner.getValue()));

        function.setMass(0, ((Double) m1Spinner.getValue()));
        function.setMass(1, ((Double) m2Spinner.getValue()));
        function.setK(0, ((Double) k1Spinner.getValue()));
        function.setK(1, ((Double) k2Spinner.getValue()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == simplePendulum) {
            currentSimulation.requestToFinish();
            updateInitialValuesCombo(simplePendulum);
            setNewCanvas(createSimplePendulumCanvas());
        } else if (source == springPendulum) {
            currentSimulation.requestToFinish();
            updateInitialValuesCombo(springPendulum);
            setNewCanvas(createSpringPendulumCanvas());
        } else if (source == doublePendulum) {
            currentSimulation.requestToFinish();
            updateInitialValuesCombo(doublePendulum);
            setNewCanvas(createDoublePendulumCanvas());
        } else if (source == doubleSpringPendulum) {
            currentSimulation.requestToFinish();
            updateInitialValuesCombo(doubleSpringPendulum);
            setNewCanvas(createDoubleSpringPendulumCanvas());
        } else if (source == clearScreenButton) {
            setNewCanvas(null);
        } else if (source == resetButton) {
            resetOrStartSimulation();
        } else if (source == saveInitialValuesButton) {

            InitValues initValues = new InitValues(((Double) (angle1Spinner
                    .getValue())), ((Double) (angle2Spinner.getValue())),
                    ((Double) (l1Spinner.getValue())), ((Double) (l2Spinner
                            .getValue())), ((Double) (m1Spinner.getValue())),
                    ((Double) (m2Spinner.getValue())), ((Double) (k1Spinner
                            .getValue())), ((Double) (k2Spinner.getValue())));

            JRadioButton b = null;

            if (simplePendulum.isSelected()) {
                b = simplePendulum;
            } else if (springPendulum.isSelected()) {
                b = springPendulum;
            } else if (doublePendulum.isSelected()) {
                b = doublePendulum;
            } else if (doubleSpringPendulum.isSelected()) {
                b = doubleSpringPendulum;
            }

            initValuesMap.get(b).add(initValues);
            initialValuesCombo.addItem(initValues);

        } else if (helpButton.equals(source)) {
            JFrame f = new HelpMessageFrame();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationRelativeTo(null);
            f.setVisible(true);

        }
    }

    /**
     * Start the simulation if it hasn't been started. If the simulation is
     * going restart it based on the current initial values.
     */
    private void resetOrStartSimulation() {

        if (this.currentCanvas == null) {
            resetSimulation();
        } else if (!this.currentThread.isAlive()
                && !this.currentSimulation.hasRequestToFinish()) {
            this.currentThread.start();
        } else {
            resetSimulation();
        }
    }

    /**
     * Restart the simulation based on the current values.
     */
    private void resetSimulation() {
        currentSimulation.requestToFinish();
        runSimulation.setSelected(true);

        if (simplePendulum.isSelected()) {
            setNewCanvas(createSimplePendulumCanvas());
        } else if (springPendulum.isSelected()) {
            setNewCanvas(createSpringPendulumCanvas());
        } else if (doublePendulum.isSelected()) {
            setNewCanvas(createDoublePendulumCanvas());
        } else if (doubleSpringPendulum.isSelected()) {
            setNewCanvas(createDoubleSpringPendulumCanvas());
        }
    }

    /**
     * Replaces the current 3d environment with the given canvas.
     * 
     * @param newCanvas
     *            The new 3d environment
     */
    private void setNewCanvas(Canvas3D newCanvas) {

        if (currentCanvas != null) {
            canvasPanel.remove(currentCanvas);
        }

        currentCanvas = newCanvas;

        if (newCanvas != null) {
            canvasPanel.add(newCanvas);
            currentCanvas.addKeyListener(this);
            currentCanvas.addMouseListener(this);
        }

        canvasPanel.validate();
        canvasPanel.repaint();

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {

        Object source = e.getSource();

        if (currentPendulum == null) {
            return;
        }

        try {

            if (m1Spinner.equals(source)) {
                currentPendulum.setMass(0, (Double) m1Spinner.getValue());
            } else if (m2Spinner.equals(source)) {
                currentPendulum.setMass(1, (Double) m2Spinner.getValue());
            } else if (angle1Spinner.equals(source)) {
                currentPendulum.setRodAngle(0, Math
                        .toRadians((Double) angle1Spinner.getValue()));
            } else if (angle2Spinner.equals(source)) {
                currentPendulum.setRodAngle(1, Math
                        .toRadians((Double) angle2Spinner.getValue()));
            } else if (l1Spinner.equals(source)) {
                currentPendulum.setRodLength(0, (Double) l1Spinner.getValue());
            } else if (l2Spinner.equals(source)) {
                currentPendulum.setRodLength(1, (Double) l2Spinner.getValue());
            } else if (k1Spinner.equals(source)) {
                currentPendulum.setK(0, (Double) k1Spinner.getValue());
            } else if (k2Spinner.equals(source)) {
                currentPendulum.setK(1, (Double) k2Spinner.getValue());
            } else if (simulationSpeed.equals(source)) {
                this.currentSimulation.setDelay(getSimulationSpeed());
            }

        } catch (BadTransformException e1) {
            this.currentSimulation.errorInCalculationMessage();
            resetSimulation();
        }
    }

    /**
     * @return The current speed specified by the simulation spinner inverted
     */
    private int getSimulationSpeed() {
        SpinnerNumberModel m = (SpinnerNumberModel) simulationSpeed.getModel();
        return (Integer) m.getMaximum() - (Integer) simulationSpeed.getValue()
                + 1;
    }

    /**
     * @return A canvas with just a single pendulum
     */
    public Canvas3D createSimplePendulumCanvas() {
        Pendulum pendulum = new Pendulum();
        currentPendulum = new SimplePendulum(pendulum);
        return createPendulumApp(currentPendulum, pendulum.getBG());
    }

    /**
     * @return A canvas with a single pendulum where the rod behaves as if it is
     *         a spring
     */
    public Canvas3D createSpringPendulumCanvas() {
        Pendulum pendulum = new Pendulum();
        currentPendulum = new SpringPendulum(pendulum);
        return createPendulumApp(currentPendulum, pendulum.getBG());
    }

    /**
     * @return A canvas with a double pendulum
     */
    private Canvas3D createDoublePendulumCanvas() {
        DoublePendulum pendulum = new DoublePendulum();
        currentPendulum = new SimpleDoublePendulum(pendulum);
        return createPendulumApp(currentPendulum, pendulum.getBG());

    }

    /**
     * @return A canvas with a double pendulum where the rods behave as if they
     *         are springs
     */
    private Canvas3D createDoubleSpringPendulumCanvas() {
        DoublePendulum pendulum = new DoublePendulum();
        currentPendulum = new SpringDoublePendulum(pendulum);
        return createPendulumApp(currentPendulum, pendulum.getBG());
    }

    /**
     * Create some default values and put them in the initial values combination
     * box.
     */
    private void createDefaultValues() {

        initValuesMap = new HashMap<JRadioButton, Vector<InitValues>>();

        initValuesMap.put(simplePendulum, new Vector<InitValues>());
        initValuesMap.put(springPendulum, new Vector<InitValues>());
        initValuesMap.put(doublePendulum, new Vector<InitValues>());
        initValuesMap.put(doubleSpringPendulum, new Vector<InitValues>());

        double kValue = 20;

        InitValues v1 = new InitValues(0, 0, 5, 5, 1, 1, kValue, kValue);
        InitValues v2 = new InitValues(0, 90, 5, 5, 2000, 1.5, 800000, 800000);
        InitValues v3 = new InitValues(45, 45, 5, 5, 2, 3000, 800000, 800000);
        InitValues v4 = new InitValues(135, 135, 5, 5, 1, 1, kValue, kValue);
        InitValues v5 = new InitValues(180, 180, 5, 5, 1, 1, kValue, kValue);

        for (Vector<InitValues> v : initValuesMap.values()) {
            v.add(v1);
            v.add(v5);
            v.add(v2);
            v.add(v3);
            v.add(v4);

        }
    }

    /**
     * Changed the simulation initial values that have been saved for the given
     * type of simulation.
     * 
     * @param button
     *            The current simulation type
     */
    private void updateInitialValuesCombo(JRadioButton button) {
        initialValuesCombo.removeAllItems();

        Vector<InitValues> values = initValuesMap.get(button);

        for (InitValues i : values) {
            initialValuesCombo.addItem(i);
        }

        initialValuesCombo.setSelectedItem(values.get(0));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {

        Object source = e.getSource();

        if (initialValuesCombo.equals(source)) {

            InitValues i = (InitValues) initialValuesCombo.getSelectedItem();

            if (i == null) {
                return; // Nothing selected
            }

            angle1Spinner.setValue(i.getA1());
            angle2Spinner.setValue(i.getA2());

            l1Spinner.setValue(i.getL1());
            l2Spinner.setValue(i.getL2());

            m1Spinner.setValue(i.getM1());
            m2Spinner.setValue(i.getM2());

            k1Spinner.setValue(i.getK1());
            k2Spinner.setValue(i.getK2());

            if (this.currentThread != null)
                resetSimulation();

        } else if (showAxis.equals(source)) {
            axis.setVisible(showAxis.isSelected());
        } else if (rotateAxis.equals(source)) {
            setRotate(rotateAxis.isSelected());
        } else if (runSimulation.equals(source)) {
            updateRunSimulation();
        }
    }

    /**
     * Runs the simulation if the run simulation check box is selected,
     * otherwise the simulation is paused.
     */
    private void updateRunSimulation() {
        if (!runSimulation.isSelected()) {
            currentSimulation.pause();
        } else {
            currentSimulation.resume();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_A) {
            SpinnerNumberModel m = (SpinnerNumberModel) angle1Spinner
                    .getModel();
            updateSpinnerValue(e, m);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            SpinnerNumberModel m = (SpinnerNumberModel) angle2Spinner
                    .getModel();
            updateSpinnerValue(e, m);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            SpinnerNumberModel m = (SpinnerNumberModel) l1Spinner.getModel();
            updateSpinnerValue(e, m);
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            SpinnerNumberModel m = (SpinnerNumberModel) l2Spinner.getModel();
            updateSpinnerValue(e, m);

            // Bottom Row
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            SpinnerNumberModel m = (SpinnerNumberModel) m1Spinner.getModel();
            updateSpinnerValue(e, m);
        } else if (e.getKeyCode() == KeyEvent.VK_X) {
            SpinnerNumberModel m = (SpinnerNumberModel) m2Spinner.getModel();
            updateSpinnerValue(e, m);
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            SpinnerNumberModel m = (SpinnerNumberModel) k1Spinner.getModel();
            updateSpinnerValue(e, m);
        } else if (e.getKeyCode() == KeyEvent.VK_V) {
            SpinnerNumberModel m = (SpinnerNumberModel) k2Spinner.getModel();
            updateSpinnerValue(e, m);

            // Quit and Reset buttons
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            resetOrStartSimulation();
        }

    }

    /**
     * Change the value of the spinner model according to the button that was
     * pressed.
     * 
     * @param e
     *            Key that was pressed
     * @param m
     *            The corresponding spinner model
     */
    private void updateSpinnerValue(KeyEvent e, SpinnerNumberModel m) {
        if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK) {
            if (m.getNextValue() != null)
                m.setValue(m.getNextValue());
        } else {
            if (m.getPreviousValue() != null)
                m.setValue(m.getPreviousValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {

        // We'll grab the focus if the mouse hovers over the simulation
        currentCanvas.requestFocus();
    }

    // The following methods are required by some of the listeners but are not
    // used.

    public void keyReleased(KeyEvent e) {
        // Do nothing
    }

    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    public void mouseClicked(MouseEvent e) {
        // Do nothing
    }

    public void mouseExited(MouseEvent e) {
        // Do nothing
    }

    public void mousePressed(MouseEvent e) {
        // Do nothing
    }

    public void mouseReleased(MouseEvent e) {
        // Do nothing
    }
}
