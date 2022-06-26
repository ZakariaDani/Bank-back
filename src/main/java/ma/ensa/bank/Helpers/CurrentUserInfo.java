package ma.ensa.bank.Helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;

public class CurrentUserInfo {

    private String phone;

    public static String getPhoneNumber(HttpServletRequest request){

        String token = request.getHeader("Authorization");
        String jwt = token.substring(7);
        Algorithm algorithm = Algorithm.HMAC256("ATLASSEBANK");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getSubject();

    }

}