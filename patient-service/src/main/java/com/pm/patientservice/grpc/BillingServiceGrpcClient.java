package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub stub;

    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress, @Value("${billing.service.grpc.port:9001}") int serverPort) {
        // BILLING_SERVICE_ADDRESS  BILLING_SERVICE_GRPC_PORT
        log.info("Initializing BillingServiceGrpcClient to {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        stub = BillingServiceGrpc.newBlockingStub(channel);
    }

    //create account
    public BillingResponse createBillingAccount(
            String name, String email, String patientId
    ){
        BillingRequest request = BillingRequest.newBuilder().setEmail(email).setName(name).setPatientId(patientId).build();
        BillingResponse response = stub.createBillingAccount(request);
        log.info("Received Billing Account Response via GRPC: {}", response);
        return response;
    }
}
