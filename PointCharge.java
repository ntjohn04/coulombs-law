import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class PointCharge extends Group {
	
	public double charge;
	
	public Sphere sphere;
	public PhongMaterial material;
	public Color color;
	
	public double origx;
	public double origy;
	public double origz;
	
	double velx = 0;
	double vely = 0;
	double velz = 0;
	
	double accx = 0;
	double accy = 0;
	double accz = 0;
	
	public Timeline time;
	public boolean isPause;
	
	public boolean isGrav;
	
	public KeyFrame f;
	
	Line tether;
	UVector uVtether;
	Boolean isTether;
	Double tetherRadius;
	Boolean outside = false;
	
	public PointCharge(double charge, double radius, double x, double y, double z) {
		
		this.isTether = false;
		
		this.charge = charge;
		
		material = new PhongMaterial();
		ColorTest();
		material.setDiffuseColor(color);
		
		sphere = new Sphere(radius);
		sphere.setMaterial(material);
		
		setX(x);
		setY(y);
		setZ(z);
	
		time = new Timeline(new KeyFrame(Duration.millis(25), new 
			EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				
				if (isTether == true && tetherRadius == 0.0) return;
				if (PointList.isPause == true) return;
				
				//if (Math.abs(velx) > 0.5) sphere.setTranslateX(sphere.getTranslateX()+Math.signum(velx)*75);
				sphere.setTranslateX(sphere.getTranslateX()+velx*75);
				//if (Math.abs(vely) > 0.5) sphere.setTranslateY(sphere.getTranslateY()+Math.signum(vely)*75);
				sphere.setTranslateY(sphere.getTranslateY()+vely*75);
				//if (Math.abs(velz) > 0.5) sphere.setTranslateZ(sphere.getTranslateZ()+Math.signum(velz)*75);
				sphere.setTranslateZ(sphere.getTranslateZ()+velz*75);
				
				
				processAcc();
				if (SimulationDriver.toggleBounds.isSelected() != true)processBounds();
				
			}
		}
		));
		
		time.setCycleCount(Timeline.INDEFINITE);
		time.play();
		
		
		getChildren().addAll(sphere);
	}
	
	public void ColorTest() {
		if (charge > 0.0) this.color = Color.RED;
		if (charge < 0.0) this.color = Color.BLUE;
		if (charge == 0.0) this.color = Color.GRAY;
	}
	
	public void setX(double posx) {
		this.origx = posx;
		sphere.setTranslateX(posx);
	}
	
	public void setY(double posy) {
		this.origy = posy;
		sphere.setTranslateY(posy);
	}
	
	public void setZ(double posz) {
		this.origz = posz;
		sphere.setTranslateZ(posz);
	}
	
	public void processAcc() {
		velx = velx + accx;
		vely = vely + accy;
		velz = velz + accz;
		
	}
	
	public void finalize() {
	}
	
	public void pause() {
		if (isPause != true) {
			time.pause();
			isPause = true;
		}
		else {
			time.play();
			isPause = false;
		}
	}
	
	public void processBounds() {
		if (sphere.getTranslateX() <= 0 + sphere.getRadius()) {
			velx = -velx;
			sphere.setTranslateX(sphere.getTranslateX()+5);
		}
		if (sphere.getTranslateX() >= 1200 - sphere.getRadius()) {
			velx = -velx;
			sphere.setTranslateX(sphere.getTranslateX()-5);
		}
		if (sphere.getTranslateY() >= 960 - sphere.getRadius()) {
			vely = -vely;
			sphere.setTranslateY(sphere.getTranslateY()-5);
		}
		if (sphere.getTranslateY() <= 50 + sphere.getRadius()) {
			vely = -vely;
			sphere.setTranslateY(sphere.getTranslateY()+5);
		}
		if (sphere.getTranslateZ() >= 500 - sphere.getRadius()) {
			velz = -velz;
			sphere.setTranslateZ(sphere.getTranslateZ()-5);
		}
		if (sphere.getTranslateZ() <= -500 + sphere.getRadius()) {
			velz = -velz;
			sphere.setTranslateZ(sphere.getTranslateZ()+5);
		}
	}
	
	public void addTether(double radius) {
		isTether = true;
		tetherRadius = radius;
		tether = new Line();
		tether.setStartX(origx);
		tether.setStartY(origy);
		tether.setEndX(origx);
		tether.setEndY(origy);
		tether.setStrokeWidth(10);
		tether.setStrokeLineCap(StrokeLineCap.ROUND);
		tether.setStroke(Color.BLACK);
		
		getChildren().addAll(tether);
		
		uVtether = new UVector(sphere.getTranslateX(), sphere.getTranslateY(), sphere.getTranslateZ());
	}
	
	public void processTether() {
		
		if (tetherRadius == 0.0) return;
		
		uVtether.x = origx-sphere.getTranslateX();
		uVtether.y = origy-sphere.getTranslateY();
		uVtether.z = origz-sphere.getTranslateZ();
		
		tether.setEndX(sphere.getTranslateX());
		tether.setEndY(sphere.getTranslateY());
		
		if (uVtether.magnitude() >= tetherRadius && outside == false) {
			
			this.velx *= -1;
			this.vely *= -1;
			this.velz *= -1;
			
			this.accx *= -1;
			this.accy *= -1;
			this.accz *= -1;
			
			
			outside = true;
			
			//this.velx += -Math.signum(origx-sphere.getTranslateX())*0.3;
			//this.vely += -Math.signum(origy-sphere.getTranslateZ())*0.3;
		//	this.velz += -Math.signum(origz-sphere.getTranslateZ())*0.3;
			
			//this.accx *= -(Math.signum(accx))*(uVtether.x-tetherRadius);
			//this.accy *= -(Math.signum(accy))*(uVtether.y-tetherRadius);
			//this.accz *= -(Math.signum(accz))*(uVtether.z-tetherRadius);
			
			
		}
		else if (uVtether.magnitude() <= tetherRadius) outside = false;
	}
}
