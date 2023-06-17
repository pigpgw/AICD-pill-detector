package com.example.aicd;

// 리얼타임 데이터베이스
// 사용자 계정 정보 모델 클래스
public class UserAccount {

    // 추후에 저장할 것이 있다면 더 추가해서 불러올 수 있음
    private String emailID;     // 파이어베이스 고유 토큰 정보 UID
    private String password;    // 이메일 아이디
    private String idToken;     // 비밀번호

    // 리얼타임데이터베이스를 사용하기 위한 구문
    public UserAccount() { }

    // 설정하고 가져오는 과정
    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
