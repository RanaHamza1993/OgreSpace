package com.brainplow.ogrespace.apputils

object Urls {

    val baseUrl=                                 "https://apis.officedoor.ai/"
    val baseStorageUrl=                          "https://storage.officedoor.ai/"
    val urlAddToFav =                      baseUrl + "office/user_favourite_android/"
    val urlGetFav =                        baseUrl + "office/get_user_favourite/"
    val urlDelFav =                        baseUrl + "office/delete_user_favourite/"
    val urlProperyDetail =                 baseUrl + "office/property_detail_by_id/"
    val urlGetFavItems =                   baseUrl +"office/favourite_properties_id"

    //val baseImageStorageUrl=               baseStorageUrl+"OfficeImages/"
    val iconStorageUrl=                    baseStorageUrl+"final/"
    val urlImageUpload=                    baseStorageUrl+"hamzatest.php"
    val urlSignUp=                         baseUrl+"user/createuser/"
    val urlSignIn=                         baseUrl+"login/"
    val urlStates=                         baseUrl+"office/state_list/"
    val urlPropertyByState=                baseUrl+"office/state_properties/"
    val urlForgotPwd=                      baseUrl+"user/ForgetPssword/"
    val urlGetLeaseProperties=             baseUrl+"office/Lease_Proterties_android/"
    val urlGetSaleProperties=              baseUrl+"office/Sale_Property_android/"
    val urlGetUserProfile=                 baseUrl+"user/GetUserDetail/"
    val urlUpdateUserProfile=              baseUrl+"user/UpdateProfile/"
    val urlSearchProperties=               baseUrl+"office/search_properties1/"
    val urlGooglePlaceSearch=                     "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
}