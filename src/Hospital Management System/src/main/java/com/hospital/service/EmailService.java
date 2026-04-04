package com.hospital.service;

import com.hospital.model.base.Person;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST     = "sandbox.smtp.mailtrap.io";
    private static final String SMTP_PORT     = "2525";
    private static final String FROM_EMAIL    = "your-mailtrap-username";
    private static final String FROM_PASSWORD = "your-mailtrap-password";
    private static final String FROM_NAME     = "Hospital Management System";
    private static final String BASE_URL      = "http://localhost:8080";

    private EmailService() {}


    private static void send(String toEmail, String toName,
                             String subject, String htmlBody) {
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send email to "
                    + toEmail + ": " + e.getMessage());
        }
    }


    public static void sendVerificationEmail(Person person, String token) {
        String verifyUrl = BASE_URL + "/auth/verify?token=" + token;

        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px;">
                    <h2 style="color: #2c3e50;">Verify Your Email</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>Thank you for registering. Please verify your email:</p>
                    <a href="%s"
                       style="background:#3498db;color:white;padding:12px 24px;
                              text-decoration:none;border-radius:4px;">
                        Verify Email
                    </a>
                    <p style="color:#999;margin-top:20px;">
                        This link expires in 30 minutes.
                    </p>
                </div>
                """.formatted(person.getDisplayName(), verifyUrl);

        send(person.getEmail(),
                person.getDisplayName(),
                "Verify your email — Hospital System",
                html);
    }


    public static void sendPasswordResetEmail(Person person, String token) {
        String resetUrl = BASE_URL + "/auth/reset-password?token=" + token;

        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px;">
                    <h2 style="color: #e74c3c;">Password Reset</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>We received a request to reset your password:</p>
                    <a href="%s"
                       style="background:#e74c3c;color:white;padding:12px 24px;
                              text-decoration:none;border-radius:4px;">
                        Reset Password
                    </a>
                    <p style="color:#999;margin-top:20px;">
                        This link expires in 30 minutes.
                        If you did not request this, ignore this email.
                    </p>
                </div>
                """.formatted(person.getDisplayName(), resetUrl);

        send(person.getEmail(),
                person.getDisplayName(),
                "Password reset — Hospital System",
                html);
    }


    public static void sendAppointmentConfirmation(Person patient,
                                                   String doctorName,
                                                   String scheduledAt) {
        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px;">
                    <h2 style="color: #27ae60;">Appointment Confirmed</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>Your appointment has been booked:</p>
                    <table style="border-collapse:collapse;width:100%%">
                        <tr>
                            <td style="padding:8px;border:1px solid #ddd">
                                <strong>Doctor</strong></td>
                            <td style="padding:8px;border:1px solid #ddd">
                                %s</td>
                        </tr>
                        <tr>
                            <td style="padding:8px;border:1px solid #ddd">
                                <strong>Date &amp; Time</strong></td>
                            <td style="padding:8px;border:1px solid #ddd">
                                %s</td>
                        </tr>
                    </table>
                </div>
                """.formatted(patient.getDisplayName(), doctorName, scheduledAt);

        send(patient.getEmail(),
                patient.getDisplayName(),
                "Appointment confirmed — Hospital System",
                html);
    }


    public static void sendAccountLockedEmail(Person person,
                                              String lockedUntil) {
        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px;">
                    <h2 style="color: #e74c3c;">Account Locked</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>Your account has been temporarily locked due to
                       too many failed login attempts.</p>
                    <p><strong>Locked until:</strong> %s</p>
                    <p>If this was not you, please reset your password
                       immediately.</p>
                </div>
                """.formatted(person.getDisplayName(), lockedUntil);

        send(person.getEmail(),
                person.getDisplayName(),
                "Account locked — Hospital System",
                html);
    }
}