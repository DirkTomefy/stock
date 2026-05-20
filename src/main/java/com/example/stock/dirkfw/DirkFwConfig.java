package com.example.stock.dirkfw;

import java.util.HashMap;
import com.example.stock.dirkfw.start.mapping.TableMap;



public class DirkFwConfig {
    public static String displayMethodComboboxName="toDisplayOnCombobox";
    public static String displayMethodOnListName="toDisplayOnList";
    public static HashMap<String, TableMap> classInfos;
    
    public static HashMap<String, TableMap> getClassInfos() {
        return classInfos;
    }

    public static void setClassInfos(HashMap<String, TableMap> classInfos) {
        DirkFwConfig.classInfos = classInfos;
    }

}
