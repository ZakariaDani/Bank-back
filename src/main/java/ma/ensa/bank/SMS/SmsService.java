package ma.ensa.bank.SMS;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vonage.client.VonageClient;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    //twilio
    public static final String ACCOUNT_SID = "ACee29398a4819ecd6e704046892ed2c36";
    public static final String AUTH_TOKEN ="6f52bf71d814b7398256b6b789b0f3c1";
    public static final String MY_NUMBER = "+12056513308";

    //Vonage
    VonageClient client;

    public SmsService(){
        //initializing vonage api
        client = VonageClient.builder()
                .apiKey("112d94a6")
                .apiSecret("u8pI4HFQSyucFUJT")
                .build();

        //initializing twilio api
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public boolean sendSmsUsingVonageAPI(SmsEntity smsEntity) {

        com.vonage.client.sms.messages.TextMessage message = new com.vonage.
                client.sms.messages.TextMessage("E banking APP",
                smsEntity.getSmsReceiver(),
                smsEntity.getMessage()
        );

        com.vonage.client.sms.SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == com.vonage.client.sms.MessageStatus.OK) {
            return true;
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
            return false;
        }
    }

    public boolean sendSmsUsingTwilioAPI(SmsEntity smsEntity){
        try{
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+"+smsEntity.getSmsReceiver()),
                            new com.twilio.type.PhoneNumber(MY_NUMBER),
                            smsEntity.getMessage())
                    .create();
            return true;
        }
        catch(Exception exception){
            System.out.print(exception.getMessage());
            return false;
        }
    }
}