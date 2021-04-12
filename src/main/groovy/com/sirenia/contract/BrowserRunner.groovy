package com.sirenia.contract

class BrowserRunner {
    static String chromePath = ""
    static void open(Map opts){
        int i = opts.url.indexOf(':')
        String disk = 'c'
        def cmd
        def process
        if(i>0){
            disk = opts.url[0..<i]
            cmd = """cmd /c $disk:"""
            println cmd
            process = cmd.execute()
            println process.err.text
            println process.text
        }

        cmd = $/cmd /c cd "$chromePath"/$
        println cmd
        process = cmd.execute()
        println process.err.text
        println process.text
    //start chrome.exe https://cn.bing.com --allow-file-access-from-files
        cmd = """cmd /c start chrome.exe ${opts.url} ${opts.options?.join(' ')}"""
        //cmd = """cmd /c start chrome.exe ${opts.url}"""
        println cmd
        process = cmd.execute()
        println process.err.text
        println process.text
    }
}
