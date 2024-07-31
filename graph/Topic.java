package graph;

import java.util.List;
import java.util.ArrayList;

/**
 * The Topic class represents a communication topic in a publish-subscribe system.
 * It manages the subscribers and publishers associated with the topic and handles message publishing.
 */
public class Topic {

	// Define members
	public final String name;                       // The name of the topic
	public List<Agent> subs = new ArrayList<>();    // List of subscriber agents
	public List<Agent> pubs = new ArrayList<>();    // List of publisher agents
	private Message lastMessage = new Message(0.0); // The last published message

	/**
	 * Constructor to initialize a topic with a given name.
	 *
	 * @param name The name of the topic.
	 */
	public Topic(String name) {
		this.name = name;
		this.lastMessage = new Message(0); // Initialize last message with a default value
	}

	/**
	 * Adds a subscriber to the list of subscribers.
	 *
	 * @param sub The agent to subscribe.
	 */
	public void subscribe(Agent sub) {
		if (!subs.contains(sub)) {
			subs.add(sub);
		}
	}

	/**
	 * Removes a subscriber from the list of subscribers.
	 *
	 * @param unSub The agent to unsubscribe.
	 */
	public void unsubscribe(Agent unSub) {
		if (subs.contains(unSub)) {
			subs.remove(unSub);
		}
	}

	/**
	 * Publishes a message to all subscribed agents.
	 *
	 * @param msg The message to publish.
	 */
	public void publish(Message msg) {
		lastMessage = msg; // Store the last message

		for (int i = 0; i < subs.size(); i++) {
			Agent agent = subs.get(i); // Get the specific subscriber from the list
			agent.callback(this.name, msg); // Notify the subscriber with the message
		}
	}

	/**
	 * Adds a publisher to the list of publishers.
	 *
	 * @param publisher The agent to add as a publisher.
	 */
	public void addPublisher(Agent publisher) {
		if (!pubs.contains(publisher)) {
			pubs.add(publisher);
		}
	}

	/**
	 * Removes a publisher from the list of publishers.
	 *
	 * @param unPublisher The agent to remove as a publisher.
	 */
	public void removePublisher(Agent unPublisher) {
		if (pubs.contains(unPublisher)) {
			pubs.remove(unPublisher);
		}
	}

	// Getter and Setter methods for subscribers and publishers
	public List<Agent> getSubs() {
		return subs;
	}

	public List<Agent> getPubs() {
		return pubs;
	}

	public void setSubs(List<Agent> subs) {
		this.subs = subs;
	}

	public void setPubs(List<Agent> pubs) {
		this.pubs = pubs;
	}

	// Getter method for topic name
	public String getName() {
		return name;
	}

	/**
	 * Gets the last published message as a string.
	 *
	 * @return The last message as a string, or an empty string if no message exists.
	 */
	public String getLastMessage() {
		return lastMessage != null ? lastMessage.toString() : "";
	}

	/**
	 * Prints the name of the topic to the console.
	 *
	 * @param message The message to print.
	 */
	public void print(String message) {
		System.out.println(this.name);
	}
}
