package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.service.SendMailService;
import com.mozcalti.gamingapp.utils.FileUtils;
import com.mozcalti.gamingapp.utils.ObjectUtils;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SendMailServiceImpl implements SendMailService {

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
    public void sendMail(String toAddress, String subject, String templateMessage, Map<String, String> imagesMessage)
            throws SendMailException, UtilsException {

        try {
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
            messageBodyPart.setContent(templateMessage, "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(messageBodyPart);

            for(String key : imagesMessage.keySet()) {
                DataSource imageDs = new URLDataSource(SendMailServiceImpl.class.getResource(imagesMessage.get(key)));
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setDataHandler(new DataHandler(imageDs));
                imagePart.setHeader("Content-ID", "<" + key + ">");
                multiParte.addBodyPart(imagePart);
            }

            msg.setContent(multiParte);

            Transport.send(msg);
        } catch (Exception e) {
            System.out.println("Error en el servicio readFileTemplate():\n"
                    + StackTraceUtils.getCustomStackTrace(e));
            throw new SendMailException("Error en el servicio readFileTemplate():\n"
                    + StackTraceUtils.getCustomStackTrace(e), e);
        }

    }

    @Override
    public String readFileTemplate(String pathname, Map<String, Object> parameters) throws SendMailException, UtilsException {

        Configuration configuration;
        TemplateLoader templateLoader;
        Template template;
        ByteArrayOutputStream out;
        Writer templateSalida;
        String ruta;
        String nomArchivo;
        String salida;

        try {
            System.out.println("Cargando archivo desde una ruta externa...");

            configuration = new Configuration(Configuration.VERSION_2_3_23);

            if (!ObjectUtils.isEmpty(pathname)) {
                nomArchivo = FileUtils.getFileName(pathname);
                ruta = FileUtils.getFileRuta(pathname);
                templateLoader = new ClassTemplateLoader(SendMailServiceImpl.class, ruta);
                configuration.setTemplateLoader(templateLoader);
                configuration.setDefaultEncoding("UTF-8");
                configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

                template = configuration.getTemplate(nomArchivo);

                out = new ByteArrayOutputStream();
                templateSalida = new OutputStreamWriter(out);

                if (ObjectUtils.isEmpty(parameters)) {
                    parameters = new HashMap<>();
                }

                template.process(parameters, templateSalida);
                templateSalida.flush();

                salida = new String(out.toByteArray(), "UTF-8");

                out.close();
                templateSalida.close();
            } else {
                throw new SendMailException("Error en el servicio readFileTemplate():\n"
                        + "Debe especificar el pathname con la ruta y nombre del archivo");
            }
        } catch (Exception e) {
            System.out.println("Error en el servicio readFileTemplate():\n"
                    + StackTraceUtils.getCustomStackTrace(e));
            throw new SendMailException("Error en el servicio readFileTemplate():\n"
                    + StackTraceUtils.getCustomStackTrace(e), e);
        }

        return salida;

    }
}
