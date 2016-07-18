package org.hcc.gridftp;

import org.onosproject.store.Store;

/**
 * Service interface exported by Gridftp distributed store.
 */
public interface GridftpStore extends Store {

    /**
     * Get application level information for Gridftp file transfer
     *
     * @return GridftpAppInfo instance
     */
    GridftpAppInfo getAppLevelInfo(String ipAddr, String port);


    /**
     * Adds an instance of application level information for a Gridftp file transfer
     */

    void addApplicationLevelInfo(GridftpAppInfo newAppInfo);

    /**
     * Remove application level information for a Gridftp file transfer
     */
    void removeAppLevelInfo(String ipAddr, String port);

    /**
     * Clear Gridftp application level info dictionary
     */
    void clearGridftpAppInfoDict();
}
