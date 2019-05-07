package oktenweb.try_register_with_email.controllers;

import oktenweb.try_register_with_email.models.ResponseTransfer;
import oktenweb.try_register_with_email.models.User;
import oktenweb.try_register_with_email.services.impl.MailServiceImpl;
import oktenweb.try_register_with_email.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainRestController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    MailServiceImpl mailServiceImpl;

      User user = new User();
      String username = "";
      String responseOnConfirmPass = "";
     private String confirmPass = "<div>\n" +
            "    <a href=\"http://localhost:8080/confirmSaveUser\" target=\"_blank\"> Confirm your registration </a>\n" +
            "</div>";

    @CrossOrigin(origins = "*")
    @PostMapping("/saveUser")
    public ResponseTransfer saveUser(@RequestBody User u,
                                     Model model){
        user = u;
        List<User> users = userServiceImpl.findAll();
//        if(users.stream().anyMatch(user1 ->
//                user1.getEmail().equals(user.getEmail()))){
        if(userServiceImpl.existsByEmail(user.getEmail())){
            return new ResponseTransfer("User with such email already exists!");
        }else {
            String responseFromMailSender =
                    mailServiceImpl.send(user.getEmail(), confirmPass);
            if(responseFromMailSender.equals("Message was sent")){

                username = user.getUsername();
                model.addAttribute("username", username);
                return new ResponseTransfer("Preliminary Registration is completed. Take a look into your email");
            }else {
                return new ResponseTransfer(responseFromMailSender);
            }

        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveUserFinally")
    public String saveUserFinally(@RequestBody ResponseTransfer r,
                                  Model model){
        String results = "";
        System.out.println("password: " + r.getText());
        if(user.getPassword().equals(r.getText())){
            userServiceImpl.save(user);
            results = "Your account was saved successfully";
        }else {
            results = "WRONG PASSWORD";
        }
        System.out.println(results);
        responseOnConfirmPass = results;
        model.addAttribute("responseOnConfirmPass", responseOnConfirmPass);
        return results;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userServiceImpl.findAll();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseTransfer deleteUser(@PathVariable("id") int id) {

        return userServiceImpl.deleteById(id);
    }

}
