package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.AuthenticationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.GoogleLoginRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.AuthenticationResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.IntrospectRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.GoogleLoginResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.IntrospectResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.enums.Role;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.AppException;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.ErrorCode;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IUserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin("http://localhost:3000")
public class AuthenticationService {
    static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    final GoogleIdTokenVerifier verifier; // Ensure you initialize this verifier properly
    final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token= request.getToken();

        JWSVerifier verifier =new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT= SignedJWT.parse(token);

        Date expityTime= signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return  IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user= IUserRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        //kiểm tra trang thái user có xác thực email chưa
        if(!user.isEmailVerified()){
            throw new AppException(ErrorCode.USER_NOT_VERIFIED);
        }
        // Kiểm tra trạng thái của user (chỉ cho phép đăng nhập nếu status là "available")
        if (!"available".equals(user.getAvailableStatus())) {
            throw new AppException(ErrorCode.USER_NOT_AVAILABLE);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated= passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token= generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public GoogleLoginResponse authenticateWithGoogle(GoogleLoginRequest request) {
        // Verify the Google ID token
        log.info("Starting Google login authentication");
        try {
            GoogleIdToken idToken = verifier.verify(request.getToken());
            if (idToken == null) {
                log.warn("ID token verification failed");
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            // Get user information from the token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            // Check if the user already exists
            User user = IUserRepository.findByEmail(email).orElseGet(() -> {
                // If the user does not exist, create one
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(payload.get("name").toString()); // Set username or any other necessary fields
                newUser.setAvailableStatus("available");// Set initial status
                newUser.setPassword(passwordEncoder.encode("12345"));// default password
                newUser.setEmailVerified(true); // Consider that Google verifies the email
                Set<String> roles = new HashSet<>();
                roles.add(Role.BUYER.name());
                newUser.setRoles(roles);
                // Set other necessary fields if needed
                return IUserRepository.save(newUser);
            });

            // Check if user is email verified (if applicable)
            if (!user.isEmailVerified()) {
                throw new AppException(ErrorCode.USER_NOT_VERIFIED);
            }

            // Check user availability status
            if (!"available".equals(user.getAvailableStatus())) {
                throw new AppException(ErrorCode.USER_NOT_AVAILABLE);
            }

            // Generate a JWT token for the user
            var token = generateToken(user);

            return GoogleLoginResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            log.error("Error during Google login authentication", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }


    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet= new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("EventFlowerExchange")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userID", user.getUserID())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("address", user.getAddress())
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("roles", user.getRoles())
                .claim("scope", buildScope(user))

                .build();


        Payload payload= new Payload(jwtClaimSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
            StringJoiner stringJoiner= new StringJoiner(" ");
            if(!CollectionUtils.isEmpty(user.getRoles())) {
                user.getRoles().forEach(stringJoiner::add);
            }
            return stringJoiner.toString();
    }
}
