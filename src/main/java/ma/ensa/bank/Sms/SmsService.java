package ma.ensa.bank.Sms;


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

    public String sendSms(SmsEntity smsEntity) {

        com.vonage.client.sms.messages.TextMessage message = new com.vonage.client.sms.messages.TextMessage("Vonage APIs",
                "212684038130",
                "Your Verification Code for the Payment is : "+ (int) (Math.random() * 10000)
        );

        com.vonage.client.sms.SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == com.vonage.client.sms.MessageStatus.OK) {
            return "Message sent successfully.";
        } else {
            return "Message failed with error: " + response.getMessages().get(0).getErrorText();
        }

    }
}
