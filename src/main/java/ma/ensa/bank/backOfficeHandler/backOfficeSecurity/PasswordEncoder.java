package ma.ensa.bank.backOfficeHandler.backOfficeSecurity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Configuration
public class PasswordEncoder {
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public static String myEncryptionAlgorithm(String clearText) {
        Algorithm algorithm = Algorithm.HMAC256("ATLASSEBANK");
        String encoded_clearText = JWT.create()
                .withSubject(clearText)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L *24*60 * 60 * 1000))
                .sign(algorithm);

        return encoded_clearText;
    }

    public static String myDecreptionAlgorithm(String encodedText){
        Algorithm algorithm = Algorithm.HMAC256("ATLASSEBANK");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(encodedText);
        return decodedJWT.getSubject();

    }
}
