package com.findgirls.widget;

import android.content.Context;

public abstract class DialogMenu {

	public static final String HTTP_HEADER = "http://";

    public static final int MENU_TICKET_DIALOG = -2;
    public static final int MENU_NUMBER_DIALOG = -1;
    public static final int MENU_URL_DIALOG = -3;
    private static final int ITEM_COPY = 0;
    private static final int ITEM_CONFORM = 0;


    public abstract boolean handle(int requestCode, int position, Object userInfo, Context context);



}
