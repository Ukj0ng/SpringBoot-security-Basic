package com.example.security1.oauth;

import com.example.security1.auth.PrincipalDetail;
import com.example.security1.model.User;
import com.example.security1.oauth.provider.GoogleUserInfo;
import com.example.security1.oauth.provider.NaverUserInfo;
import com.example.security1.oauth.provider.OAuth2UserInfo;
import com.example.security1.repository.UserRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 해당 함수 종료시, @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: " + userRequest.getClientRegistration());
        System.out.println("token: " + userRequest.getAccessToken());
        System.out.println("registerationId: " + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("id: " + userRequest.getClientRegistration().getClientId());
        System.out.println("name: " + userRequest.getClientRegistration().getClientName());
        System.out.println("secret: " + userRequest.getClientRegistration().getClientSecret());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            // response = {resultcode=00, message=success, response={id=??, email=dkfnfn17@naver.com, name=한종욱}} 안에 response가 있기 때문에 아래처럼 처리
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> Access Token요청
        // userRequest 정보 -> 회원프로필 받아야함(loadUser 함수 호출) -> 구글로부터 회원프로필 받아줌
//        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
//            System.out.println("구글 로그인 요청");
//            oAuth2UserInfo = new GoogleUserInfo(o)
//        }
        System.out.println("객체 정보: " + oAuth2User.getAttributes());
        // 회원가입을 강제로 진행해볼 예정
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("소셜 로그인이 최초입니다.");
            userEntity = User.builder()
                .username(username)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("로그인을 이미 한 적이 있습니다. 자동회원가입이 되어 있습니다.");
        }

        return new PrincipalDetail(userEntity, oAuth2User.getAttributes());
    }
}
