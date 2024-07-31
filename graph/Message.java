package graph;

import java.util.Date;

/**
 * The Message class represents a message that can be passed between agents in a system.
 * It can hold data in various formats: byte array, text, and double.
 */
public class Message {

	// Define members
	public final byte[] data;   // Byte array representation of the message
	public final String asText; // Text representation of the message
	public final double asDouble; // Double representation of the message
	public final Date date;     // Timestamp when the message was created

	/**
	 * Constructor for creating a Message from a byte array.
	 *
	 * @param data The byte array data of the message.
	 */
	public Message(byte[] data) {
		this.data = data;
		this.asText = new String(data);
		this.asDouble = convertToDouble(this.asText);
		this.date = new Date(); // Set the current date and time
	}

	/**
	 * Constructor for creating a Message from a string.
	 * This constructor reuses the byte array constructor.
	 *
	 * @param dataText The text data of the message.
	 */
	public Message(String dataText) {
		double temp;
		this.asText = dataText;
		this.data = dataText.getBytes();

		// In case the string is not a valid double number, asDouble will be NaN
		try {
			temp = Double.parseDouble(dataText);
		} catch (NumberFormatException e) {
			temp = Double.NaN;
		}
		this.asDouble = temp;

		// Set the date object to the current date and time
		this.date = new Date();
	}

	/**
	 * Constructor for creating a Message from a double.
	 * This constructor reuses the byte array constructor.
	 *
	 * @param dataDouble The double data of the message.
	 */
	public Message(double dataDouble) {
		this(Double.toString(dataDouble).getBytes());
	}

	/**
	 * Converts a string to a double, handling exceptions by returning NaN if conversion fails.
	 *
	 * @param dataText The string data to be converted.
	 * @return The double representation of the string, or NaN if conversion fails.
	 */
	private double convertToDouble(String dataText) {
		try {
			return Double.parseDouble(dataText);
		} catch (NumberFormatException e) {
			return Double.NaN; // Not a number
		}
	}

	/**
	 * Returns the text representation of the message.
	 *
	 * @return The text data of the message.
	 */
	@Override
	public String toString() {
		return this.asText;
	}
}
