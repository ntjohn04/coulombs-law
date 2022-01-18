import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SimulationDriver extends Application {

	public static PointList pointList;
	
	Group root;
	Scene scene;
	
	public boolean proton;
	public boolean electron;
	
	public TextField radiusText;
	public TextField tetherText;
	public TextField chargeText;
	
	public static double customRadius = 25;
	public static double customTether = 0;
	public static double customCharge;
	
	public static int backforth = 2;
	
	Button customBut;
	Button proBut;
	Button eleBut;
	Button pause;
	Button reset;
	Button stop;

	CheckBox zAxis;
	static CheckBox toggleGrav;
	static CheckBox toggleBounds;
	static CheckBox toggleCollisions;
	CheckBox toggleTether;
	Button changeBackground;
	
	Random rand;
	
	public InputStream yellowpink;
	public InputStream bluegreen;
	public InputStream redblue;
	public InputStream greenpurple;
	public InputStream greenred;
	public InputStream blueyellow;
	
	public Image iyellowpink;
	public Image ibluegreen;
	public Image iredblue;
	public Image igreenpurple;
	public Image igreenred;
	public Image iblueyellow;
	
	public ImageView background1;
	public ImageView background2;
	public ImageView background3;
	public ImageView background4;
	public ImageView background5;
	public ImageView background6;
	
	static public int bckCnt = 1;
	
	PointLight light1;
	PointLight light2;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		rand = new Random();
		
		light1 = new PointLight();
		light2 = new PointLight();
		
		light1.setTranslateX(600);
		light1.setTranslateY(430);
		light1.setTranslateZ(-500);
		
		light2.setTranslateX(600);
		light2.setTranslateY(430);
		light2.setTranslateZ(5500);
		
		light2.setRotate(180);
		
		yellowpink = new FileInputStream("src\\backgrounds\\yellowpink.jpg");
		iyellowpink = new Image(yellowpink);
		bluegreen = new FileInputStream("src\\backgrounds\\bluegreen.jpg");
		ibluegreen = new Image(bluegreen);
		redblue = new FileInputStream("src\\backgrounds\\redblue.jpg");
		iredblue = new Image(redblue);
		greenpurple = new FileInputStream("src\\backgrounds\\greenpurple.jpg");
		igreenpurple = new Image(greenpurple);
		greenred = new FileInputStream("src\\backgrounds\\greenred.jpg");
		igreenred = new Image(greenred);
		blueyellow = new FileInputStream("src\\backgrounds\\blueyellow.jpg");
		iblueyellow = new Image(blueyellow);
		
		background1 = new ImageView();
		background2 = new ImageView();
		background3 = new ImageView();
		background4 = new ImageView();
		background5 = new ImageView();
		background6 = new ImageView();
		
		background1.setImage(iyellowpink);
		background2.setImage(ibluegreen);
		background3.setImage(iredblue);
		background4.setImage(igreenpurple);
		background5.setImage(igreenred);
		background6.setImage(iblueyellow);
		
		background1.toBack();
		background2.toBack();
		background3.toBack();
		background4.toBack();
		background5.toBack();
		background6.toBack();
		
		background2.setVisible(false);
		background3.setVisible(false);
		background4.setVisible(false);
		background5.setVisible(false);
		background6.setVisible(false);
		
		pointList = new PointList();
		
		radiusText = new TextField("Enter Radius (Def 25)");
		radiusText.setOnMouseClicked(this::processRadiusTextClick);
		radiusText.setOnKeyPressed(this::processRadiusTextKey);
		
		tetherText = new TextField("Enter Tether Radius");
		tetherText.setOnMouseClicked(this::processTetherTextClick);
		tetherText.setOnKeyPressed(this::processTetherTextKey);
		
		chargeText = new TextField("Enter Charge (qe)");
		chargeText.setOnMouseClicked(this::processChargeTextClick);
		chargeText.setOnKeyPressed(this::processChargeTextKey);
		
		zAxis = new CheckBox("Z Axis Variaton");
		
		toggleGrav = new CheckBox("Toggle Gravity");
		toggleGrav.setOnAction(this::processGrav);
		
		toggleCollisions = new CheckBox("Toggle Collisions");
		
		toggleTether = new CheckBox("Toggle Tether");
		
		toggleBounds = new CheckBox("Disable Bounds");
		
		changeBackground = new Button("Change Background");
		changeBackground.setOnAction(this::cycleBackground);
		
		customBut = new Button("Custom");
		customBut.setOnAction(this::setCustom);
		
		proBut = new Button("Proton");
		proBut.setOnAction(this::setProton);
		
		eleBut = new Button("Electron");
		eleBut.setOnAction(this::setElectron);
		
		pause = new Button("Pause/Play");
		pause.setOnAction(this::pause);
		
		reset = new Button("Reset");
		reset.setOnAction(this::reset);
		
		stop = new Button("Stop");
		stop.setOnAction(this::stop);
		
		VBox buttons = new VBox(pause, reset, stop, changeBackground, zAxis, toggleGrav, toggleBounds, toggleCollisions, toggleTether, radiusText, tetherText, chargeText, customBut, proBut, eleBut);
		
		setProton(null);
		
		Group backgrounds = new Group(background1, background2, background3, background4, background5, background6);
		
		root = new Group(backgrounds, buttons);
		
		root.getChildren().addAll(light1, light2);

		scene = new Scene(root, 1200, 960);
		scene.setCamera(new PerspectiveCamera());
		
		scene.setOnMouseClicked(this::processClick);
		
		primaryStage.setTitle("RealTime Simulator v0.02");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	public void setProton(ActionEvent event) {
		electron = false;
		proton = true;
		proBut.setTextFill(Color.RED);
		eleBut.setTextFill(Color.BLACK);
		customBut.setTextFill(Color.BLACK);
	}
	
	public void setElectron(ActionEvent event) {
		electron = true;
		proton = false;
		proBut.setTextFill(Color.BLACK);
		eleBut.setTextFill(Color.BLUE);
		customBut.setTextFill(Color.BLACK);
	}
	
	public void setCustom(ActionEvent event) {
		try {
			customCharge = Double.parseDouble(chargeText.getText())*0.000005;
			electron = false;
			proton = false;
			proBut.setTextFill(Color.BLACK);
			eleBut.setTextFill(Color.BLACK);
			customBut.setTextFill(Color.PURPLE);
		}
		catch (NumberFormatException e){
			
		}
		
	}
	
	public void pause(ActionEvent event) {
		PointList.pause();
	}

	public void reset(ActionEvent event) {
		PointList.resetList();
	}
	
	public void stop(ActionEvent event) {
		PointList.stopVel();
	}
	
	public static void main(String[] args) {
		launch(args);

	}
	
	public void processGrav(ActionEvent event) {
		if (toggleGrav.isSelected() == false) PointList.removeGrav();
	}
	
	public void processClick(MouseEvent event) {
		
		int z;
		if (zAxis.isSelected() == true) {
			 z = rand.nextInt(100);
			if (rand.nextDouble() >= 0.5) z = -z;
		}
		else z = 0;
		
		if (toggleTether.isSelected() == true && proton == true) {
			pointList.addPoint(0.000005, 25, event.getX(), event.getY(), z);
			PointList.pointList[PointList.count].addTether(customTether);
		}
		else if (toggleTether.isSelected() == true && electron == true) {
			pointList.addPoint(-0.000005, 25, event.getX(), event.getY(), z);
			PointList.pointList[PointList.count].addTether(customTether);
		}
		else if (toggleTether.isSelected() == true) {
			pointList.addPoint(customCharge, customRadius, event.getX(), event.getY(), z);
			PointList.pointList[PointList.count].addTether(customTether);
		}
		else if (proton == true)pointList.addPoint(0.000005, 25, event.getX(), event.getY(), z);
		else if (electron == true)pointList.addPoint(-0.000005, 25, event.getX(), event.getY(), z);
		else pointList.addPoint(customCharge, customRadius, event.getX(), event.getY(), z);
		root.getChildren().addAll(PointList.pointList[PointList.count]);
		
		System.out.println(PointList.count + "Point Charges");

	}
	
	public void processRadiusTextClick(MouseEvent event) {
		radiusText.selectAll();
	}
	
	public void processRadiusTextKey(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			
			try {
            customRadius = Double.parseDouble(radiusText.getText());
			}
			catch (NumberFormatException e){
				
			}
        }
	}
	
	public void processTetherTextClick(MouseEvent event) {
		tetherText.selectAll();
	}
	
	public void processTetherTextKey(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			try {
	            customTether = Double.parseDouble(tetherText.getText());
				}
				catch (NumberFormatException e){
					
				}
        }
	}
	
	public void processChargeTextClick(MouseEvent event) {
		chargeText.selectAll();
	}
	
	public void processChargeTextKey(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
            setCustom(null);
        }
	}
	
	public void cycleBackground(ActionEvent event) {
		switch(bckCnt) {
		case 1: background1.setVisible(false);
				background2.setVisible(true);
				bckCnt++;
				return;
		case 2: background2.setVisible(false);
				background3.setVisible(true);
				bckCnt++;
				return;
		case 3: background3.setVisible(false);
				background4.setVisible(true);
				bckCnt++;
				return;
		case 4: background4.setVisible(false);
				background5.setVisible(true);
				bckCnt++;
				return;
		case 5: background5.setVisible(false);
				background6.setVisible(true);
				bckCnt++;
				return;
		case 6: background6.setVisible(false);
				background1.setVisible(true);
				bckCnt = 1;
				return;
		}
	}
}
