package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] patientEventBytes) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(patientEventBytes);
            log.info("Received patient event: [ Patient ID : {}, patientName :{}, patientEmail :{} ]", patientEvent.getPatientId(),patientEvent.getName(),patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error while parsing patient event", e);
//            throw new RuntimeException(e);
        }

    }
}

