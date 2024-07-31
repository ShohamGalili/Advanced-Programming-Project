package graph;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class for managing topics in a publish-subscribe system.
 * It ensures that only one instance of TopicManager exists.
 */
public class TopicManagerSingleton {

    /**
     * Inner static class that holds the singleton instance of TopicManager.
     */
    public static class TopicManager {

        // Define members
        public static final TopicManager instance = new TopicManager(); // Singleton instance
        private ConcurrentHashMap<String, Topic> topics = new ConcurrentHashMap<>(); // Thread-safe map of topics

        /**
         * Private constructor to prevent instantiation from other classes.
         */
        private TopicManager() {}

        /**
         * Method to get the singleton instance of TopicManager.
         *
         * @return The singleton instance of TopicManager.
         */
        public static TopicManager get() {
            return instance;
        }

        /**
         * Method to get a topic by name. If the topic does not exist, it is created.
         *
         * @param name The name of the topic.
         * @return The topic with the specified name.
         */
        public Topic getTopic(String name) {
            // ComputeIfAbsent ensures atomicity and thread safety
            return topics.computeIfAbsent(name, k -> new Topic(k));
        }

        /**
         * Method to get all topics.
         *
         * @return A ConcurrentHashMap containing all topics.
         */
        public ConcurrentHashMap<String, Topic> getTopics() {
            return topics;
        }

        /**
         * Method to clear all topics.
         */
        public void clear() {
            topics.clear();
        }

        /**
         * Method to print all topics.
         */
        public void printTopics() {
            if (topics.isEmpty()) {
                System.out.println("No topics available.");
            } else {
                for (Topic topic : topics.values()) {
                    System.out.println("Topic: " + topic.getName());
                }
            }
        }

        /**
         * Method to get the names of all topics.
         *
         * @return An array of topic names.
         */
        public String[] getTopicsNames() {
            return topics.keySet().toArray(new String[0]);
        }

    }

    /**
     * Method to get the singleton instance of TopicManager.
     *
     * @return The singleton instance of TopicManager.
     */
    public static TopicManager get() {
        return TopicManager.instance;
    }
}
