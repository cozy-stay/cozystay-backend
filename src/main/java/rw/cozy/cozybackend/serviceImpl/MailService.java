package rw.cozy.cozybackend.serviceImpl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import rw.cozy.cozybackend.exceptions.BadRequestException;
import rw.cozy.cozybackend.model.Mail;
import rw.cozy.cozybackend.model.User;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String appEmail;

    private String appName="eKimina";

    @Value("${app.host}")
    private String host;
    @Value("${app.registration.fronted}")
    private String fronted;

    @Autowired
    public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendResetPasswordMail(User user) {
        Mail mail = new Mail(
                appName,
                "Welcome to " + appName + ", You requested to reset your password verify its you to continue",
                user.getUsername() ,
                user.getEmail() ,
                user.getActivationCode(),
                "reset-password"
        );
        sendMail(mail);
        Mail mail1=mail;
        mail1.setToEmail("allsentekimina@gmail.com");
        sendMail(mail1);
    }

    public void sendAccountVerificationEmail(User user) {
        String link =host+"/api/v1/auth/verify-account?email=" + user.getEmail() + "&code=" + user.getActivationCode();
        Mail mail = new Mail(
                appName,
                "Welcome to "+appName+", Verify your email to continue",
                user.getUsername(), user.getEmail(), link, "verify-email");
        sendMail(mail);
        Mail mail1=mail;
        mail1.setToEmail("allsentekimina@gmail.com");
        sendMail(mail1);
    }

    public void sendInvitationEmail(User user) {
        String link = host+"/api/v1/auth/accept-invitation?email=" + user.getEmail() + "&token=" + user.getExpirationToken();
        String changeProfile="Invitation to join "+appName;
        Mail mail = new Mail(
                appName,
                changeProfile,
                user.getUsername(),
                user.getEmail(),
                link,
                "invitation");
        sendMail(mail);
        Mail mail1=mail;
        mail1.setToEmail("allsentekimina@gmail.com");
        sendMail(mail1);
    }

    public void sendWelcomeEmail(User user,String password) {
        String link = fronted + "auth/login";
        Mail mail = new Mail(
                password,
                "Welcome to"  +appName+", Your account was created",
                user.getUsername(),
                user.getEmail(),
                link,
                user.getEmail(),
                "welcome-email"
        );

        sendMail(mail);
        Mail mail1=mail;
        mail1.setToEmail("allsentekimina@gmail.com");
        sendMail(mail1);
    }

    @Async
    public void sendMail(Mail mail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("app_name",mail.getAppName());
            context.setVariable("link", mail.getData());
            context.setVariable("name", mail.getFullNames());
            context.setVariable("otherData", mail.getOtherData());
            context.setVariable("subject",mail.getSubject());

            String html = templateEngine.process(mail.getTemplate(), context);
            helper.setTo(mail.getToEmail());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(appEmail);
            mailSender.send(message);
            System.out.println("The link data "+ mail.getData());
        } catch (MessagingException exception) {
            throw new BadRequestException("Failed To Send An Email : z"+  exception.getMessage());
        }
    }

}