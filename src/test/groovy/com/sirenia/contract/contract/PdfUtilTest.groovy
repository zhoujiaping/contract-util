package com.sirenia.contract.contract

import org.apache.commons.io.FileUtils
import org.junit.Ignore
import org.junit.Test
import org.springframework.util.ResourceUtils
import org.springframework.util.StringUtils

@Ignore
class PdfUtilTest {
    @Test
    void testHtmlToPdf(){
        def inFile = ResourceUtils.getFile("classpath:aaa.html")
        def is = inFile.newInputStream()
        def simpleName = StringUtils.stripFilenameExtension(inFile.name) + '.pdf'
        def outFile = new File(inFile.parentFile,simpleName)
        FileUtils.touch(outFile)
        println outFile
        def out = outFile.newOutputStream()
        PdfUtils.htmlToPdfWithCloseStream(is, out)
    }
    @Test
    void testHtmlFileToPdfFile(){
        def inFile = ResourceUtils.getFile("classpath:contract.html")
        def simpleName = StringUtils.stripFilenameExtension(inFile.name) + '.pdf'
        def outFile = new File(inFile.parentFile,simpleName)
        PdfUtils.htmlToPdf(inFile, outFile)
    }

}
