package com.rapidminer;


import java.sql.SQLException;
import java.util.List;


import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.tools.jdbc.DatabaseHandler;
import com.rapidminer.tools.jdbc.connection.ConnectionEntry;
import com.rapidminer.tools.jdbc.connection.ConnectionProvider;
import com.uah.items.LMS;
import com.uah.main.dokeos.DokeosLMS;
import com.uah.main.moodle.MoodleLMS;

public class MavselInitConnection extends Operator implements ConnectionProvider{

	//private InputPort setInput = getInputPorts().createPort("example set");
	private OutputPort setOutput = getOutputPorts().createPort("mavsel connection");
	
	private DatabaseHandler databaseHandler;
	
	private LMS miLMS; 
	
	public static final String[] EXTRACTING_TYPES = { "Moodle" , "Dokeos"};

	public static final String COMBO = "Extracting Type";
	
	public static final String TABLE_NAME = "course";
	
    public static final int QUERY_QUERY = 0;
    
	
	public MavselInitConnection(OperatorDescription description){
		super(description);
		

	}
	
	@Override
	public void doWork() throws OperatorException {
		try {
			databaseHandler = DatabaseHandler.getConnectedDatabaseHandler(this);
			
			MavselConnection mavselConnection = new MavselConnection();
			mavselConnection.createSchema(databaseHandler.getConnection());
						  
	        String extractingType = getParameterAsString(COMBO);
	        
	        miLMS = null;
	         
	        if (extractingType.equalsIgnoreCase(EXTRACTING_TYPES[0])){
	        	System.out.println("----------------------------");
	            System.out.println("----Moodle Course-----------");
	            System.out.println("----------------------------");
	            
	            miLMS = new MoodleLMS();
	            miLMS.configureLMS(databaseHandler.getConnection());
	            
	        }else if (extractingType.equalsIgnoreCase(EXTRACTING_TYPES[1])){
	        	System.out.println("----------------------------");
	            System.out.println("----DOKEOS Course-----------");
	            System.out.println("----------------------------");
	            
	            miLMS = new DokeosLMS();            
	            miLMS.configureLMS(databaseHandler.getConnection());
	        }
			 MavselConnectionIOObject output = new MavselConnectionIOObject(miLMS, mavselConnection);
			 
			 setOutput.deliver(output);
		}catch (OperatorException e) {
			throw e;		
		} catch(Exception e){
			e.printStackTrace();	
			
		}	
		
		}
		
	
	@Override
	public ConnectionEntry getConnectionEntry() {
		return DatabaseHandler.getConnectionEntry(this);
	}
	

	
	
	@Override
	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		types.addAll(DatabaseHandler.getConnectionParameterTypes(this));
		//types.addAll(DatabaseHandler.getQueryParameterTypes(this, true));
		
		ParameterType extratingTypes = null;
		extratingTypes = new ParameterTypeCategory(COMBO, "TODO enter a description.", EXTRACTING_TYPES, QUERY_QUERY);
		extratingTypes.setExpert(false);
		types.add(extratingTypes);
		//types.add(new ParameterTypeBoolean(PARAMETER_RECREATE_INDEX, "Indicates if a recreation of the index or index mapping table should be forced.", false));
		//ParameterType type = new ParameterTypeString(ResultSetExampleSource.PARAMETER_LABEL_ATTRIBUTE, "The (case sensitive) name of the label attribute");
		//type.setExpert(false);
		
		//types.add(type);
		//types.add(new ParameterTypeString(ResultSetExampleSource.PARAMETER_WEIGHT_ATTRIBUTE, "The (case sensitive) name of the weight attribute"));
		return types;
	}
	
	/*
	@Override
	public ExampleSet createExampleSet() throws OperatorException {		
		try {
			databaseHandler = DatabaseHandler.getConnectedDatabaseHandler(this);
			//String tableName = getParameterAsString(DatabaseHandler.PARAMETER_TABLE_NAME);
			//boolean recreateIndex = getParameterAsBoolean(PARAMETER_RECREATE_INDEX);
			IndexCachedDatabaseExampleTable table = new IndexCachedDatabaseExampleTable(databaseHandler, TABLE_NAME, DataRowFactory.TYPE_DOUBLE_ARRAY, false, this);
			// TODO copy functionality from ResultSetExampleSource and remove ResultSetExampleSource!
			return ResultSetExampleSource.createExampleSet(table, this);
		} catch (SQLException e) {
			throw new UserError(this, e, 304, e.getMessage());
		}
	}*/
	
	@Override
	public void processFinished() {
		disconnect();
	}
	
	
	private void disconnect() {    	
		// close database connection
		if (databaseHandler != null) {
			try {
				databaseHandler.disconnect();
				databaseHandler = null;
			} catch (SQLException e) {
				logWarning("Cannot disconnect from database: " + e);
			}
		}        
	}
}
