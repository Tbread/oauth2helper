package com.tbread.oauthhelper;

public abstract class SocialUserAttributes {

    public abstract SocialProvider getProvider();
    public abstract String getUsername();
    public abstract String getSocialId();
    public abstract String getName();


}
