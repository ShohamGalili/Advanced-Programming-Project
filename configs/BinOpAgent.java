package configs;

import java.util.function.BinaryOperator;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

/**
 * BinOpAgent represents an agent that performs a binary operation on two input values.
 * It subscribes to two input topics, performs the operation, and publishes the result to an output topic.
 */
public class BinOpAgent implements Agent {
	private String agentName;
	private String firstInputTopic;
	private String secondInputTopic;
	private String outputTopic;
	private Double x;
	private Double y;
	private Double outputResultMsg;
	private BinaryOperator<Double> operator;

	/**
	 * Constructs a BinOpAgent with the specified parameters.
	 *
	 * @param operatorName The name of the operator (agent).
	 * @param firstInputTopic The first input topic to subscribe to.
	 * @param secondInputTopic The second input topic to subscribe to.
	 * @param outputTopic The output topic to publish the result.
	 * @param operator The binary operator to apply to the inputs.
	 */
	public BinOpAgent(String operatorName, String firstInputTopic, String secondInputTopic, String outputTopic, BinaryOperator<Double> operator) {
		this.firstInputTopic = firstInputTopic;
		this.secondInputTopic = secondInputTopic;
		this.outputTopic = outputTopic;
		this.operator = operator;
		this.agentName = operatorName;

		// Subscribe the agent to the input topics and add it as a publisher to the output topic.
		TopicManagerSingleton.get().getTopic(firstInputTopic).subscribe(this);
		TopicManagerSingleton.get().getTopic(secondInputTopic).subscribe(this);
		TopicManagerSingleton.get().getTopic(outputTopic).addPublisher(this);
	}

	/**
	 * Returns the name of the agent.
	 *
	 * @return The name of the agent.
	 */
	@Override
	public String getName() {
		return this.agentName;
	}

	/**
	 * Resets the agent's state by setting the input values to 0.
	 */
	@Override
	public void reset() {
		this.x = 0.0;
		this.y = 0.0;
	}

	/**
	 * Callback method invoked when a message is received on a subscribed topic.
	 * Updates the input values and performs the binary operation if both inputs are available.
	 *
	 * @param topic The topic on which the message was received.
	 * @param msg The message received.
	 */
	@Override
	public void callback(String topic, Message msg) {
		if (topic.equals(firstInputTopic)) { // Check if the current topic is the first input topic.
			this.x = msg.asDouble;
		}
		if (topic.equals(secondInputTopic)) { // Check if the current topic is the second input topic.
			this.y = msg.asDouble;
		}

		// Perform the binary operation if both inputs are available and publish the result.
		if (this.x != null && this.y != null) {
			this.outputResultMsg = this.operator.apply(x, y);
			TopicManagerSingleton.get().getTopic(outputTopic).publish(new Message(outputResultMsg));
		}
	}

	/**
	 * Closes the agent. Currently, this method does nothing.
	 */
	@Override
	public void close() {
		// No resources to close in this implementation.
	}
}
