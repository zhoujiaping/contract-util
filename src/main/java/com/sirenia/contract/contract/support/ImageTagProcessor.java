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
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageTagProcessor extends Image {
    @Override
    public List<Element> end(WorkerContext ctx, Tag tag, List<Element> currentContent) {
        final Map<String, String> attributes = tag.getAttributes();
        String src = attributes.get(HTML.Attribute.SRC);
        List<Element> elements = new ArrayList<>(1);
        if (null != src && src.length() > 0) {
            com.itextpdf.text.Image img = null;
            if (src.startsWith("data:image/")) {
                final String base64Data = src.substring(src.indexOf(",") + 1);
                try {
                    img = com.itextpdf.text.Image.getInstance(Base64.decode(base64Data));
                } catch (Exception e) {
                    throw new RuntimeWorkerException(e);
                }
                if (img != null) {
                    try {
                        final HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
                        CssAppliers cssAppliers = getCssAppliers();
                        Element imgElement = cssAppliers.apply(img, tag, htmlPipelineContext);
                        Chunk chunk = new Chunk((com.itextpdf.text.Image)imgElement, 0, 0, true);
                        elements.add(cssAppliers.apply(chunk, tag, htmlPipelineContext));
                    } catch (NoCustomContextException e) {
                        throw new RuntimeWorkerException(e);
                    }
                }
            }
            if (img == null) {
                elements = super.end(ctx, tag, currentContent);
            }
        }
        return elements;
    }
}
