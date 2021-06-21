package it.inps.pocmessagebroker.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageSender {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String message, String queue) {
        log.info("tentativo di invio del messaggio {} sulla coda : {}", message, queue);
        this.jmsTemplate.convertAndSend(queue, message);
    }
}
