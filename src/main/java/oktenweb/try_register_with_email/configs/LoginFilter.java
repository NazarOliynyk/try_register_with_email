package oktenweb.try_register_with_email.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import oktenweb.try_register_with_email.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private User user;
    private UserDetailsService userDetailsService;

    public LoginFilter(String url, AuthenticationManager authManager, UserDetailsService userDetailsService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.userDetailsService = userDetailsService;
    }


//    During the authentication attempt,
// which is dealt by the attemptAuthentication method,
// we retrieve the username and password from the request.
// After they are retrieved, we use the AuthenticationManager to verify that these details match with an existing user.
// If it does, we enter the successfulAuthentication method.
// In this method we fetch the name from the authenticated user,
// and pass it on to TokenAuthenticationService, which will then add a JWT to the response.



    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        //this method react  on /login url  and retrive body from request
        //then map it to model AccountCredential
        user = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), User.class);

        System.out.println(user);
        // find our object in database, parse it and ad it as auth object


        // then  get default method getAuthenticationManager()
        // and set Authentication object based on data from creds object

        // if auth process if success we jump to line 65 successfulAuthentication()
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        // Collections.emptyList()  // it did not work
                        Collections.<GrantedAuthority>emptyList()
                )
        );


    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        // if in prev method we was authenticate
        // we create token

        String jwtoken = Jwts.builder()
                .setSubject(auth.getName()) // this auth goes from the previous method
                // returned from that method
                .signWith(SignatureAlgorithm.HS512, "yes".getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .compact();
        //and add it to header
        res.addHeader("Authorization", "Bearer " + jwtoken);
        User userLogged= (User) userDetailsService.loadUserByUsername(auth.getName());
       System.out.println(userLogged.toString());
        res.addHeader("UserClass", String.valueOf(userLogged.getClass()));
        res.addHeader("UserLogged", new ObjectMapper().writeValueAsString(userLogged));
    }
}

