package com.sirenia.contract.contract.support;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;

public class ChineseFontsProvider implements FontProvider {
    @Override
    public boolean isRegistered(String fontName) {
        return false;
    }

    @Override
    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
        BaseFont bf;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Font font = new Font(bf, size, style, color);
        font.setColor(color);
        return font;
    }
}
