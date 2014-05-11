
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mark Dechamps
 */
public class TestWebserver {

   /* @Test
    public void testJettyWebserver() {
        Logger log = Logger.getAnonymousLogger();
        try {
            log.info("Starting the embedded Jetty server");
            String workingDirectory = System.getProperty("user.dir");
            String descriptor = workingDirectory + "/web/WEB-INF/web.xml";
            String resourceBase = workingDirectory + "/web";
            Server server = new Server(8090);
            WebAppContext context = new WebAppContext();
            context.setDescriptor(descriptor);
            context.setResourceBase(resourceBase);
            context.setContextPath("/");
            context.setParentLoaderPriority(true);
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }*/
}
