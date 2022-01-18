import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.util.Duration;

public class PointList extends Group {

	public static final double Ke = 8987551792.3;
	
	public static PointCharge[] pointList;
	public static int count = -1;
	
	public static Timeline time;
	public static boolean isPause = false;
	
	public PointList() {
		pointList = new PointCharge[125];
		
		time = new Timeline(new KeyFrame(Duration.millis(25), new 
				EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent t) {
					processForces();
				}
			}
			));
			
			time.setCycleCount(Timeline.INDEFINITE);
			time.play();
		
	}
	
	public static void resetList() {
		for (int i = 0; i <= count; i++) {
			pointList[i].sphere.setDisable(true);
			pointList[i].sphere.setVisible(false);
			if (pointList[i].isTether == true) {
				pointList[i].tether.setDisable(true);
				pointList[i].tether.setVisible(false);
			}
		}
		pointList = null;
		pointList = new PointCharge[125];
		count = -1;
	}

	public void addPoint(double charge, double radius, double x, double y, double z) {
		count++;
		pointList[count] = new PointCharge(charge, radius, x, y, z);
	}
	
	public static void stopVel() {
		for (int i = 0; i <= count; i++) {
			pointList[i].velx = 0;
			pointList[i].vely = 0;
			pointList[i].velz = 0;
		}
	}
	
	public static void processForces() {
		if (isPause == true) return;
		clearAcc();
		
		if (SimulationDriver.toggleGrav.isSelected() == true) addGrav();
		if (SimulationDriver.toggleCollisions.isSelected() == true) processCollisions();
		
		boolean[][] dupeCheckArr = new boolean[125][125];
		
		for (int i = 0; i <= count; i++) {
			if (pointList[i].isTether == true) pointList[i].processTether();
			for (int j = 1; j <= count; j++) {
				dupeCheckArr[i][j] = true;
				if (dupeCheckArr[j][i] != true) eleForce(pointList[i], pointList[j]);
			}
		}
	}
	
	public static void processCollisions() {
		boolean[][] dupeCheckArr = new boolean[125][125];
		for (int i = 0; i <= count; i++) {
			for (int j = 1; j <= count; j++) {
				dupeCheckArr[i][j] = true;
				if (dupeCheckArr[j][i] != true) {
					
				//	if (pointList[i].sphere.getTranslateX() >= pointList[j].sphere.getTranslateX() && pointList[i].sphere.getTranslateY() >= pointList[j].sphere.getTranslateY() && pointList[i].sphere.getTranslateZ() >= pointList[j].sphere.getTranslateZ()) {
					//	if (pointList[i].sphere.getTranslateX()-pointList[i].sphere.getRadius() <= pointList[j].sphere.getTranslateX()+pointList[j].sphere.getRadius() || pointList[i].sphere.getTranslateY()-pointList[i].sphere.getRadius() <= pointList[j].sphere.getTranslateY()+pointList[j].sphere.getRadius() || pointList[i].sphere.getTranslateZ()-pointList[i].sphere.getRadius() <= pointList[j].sphere.getTranslateZ()+pointList[j].sphere.getRadius()) {
						//	collide(pointList[i], pointList[j]);
					//	}
					//}
					UVector collisionLine = new UVector(Math.abs(pointList[i].sphere.getTranslateX()-pointList[j].sphere.getTranslateX()), Math.abs(pointList[i].sphere.getTranslateY()-pointList[j].sphere.getTranslateY()), Math.abs(pointList[i].sphere.getTranslateZ()-pointList[j].sphere.getTranslateZ()));
					if (collisionLine.magnitude() <= pointList[i].sphere.getRadius()+pointList[j].sphere.getRadius()) collide(pointList[i], pointList[j], collisionLine);

				}
			}
		}
	}
	
	public static void collide(PointCharge i, PointCharge j, UVector line) {
		line.unitize();
		//System.out.println(line.x);
		//System.out.println(line.y);
		//System.out.println(line.z);
		
		//System.out.println("Velocity Before: " + i.velx + " --- " + i.vely + " --- " + i.velz);
		
		
		if (i.getTranslateX() > j.getTranslateX()) {
			i.setTranslateX(i.getTranslateX()+(5/1)*line.x);
			j.setTranslateX(j.getTranslateX()-(5/1)*line.x);
		}
		else if (i.getTranslateX() < j.getTranslateX()){
			i.setTranslateX(i.getTranslateX()-(5/1)*line.x);
			j.setTranslateX(j.getTranslateX()+(5/1)*line.x);
		}
		
		if (i.getTranslateY() > j.getTranslateY()) {
			i.setTranslateY(i.getTranslateY()+(5/1)*line.y);
			j.setTranslateY(j.getTranslateY()-(5/1)*line.y);
		}
		else if (i.getTranslateY() < j.getTranslateY()){
			i.setTranslateY(i.getTranslateY()-(5/1)*line.y);
			j.setTranslateY(j.getTranslateY()+(5/1)*line.y);
		}
		
		if (i.getTranslateZ() > j.getTranslateZ()) {
			i.setTranslateZ(i.getTranslateZ()+(5/1)*line.z);
			j.setTranslateZ(j.getTranslateZ()-(5/1)*line.z);
		}
		else if (i.getTranslateZ() < j.getTranslateZ()){
			i.setTranslateZ(i.getTranslateZ()-(5/1)*line.z);
			j.setTranslateZ(j.getTranslateZ()+(5/1)*line.z);
		}
		
		
		/* old code
		if (i.getTranslateX() > j.getTranslateX()) {
			i.setTranslateX(i.getTranslateX()+5*line.x);
			j.setTranslateX(j.getTranslateX()-5*line.x);
		}
		else if (i.getTranslateX() < j.getTranslateX()){
			i.setTranslateX(i.getTranslateX()-5*line.x);
			j.setTranslateX(j.getTranslateX()+5*line.x);
		}
		
		if (i.getTranslateY() > j.getTranslateY()) {
			i.setTranslateY(i.getTranslateY()+5*line.y);
			j.setTranslateY(j.getTranslateY()-5*line.y);
		}
		else if (i.getTranslateY() < j.getTranslateY()){
			i.setTranslateY(i.getTranslateY()-5*line.y);
			j.setTranslateY(j.getTranslateY()+5*line.y);
		}
		
		if (i.getTranslateZ() > j.getTranslateZ()) {
			i.setTranslateZ(i.getTranslateZ()+5*line.z);
			j.setTranslateZ(j.getTranslateZ()-5*line.z);
		}
		else if (i.getTranslateZ() < j.getTranslateZ()){
			i.setTranslateZ(i.getTranslateZ()-5*line.z);
			j.setTranslateZ(j.getTranslateZ()+5*line.z);
		}
		*/
		
		
		if (i.velx == 0) i.velx = (Math.signum(j.velx)) * ((Math.abs(i.velx)+Math.abs(j.velx))/2);
		else i.velx = -(Math.signum(i.velx)) * ((Math.abs(i.velx)+Math.abs(j.velx))/2);
		if (i.vely == 0) i.vely = (Math.signum(j.vely)) * ((Math.abs(i.vely)+Math.abs(j.vely))/2);
		else i.vely = -(Math.signum(i.vely)) * ((Math.abs(i.vely)+Math.abs(j.vely))/2);
		if (i.velz == 0) i.velz = (Math.signum(j.velz)) * ((Math.abs(i.velz)+Math.abs(j.velz))/2);
		else i.velz = -(Math.signum(i.velz)) * ((Math.abs(i.velz)+Math.abs(j.velz))/2);
		
		if (j.velx == 0) j.velx = (Math.signum(i.velx)) * ((Math.abs(i.velx)+Math.abs(j.velx))/2);
		else j.velx = -(Math.signum(j.velx)) * ((Math.abs(i.velx)+Math.abs(j.velx))/2);
		if (j.vely == 0) j.vely = (Math.signum(i.vely)) * ((Math.abs(i.vely)+Math.abs(j.vely))/2);
		else j.vely = -(Math.signum(j.vely)) * ((Math.abs(i.vely)+Math.abs(j.vely))/2);
		if (j.velz == 0) j.velz = (Math.signum(i.velz)) * ((Math.abs(i.velz)+Math.abs(j.velz))/2);
		else j.velz = -(Math.signum(j.velz)) * ((Math.abs(i.velz)+Math.abs(j.velz))/2);
		
		System.out.println("Velocity After: " + i.velx + " --- " + i.vely + " --- " + i.velz);
		
		/* old code
		i.velx = -i.velx;//*line.x;
		i.vely = -i.vely;//*line.y;
		i.velz = -i.velz;//*line.z;
		
		j.velx = -j.velx;//*line.x;
		j.vely = -j.vely;//*line.y;
		j.velz = -j.velz;//*line.z;
		*/
	}
	
	public static void clearAcc() {
		for (int i = 0; i <= count; i++) {
			pointList[i].accx = 0;
			if (pointList[i].isGrav == true) pointList[i].accy = 0.0028 ;
			else pointList[i].accy = 0;
			pointList[i].accz = 0;
		}
	}
	
	public static void addGrav() {
		for (int i = 0; i <= count; i++) {
			if (pointList[i].isGrav != true) {
			pointList[i].isGrav = true;
			System.out.println("added grav to " + pointList[i]);
			}
		}
	}
	
	public static void removeGrav() {
		for (int i = 0; i <= count; i++) {
			if (pointList[i].isGrav = true) {
			pointList[i].isGrav = false;
			System.out.println("removed grav from " + pointList[i]);
			}
		}
	}
	
	public static void pause() {
		if (isPause != true) {
			time.pause();
			isPause = true;
			clearAcc();
		}
		else {
			time.play();
			isPause = false;
		}
	}
	
	public static double distance(PointCharge q1, PointCharge q2) {
		 return Math.sqrt(Math.pow(q1.sphere.getTranslateX()-q2.sphere.getTranslateX(), 2) + 
				Math.pow(q1.sphere.getTranslateY()-q2.sphere.getTranslateY(), 2) + 
				Math.pow(q1.sphere.getTranslateZ()-q2.sphere.getTranslateZ(), 2));
	}
	
	public static void eleForce(PointCharge q1, PointCharge q2) {
		double eleForce = Math.abs((Ke*q1.charge*q2.charge)/(Math.pow(distance(q1, q2), 2)));
		
		//old code
		//double eleX = Math.abs((Ke*q1.charge*q2.charge)/(Math.pow(q1.sphere.getTranslateX()-q2.sphere.getTranslateX(), 2)));
		//double eleY = Math.abs((Ke*q1.charge*q2.charge)/(Math.pow(q1.sphere.getTranslateY()-q2.sphere.getTranslateY(), 2)));
		//double eleZ = Math.abs((Ke*q1.charge*q2.charge)/(Math.pow(q1.sphere.getTranslateZ()-q2.sphere.getTranslateZ(), 2)));
		
		UVector vect = new UVector(q1.sphere.getTranslateX()-q2.sphere.getTranslateX(), q1.sphere.getTranslateY()-q2.sphere.getTranslateY(), q1.sphere.getTranslateZ()-q2.sphere.getTranslateZ());
		vect.unitize();
		
		double eleX = Math.abs(eleForce*vect.x);
		double eleY = Math.abs(eleForce*vect.y);
		double eleZ = Math.abs(eleForce*vect.z);
		
		boolean like;
		
		if (q1.charge < 0 && q2.charge < 0) like = true;
		else if (q1.charge > 0 && q2.charge > 0) like = true;
		else like = false;
		
		if (like == false && q1.sphere.getTranslateX() > q2.sphere.getTranslateX()) {
			q1.accx = q1.accx - eleX;
			q2.accx = q2.accx + eleX;
		}
		else if (like == false && q1.sphere.getTranslateX() < q2.sphere.getTranslateX()) {
			q1.accx = q1.accx + eleX;
			q2.accx = q2.accx - eleX;
		}
		else if (like == true && q1.sphere.getTranslateX() > q2.sphere.getTranslateX()) {
			q1.accx = q1.accx + eleX;
			q2.accx = q2.accx - eleX;
		}
		else if (like == true && q1.sphere.getTranslateX() < q2.sphere.getTranslateX()) {
			q1.accx = q1.accx - eleX;
			q2.accx = q2.accx + eleX;
		}
		
		
		if (like == false && q1.sphere.getTranslateY() > q2.sphere.getTranslateY()) {
			q1.accy = q1.accy - eleY;
			q2.accy = q2.accy + eleY;
		}
		else if (like == false && q1.sphere.getTranslateY() < q2.sphere.getTranslateY()) {
			q1.accy = q1.accy + eleY;
			q2.accy = q2.accy - eleY;
		}
		else if (like == true && q1.sphere.getTranslateY() > q2.sphere.getTranslateY()) {
			q1.accy = q1.accy + eleY;
			q2.accy = q2.accy - eleY;
		}
		else if (like == true && q1.sphere.getTranslateY() < q2.sphere.getTranslateY()) {
			q1.accy = q1.accy - eleY;
			q2.accy = q2.accy + eleY;
		}
		
		
		if (like == false && q1.sphere.getTranslateZ() > q2.sphere.getTranslateZ()) {
			q1.accz = q1.accz - eleZ;
			q2.accz = q2.accz + eleZ;
		}
		else if (like == false && q1.sphere.getTranslateZ() < q2.sphere.getTranslateZ()) {
			q1.accz = q1.accz + eleZ;
			q2.accz = q2.accz - eleZ;
		}
		else if (like == true && q1.sphere.getTranslateZ() > q2.sphere.getTranslateZ()) {
			q1.accz = q1.accz + eleZ;
			q2.accz = q2.accz - eleZ;
		}
		else if (like == true && q1.sphere.getTranslateZ() < q2.sphere.getTranslateZ()) {
			q1.accz = q1.accz - eleZ;
			q2.accz = q2.accz + eleZ;
		}
		
	}
}
