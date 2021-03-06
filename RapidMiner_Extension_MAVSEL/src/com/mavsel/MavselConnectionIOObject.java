package com.mavsel;


import java.util.List;

import com.rapidminer.operator.ResultObjectAdapter;
import com.uah.items.Course;
import com.uah.items.LMS;

/**
 * 
 * @author Pablo Sicilia
 * @version Mavsel Workbench 1.0 15-8-2012
 */
public class MavselConnectionIOObject  extends ResultObjectAdapter{

	private LMS lms;
	private MavselConnection mavselConnection;

	private static final long serialVersionUID = 1725159059797569345L;
	
	public MavselConnectionIOObject(LMS lms, MavselConnection mavselConnection){
		this.lms = lms;
		this.mavselConnection = mavselConnection;
	}
	
	public LMS getLms() {
		return lms;
	}
	
	public List<Course> getCurses(){
		return lms.getCourse();
	}

	public MavselConnection getMavselConnection() {
		return mavselConnection;
	}
	
	
}
