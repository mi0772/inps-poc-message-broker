package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.jms.MessageSender;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDetailsSendToQueueProcessor implements Processor {

    private final ApplicazioneRepository applicazioneRepository;
    private final MessageSender messageSender;

    @Autowired
    public EventDetailsSendToQueueProcessor(ApplicazioneRepository applicazioneRepository, MessageSender messageSender) {
        this.applicazioneRepository = applicazioneRepository;
        this.messageSender = messageSender;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        EventoArcaPending eventoPending = (EventoArcaPending)exchange.getIn().getHeader("eventPending");
        Applicazione applicazione = this.applicazioneRepository.findById(eventoPending.getIdApplicazione()).orElseThrow(RuntimeException::new);
        this.messageSender.send(eventoPending.getXml(), applicazione.getQueue());
    }
}
