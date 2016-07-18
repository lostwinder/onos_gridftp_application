package org.hcc.gridftp;

import org.apache.felix.scr.annotations.*;
import org.hcc.gridftp.GridftpAppInfo;
import org.hcc.gridftp.GridftpStore;
import org.onlab.util.KryoNamespace;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.store.AbstractStore;
import org.onosproject.store.serializers.KryoNamespaces;
import org.onosproject.store.service.ConsistentMap;
import org.onosproject.store.service.Serializer;
import org.onosproject.store.service.StorageService;
import org.onosproject.store.service.Versioned;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implementation of the GridFTP store service.
 */
@Component(immediate = true)
@Service
public class DistributedGridftpStore extends AbstractStore implements GridftpStore {

    private final Logger log = getLogger(getClass());

    private ConsistentMap<String, GridftpAppInfo> gridftpAppInfoDict;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected StorageService storageService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Activate
    public void activate() {
        ApplicationId appId = coreService.getAppId("org.hcc.gridftp");

        KryoNamespace.Builder serializer = KryoNamespace.newBuilder()
                .register(KryoNamespaces.API)
                .register(GridftpAppInfo.class);


        gridftpAppInfoDict = storageService.<String, GridftpAppInfo>consistentMapBuilder()
                .withSerializer(Serializer.using(serializer.build()))
                .withName("gridftp-app-info-dict")
                .withApplicationId(appId)
                .withPurgeOnUninstall()
                .build();

        log.info("Started");
    }

    @Deactivate
    public void deactivate() {
        log.info("Stopped");
    }

    @Override
    public GridftpAppInfo getAppLevelInfo(String ipAddr, String port) {
        String key = new StringBuilder().append(ipAddr).append(":").append(port).toString();
        Versioned<GridftpAppInfo> appInfo = gridftpAppInfoDict.get(key);
        if (appInfo != null) {
            return appInfo.value();
        } else {
            return null;
        }

    }

    @Override
    public void addApplicationLevelInfo(GridftpAppInfo newAppInfo) {
        String key = newAppInfo.ipAddrPort();
        gridftpAppInfoDict.putIfAbsent(key, newAppInfo);
    }

    @Override
    public void removeAppLevelInfo(String ipAddr, String port) {
        String key = new StringBuilder().append(ipAddr).append(":").append(port).toString();
        gridftpAppInfoDict.remove(key);
    }

    @Override
    public void clearGridftpAppInfoDict() {
        gridftpAppInfoDict.clear();
    }


}
