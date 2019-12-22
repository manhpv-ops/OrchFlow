package neo4j;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import modelos.SwitchData;

public class Neo4jNodes {
	private static String passwd = "Basic bmVvNGo6MTIzNA==";
	public static String SERVER_ROOT_URI = "http://localhost:7474/db/data/";

	public SwitchData nodes(SwitchData swD) {
		System.out.println("Switch data before:" + swD);
		swD.setLocation(createNode(swD));
		System.out.println("Switch data:" + swD.getLocation());
		addLabel(swD.getLocation(), "Switch");
		return swD;
	}

	private static URI createNode(SwitchData swD) {
		final String nodeEntryPointUri = SERVER_ROOT_URI + "index/node/index_1445034018615_1?uniqueness=get_or_create";
		System.out.println("URL createNode" + nodeEntryPointUri);
		String createJson = generateJsonCreate(swD);
		System.out.println("Json ne:" + createJson);
		WebResource resource = Client.create().resource(nodeEntryPointUri);
		System.out.println("Resource :" + resource);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.entity(createJson).header("Authorization", passwd).post(ClientResponse.class);
		System.out.println("RESPONSE:" + response + "of this JSON "+createJson);
		if (response.getStatus() == 200 || response.getStatus()==201) {
			// final URI location = response.getLocation();
			// System.out.println(String.format("POST to [%s], status code [%d],
			// location header [%s]", nodeEntryPointUri,
			// response.getStatus(), location.toString()));

			JSONObject entity = new JSONObject(response.getEntity(String.class));
			String uri = entity.get("self").toString();
			System.out.println("URI ne may ba" + uri);
			response.close();

			return URI.create(uri);
		} else {
			// System.out
			// .println(String.format("POST to [%s], status code [%d]",
			// nodeEntryPointUri, response.getStatus()));
			// System.exit(0);
		}

		return null;
	}

	private static String generateJsonCreate(SwitchData swD) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"key\" : \"DPID\",");

		sb.append("\"value\" : \"");
		sb.append(swD.getDPID().toString());
		sb.append("\", ");

		sb.append("\"properties\" : {");
		sb.append("\"DPID\" : \"");
		sb.append(swD.getDPID().toString());
		sb.append("\", ");

		sb.append("\"inetAddress\" : \"");
		sb.append(swD.getInetAddress().toString());
		sb.append("\", ");

		sb.append("\"name\" : \"");
		sb.append(swD.getName().toString());
		sb.append("\", ");

		sb.append("\"controller\" : \"");
		sb.append(swD.getController().toString());
		sb.append("\"");

		sb.append(" }");

		sb.append(" }");
		System.out.println("STRING JSON TEST= [" + sb.toString() + "]");
		return sb.toString();
	}

	private static void addLabel(URI nodeUri, String label) {
		System.out.println("LABEL lol :" + nodeUri);
		String propertyUri = nodeUri.toString() + "/labels";
		System.out.println("Ro bo ty:"+ propertyUri);
		WebResource resource = Client.create().resource(propertyUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.entity("\"" + label + "\"").header("Authorization", passwd).post(ClientResponse.class);

		// System.out.println(String.format("PUT to [%s], status code [%d]",
		// propertyUri, response.getStatus()));
		if (response.getStatus() == 404) {
			// System.exit(0);
		}
		response.close();

	}
}
