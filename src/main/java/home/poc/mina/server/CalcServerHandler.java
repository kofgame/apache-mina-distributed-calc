package home.poc.mina.server;


import home.poc.mina.calculator.Operation;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import home.poc.mina.calculator.DummyCalculator;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalcServerHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalcServerHandler.class);
    private static final String SPACE_DELIMITER = " ";

    private DummyCalculator dummyCalculator = new DummyCalculator();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause ) {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        if ( !(message instanceof String)) {
            throw new IllegalArgumentException("Unrecognized calc request type");
        }
        String originalMsg = String.class.cast(message);

        if(StringUtils.contains(originalMsg, Operation.CLOSE.name())) {
            processCloseMessage(session);
            return;
        }
        List<String> requestArgs = new ArrayList<String>(Arrays.asList( originalMsg.split(SPACE_DELIMITER)) );
        Operation operator = Operation.fromValue(requestArgs.remove(0));

        List<Integer> numericArgs = Lists.transform(requestArgs, new Function<String, Integer>() {
            public Integer apply(String str) {
                return Integer.parseInt(str);
            }
        });
        int result = dummyCalculator.calculate(operator, numericArgs);

        session.write("RESULT " + result);

        LOGGER.info("Calculated RESULT: " + result + " for REQUEST: " + originalMsg);
    }

    private void processCloseMessage(IoSession session) {
        session.write("CLOSED");
        session.close(false); // i.e. after all queued write requests are flushed
    }

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
        LOGGER.info("Session IDLE, idleCount: " + session.getIdleCount( status ));
    }

}