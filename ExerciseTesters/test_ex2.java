package ExerciseTesters;

import graph.*;

public class test_ex2
{
	 public static void main(String[] args) 
	 {
	        // Create a base agent instance
	        Agent baseAgent = new SimpleAgent("BaseAgent");

	        // Create a ParallelAgent that decorates the base agent
	        ParallelAgent parallelAgent = new ParallelAgent(baseAgent, 10);

	        // Create a TopicManager instance using the singleton pattern
	        TopicManagerSingleton.TopicManager topicManager = TopicManagerSingleton.get();

	        // Create a new topic
	        Topic myTopic = topicManager.getTopic("ExampleTopic");

	        // Subscribe the parallel agent to the topic
	        myTopic.subscribe(parallelAgent);

	        // Create messages and publish them
	        Message msg1 = new Message("Hello, this is the first message.");
	        Message msg2 = new Message("Here comes the second message.");
	        Message msg3 = new Message("And here is the third message.");

	        myTopic.publish(msg1);
	        myTopic.publish(msg2);
	        myTopic.publish(msg3);

	        // Give some time for the messages to be processed
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }

	        // Close the parallel agent
	        parallelAgent.close();

	        // Print the name of the agent
	        System.out.println("Parallel Agent Name: " + parallelAgent.getName());
	    }
	// Simple implementation of Agent interface for demonstration purposes
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
