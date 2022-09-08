package ca.utoronto.utm.mcs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.*;

// TODO Please Write Your Tests For CI/CD In This Class. You will see
// these tests pass/fail on github under github actions.
public class AppTest {
	@Test
	public void addMovieTest_200() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			String url = "http://localhost:8080/api/v1/addMovie";
			JSONObject req_body_json = new JSONObject();
			req_body_json.put("name", "A few good man");
			req_body_json.put("movieId", "nm1453");
			String req_body = req_body_json.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.PUT(HttpRequest.BodyPublishers.ofString(req_body)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void addMovieTest_400() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			String url = "http://localhost:8080/api/v1/addMovie";
			JSONObject req_body_json = new JSONObject();
			req_body_json.put("name", "A few good man");
			String req_body = req_body_json.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.PUT(HttpRequest.BodyPublishers.ofString(req_body)).build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(400, status);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void addActor200() {
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "http://localhost:8080/api/v1/addActor";

		try {
			JSONObject actor1Obj = new JSONObject("{'name': 'Geralt', 'actorId': 'nm7897896'}");
			String actor1Str = actor1Obj.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.PUT(HttpRequest.BodyPublishers.ofString(actor1Str)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addActor400() {
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "http://localhost:8080/api/v1/addActor";

		try {
			// response 1
			JSONObject actor1Obj = new JSONObject("{'name': 'Bob', 'actorId': 'nm2001234'}");
			String actor1Str = actor1Obj.toString();
			HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(url))
					.PUT(HttpRequest.BodyPublishers.ofString(actor1Str)).build();

			httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

			// response 2
			JSONObject actor2Obj = new JSONObject("{'name': 'Bob', 'actorId': 'nm2001234'}");
			String actor2Str = actor2Obj.toString();
			HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(url))
					.PUT(HttpRequest.BodyPublishers.ofString(actor2Str)).build();

			HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

			int status = response2.statusCode();
			assertEquals(status, 400);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addRelationship200() {
		HttpClient httpClient = HttpClient.newHttpClient();

		try {
			// add Alice
			String url1 = "http://localhost:8080/api/v1/addActor";
			JSONObject actor1Obj = new JSONObject("{'name': 'Alice', 'actorId': 'nm1234567'}");
			String actor1Str = actor1Obj.toString();
			HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(url1))
					.PUT(HttpRequest.BodyPublishers.ofString(actor1Str)).build();

			httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

			// add Hello_World
			String url2 = "http://localhost:8080/api/v1/addMovie";
			JSONObject movieObj = new JSONObject("{'name': 'Hello_World', 'movieId': 'nm7654321'}");
			String movieStr = movieObj.toString();
			HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(url2))
					.PUT(HttpRequest.BodyPublishers.ofString(movieStr)).build();

			httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

			// add relationship
			String url3 = "http://localhost:8080/api/v1/addRelationship";
			JSONObject realationObj = new JSONObject("{'actorId': 'nm1234567', 'movieId': 'nm7654321'}");
			String relationStr = realationObj.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url3))
					.PUT(HttpRequest.BodyPublishers.ofString(relationStr)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addRelationship400() {
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "http://localhost:8080/api/v1/addRelationship";

		try {

			JSONObject relationObj = new JSONObject("{'actorId': 'nm2001234'}");
			String relationStr = relationObj.toString();
			HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create(url))
					.PUT(HttpRequest.BodyPublishers.ofString(relationStr)).build();

			HttpResponse<String> response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());

			int status = response3.statusCode();
			assertEquals(400, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void hasRelationship200() {
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "http://localhost:8080/api/v1/hasRelationship";

		try {
			JSONObject relationObj = new JSONObject("{'actorId': 'nm1234567', 'movieId': 'nm7654321'}");
			String relationStr = relationObj.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(relationStr)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void hasRelationship400() {
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "http://localhost:8080/api/v1/hasRelationship";

		try {
			JSONObject relationObj = new JSONObject("{'actorId': 'nm1234567'}");
			String relationStr = relationObj.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(relationStr)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(400, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getActor200() {
		HttpClient httpClient = HttpClient.newHttpClient();

		try {
			// add Bacon
			String url1 = "http://localhost:8080/api/v1/addActor";
			JSONObject actor1Obj = new JSONObject("{'name': 'Tom', 'actorId': 'nm1111111'}");
			String actor1Str = actor1Obj.toString();
			HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(url1))
					.PUT(HttpRequest.BodyPublishers.ofString(actor1Str)).build();

			httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

			// get Bacon
			String url = "http://localhost:8080/api/v1/getActor";
			JSONObject relationObj = new JSONObject("{'actorId': 'nm1111111'}");
			String relationStr = relationObj.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(relationStr)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getActor400() {
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "http://localhost:8080/api/v1/getActor";

		try {
			JSONObject relationObj = new JSONObject("{}");
			String relationStr = relationObj.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(relationStr)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			assertEquals(400, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void computeBaconNumber_200() {
		try {

			HttpClient client = HttpClient.newHttpClient();

			String url1 = "http://localhost:8080/api/v1/addActor";
			JSONObject actor1Obj = new JSONObject("{'name': 'Bacon', 'actorId': 'nm0000102'}");
			String actor1Str = actor1Obj.toString();
			HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(url1))
					.PUT(HttpRequest.BodyPublishers.ofString(actor1Str)).build();
			HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

			String url = "http://localhost:8080/api/v1/computeBaconNumber";
			JSONObject req_body_json = new JSONObject();
			req_body_json.put("actorId", "nm0000102");
			String req = req_body_json.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(req)).build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void computeBaconNumber_400() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			String url = "http://localhost:8080/api/v1/computeBaconNumber";
			JSONObject req_body_json = new JSONObject("{}");
			String req = req_body_json.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(req)).build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			assertEquals(400, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void computeBaconPath_400() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			String url = "http://localhost:8080/api/v1/computeBaconPath";
			JSONObject req_body_json = new JSONObject("{}");
			String req = req_body_json.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(req)).build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			assertEquals(400, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void computeBaconPath_200() {
		try {
			HttpClient client = HttpClient.newHttpClient();

			String url1 = "http://localhost:8080/api/v1/addActor";
			JSONObject actor1Obj = new JSONObject("{'name': 'Bacon', 'actorId': 'nm0000102'}");
			String actor1Str = actor1Obj.toString();
			HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(url1))
					.PUT(HttpRequest.BodyPublishers.ofString(actor1Str)).build();
			HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

			String url = "http://localhost:8080/api/v1/computeBaconPath";
			JSONObject req_body_json = new JSONObject();
			req_body_json.put("actorId", "nm0000102");
			String req = req_body_json.toString();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.method("GET", HttpRequest.BodyPublishers.ofString(req)).build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			assertEquals(200, status);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
