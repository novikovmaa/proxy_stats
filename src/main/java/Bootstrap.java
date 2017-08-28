/**
 * Created by novikovma on 8/28/2017.
 */
public class Bootstrap {
    public static void main(String[] args) {
        final TunnelServerWebAPI webAPI = new TunnelServerWebAPI();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                webAPI.shutdown();
            }
        });
    }
}
