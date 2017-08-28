/**
 * Created by novikovma on 8/28/2017.
 */
public class PeerInfo {
    public final String osName;
    public final String osVersion;
    public final String uuidString;
    private Long connectTime;

    public PeerInfo(String osName, String osVersion, String uidString) {
        this.osName = osName;
        this.osVersion = osVersion;
        this.uuidString = uidString;
    }

    public Long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Long connectTime) {
        this.connectTime = connectTime;
    }

    public String getUuidString() {
        return uuidString;
    }
}
