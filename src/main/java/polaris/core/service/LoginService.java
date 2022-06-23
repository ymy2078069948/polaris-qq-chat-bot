package polaris.core.service;

public interface LoginService {
    Boolean loginBot(Long qq,String password,String protocol);
    Boolean switchBot(Long qq,String password,String protocol);
    Boolean closeBot();
}
