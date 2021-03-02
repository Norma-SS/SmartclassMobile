package com.projectbelajar.yuukbelajar;

public class AppVar {
  //URL to our login.php file, url bisa diganti sesuai dengan alamat server kita
  public static final String LOGIN_URL = "https://yuukbelajar.com/login.php";
  public static final String URL_PRODUCTS = "https://yuukbelajar.com/Api.php";
  public static final String PESAN1 = "https://yuukbelajar.com/pesan.php";

  //Keys for email and password as defined in our $_POST['key'] in login.php
  public static final String KEY_EMAIL = "email";
  public static final String KEY_PASSWORD = "password";
  public static final String KEY_EML = "emailku";
  //public static final String KEY_NAMA = "nama";

  //If server response is equal to this that means login is successful
  public static final String LOGIN_SUCCESSI = "success";
  //public static final String REGISTER_SUCCESS = "sukses";
}
