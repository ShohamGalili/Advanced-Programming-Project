package configs;

import graph.Agent;
import graph.ParallelAgent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * GenericConfig is a class that implements the Config interface.
 * It reads configuration data from a file, creates agents based on the configuration,
 * and manages these agents within a list of ParallelAgent instances.
 */
public class GenericConfig implements Config {

	/** Path to the configuration file. */
	public String path = "";

	/** List of ParallelAgent instances created from the configuration file. */
	public List<ParallelAgent> agents;

	/**
	 * Creates the configuration by reading the configuration file,
	 * initializing agents based on the file content, and adding them to the list of agents.
	 *
	 * @throws Exception if an error occurs during the creation of the agents.
	 */
	@Override
	public void create() throws Exception {
		List<String> lines = readFile(this.path);

		if (lines.size() % 3 != 0) {
			throw new IllegalArgumentException("Invalid input file format: number of lines is not divisible by 3.");
		}

		agents = new ArrayList<>(); // Ensure agents list is initialized

		for (int i = 0; i < lines.size(); i += 3) {
			String agentType = lines.get(i);
			agentType = agentType.substring(agentType.indexOf('.') + 1); // Extract agent type
			String[] subs = lines.get(i + 1).split(","); // Subscriptions
			String[] pubs = lines.get(i + 2).split(","); // Publications

			Agent agent = createAgent(agentType, subs, pubs);
			agents.add(new ParallelAgent(agent));
		}
	}

	/**
	 * Creates an agent instance using reflection based on the provided agent type, subscriptions, and publications.
	 *
	 * @param agentType The type of the agent to be created.
	 * @param subs The topics to which the agent subscribes.
	 * @param pubs The topics to which the agent publishes.
	 * @return The created Agent instance.
	 * @throws Exception if an error occurs during agent creation.
	 */
	private Agent createAgent(String agentType, String[] subs, String[] pubs) throws Exception {
		Class<?> agentClass = Class.forName(agentType);
		Constructor<?> constructor = agentClass.getConstructor(String[].class, String[].class);
		return (Agent) constructor.newInstance((Object) subs, (Object) pubs); // Casting to Object is necessary to match varargs signature
	}

	/**
	 * Parses a comma-separated list of strings into a List.
	 *
	 * @param line The line to be parsed.
	 * @return The List of parsed strings.
	 */
	private List<String> parseList(String line) {
		String[] parts = line.split(",");
		List<String> list = new ArrayList<>();
		for (String part : parts) {
			list.add(part);
		}
		return list;
	}

	/**
	 * Returns the name of the configuration.
	 *
	 * @return The name of the configuration.
	 */
	@Override
	public String getName() {
		return null;
	}

	/**
	 * Returns the version of the configuration.
	 *
	 * @return The version of the configuration.
	 */
	@Override
	public int getVersion() {
		return 0;
	}

	/**
	 * Closes the configuration by closing all ParallelAgent instances in the agents list.
	 */
	@Override
	public void close() {
		for (ParallelAgent parallelAgent : agents) {
			parallelAgent.close();
		}
	}

	/**
	 * Reads the content of a file line by line and returns the lines as a List.
	 *
	 * @param filePath The path to the file.
	 * @return The List of lines read from the file.
	 */
	public List<String> readFile(String filePath) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * Sets the path to the configuration file.
	 *
	 * @param path The path to the configuration file.
	 */
	public void setConfFile(String path) {
		this.path = path;
	}
}
