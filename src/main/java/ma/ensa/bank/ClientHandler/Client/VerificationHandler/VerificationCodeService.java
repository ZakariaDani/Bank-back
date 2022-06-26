package ma.ensa.bank.ClientHandler.Client.VerificationHandler;


import ma.ensa.bank.ClientHandler.Client.TransactionHandler.NotValidatedTransaction;
import ma.ensa.bank.SMS.SmsEntity;
import ma.ensa.bank.SMS.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.rmi.server.ExportException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private SmsService smsService;

    @Transactional
    public void sendVerificationCode(String receiver, Double amount, NotValidatedTransaction transaction){


        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            VerificationCode verificationCode = new VerificationCode();
            String code = "" + Math.round(Math.random()*100000);
            String smsReceiver = receiver.substring(1);
            smsReceiver = "212" + smsReceiver;
            String message = "You have just requested to make a transaction of an amount of "+
                    amount+
                    " DH, Here it is the verification code: "+code;

            verificationCode.setReceiver(receiver);
            verificationCode.setCode(code);
            verificationCode.setDate(
                    dtf.format(now)
            );
            verificationCode.setTransaction(transaction);
            verificationCodeRepository.save(verificationCode);

            SmsEntity smsEntity = new SmsEntity(smsReceiver,message,code);

            boolean theMessageWasSent = smsService.sendSmsUsingTwilioAPI(smsEntity);

            if(theMessageWasSent == false){
                throw new RuntimeException("There is something wrong with the sending message service");
            }
        }
        catch(Exception exception){
            throw exception;
        }

    }

    public VerificationCode getVerificationCode(Long transactionId, String phoneNumber) {

        VerificationCode verificationCode = verificationCodeRepository.
                findVerificationCodeByTransactionIdAndReceiver(transactionId,phoneNumber);

        return verificationCode;
    }
}