package configs;

/**
 * Config is an interface that defines the basic methods required for configuration objects.
 * Implementations of this interface should provide methods for creating the configuration,
 * retrieving its name and version, and closing any resources associated with it.
 */
public interface Config {

    /**
     * Creates the configuration. This method may involve initializing resources,
     * setting up connections, or any other setup required for the configuration.
     *
     * @throws Exception if an error occurs during the creation of the configuration.
     */
    void create() throws Exception;

    /**
     * Returns the name of the configuration.
     *
     * @return The name of the configuration.
     */
    String getName();

    /**
     * Returns the version of the configuration.
     *
     * @return The version of the configuration.
     */
    int getVersion();

    /**
     * Closes the configuration. This method should release any resources associated with the configuration,
     * such as connections or file handles.
     */
    void close();
}
