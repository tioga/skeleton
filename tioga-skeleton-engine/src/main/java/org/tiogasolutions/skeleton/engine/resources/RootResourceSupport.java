package org.tiogasolutions.skeleton.engine.resources;

import org.tiogasolutions.app.standard.view.embedded.EmbeddedContent;
import org.tiogasolutions.dev.common.net.InetMediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public abstract class RootResourceSupport {

    public RootResourceSupport() {
    }

    public abstract UriInfo getUriInfo();

    @GET
    @Produces(InetMediaType.IMAGE_PNG_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(jpg|JPG))$) }")
    public EmbeddedContent renderJPGs() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.IMAGE_PNG_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(png|PNG))$) }")
    public EmbeddedContent renderPNGs() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.IMAGE_GIF_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(gif|GIF))$) }")
    public EmbeddedContent renderGIFs() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.TEXT_PLAIN_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(txt|TXT|text|TEXT))$) }")
    public EmbeddedContent renderText() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.TEXT_HTML_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(html|HTML))$) }")
    public EmbeddedContent renderHtml() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.TEXT_CSS_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(css|CSS))$) }")
    public EmbeddedContent renderCSS() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.APPLICATION_JAVASCRIPT_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(js|JS))$) }")
    public EmbeddedContent renderJavaScript() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.IMAGE_ICON_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(ico|ICO))$) }")
    public EmbeddedContent renderICOs() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Produces(InetMediaType.APPLICATION_PDF_VALUE)
    @Path("{resource: ([^\\s]+(\\.(?i)(pdf|PDF))$) }")
    public EmbeddedContent renderPDFs() throws Exception {
        return new EmbeddedContent(getUriInfo());
    }

    @GET
    @Path("/trafficbasedsspsitemap.xml")
    public Response trafficbasedsspsitemap_xml() {
        return Response.status(404).build();
    }

    @GET
    @Path("/apple-touch-icon-precomposed.png")
    public Response apple_touch_icon_precomposed_png() {
        return Response.status(404).build();
    }

    @GET
    @Path("/apple-touch-icon.png")
    public Response apple_touch_icon_png() {
        return Response.status(404).build();
    }

    @GET
    @Path("/manager/status")
    public Response managerStatus() throws Exception {
        return Response.status(404).build();
    }

    @GET
    @Path("{resource: ([^\\s]+(\\.(?i)(php|PHP))$) }")
    public Response renderTXTs() throws Exception {
        return Response.status(404).build();
    }
}
