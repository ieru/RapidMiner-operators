package com.rapidminer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;
import com.uah.items.Discussion;
import com.uah.items.LMS;

/**
* This rapidminer operator extracts all discussions of a LMS using the MAVSEL library. 
* @author Pablo Sicilia
* @version Mavsel Workbench 1.0 15-8-2012
*/
public class ExtractDiscussionsMavsel extends OperatorChain {

	private InputPort setInput = getInputPorts()
			.createPort("mavsel connection");
	private OutputPort exampleSetOutput = getOutputPorts()
			.createPort("discussions");
	public static final String PARAMETER_LABEL_ID = "IdDiscussion";

	private List<Discussion> discussions;

	public ExtractDiscussionsMavsel(OperatorDescription description) {
		super(description, "Property Extraction");

		getTransformer().addGenerationRule(exampleSetOutput, ExampleSet.class);
	}

	/**
	 * This method executes the main process of this operator
	 */
	@Override
	public void doWork() throws OperatorException {
		try {
			MavselConnectionIOObject mavselConnectionObject = setInput.getData();
			LMS lms = mavselConnectionObject.getLms();
			
			MavselConnection mavselConnection = new MavselConnection();
			mavselConnection.createTableDiscussion(lms.getConnection());

			String idDiscussion = getParameterAsString(PARAMETER_LABEL_ID);
			this.discussions = new ArrayList<Discussion>();

			if (idDiscussion == null || idDiscussion.isEmpty()) {
				discussions = lms.getDiscussions();
			} else {
				discussions.add(lms.getDiscussion(idDiscussion));
			}

			ExampleSet resultSet = createExampleSet();
			exampleSetOutput.deliver(resultSet);

		} catch (OperatorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Create an exampleSet with all the data-discussions required
	 * @return the exampleSet returned at the end of the main execution process 
	 * @throws OperatorException
	 */
	public ExampleSet createExampleSet() throws OperatorException {
		// ResultSet resultSet = getResultSet();
		MemoryExampleTable table;
		try {
			List<Attribute> attributes = getAttributes(Discussion.class
					.getDeclaredFields());
			table = createExampleTable(discussions, attributes);
		} catch (SQLException e) {
			throw new UserError(this, e, 304, e.getMessage());
		}
		return table.createExampleSet();
	}
	
	
	/**
	 * Creates the rows of the exampleSet
	 * @param discussions
	 * @param attributes
	 * @return all discussions data
	 * @throws SQLException
	 * @throws OperatorException
	 */
	private MemoryExampleTable createExampleTable(List<Discussion> discussions,
			List<Attribute> attributes) throws SQLException, OperatorException {
		
		Attribute[] attributeArray = attributes
				.toArray(new Attribute[attributes.size()]);
		MemoryExampleTable table = new MemoryExampleTable(attributes);
		DataRowFactory factory = new DataRowFactory(
				DataRowFactory.TYPE_DOUBLE_ARRAY, '.');
		DataRow dataRow = factory.create(attributeArray.length);

		for (Discussion discussion : discussions) {

			for (Attribute attribute : attributes) {
				String valueString;
				try {
					Method getter = discussion.getClass().getMethod(
							"get"
									+ String.valueOf(
											attribute.getName().charAt(0))
											.toUpperCase()
									+ attribute.getName().substring(1));

					Object valueInvoke = getter.invoke(discussion, new Object[0]);
					valueString = (String) valueInvoke;
					System.out.println("valueString" + valueString);
				} catch (Exception e) {
					valueString = null;
				}

				int valueType = attribute.getValueType();
				double value;

				if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(valueType,
						Ontology.NOMINAL)) {

					if (valueString == null) {
						value = Double.NaN;
					} else {
						value = attribute.getMapping().mapString(valueString);
					}
				} else {
					getLogger().warning("Unknown column type: " + attribute);
					value = Double.NaN;
				}

				dataRow.set(attribute, value);
			}

			table.addDataRow(dataRow);
		}
		return table;
	}

	
	/**
	 * Creates the column names of the exampleSet
	 * @param fields
	 * @return
	 */
	private List<Attribute> getAttributes(Field[] fields) {

		List<Attribute> result = new LinkedList<Attribute>();

		for (Field field : fields) {
			String columnName = field.getName();
			int attributeType = Ontology.STRING;
			final Attribute attribute = AttributeFactory.createAttribute(
					columnName, attributeType);

			result.add(attribute);
		}
		return result;
	}
	
}