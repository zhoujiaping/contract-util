package com.sirenia.contract.contract.util;

import com.itextpdf.text.*;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.html.*;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.sirenia.contract.contract.support.ChineseFontsProvider;
import com.sirenia.contract.contract.support.ImageTagProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class PdfUtils {
    private static final Logger logger = LoggerFactory.getLogger(PdfUtils.class);
    public static void htmlToPdfWithCloseStream(InputStream in, OutputStream out) {
        try{
            htmlToPdf(in, out);
        }finally{
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    public static void htmlToPdf(File htmlFile, File pdfFile) {
        ExceptionUtils.wrapWithRuntimeEx(()->{
            FileUtils.touch(pdfFile);
            InputStream in = new BufferedInputStream(new FileInputStream(htmlFile));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
            htmlToPdfWithCloseStream(in, out);
        });
    }
    public static void htmlToPdf(InputStream in, OutputStream out) {
        Document doc = new Document();
        PdfWriter writer = ExceptionUtils.wrapWithRuntimeEx(()->PdfWriter.getInstance(doc, out));
        doc.open();
        TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
        tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
        tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);

        //tagProcessorFactory.removeProcessor(HTML.Tag.SPAN);
        //tagProcessorFactory.addProcessor(new SpanTagProcessor(), HTML.Tag.SPAN);

        FontProvider fontProvider = new ChineseFontsProvider();
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory()).setTagFactory(tagProcessorFactory);
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(doc, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);
        ExceptionUtils.wrapWithRuntimeEx(()->p.parse(new InputStreamReader(in, Charset.forName("utf-8"))));
        p.flush();
        worker.close();
        doc.close();
        ExceptionUtils.wrapWithRuntimeEx(()->out.flush());


    }
}
