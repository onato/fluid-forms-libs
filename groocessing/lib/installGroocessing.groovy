import org.apache.commons.net.*

println "Installing Groocessing..."

new File("../../tools/").mkdir()
new File("../../tools/Groocessing/").mkdir()
def tool = new File("../../tools/Groocessing/tool")
tool.mkdir()
tool.eachFile{println it}

def files = ["FluidFormsLibs.jar", "groovy-all-1.6.1.jar"]
files.each{
	org.apache.commons.io.FileUtils.copyFile(new File("library/"+it), new File("../../tools/Groocessing/tool/"+it))
}

println "Done! Restart Process and you will find the Run Groocessing command under the 'Tools' menu."
