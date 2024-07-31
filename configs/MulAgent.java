package configs;

import graph.Agent;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

/**
 * The MulAgent class implements the Agent interface and performs multiplication operations
 * on messages received from two subscribed topics, then publishes the result to a third topic.
 */
public class MulAgent implements Agent {
    /** The values for multiplication. */
    private double firstValue = 0;
    private double secondValue = 0;

    /** The name of the topics this agent subscribes to. */
    private final String inputTopic1;
    private final String inputTopic2;
    private final String outputTopic;

    /** The last message received from the first and second topic. */
    private Message messageFromTopic1 = null;
    private Message messageFromTopic2 = null;

    private final String name;
    private static int counter=0;

    /**
     * Constructs a new MulAgent with specified subscription and publication topics.
     *
     * @param subscriptions An array of topic names to subscribe to. The first two elements are used.
     * @param publications An array of topic names to publish to. The first element is used.
     */
    public MulAgent(String[] subscriptions, String[] publications) {
        if (subscriptions.length != 2) {
            throw new IllegalArgumentException("MulAgent requires at least 2 subscription topics.");
        }
        if (publications.length < 1) {
            throw new IllegalArgumentException("MulAgent requires at least 1 publication topic.");
        }

        counter++;
        this.name = ("MulAgent" +counter);

        // Subscribe to the first 2 topics from subscriptions array:
        TopicManager topicManager = TopicManagerSingleton.get();
        topicManager.getTopic(subscriptions[0]).subscribe(this);
        topicManager.getTopic(subscriptions[1]).subscribe(this);
        // Initialize input topic names:
        inputTopic1 = subscriptions[0];
        inputTopic2 = subscriptions[1];

        // Add the first publisher from publications array:
        topicManager.getTopic(publications[0]).addPublisher(this);
        // Initialize output topic name:
        outputTopic = publications[0];
    }

    /**
     * Processes incoming messages from subscribed topics.
     * When messages from both subscribed topics are received, their values are multiplied
     * and the result is published to the output topic.
     *
     * @param topic The name of the topic from which the message was received.
     * @param msg The received message.
     */
    @Override
    public void callback(String topic, Message msg) {
        // Store the message if it's from the first topic
        if (topic.equals(inputTopic1)) {
            messageFromTopic1 = msg;
        }
        // Store the message if it's from the second topic
        else if (topic.equals(inputTopic2)) {
            messageFromTopic2 = msg;
        }

        // If both messages are received, multiply their values and publish the result
        if (messageFromTopic1 != null && messageFromTopic2 != null) {
            firstValue = messageFromTopic1.asDouble;
            secondValue = messageFromTopic2.asDouble;

            if (Double.isNaN(firstValue) || Double.isNaN(secondValue)) {
                return;
            }
            // Publish the product of the values
            TopicManagerSingleton.get().getTopic(outputTopic).publish(new Message(firstValue * secondValue));
        }
    }

    /**
     * Closes the agent, unsubscribing from all topics and removing itself as a publisher.
     */
    @Override
    public void close() {
        // Unsubscribe from the topics
        TopicManager topicManager = TopicManagerSingleton.get();
        Topic topic1 = topicManager.getTopic(inputTopic1);
        topic1.unsubscribe(this);

        Topic topic2 = topicManager.getTopic(inputTopic2);
        topic2.unsubscribe(this);

        Topic resultTopic = topicManager.getTopic(outputTopic);
        resultTopic.removePublisher(this);
    }

    /**
     * Returns the name of this agent.
     *
     * @return The string "MulAgent".
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Resets the agent's internal state, setting both values to zero.
     */
    @Override
    public void reset() {
        this.firstValue = 0;
        this.secondValue = 0;
    }
}
