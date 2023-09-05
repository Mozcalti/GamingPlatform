package com.mozcalti.gamingapp.service.correos.impl;

import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.FileUtils;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    private final MessageSource mailMessageSource;

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port}")
    private String mailPort;

    @Value("${mail.user}")
    private String mailUser;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${mail.from}")
    private String mailFrom;

    @Override
    public void sendMail(String toAddress, String templateKey, Map<String, Object> templateParameters)
            throws SendMailException, UtilsException {

        String subject = mailMessageSource.getMessage("mail.%s.subject".formatted(templateKey), null, Locale.getDefault());
        String mailTemplate = mailMessageSource.getMessage("mail.%s.template".formatted(templateKey), null, Locale.getDefault());

        String templateMessage = readMailTemplate(mailTemplate, templateParameters);

        try {
            Map<String, String> imagesMessage = new HashMap<>();
            imagesMessage.put("logo_mozcalti", Constantes.IMAGES_MOZCALTI_LOGO);
            // SMTP server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", mailHost);
            properties.put("mail.smtp.port", mailPort);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailUser, mailPassword);
                }
            };

            Session session = Session.getInstance(properties, auth);

            // creates a new e-mail message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(mailFrom));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // set html message
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(templateMessage, Constantes.MAIL_MESSAGE_TYPE);
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(messageBodyPart);

            for(Map.Entry<String, String> entry : imagesMessage.entrySet()) {
                DataSource imageDs = new URLDataSource(SendMailServiceImpl.class.getResource(entry.getValue()));
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setDataHandler(new DataHandler(imageDs));
                imagePart.setHeader(Constantes.MAIL_IMAGE_HEADER, "<" + entry.getKey() + ">");
                multiParte.addBodyPart(imagePart);
            }

            msg.setContent(multiParte);

            Transport.send(msg);
        } catch (Exception e) {
            log.error("Error en el servicio sendMail(): {}", StackTraceUtils.getCustomStackTrace(e));
            throw new SendMailException("Error en el servicio sendMail():\n"
                    + StackTraceUtils.getCustomStackTrace(e), e);
        }

    }

    private String readMailTemplate(String pathname, Map<String, Object> parameters) throws SendMailException, UtilsException {

        Configuration configuration;
        TemplateLoader templateLoader;
        Template template;
        ByteArrayOutputStream out;
        Writer templateSalida;
        String ruta;
        String nomArchivo;
        String salida;

        try {
            configuration = new Configuration(Configuration.VERSION_2_3_23);

            if (!pathname.isEmpty()) {
                nomArchivo = FileUtils.getFileName(pathname);
                ruta = FileUtils.getFileRuta(pathname);
                templateLoader = new ClassTemplateLoader(SendMailServiceImpl.class, ruta);
                configuration.setTemplateLoader(templateLoader);
                configuration.setDefaultEncoding(Constantes.MAIL_ENCODING);
                configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

                template = configuration.getTemplate(nomArchivo);

                out = new ByteArrayOutputStream();
                templateSalida = new OutputStreamWriter(out);

                if (parameters.isEmpty()) {
                    parameters = new HashMap<>();
                }

                template.process(parameters, templateSalida);
                templateSalida.flush();

                salida = new String(out.toByteArray(), StandardCharsets.UTF_8);

                out.close();
                templateSalida.close();
            } else {
                throw new SendMailException("Error en el servicio readFileTemplate():\n"
                        + "Debe especificar el pathname con la ruta y nombre del archivo");
            }
        } catch (Exception e) {
            log.error("Error en el servicio readMailTemplate(): {}", StackTraceUtils.getCustomStackTrace(e));
            throw new SendMailException("Error en el servicio readMailTemplate():\n"
                    + StackTraceUtils.getCustomStackTrace(e), e);
        }

        return salida;

    }
}
