package it.inps.pocmessagebroker.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDiscoveryEndProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("* EVENTI ARCA - TERMINATA");
        log.info("======================================================================");
    }
}
