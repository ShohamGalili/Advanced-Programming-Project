package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The ParallelAgent class wraps an Agent to enable asynchronous message processing.
 * It uses a blocking queue to hold messages and a separate thread to process them.
 */
public class ParallelAgent implements Agent {

    // Define members
    private Agent agent;                          // The encapsulated agent
    private BlockingQueue<Message> messageQueue;  // Queue to hold incoming messages
    private volatile boolean running = true;      // Flag to control the running state of the processing thread
    private Thread messageProcessingThread;       // Thread to process messages asynchronously

    /**
     * Constructor with specified queue capacity.
     *
     * @param agent    The agent to be encapsulated.
     * @param capacity The capacity of the message queue.
     */
    public ParallelAgent(Agent agent, int capacity) {
        this.agent = agent;
        this.messageQueue = new ArrayBlockingQueue<>(capacity);
        startMessageProcessingThread(); // Start the message processing thread
    }

    /**
     * Constructor with default queue capacity of 100.
     *
     * @param agent The agent to be encapsulated.
     */
    public ParallelAgent(Agent agent) {
        this(agent, 100); // Default capacity of 100
    }

    @Override
    public String getName() {
        return agent.getName(); // Return the encapsulated agent's name
    }

    @Override
    public void reset() {
        agent.reset(); // Delegate reset to the encapsulated agent
    }

    @Override
    public void close() {
        running = false; // Stop the processing thread
        messageProcessingThread.interrupt();
        try {
            messageProcessingThread.join(); // Ensure thread has stopped
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        agent.close(); // Close the encapsulated agent
    }

    @Override
    public void callback(String topic, Message msg) {
        // Combine topic and message text into a single message
        String messageText = topic + ":" + msg.asText;
        Message messageWithTopic = new Message(messageText);

        try {
            messageQueue.put(messageWithTopic); // Add the message to the queue
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Starts the message processing thread.
     * This thread takes messages from the queue and processes them by calling the encapsulated agent's callback method.
     */
    private void startMessageProcessingThread() {
        messageProcessingThread = new Thread(() -> {
            while (running) {
                try {
                    Message msg = messageQueue.take(); // Take a message from the queue
                    String[] parts = msg.asText.split(":", 2);
                    if (parts.length == 2) {
                        String topic = parts[0];
                        String msgText = parts[1];
                        Message originMsg = new Message(msgText);
                        agent.callback(topic, originMsg); // Process the message with the encapsulated agent
                    }
                } catch (InterruptedException e) {
                    if (!running) {
                        break; // Exit the loop if interrupted and not running
                    }
                    Thread.currentThread().interrupt();
                }
            }
        });
        messageProcessingThread.start(); // Start the thread
    }

    /**
     * Returns the encapsulated agent.
     *
     * @return The encapsulated agent.
     */
    public Agent getAgent() {
        return this.agent;
    }
}
