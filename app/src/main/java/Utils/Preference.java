package Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shashank on 4/17/2017.
 */

public class Preference {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context mContext;

    private int PRIVATE_MODE = 0;

    private String USERID="user_id";

    private String PREF_NAME = "splitbillpref";






    public Preference(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void updateUserID(String user)
    {
        editor.putString(USERID,user);
        editor.commit();
    }


    public String getUserid()
    {
        return sharedPreferences.getString(USERID,"");
    }





}
