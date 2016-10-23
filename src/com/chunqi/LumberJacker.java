package com.chunqi;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class LumberJacker {
	private static final Point LEFT_BUTTON = new Point(550, 1275);
	private static final Point RIGHT_BUTTON = new Point(720, 1275);
	
	private static final Point LEFT_DANGER = new Point(555, 960);
	private static final Point RIGHT_DANGER = new Point(725, 960);
	
	private static final Color DANGER_COLOR = new Color(153, 204, 102);
	private static final Color CLEAR_COLOR = new Color(211, 247, 255);
	
	private static final int THRESHOLD = 5;
	
	private static final int SLEEP = 100;
	
	private static Robot ROBOT;
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		init();
		
		//calibrateColor();
		
		boolean clickLeft = true;
		
		while(true) {
			Color leftDanger = getPixelColor(LEFT_DANGER);
			Color rightDanger = getPixelColor(RIGHT_DANGER);
			boolean leftThreshold = threshold(leftDanger, CLEAR_COLOR);
			boolean rightThreshold = threshold(rightDanger, CLEAR_COLOR);
			//System.out.println(leftDanger.toString() + ' ' + rightDanger.toString());
			//System.out.println(leftThreshold);
			//System.out.println(rightThreshold);
			
			if(!leftThreshold) {
				clickLeft = false;
			} else if(!rightThreshold) {
				clickLeft = true;
			}
			
			
			if(clickLeft) {
				clickAt(LEFT_BUTTON);
				System.out.println("LEFT");
			} else {
				clickAt(RIGHT_BUTTON);
				System.out.println("RIGHT");
			}
			Thread.sleep(SLEEP);
		}
	}
	
	public static void init() throws AWTException {
		ROBOT = new Robot();
	}
	
	public static void calibratePointer() {
		Point mouseLoc;
		while(true) {
			mouseLoc = getPointerLocation();
			System.out.println(mouseLoc);
		}
	}
	
	public static void calibrateColor() throws AWTException {
		while(true) {
			Color c = getPixelColor(getPointerLocation());
			System.out.println(c);
		}
	}
	
	public static void calibrateThreshold() throws AWTException {
		while(true) {
			boolean left = threshold(getPixelColor(LEFT_DANGER), DANGER_COLOR);
			boolean right = threshold(getPixelColor(RIGHT_DANGER), DANGER_COLOR);
			System.out.println("left: " + left + "  right: " + right);
		}
	}
	
	private static Point getPointerLocation() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	private static void clickAt(Point p) {
		ROBOT.mouseMove(p.x, p.y);
		ROBOT.mousePress(InputEvent.BUTTON1_MASK);
		ROBOT.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	private static Color getPixelColor(Point p) throws AWTException {
		return ROBOT.getPixelColor(p.x, p.y);
	}
	
	private static boolean threshold(Color actual, Color expected) {
		int dr = actual.getRed() - expected.getRed();
		int dg = actual.getGreen() - expected.getGreen();
		int db = actual.getBlue() - expected.getBlue();
		
		return (dr * dr + dg * dg + db * db) < (THRESHOLD * THRESHOLD);
	}
}
