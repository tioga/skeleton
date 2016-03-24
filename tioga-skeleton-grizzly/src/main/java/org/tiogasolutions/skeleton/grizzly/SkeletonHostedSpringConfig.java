package org.tiogasolutions.skeleton.grizzly;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tiogasolutions.app.standard.StandardApplication;
import org.tiogasolutions.app.standard.execution.ExecutionManager;
import org.tiogasolutions.app.standard.jackson.StandardObjectMapper;
import org.tiogasolutions.app.standard.jaxrs.filters.StandardRequestFilterConfig;
import org.tiogasolutions.app.standard.jaxrs.filters.StandardResponseFilterConfig;
import org.tiogasolutions.app.standard.readers.BundledStaticContentReader;
import org.tiogasolutions.app.standard.session.DefaultSessionStore;
import org.tiogasolutions.app.standard.session.SessionStore;
import org.tiogasolutions.app.standard.view.thymeleaf.ThymeleafMessageBodyWriterConfig;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.lib.couchace.DefaultCouchServer;
import org.tiogasolutions.notify.notifier.Notifier;
import org.tiogasolutions.notify.sender.couch.CouchNotificationSender;
import org.tiogasolutions.runners.grizzly.GrizzlyServer;
import org.tiogasolutions.runners.grizzly.GrizzlyServerConfig;
import org.tiogasolutions.skeleton.engine.kernel.CouchServersConfig;
import org.tiogasolutions.skeleton.engine.resources.RootResource;

import java.util.Collections;
import java.util.List;

@Profile("hosted")
@Configuration
public class SkeletonHostedSpringConfig {

    // By all rights this is just silly... but, when an app has extra modules, as is the case with the Push server,
    // this helps ensure that both the ObjectMapper (used by JAX-RS) and the CouchDatabase is configured the same.
    private List<Module> jacksonModule = Collections.singletonList(new TiogaJacksonModule());

    @Bean
    public Notifier notifier(@Value("${skeleton.notifyCouchUrl}") String url,
                             @Value("${skeleton.notifyCouchUsername}") String username,
                             @Value("${skeleton.notifyCouchPassword}") String password,
                             @Value("${skeleton.notifyCouchDatabaseName}") String databaseName) {

        CouchNotificationSender sender = new CouchNotificationSender(url, databaseName, username, password);
        return new Notifier(sender);
    }

    @Bean
    public ThymeleafMessageBodyWriterConfig thymeleafMessageBodyWriterConfig() {
        ThymeleafMessageBodyWriterConfig config = new ThymeleafMessageBodyWriterConfig();
        config.setPathPrefix("/tioga-skeleton-engine/bundled");
        config.setPathSuffix(".html");
        config.setCacheable(true);
        return config;
    }

    @Bean
    public ExecutionManager executionManager() {
        return new ExecutionManager();
    }

    @Bean
    public StandardRequestFilterConfig standardRequestFilterConfig() {
        StandardRequestFilterConfig config = new StandardRequestFilterConfig();
        config.setUnauthorizedQueryParamName(RootResource.REASON_CODE_QUERY_PARAM_NAME);
        config.setUnauthorizedQueryParamValue(RootResource.REASON_CODE_UNAUTHORIZED_QUERY_PARAM_VALUE);
        config.setSessionRequired(true);
        config.setAuthenticationScheme("FORM_AUTH");
        config.setUnauthorizedPath("/");
        config.setRedirectUnauthorized(true);
        return config;
    }

    @Bean
    public StandardResponseFilterConfig standardResponseFilterConfig() {
        StandardResponseFilterConfig config = new StandardResponseFilterConfig();
        config.getExtraHeaders().put(StandardResponseFilterConfig.P3P, "CP=\"The Skeleton App does not have a P3P policy.\"");
        return config;
    }

    @Bean
    public SessionStore sessionStore(@Value("${skeleton.maxSessionDuration}") long maxSessionDuration) {
        return new DefaultSessionStore(maxSessionDuration, "session-id");
    }

    @Bean
    BundledStaticContentReader bundledStaticContentReader() {
        return new BundledStaticContentReader("/tioga-skeleton-grizzly/bundled");
    }

    @Bean
    public GrizzlyServerConfig grizzlyServerConfig(@Value("${skeleton.hostName}") String hostName,
                                                   @Value("${skeleton.port}") int port,
                                                   @Value("${skeleton.shutdownPort}") int shutdownPort,
                                                   @Value("${skeleton.context}") String context,
                                                   @Value("${skeleton.toOpenBrowser}") boolean toOpenBrowser) {

        GrizzlyServerConfig config = new GrizzlyServerConfig();
        config.setHostName(hostName);
        config.setPort(port);
        config.setShutdownPort(shutdownPort);
        config.setContext(context);
        config.setToOpenBrowser(toOpenBrowser);
        return config;
    }

    @Bean
    public CouchServersConfig couchServersConfig(@Value("${skeleton.couchUrl}") String url,
                                                 @Value("${skeleton.couchUsername}") String username,
                                                 @Value("${skeleton.couchPassword}") String password,
                                                 @Value("${skeleton.couchDatabaseName}") String databaseName) {

        return new CouchServersConfig(url, databaseName, username, password, jacksonModule);
    }

    @Bean
    public DefaultCouchServer defaultCouchServer(CouchServersConfig config) {
        return new DefaultCouchServer(config.toCouchSetup());
    }

    @Bean
    public ResourceConfig resourceConfig(ApplicationContext applicationContext) {
        StandardApplication application = new StandardApplication();
        ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
        resourceConfig.property("contextConfig", applicationContext);
        resourceConfig.packages("org.tiogasolutions.skeleton");
        return resourceConfig;
    }

    @Bean
    public StandardObjectMapper objectMapper() {
        return new StandardObjectMapper(jacksonModule, Collections.emptyList());
    }

    @Bean
    public TiogaJacksonTranslator tiogaJacksonTranslator(ObjectMapper objectMapper) {
        return new TiogaJacksonTranslator(objectMapper);
    }

    @Bean
    public GrizzlyServer grizzlyServer(GrizzlyServerConfig grizzlyServerConfig, ResourceConfig resourceConfig) {
        return new GrizzlyServer(grizzlyServerConfig, resourceConfig);
    }
}
