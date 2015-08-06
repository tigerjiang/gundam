package org.booster.sdk.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.booster.sdk.bean.ADDataListReply;
import org.booster.sdk.bean.ADDataReply;
import org.booster.sdk.bean.ErrorInfo;
import org.booster.sdk.bean.LauncherDataReply;
import org.booster.sdk.util.CommonTools;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;



public class LauncherContentParser extends GundamDataParser {
    final String timeTag = "time";
    final String nodeTag = "info";
    final String urlTag = "url";
    LauncherDataReply reply = new LauncherDataReply();
    public LauncherContentParser(){
        rootTag = "launcher";
        errorCodeTag = "result";
    }
    @Override
    public void parse() {
        if (CommonTools.isEmpty(data) || !data.startsWith("<?xml")) {
            result = reply;
            return;
        }
        Reader reader = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            reader = new StringReader(data);
            parser.setInput(reader);
            int eventCode = parser.getEventType();
            String qName = "";
            while ((eventCode != XmlPullParser.END_DOCUMENT)
                && (endTag == false)) {
                temp = "";
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        qName = parser.getName();
                        if (qName.equalsIgnoreCase(rootTag)) {

                        } else if (qName.equalsIgnoreCase(errorCodeTag)) {
                            temp = parser.nextText();
//                            temp = (temp != null) ? temp.trim() : "";
                            if(Integer.parseInt(temp)!=1){
                                errorInfo = (errorInfo == null) ? (new ErrorInfo())
                                    : errorInfo;
                                errorInfo.setErrorCode(temp);
                            }
                            
                        } else if (qName.equalsIgnoreCase(errorNameTag)) {
                            temp = parser.nextText();
                            temp = (temp != null) ? temp.trim() : "";
                            if(!CommonTools.isEmpty(temp)){
                                errorInfo = (errorInfo == null) ? (new ErrorInfo())
                                : errorInfo;
                                errorInfo.setErrorName(temp);
                            }
                            
                        } else if (qName.equalsIgnoreCase(timeTag)) {
                            temp = parser.nextText();
                            temp = (temp != null) ? temp.trim() : "";
                            reply.setEditTime(temp);
                            
                        } else if (qName.equalsIgnoreCase(urlTag)) {
                            temp = parser.nextText();
                            temp = (temp != null) ? temp.trim() : "";
                            reply.setResourceURL(temp);
                            
                        } 
                        
                        break;
                    case XmlPullParser.END_TAG:
                        qName = parser.getName();
                        if (qName.equalsIgnoreCase(rootTag)) {
                            endTag = true;
                        }
                        break;
                    default:
                        break;

                }
                eventCode = parser.next();
            }
   
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("DataParser", "error in parse data");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("DataParser", "error in parse data");
                }
                reader = null;
            }
        }
        result = reply;
    }


}
