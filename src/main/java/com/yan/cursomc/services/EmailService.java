package com.yan.cursomc.services;

import javax.mail.internet.MimeMessage;

import com.yan.cursomc.domain.Cliente;
import com.yan.cursomc.domain.Pedido;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService{

    void sendOrderConfirmationEmail(Pedido obj);

    void sendEmail(SimpleMailMessage msg);

    void sendOrderConfirmationHtmlEmail(Pedido obj);
    void sendHtmlEmail(MimeMessage msg);

    void sendNewPasswordEmail(Cliente cliente, String newPass);

}