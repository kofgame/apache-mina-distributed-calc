package home.poc.mina.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("calcClientHandler")
public class CalcClientHandler extends IoHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(CalcClientHandler.class);

    private String operatorAndArguments;

    public CalcClientHandler() {}

    public void setOperatorAndArguments(String operatorAndArguments) {
        this.operatorAndArguments = operatorAndArguments;
    }

    @Override
    public void sessionOpened(IoSession session) {
        // send Calc request
        session.write(operatorAndArguments);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        System.out.println("Received answer: " + message.toString());

        String response = String.class.cast(message);
        LOGGER.info("Received from Server RESPONSE: " + response);
        session.close(true);
    }
}