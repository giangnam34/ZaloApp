package com.essay.zaloapp.domain.enums;

public enum Audience {

    // Public
    Public,
    // Tất cả bạn bè
    AllFriend,

    // Chỉ mình tôi
    OnlyMe,

    // Chỉ hiển thị với một số bạn bè
    SomeOneCanSee,

    // Tất cả ngoại trừ một số người
    AllExceptSomeOne;

    public static Audience findByName(String name){
        for(Audience audience: values()){
            if (audience.name().equalsIgnoreCase(name))
                return audience;
        }
        return null;
    }
}