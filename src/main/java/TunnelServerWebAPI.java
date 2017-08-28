import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import authentication.AuthenticationDetails;
import authentication.BasicAuthenticationFilter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static spark.Spark.*;

public class TunnelServerWebAPI {
	private static final String success = "success";
	private static final Logger logger = LoggerFactory.getLogger(TunnelServerWebAPI.class);
	private Set<PeerInfo> peers = new HashSet<>();
	private static final Gson gson = new GsonBuilder().setLenient().enableComplexMapKeySerialization()
			.create();

	public TunnelServerWebAPI() {
		super();
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
			ipAddress(address.getHostAddress());
			port(8883);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}


		String routePrefix = StringUtils.EMPTY;

		staticFileLocation("/public");
		before(new BasicAuthenticationFilter(routePrefix + "/",
				new AuthenticationDetails("user", "password")));

		get("/", "text/html", (req,resp)-> {return  new ModelAndView(Maps.newHashMap(), "index.html");}, new HtmlEngine());

		get(routePrefix + "/stats", this::stats);
		post(routePrefix+ "/update", this::updateInfo);
		post(routePrefix + "/disable", this::disconnect);
		System.out.println(String.format("Tunnel server Web API started at %s", address.getHostName()));
	}

	private Object stats(Request request, Response response) throws Exception {

		response.header("Content-Type", "application/json; charset=utf-8");
		return gson.toJson(peers);

	}

	private Object updateInfo(Request request, Response response) throws Exception {
		System.out.println("GOT CLIENT - " + request.body());

		PeerInfo info = gson.fromJson(request.body(), PeerInfo.class);
		info.setConnectTime(System.currentTimeMillis());
		PeerInfo existing = findPeer(info.getUuidString());
		if(existing != null){
			peers.remove(existing);
		}
		peers.add(info);
		System.out.println("Total peers - " + peers.size());
		return success;
	}

	private Object disconnect(Request request, Response response) throws Exception {
		System.out.println("Disconnecting ");
		PeerInfo info = gson.fromJson(request.body(), PeerInfo.class);
		PeerInfo existing = findPeer(info.getUuidString());
		if(existing != null){
			peers.remove(existing);
		}
		return success;
	}

	private PeerInfo findPeer(String uid){
	return 	peers.stream().filter(new Predicate<PeerInfo>() {
			@Override
			public boolean test(PeerInfo peerInfo) {
				return peerInfo.getUuidString().equals(uid);
			}
		}).findFirst().orElse(null);
	}
	public void shutdown() {
		stop();
		logger.info("Tunnel server Web API shutdown.");
	}

}
