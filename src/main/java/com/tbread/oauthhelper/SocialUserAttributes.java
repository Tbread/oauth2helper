package com.tbread.oauthhelper;

import org.json.JSONObject;

public abstract class SocialUserAttributes {

    public abstract SocialProvider getProvider();
    public abstract String getUsername();
    public abstract String getSocialId();
    public abstract String getName();

    public abstract JSONObject getAttr();
}
