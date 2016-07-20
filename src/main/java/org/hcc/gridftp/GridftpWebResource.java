/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hcc.gridftp;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import org.onosproject.rest.AbstractWebResource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.onlab.util.Tools.nullIsNotFound;

/**
 * Manage the mapping between ip/port and GridFTP file transfer application level info.
 */
@Path("gridftp")
public class GridftpWebResource extends AbstractWebResource {

    /**
     * Get hello world greeting.
     *
     * @return 200 OK
     */
    @GET
    public Response getGreeting() {
        ObjectNode node = mapper().createObjectNode().put("hello", "world");
        return ok(node).build();
    }

    /**
      * Get the GridFTP application level information with IP/Port
      *
      * @param ipAddr Host IP address
      * @param port Host port
      * @return 200 OK
      */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{ipAddr}/{port}")
    public Response queryApplicationLevelInfo(@PathParam("ipAddr") String ipAddr,
                                              @PathParam("port") String port) {
        GridftpService service = get(GridftpService.class);
        final GridftpAppInfo appInfo = service.getAppLevelInfo(ipAddr, port);
        final ObjectNode root = codec(GridftpAppInfo.class).encode(appInfo, this);
        return Response.ok(root).build();
    }

    /**
      * Add application level info for specific GridFTP file transfer
      * 
      * @param stream JSON data describing the file transfer and corresponding app level info
      * @return 200 OK
      * @throws URISyntaxException uri syntax exception
      */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addApplicationLevelInfo(InputStream stream) throws URISyntaxException {
        GridftpAppInfo newAppInfo = jsonToAppInfo(stream);
        return get(GridftpService.class).addApplicationLevelInfo(newAppInfo) ?
                Response.ok().build():
                Response.serverError().build();
    }

    /**
     * Remove Gridftp application level info for an IP + port combination
     * 
     * @param ipAddr Host IP address
     * @param port Host port
     * @return 204 NO CONTENT
     */
    @DELETE
    @Path("{ipAddr}/{port}")
    public Response removeAppLevelInfo(@PathParam("ipAddr") String ipAddr,
                                       @PathParam("port") String port) {
        get(GridftpService.class).removeAppLevelInfo(ipAddr, port);
        return Response.noContent().build();
    }

    /**
     * Clear Gridftp application level info store
     *
     * @return 204 NO CONTENT
     */
    @DELETE
    public Response clearGridftpAppInfoDict() {
        get(GridftpService.class).clearGridftpAppInfoDict();
        return Response.noContent().build();
    }

    /**
     * Turns a JSON data into an GridftpAppInfo instance.
     */
    private GridftpAppInfo jsonToAppInfo(InputStream stream) {
        JsonNode node;
        try {
            node = mapper().readTree(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse Gridftp app info request", e);
        }

        String ipAddr = node.path("ipAddr").asText(null);
        String port  = node.path("port").asText(null);
        String username  = node.path("username").asText(null);
        String filename  = node.path("filename").asText(null);
        String direction  = node.path("direction").asText(null);

        if (ipAddr!=null && port!=null && username!=null && filename!=null && direction!=null) {
            return new GridftpAppInfo(ipAddr, port, username, filename, direction);
        }
        else {
            throw new IllegalArgumentException("Arguments must not be null.");
        }
    }

}
