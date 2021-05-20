package it.inps.pocmessagebroker.processors;

import com.google.gson.Gson;
import it.inps.pocmessagebroker.model.EventoArcaPendingMessage;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDiscoveryMarkSentProcessor implements Processor {

    private final EventoArcaPendingRepository eventoArcaPendingRepository;

    @Autowired
    public EventDiscoveryMarkSentProcessor(EventoArcaPendingRepository eventoArcaPendingRepository) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        var messageStr = (String)exchange.getIn().getBody();
        var message = new Gson().fromJson(messageStr, EventoArcaPendingMessage.class);
        var eventoArcaPending = this.eventoArcaPendingRepository.findByArcaKeyAndIdApplicazione(message.getArcaKey(), message.getApplicazione().getId());
        eventoArcaPending.ifPresent(x -> {
            x.setStato(1);
            this.eventoArcaPendingRepository.save(x);
        });
    }
}
