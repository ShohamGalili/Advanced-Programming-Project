package ExerciseTesters;

import graph.Agent;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;

public class test_ex1
{
	public static void main(String[] args) {
        // Create a TopicManager instance using the singleton pattern
        TopicManagerSingleton.TopicManager topicManager = TopicManagerSingleton.get();

        // Create a new topic
        Topic myTopic = topicManager.getTopic("ExampleTopic");

        // Create and register agents
        Agent agent1 = new SimpleAgent("Agent1");
        Agent agent2 = new SimpleAgent("Agent2");

        myTopic.subscribe(agent1);
        myTopic.subscribe(agent2);

        // Create messages
        Message msg1 = new Message("Hello, this is a test message.");
        Message msg2 = new Message(42.0);

        // Publish messages to the topic
        myTopic.publish(msg1);
        myTopic.publish(msg2);

        // Print subscribers
        System.out.println("Subscribers to " + myTopic.getName() + ":");
        for (Agent agent : myTopic.getSubs()) {
            System.out.println("- " + agent.getName());
        }
    }

    // Simple implementation of Agent interface
    private static class SimpleAgent implements Agent {
        private String name;

        public SimpleAgent(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void reset() {
            // Reset agent state if necessary
        }

        @Override
        public void callback(String topic, Message msg) {
            System.out.println(name + " received a message on topic '" + topic + "': " + msg.asText);
        }

        @Override
        public void close() {
            // Clean up resources if necessary
        }
    }
}
