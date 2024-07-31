package configs;

import graph.Agent;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

/**
 * Represents an Increment Agent that subscribes to a topic, increments the received value by 1,
 * and publishes the result to one or more topics.
 */
public class IncAgent implements Agent {

	// Subscription and publication topic names
	private final String name;
	private static int counter=0;

	public String[] subs;
	public String[] pubs;
	private Double x = 0.0;


	// Instance variables to store specific topic names
	private String firstTopicName;
	private String resultTopicName;

	/**
	 * Constructor for IncAgent.
	 *
	 * @param subs Array of subscription topic names. Must contain exactly one topic.
	 * @param pubs Array of publication topic names. Must contain at least one topic.
	 */
	public IncAgent(String[] subs, String[] pubs) {

		// Validate the number of subscription and publication topics
		if (subs.length != 1) {
			throw new IllegalArgumentException("IncAgent requires one subscription topic.");
		}
		if (pubs.length < 1) {
			throw new IllegalArgumentException("IncAgent requires at least one publication topic.");
		}
		counter++;
		this.name = ("IncAgent" +counter);

		this.subs = subs;
		this.pubs = pubs;

		// Save the topic names
		this.firstTopicName = subs[0];
		this.resultTopicName = pubs[0];

		// Subscribe to the first subscription topic
		TopicManagerSingleton.get().getTopic(subs[0]).subscribe(this);

		// Add this agent as a publisher to the publication topics
		for (String pub : pubs) {
			TopicManagerSingleton.get().getTopic(pub).addPublisher(this);
		}
	}

	/**
	 * Increments the value of x by 1 and publishes the result to the first publication topic.
	 */
	public void Inc() {
		if (x != null) {
			double result = x + 1;
			// Publish the result to the first publication topic
			TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(result));
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void reset() {
		x = 0.0;
	}

	@Override
	public void callback(String topic, Message msg) {
		// Check if the topic is the subscribed topic
		if (topic.equals(subs[0])) {
			x = msg.asDouble;
		}
		// Increment the value and publish the result
		Inc();
	}

	@Override
	public void close() {
		// Unsubscribe from the topics and remove this agent as a publisher
		TopicManager tm = TopicManagerSingleton.get();
		Topic firstTopic = tm.getTopic(firstTopicName);
		firstTopic.unsubscribe(this);
		Topic resultTopic = tm.getTopic(resultTopicName);
		resultTopic.removePublisher(this);
	}
}
