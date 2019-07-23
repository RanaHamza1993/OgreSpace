package com.brainplow.ogrespace.apputils

object Urls {

    val baseUrl = "https://apis.officedoor.ai/"
    val baseStorageUrl = "https://storage.officedoor.ai/"
    val urlAddToFav = baseUrl + "office/user_favourite_android/"
    val urlGetFav = baseUrl + "office/get_user_favourite/"
    val urlDelFav = baseUrl + "office/delete_user_favourite/"
    val urlProperyDetail = baseUrl + "office/property_detail_by_id_android/"
    val urlSimilarProperties = baseUrl + "office/similar_properties/"
    val urlGetFavItems = baseUrl + "office/favourite_properties_id"
    val urlFilterSearch = baseUrl + "office/filter_properties1/"
    val urlGetPlaces = baseUrl + "office/search_suggestions/"
    //val baseImageStorageUrl=               baseStorageUrl+"OfficeImages/"
    val iconStorageUrl = baseStorageUrl + "final/"
    val urlImageUpload = baseStorageUrl + "hamzatest.php"
    val urlSignUp = baseUrl + "user/createuser/"
    val urlEmailCheck = baseUrl + "user/EmailVerify/"
    val urlUserNameCheck = baseUrl + "user/UsernameVerify/"
    val urlSignIn = baseUrl + "login/"
    val urlStates = baseUrl + "office/state_list/"
    val urlPropertyByState = baseUrl + "office/state_properties/"
    val urlForgotPwd = baseUrl + "user/ForgetPssword/"
    val urlGetLeaseProperties = baseUrl + "office/Lease_Proterties_android/"
    val urlGetSaleProperties = baseUrl + "office/Sale_Property_android/"
    val urlGetUserProfile = baseUrl + "user/GetUserDetail/"
    val urlUpdateUserProfile = baseUrl + "user/UpdateProfile/"
    val urlChangePassword = baseUrl + "user/user_change_password/"
    val urlSearchProperties = baseUrl + "office/search_properties1/"
    val urlContactUs = baseUrl + "user/contact_us/"
    val urlAddCreditCard = baseUrl + "payment/cardinfo/"
    val urlFromZipCode = baseUrl + "office/zipcode/"
    val urlGetCreditCard = baseUrl + "payment/cardinfo/"
    val urlGetRecentlyViewed = baseUrl + "office/recently_viewed_android/"
    val urlGooglePlaceSearch = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
}