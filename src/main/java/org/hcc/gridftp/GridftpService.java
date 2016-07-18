package org.hcc.gridftp;

/**
 * Service interface exported by Gridftp application
 */
public interface GridftpService {

    /**
     * Gets Gridftp file transfer application level information for a file transfer
     *
     * return an instance of GridftpAppInfo
     */
    GridftpAppInfo getAppLevelInfo(String ipAddr, String port);

    /**
     * Adds an instance of application level information for a Gridftp file transfer
     *
     * @param instance of GridftpAppInfo
     * @return true if successfully added, otherwise false
     */
    boolean addApplicationLevelInfo(GridftpAppInfo newAppInfo);

    /**
     * Remove application level information for a Gridftp file transfer
     *
     * @param String ipAddr
     * @param String port
     */
    void removeAppLevelInfo(String ipAddr, String port);

    /**
     * Clear GridFTP application level info dictionary.
     */
    void clearGridftpAppInfoDict();
}
