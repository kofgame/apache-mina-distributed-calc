package home.poc.mina.server;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class CalcServer {
    private IoAcceptor acceptor;

    // TODO: move into props file
    public static final int SERVER_PORT = 9123;
    public static final int READ_BUFFER_SIZE = 2048;

    private static final LoggingFilter loggingFilter = new LoggingFilter();
    public static final ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName( "UTF-8" )));

    public static void main( String[] args) throws IOException {
        new CalcServer().startServer();
    }

    /**
     * Starts IoAcceptor, which listens incoming requests
     * @throws IOException
     */
    public CalcServer startServer() throws IOException {
        acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast( "logger", loggingFilter);
        acceptor.getFilterChain().addLast( "codec", codecFilter);

        acceptor.setHandler(  new CalcServerHandler() );

        acceptor.getSessionConfig().setReadBufferSize( READ_BUFFER_SIZE );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 30);

        acceptor.bind( new InetSocketAddress(SERVER_PORT) );

        return this;
    }

    /**
     * Stops the IoAcceptor
     */
    public void stopServer() {
        acceptor.dispose();
    }

}