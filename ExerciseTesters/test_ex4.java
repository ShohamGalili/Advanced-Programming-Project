package ExerciseTesters;

import configs.GenericConfig;
import graph.Message;
import graph.TopicManagerSingleton;

public class test_ex4
{
	public static void main(String[] args) throws Exception {
        // Create an instance of GenericConfig
        GenericConfig config = new GenericConfig();
        
        // Set the configuration file path (replace with the actual path)
        config.setConfFile("C:\\Users\\shoham\\eclipse-workspace\\Project-OPP\\config_files\\simple.conf");
        
        // Create the agents as specified in the configuration file
        config.create();
        
        // Simulate some inputs for the agents to process
        TopicManagerSingleton.TopicManager topicManager = TopicManagerSingleton.get();

        // Publish some messages to the topics to trigger agent actions
        topicManager.getTopic("InputTopic1").publish(new Message(10.0));
        topicManager.getTopic("InputTopic2").publish(new Message(20.0));

        // Wait for a moment to ensure agents have time to process messages
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Close the configuration, which will close all agents
        config.close();

        // Print out results or state of topics and agents if needed
        // For example, output the state of topics or any relevant messages processed
        System.out.println("Processing completed.");
    }

}
