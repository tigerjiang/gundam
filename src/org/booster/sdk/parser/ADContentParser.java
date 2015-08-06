package org.booster.sdk.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.booster.sdk.bean.ADDataListReply;
import org.booster.sdk.bean.ADDataReply;
import org.booster.sdk.bean.ErrorInfo;
import org.booster.sdk.util.CommonTools;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;



public class ADContentParser extends GundamDataParser {
    final String timeTag = "time";
    final String nodeTag = "info";
    final String adTag = "ad";
    final String urlTag = "url";
    final String adCodeTag = "board_id";
    final String adTypeTag = "type";
    final String numberTag = "count";
    ADDataReply adDataReply;
    ADDataListReply reply = new ADDataListReply();
    String adCode = "";
    String adType = "";
    public ADContentParser(){
        rootTag = "ads";
        errorCodeTag = "result";
    }
    @Override
    public void parse() {
        if (CommonTools.isEmpty(data)  || !data.startsWith("<?xml")) {
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
                            
                        } else if (qName.equalsIgnoreCase(numberTag)) {
                            temp = parser.nextText();
                            if (!CommonTools.isEmpty(temp)) {
                                reply.setTotalCount(Integer.parseInt(temp));
                            }
                            if (reply.getTotalCount() == 0) {
                                endTag = true;
                            }
                        } else if (qName.equalsIgnoreCase(adTag)) {
                            // list=new ArrayList<AppInfo>();
                            adCode = parser.getAttributeValue(0);
                            adType = parser.getAttributeValue(1);
                            
                        } else if (qName.equalsIgnoreCase(nodeTag) && parser.getAttributeCount()>0) {
                            adDataReply = new ADDataReply();
                            adDataReply.setAdCode(adCode);
                            adDataReply.setAdType(Integer.parseInt(adType));
                            startElementFlag = true;
                            
                        }
                        if (startElementFlag == true) {
                            if (qName.equalsIgnoreCase(timeTag)) {
                                temp = parser.nextText();
                                if (!CommonTools.isEmpty(temp)) {
                                   adDataReply.setEditTime(temp);
                                }
                            } else if (qName.equalsIgnoreCase(urlTag)) {
                                temp = parser.nextText();
                                temp = (temp != null) ? temp.trim() : "";
                                adDataReply.setAdResourceURL(temp);
                            } 
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        qName = parser.getName();
                        if (qName.equalsIgnoreCase(nodeTag)&&startElementFlag) {
                            reply.getList().add(adDataReply);
                            startElementFlag = false;
                        } else if (qName.equalsIgnoreCase(adTag)) {
                            endTag = true;
                        } else if (qName.equalsIgnoreCase(rootTag)) {
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
