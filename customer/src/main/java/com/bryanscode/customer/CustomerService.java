package com.bryanscode.customer;

import com.bryanscode.clients.fraud.FraudCheckResponse;
import com.bryanscode.clients.fraud.FraudClient;
import com.bryanscode.clients.notification.NotificationClient;
import com.bryanscode.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final FraudClient fraudClient;
  private final NotificationClient notificationClient;


  public void registerCustomer(CustomerRegistrationRequest request) {
    Customer customer = Customer.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .build();
    // todo: check if email valid
    // todo: check if email not taken
    customerRepository.saveAndFlush(customer);
    FraudCheckResponse fraudCheckResponse =
        fraudClient.isFraudster(customer.getId());

    if (fraudCheckResponse.isFraudster()) {
      throw new IllegalStateException("fraudster");
    }

    notificationClient.sendNotification(
        new NotificationRequest(
            customer.getId(),
            customer.getEmail(),
            String.format("Hi %s, welcome to Bryanscode...",
                customer.getFirstName())
        )
    );
  }
}
