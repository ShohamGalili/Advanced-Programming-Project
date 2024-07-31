package configs;

/**
 * MathExampleConfig class implements the Config interface to create a mathematical example
 * configuration. It sets up a series of binary operation agents that perform addition, subtraction,
 * and multiplication on input topics.
 */
public class MathExampleConfig implements Config {

    /**
     * Creates the mathematical example configuration by instantiating BinOpAgents.
     * These agents perform addition, subtraction, and multiplication operations on input topics.
     */
    @Override
    public void create() {
        // Create an addition agent that subscribes to topics "A" and "B" and publishes to "R1"
        new BinOpAgent("plus", "A", "B", "R1", (x, y) -> x + y);

        // Create a subtraction agent that subscribes to topics "A" and "B" and publishes to "R2"
        new BinOpAgent("minus", "A", "B", "R2", (x, y) -> x - y);

        // Create a multiplication agent that subscribes to topics "R1" and "R2" and publishes to "R3"
        new BinOpAgent("mul", "R1", "R2", "R3", (x, y) -> x * y);
    }

    /**
     * Returns the name of this configuration.
     *
     * @return the name of the configuration, "Math Example"
     */
    @Override
    public String getName() {
        return "Math Example";
    }

    /**
     * Returns the version of this configuration.
     *
     * @return the version of the configuration, 1
     */
    @Override
    public int getVersion() {
        return 1;
    }

    /**
     * Closes the configuration and performs any necessary cleanup.
     */
    @Override
    public void close() {
        // No resources to clean up in this example
    }
}
