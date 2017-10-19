package home.poc.mina.client;

import home.poc.mina.server.CalcServer;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component("calcClient")
public class CalcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalcClient.class);

    public static final String SERVER_HOST_NAME = "localhost";
    private static final int CONNECT_TIMEOUT = 60000;

    @Autowired
    private CalcClientHandler calcClientHandler;

    @Autowired
    private NioSocketConnector nioSocketConnector;

    /**
     *
     * @param calcArg OPERATOR and arguments, e.g. "PLUS 4325 983 0"
     */
    public void sendCalcRequest(String calcArg) throws InterruptedException {
        NioSocketConnector connector = nioSocketConnector/*new NioSocketConnector()*/;
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        calcClientHandler.setOperatorAndArguments(calcArg);
        connector.setHandler(calcClientHandler);

        IoSession session;
        for (;;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(SERVER_HOST_NAME, CalcServer.SERVER_PORT));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                LOGGER.error("Failed to connect to Server: " + e.getMessage());
                Thread.sleep(3000);
            }
        }
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }

    public void setCalcClientHandler(CalcClientHandler calcClientHandler) {
        this.calcClientHandler = calcClientHandler;
    }

}