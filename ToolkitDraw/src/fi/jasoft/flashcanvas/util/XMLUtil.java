package fi.jasoft.flashcanvas.util;

public class XMLUtil {

	static public String escapeXML(String str) {
        return str.replaceAll("&","&amp;")
                  .replaceAll("<", "&lt;")
                  .replaceAll(">", "&gt;")
                  .replaceAll("\"", "&quot;")
                  .replaceAll("'", "&apos;")
                  .replaceAll("\n", "");
    }  
	
}
