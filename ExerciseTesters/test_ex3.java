package ExerciseTesters;

import configs.BinOpAgent;
import configs.Graph;
import configs.Node;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;

public class test_ex3
{
	public static void main(String[] args) {
        // Create a TopicManager instance using the singleton pattern
        TopicManagerSingleton.TopicManager topicManager = TopicManagerSingleton.get();

        // Create topics
        Topic topic1 = topicManager.getTopic("Input1");
        Topic topic2 = topicManager.getTopic("Input2");
        Topic outputTopic = topicManager.getTopic("Output");

        // Create a binary operation agent (addition in this case)
        BinOpAgent addAgent = new BinOpAgent(
                "AddAgent",
                topic1.getName(),
                topic2.getName(),
                outputTopic.getName(),
                (a, b) -> a + b
        );

        // Create another binary operation agent (multiplication in this case)
        BinOpAgent multiplyAgent = new BinOpAgent(
                "MultiplyAgent",
                topic1.getName(),
                topic2.getName(),
                outputTopic.getName(),
                (a, b) -> a * b
        );

        // Publish some messages to the input topics
        topic1.publish(new Message(5.0));
        topic2.publish(new Message(10.0));

        // Wait for some time to allow agents to process messages
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Close the agents
        addAgent.close();
        multiplyAgent.close();

        // Create a graph from the topics and agents
        Graph graph = new Graph();
        graph.createFromTopics();

        // Check for cycles in the graph
        boolean hasCycles = graph.hasCycles();
        System.out.println("Graph has cycles: " + hasCycles);

        // Print the results
        for (Node node : graph) {
            if (node.getMsg() != null) {
                System.out.println(node.getName() + " processed message: " + node.getMsg().asText);
            }
        }
    }
}
