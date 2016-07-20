package org.hcc.gridftp;

/**
 * Class for GridFTP file transfer application level information
 */
public class GridftpAppInfo {
    private String ipAddr;
    private String port;
    private String username;
    private String filename;
    private String direction;

    public GridftpAppInfo(String ipAddr,
                          String port,
                          String username,
                          String filename,
                          String direction) {
        this.ipAddr = ipAddr;
        this.port = port;
        this.username = username;
        this.filename = filename;
        this.direction = direction;
    }

    public String ipAddrPort() {
        return new StringBuilder().append(ipAddr).append(":").append(port).toString();
    }

    public String appInfoString() {
        return new StringBuilder().append(username).append(":").append(filename).append(":").append(direction).toString();
    }
}
