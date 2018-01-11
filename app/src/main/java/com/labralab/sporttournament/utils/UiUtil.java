package com.labralab.sporttournament.utils;

/**
 * Created by pc on 10.10.2017.
 */

public class UiUtil {

    public static int getSpinnerPosition(String item, String[] items){
        for (int i = 0; i < items.length; i++){
            if(item.equals(items[i])){
                return i;
            }
        }
        return 0;
    }
    public static int getSpinnerPosition(int item, String[] items){

        String strItem = Integer.toString(item);
        for (int i = 0; i < items.length; i++){
            if(strItem.equals(items[i])){
                return i;
            }
        }
        return 0;

    }

}
