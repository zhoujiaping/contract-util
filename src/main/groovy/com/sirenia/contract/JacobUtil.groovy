package com.sirenia.contract

import com.jacob.activeX.ActiveXComponent
import com.jacob.com.ComThread
import com.jacob.com.Dispatch
import com.jacob.com.Variant
import org.springframework.util.ResourceUtils
import org.springframework.util.StringUtils

import static com.jacob.com.ComThread.InitSTA

class JacobUtil {

    static final int WORD_HTML = 8
    static final int WORD_TXT = 7
    static final int EXCEL_HTML = 44
    static final int EXCEL_XML = 46
    static final int EXCEL_43 = 43 // Excel 2003 测试可用


/**
 * WORD转HTML
 *
 * @param docfile
 *            WORD文件全路径
 * @param htmlfile
 *            转换后HTML存放路径
 */
    static void wordToHtml(String docfile, String htmlfile) {
// 初始化
        InitSTA()
        ActiveXComponent app = new ActiveXComponent("Word.Application") // 启动word
        try {
            app.setProperty("Visible", new Variant(false))
            Dispatch docs = app.getProperty("Documents").toDispatch()
            Dispatch doc = Dispatch.invoke(
                    docs,
                    "Open",
                    Dispatch.Method,
                    [docfile, new Variant(false),
                     new Variant(true)] as Object[], new int[1]).toDispatch()
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, [
                    htmlfile, new Variant(WORD_HTML)] as Object[], new int[1])
            Variant f = new Variant(false)
            Dispatch.call(doc, "Close", f)
        } finally {
            app.invoke("Quit", [] as Variant[])
            ComThread.Release()
        }
    }

    static void main(String[] args) {
        def base = "D:/test/"
        //base = "e:/"
        def docFileName = "xxx.docx"
        def htmlFileName = StringUtils.stripFilenameExtension(docFileName)+".html"
        def htmlFile = new File(StringUtils.applyRelativePath(base,htmlFileName))
        if(htmlFile.exists()){
            htmlFile.delete()
        }
        File transJs = new File(base,"trans.js")
        File lastTransJs = ResourceUtils.getFile("classpath:trans.js")
        /*if(!transJs.exists() || transJs.lastModified()<lastTransJs.lastModified()){
            transJs.bytes = lastTransJs.bytes
        }*/
        transJs.bytes = lastTransJs.bytes
        File transHtml = ResourceUtils.getFile("classpath:trans.html")
        new File(base,"trans.html").bytes = transHtml.bytes
        wordToHtml(StringUtils.applyRelativePath(base,docFileName) , StringUtils.applyRelativePath(base,htmlFileName))
        def ts = System.currentTimeMillis()
        def text = htmlFile.getText("gbk")
                .replace(/charset=gb2312/, "charset=utf-8")
                .replace("</body>", """
<script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
<script src="trans.js?v=$ts"></script>
</body>
""")
        htmlFile.withWriter("utf-8"){it << text}
        BrowserRunner.open([url:htmlFile.absolutePath,options:['--allow-file-access-from-files']])
    }
}