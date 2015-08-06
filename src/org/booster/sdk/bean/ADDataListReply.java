package org.booster.sdk.bean;

import java.util.ArrayList;

public class ADDataListReply extends GundamDataReply {
    private static final long serialVersionUID = 1L;
    
    int totalCount;
    ArrayList<ADDataReply> list;
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public ArrayList<ADDataReply> getList() {
        if(list == null){
            list = new ArrayList<ADDataReply>();
        }
        return list;
    }
    public void setList(ArrayList<ADDataReply> list) {
        this.list = list;
    }
    
}
