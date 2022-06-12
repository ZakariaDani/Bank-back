package ma.ensa.bank.SMS;

import com.vonage.client.VonageClient;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    VonageClient client;

    public SmsService(){
        client = VonageClient.builder()
                .apiKey("112d94a6")
                .apiSecret("u8pI4HFQSyucFUJT")
                .build();
    }

    public boolean sendSms(SmsEntity smsEntity) {

        com.vonage.client.sms.messages.TextMessage message = new com.vonage.
                client.sms.messages.TextMessage("E banking APP",
                smsEntity.getSmsReceiver(),
                smsEntity.getMessage()
        );

        com.vonage.client.sms.SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == com.vonage.client.sms.MessageStatus.OK) {
            System.out.println("Message sent successfully.");
            return true;
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
            return false;
        }
    }
}
