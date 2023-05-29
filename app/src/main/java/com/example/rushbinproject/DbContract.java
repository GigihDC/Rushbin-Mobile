package com.example.rushbinproject;

public class DbContract {

    private static final String ROOT_URL ="https://10.10.180.192/apismt4/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_UPDATE_PROFILE = ROOT_URL + "updateProfile";
    public static final String URL_GET_POIN = ROOT_URL + "getPoin";
    public static final String URL_UPDATE_POIN = ROOT_URL + "updatePoin";
    public static final String URL_CREATE_PENUKARAN = ROOT_URL + "createPenukaran";
    public static final String URL_GET_PROFILE = ROOT_URL + "getProfile";
    public static final String URL_PICKUP = ROOT_URL + "pickup";
    public static final String URL_GET_DATA_PENUKARAN_POIN = ROOT_URL + "getDataTukar";
    public static final String URL_GET_DATA_SETOR = ROOT_URL + "getDataSetor";
    public static final String URL_GET_DATA_ANTAR = ROOT_URL + "getDataAntar";
}
