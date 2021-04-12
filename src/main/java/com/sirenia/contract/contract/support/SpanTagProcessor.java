package com.sirenia.contract.contract.support;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.Image;
import com.itextpdf.tool.xml.html.Span;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpanTagProcessor extends Span {
    @Override
    public List<Element> end(WorkerContext ctx, Tag tag, List<Element> currentContent) {
        final Map<String, String> attributes = tag.getAttributes();
        String clazz = attributes.get(HTML.Attribute.CLASS);
        if (clazz != null && clazz.contains("seal-loc")) {
            Map<String, String> css = tag.getCSS();
            css.put("color","white");
            css.put("margin-right","-4em");
            css.put("display","inline-block");
            css.put("width","4em");
            tag.setCSS(css);
            currentContent.clear();
            Chunk chunk = new Chunk();
            //chunk.append("置位章签");
            List<Element> elements = new ArrayList<>(1);
            HtmlPipelineContext htmlPipelineContext = null;
            try {
                htmlPipelineContext = getHtmlPipelineContext(ctx);
            } catch (NoCustomContextException e) {
                throw new RuntimeException(e);
            }
            CssAppliers cssAppliers = getCssAppliers();
            elements.add(cssAppliers.apply(chunk, tag, htmlPipelineContext));
            return elements;
        }
        return super.end(ctx, tag, currentContent);
    }
}
