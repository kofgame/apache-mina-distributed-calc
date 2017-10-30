package home.poc.mina.calculator;

import home.poc.mina.calculator.Operation;
import home.poc.mina.client.CalcClient;
import home.poc.mina.client.CalcClientHandler;
import home.poc.mina.server.CalcServer;
import org.apache.mina.core.session.IoSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static home.poc.mina.calculator.Operation.DIVIDE;
import static home.poc.mina.calculator.Operation.MINUS;
import static home.poc.mina.calculator.Operation.PLUS;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:META-INF/application-context-root.xml")
public class CalculatorTest {

    CalcServer calcServer = new CalcServer();
    ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/application-context-root.xml");;

    private CalcClient calcClient = (CalcClient) context.getBean("calcClient");
    private CalcClientHandler calcClientHandler = (CalcClientHandler) context.getBean("calcClientHandler");;
    private CalcClientHandler spyCalcClientHandler = Mockito.spy(calcClientHandler);

    ArgumentCaptor<IoSession> ioSessionArgCaptor  = ArgumentCaptor.forClass(IoSession.class);
    ArgumentCaptor<Object> respMessageArgCaptor = ArgumentCaptor.forClass(Object.class);

    @Before
    public void setUp() throws IOException {
        calcClient.setCalcClientHandler(spyCalcClientHandler);
        calcServer.startServer();
    }

    @Test
    public void testPlus() throws Exception {
        calcClient.sendCalcRequest(PLUS + " 3 4 5 0");

        Mockito.verify(spyCalcClientHandler).messageReceived(ioSessionArgCaptor.capture(), respMessageArgCaptor.capture());
        Assert.assertEquals("RESULT 12", respMessageArgCaptor.getValue());
    }

    @Test
    public void testMinus() throws Exception {
        calcClient.sendCalcRequest(MINUS + " 24 4 10 5");

        Mockito.verify(spyCalcClientHandler).messageReceived(ioSessionArgCaptor.capture(), respMessageArgCaptor.capture());
        Assert.assertEquals("RESULT 5", respMessageArgCaptor.getValue());
    }

    @Test
    public void testDivide() throws Exception {
        calcClient.sendCalcRequest(DIVIDE + " 64 8 4 2");

        Mockito.verify(spyCalcClientHandler).messageReceived(ioSessionArgCaptor.capture(), respMessageArgCaptor.capture());
        Assert.assertEquals("RESULT 1", respMessageArgCaptor.getValue());
    }

    @Test
    public void testMultiply() throws Exception {
        calcClient.sendCalcRequest(Operation.MULTIPLY + " 2 3 4");

        Mockito.verify(spyCalcClientHandler).messageReceived(ioSessionArgCaptor.capture(), respMessageArgCaptor.capture());
        Assert.assertEquals("RESULT 24", respMessageArgCaptor.getValue());
    }

    @Test
    public void testClose() throws Exception {
        calcClient.sendCalcRequest(Operation.CLOSE.name);

        Mockito.verify(spyCalcClientHandler).messageReceived(ioSessionArgCaptor.capture(), respMessageArgCaptor.capture());
        Assert.assertEquals("CLOSED", respMessageArgCaptor.getValue());
    }

    @After
    public void tearDown() {
        calcServer.stopServer();
    }

}