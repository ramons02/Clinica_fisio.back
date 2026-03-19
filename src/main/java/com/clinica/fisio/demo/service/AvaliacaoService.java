package com.clinica.fisio.demo.service;

import com.clinica.fisio.demo.model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class AvaliacaoService {

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] gerarPdf(Avaliacao avaliacao) {
        Context context = new Context();

        // Cálculos puramente numéricos
        double lsiExt = (avaliacao.getExtEsq() > 0) ? (avaliacao.getExtDir() / avaliacao.getExtEsq()) * 100 : 0;
        double lsiFlex = (avaliacao.getFlexEsq() > 0) ? (avaliacao.getFlexDir() / avaliacao.getFlexEsq()) * 100 : 0;
        double lsiSingle = (avaliacao.getSingleEsq() > 0) ? (avaliacao.getSingleDir() / avaliacao.getSingleEsq()) * 100 : 0;

        context.setVariable("avaliacao", avaliacao);

        context.setVariable("lsiExtNum", lsiExt);
        context.setVariable("lsiExt", String.format("%.1f", lsiExt));
        context.setVariable("lsiFlex", String.format("%.1f", lsiFlex));
        context.setVariable("lsiSingle", String.format("%.1f", lsiSingle));

        String html = templateEngine.process("relatorio-avaliacao", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // 🔥 CORREÇÃO PRINCIPAL
            String baseUrl = new ClassPathResource("static/").getURL().toString();

            renderer.setDocumentFromString(html, baseUrl);

            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro interno no Java ao gerar PDF: " + e.getMessage());
        }
    }
}