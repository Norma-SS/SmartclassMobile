package com.projectbelajar.yuukbelajar;


public class Koneksi {
    //URL to our login.php file, url bisa diganti sesuai dengan alamat server kita
    //public static final String LOGIN_URL = "http://192.168.1.9/course/login.php";
    public static final String REGISTER_URL = "http://192.168.1.9/kis24/register.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ID = "id";
    public static final String KEY_NAMA = "nama";

    //If server response is equal to this that means login is successful
    //public static final String LOGIN_SUCCESS = "success";
    public static final String REGISTER_SUCCESS = "success";
}
